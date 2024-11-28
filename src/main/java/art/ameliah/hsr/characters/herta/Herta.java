package art.ameliah.hsr.characters.herta;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.RandomEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.ult.UltAtEndOfBattle;
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

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(10, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(20, new AlwaysUltGoal<>(this));
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
        this.doAttack(DamageType.SKILL,
                dh -> dh.logic(getBattle().getEnemies(),
                        (enemies, al) -> al.hit(enemies, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT)));
    }

    @Override
    protected void useBasic() {
        this.doAttack(DamageType.BASIC, dh -> dh.logic(this.getTarget(MoveType.BASIC), (e, al) -> {
            al.hit(e, 1.1f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            if (e.getCurrentHp().get() <= e.maxHp() * 0.5f) {
                al.hit(e, 0.4f);
            }
        }));
    }

    @Override
    protected void useUltimate() {
        this.doAttack(DamageType.ULTIMATE,
                dh -> dh.logic(getBattle().getEnemies(),
                        (e, al) -> al.hit(e, 1.16f, TOUGHNESS_DAMAGE_TWO_UNITS)));

        this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 25, 1, "No One Can Betray Me E6 Herta"));
    }

    public static class HertaSkillDMGBoost extends PermPower {
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.SKILL)) {
                return 0;
            }

            if (enemy.getCurrentHp().get() >= enemy.maxHp() * 0.5f) {
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
        public void afterAttack(AttackLogic attack) {
            boolean anyAlive = attack.getTargets().stream().anyMatch(e -> !e.isDead());
            List<AbstractEnemy> newFallen = attack.getTargets().stream()
                    .filter(t -> t.getCurrentHp().get() < t.maxHp() * 0.5f)
                    .filter(t -> !triggeredFua.contains(t)).toList();

            this.tally += newFallen.size();

            if (!anyAlive) {
                this.triggeredFua.clear();
                return;
            }

            if (this.tally == 0) {
                return;
            }

            // We need to copy or Herta might have attacks with a tally of 0
            // I'm not sure what happens in game, but I can't cancel attacks.
            // So this way is fine
            final int tallyCopy = this.tally;
            //final Collection<AbstractEnemy> snapShot = new HashSet<>(getBattle().getEnemies());

            Herta.this.actionMetric.record(MoveType.FOLLOW_UP);
            getBattle().addToLog(new DoMove(Herta.this, MoveType.FOLLOW_UP));
            Herta.this.startAttack()
                    .handle(DamageType.FOLLOW_UP, dh -> {
                        // Something like this should be added, so Herta doesn't attack an all new wave with her fua
                        // TODO: But it's not working 100%, have to investigate
                        /*if (getBattle().getEnemies().stream().noneMatch(snapShot::contains)) {
                            dh.logic(_ -> {});
                            dh.afterAttackHook(() -> {
                                getBattle().addToLog(new StringLine("Adding tally back!"));
                                // Add tally back, if not used
                                this.tally += tallyCopy;
                            });
                            return;
                        }*/

                        dh.logic(getBattle().getEnemies(), (e, al) -> {
                            al.hit(e, 0.43f * tallyCopy, TOUGHNESS_DAMAGE_HALF_UNIT * tallyCopy);
                            this.triggers++;
                        });
                    }).execute();

            this.tally = 0;
            this.triggeredFua.addAll(newFallen);
        }
    }
}
