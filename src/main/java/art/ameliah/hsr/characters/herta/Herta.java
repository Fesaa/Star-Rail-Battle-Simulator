package art.ameliah.hsr.characters.herta;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.RandomEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Herta extends AbstractCharacter<Herta> {

    public static final String NAME = "Herta";

    public Herta() {
        super(NAME, 953, 582, 397, 100, 80, ElementType.ICE, 110, 75, Path.ERUDITION);

        this.addPower(new TracePower()
                .setStat(PowerStat.ICE_DMG_BOOST, 22.4f)
                .setStat(PowerStat.DEF_PERCENT, 22.5f)
                .setStat(PowerStat.CRIT_CHANCE, 6.7f)
                .setStat(PowerStat.EFFECT_RES, 35)
        );

        this.registerGoal(0, new AlwaysSkillGoal<>(this));
        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new RandomEnemyTargetGoal<>(this));
    }

    @Override
    public void onCombatStart() {
        this.addPower(new HertaSkillDMGBoost());
        PermPower p = new HertaFuaListener();
        getBattle().getPlayers().forEach(player -> player.addPower(p));
    }

    @Override
    protected void useSkill() {
        this.startAttack()
                .hitEnemies(getBattle().getEnemies(), 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT)
                .execute();
    }

    @Override
    protected void useBasic() {
        AbstractEnemy target = this.getTarget(MoveType.BASIC);
        Attack attack = this.startAttack();

        attack.hitEnemy(target, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC);
        if (target.getCurrentHp() <= target.maxHp() * 0.5) {
            attack.hitEnemy(target, 0.4f, MultiplierStat.ATK, 0);
        }
        attack.execute();
    }

    @Override
    protected void useUltimate() {
        this.startAttack()
                .hitEnemies(getBattle().getEnemies(), 2.16f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS)
                .execute();

        this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 25, 1, "No One Can Betray Me E6 Herta"));
    }

    public static class HertaSkillDMGBoost extends PermPower {
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.SKILL)) {
                return 0;
            }

            if (enemy.getCurrentHp() >= enemy.maxHp() * 0.5f) {
                return 20 + 25;
            }

            return 0;
        }
    }

    public class HertaFuaListener extends PermPower {

        private final Set<AbstractEnemy> triggeredFua = new HashSet<>();
        private int tally = 0;

        private int triggers = 0;

        @Override
        public float getConditionalCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (character != Herta.this) {
                return 0;
            }

            return Math.min(this.triggers, 5) * 3;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (character != Herta.this) {
                return 0;
            }

            // Hit Where It Hurts E4
            return damageTypes.contains(DamageType.FOLLOW_UP) ? 10 : 0;
        }

        @Override
        public void afterAttackFinish(Attack attack) {
            boolean anyAlive = attack.getTargets().stream().noneMatch(AbstractEnemy::isDead);
            List<AbstractEnemy> newFallen = attack.getTargets().stream()
                    .filter(t -> t.getCurrentHp() < t.maxHp() * 0.4f)
                    .filter(t -> !triggeredFua.contains(t)).toList();

            this.tally += newFallen.size();

            if (!anyAlive) {
                this.triggeredFua.clear();
                return;
            }

            if (this.tally == 0) {
                return;
            }

            Herta.this.startAttack()
                    .hitEnemies(getBattle().getEnemies(),
                            0.43f * this.tally,
                            MultiplierStat.ATK,
                            TOUGHNESS_DAMAGE_HALF_UNIT * this.tally,
                            DamageType.FOLLOW_UP)
                    .execute();

            this.triggers++;
            this.tally = 0;
            this.triggeredFua.addAll(newFallen);
        }
    }
}
