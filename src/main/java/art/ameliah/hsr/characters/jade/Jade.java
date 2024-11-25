package art.ameliah.hsr.characters.jade;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.battleLogic.log.lines.entity.LoseCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.ally.DpsAllyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillCounterTurnGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.ult.UltAtEndOfBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;

public class Jade extends AbstractCharacter<Jade> implements SkillCounterTurnGoal.SkillCounterCharacter {
    public static final String NAME = "Jade";

    private int pawnedAssets = 0;
    private int fuaStacks = 0;
    private int skillCounter = 0;
    private int enhancedFua = 2;

    public Jade() {
        super(NAME, 1087, 660, 509, 103, 80, ElementType.QUANTUM, 140, 75, Path.ERUDITION);

        this.addPower(new TracePower()
                .setStat(PowerStat.QUANTUM_DMG_BOOST, 22.4f)
                .setStat(PowerStat.ATK_PERCENT, 18)
                .setStat(PowerStat.EFFECT_RES, 10)
        );

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(10, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(20, new AlwaysUltGoal<>(this));

        this.registerGoal(0, new SkillCounterTurnGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));

        this.registerGoal(0, new DpsAllyTargetGoal<>(this));
    }

    protected void increasePawnedAssets(int amount) {
        this.pawnedAssets = Math.min(50, this.pawnedAssets + amount);
    }

    protected void increaseFuaStacks(int amount) {
        int before = this.fuaStacks;
        this.fuaStacks = Math.min(40, this.fuaStacks + amount);

        getBattle().addToLog(new GainCharge(this, amount, before, this.fuaStacks));

        while (this.fuaStacks >= 8) {
            getBattle().addToLog(new LoseCharge(this, 8, this.fuaStacks, this.fuaStacks-8));
            this.fuaStacks -= 8;
            this.doFua();
        }
    }

    @Override
    public void onCombatStart() {
        this.addPower(new PawnedAsset());

        getBattle().AdvanceEntity(this, 50);
    }

    @Override
    public void useTechnique() {
        this.increasePawnedAssets(15);
        this.doAttack(dh -> dh.logic(getBattle().getEnemies(), (e, al) -> al.hit(e, 0.5f)));
    }

    @Override
    public void onEnemyJoinCombat(AbstractEnemy enemy) {
        this.increasePawnedAssets(1);
    }

    @Override
    public void afterAttack(AttackLogic attack) {
        if (attack.getTypes().contains(DamageType.FOLLOW_UP) || attack.getTypes().isEmpty()) {
            return;
        }

        this.increaseFuaStacks(attack.getTargets().size());
    }

    @Override
    public void onTurnStart() {
        this.skillCounter--;

        if (this.skillCounter == 0) {
            getBattle().getPlayers().forEach(p -> p.removePower(AcquisitionSurety.NAME));
        }

    }

    @Override
    protected void useSkill() {
        this.skillCounter = 3;

        AbstractCharacter<?> target = this.getAllyTarget();
        getBattle().IncreaseSpeed(target, new AcquisitionSurety());
    }

    @Override
    protected void useBasic() {
        this.doAttack(DamageType.BASIC, dh -> {
            AbstractEnemy target = this.getTarget(MoveType.BASIC);
            int idx = getBattle().getEnemies().indexOf(target);

            dh.logic(target, al -> al.hit(target, 0.9f, TOUGHNESS_DAMAGE_SINGLE_UNIT));
            dh.logic(idx-1, (e, al) -> al.hit(e, 0.3f, TOUGHNESS_DAMAGE_HALF_UNIT));
            dh.logic(idx+1, (e, al) -> al.hit(e, 0.3f, TOUGHNESS_DAMAGE_HALF_UNIT));
        });
    }

    @Override
    protected void useUltimate() {
        this.enhancedFua = 2;
        this.doAttack(DamageType.ULTIMATE, dh -> dh.logic(getBattle().getEnemies(), (e, al) -> {
            al.hit(e, 2.4f, TOUGHNESS_DAMAGE_TWO_UNITS);
        }));
    }

    private void doFua() {
        float mul = this.enhancedFua > 0 ? 2 : 1.2f;
        this.enhancedFua = Math.max(0, this.enhancedFua-1);

        getBattle().addToLog(new DoMove(this, MoveType.FOLLOW_UP));
        this.doAttack(DamageType.FOLLOW_UP, dh -> dh.logic(getBattle().getEnemies(), (e, al) -> {
            al.hit(e, mul, TOUGHNESS_DAMAGE_SINGLE_UNIT);
        }));
    }

    @Override
    public int getSkillCounter() {
        return this.skillCounter;
    }

    public class AcquisitionSurety extends PermPower {
        public static final String NAME = "AcquisitionSurety";

        public AcquisitionSurety() {
            super(NAME);

            this.setStat(PowerStat.FLAT_SPEED, 30);
        }

        @Override
        public void onTurnStart() {
            Jade.this.increasePawnedAssets(3);
        }

        @Override
        public void afterAttack(AttackLogic attack) {
            attack.hit(Jade.this, attack.getTargets(), 0.25f);
            Jade.this.increaseFuaStacks(attack.getTargets().size());
            // TODO: Lower HP of ally
        }
    }

    public class PawnedAsset extends PermPower {
        public static final String NAME = "Pawned Asset";

        public PawnedAsset() {
            super(NAME);
        }

        @Override
        public float getConditionalAtkBonus(AbstractCharacter<?> character) {
            return Jade.this.pawnedAssets * 0.5f;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return Jade.this.pawnedAssets * 2.4f;
        }
    }
}
