package art.ameliah.hsr.enemies.game.jarilovi;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.events.enemy.WeaknessBreakEvent;
import art.ameliah.hsr.metrics.BoolMetric;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;
import java.util.function.Function;

public class SeniorStaffTeamLeader extends AbstractEnemy {

    private final BoolMetric chargeState = metricRegistry.register("charge-state", BoolMetric.class);

    public SeniorStaffTeamLeader(int baseHP, int baseATK, int baseDEF, float baseSpeed) {
        super("Senior Staff: Team Leader", EnemyType.Elite, baseHP, baseATK, baseDEF, baseSpeed, 120, 95);

        this.setRes(ElementType.QUANTUM, 20);
        this.setRes(ElementType.WIND, 20);
        this.setRes(ElementType.LIGHTNING, 20);
        this.setRes(ElementType.PHYSICAL, 20);

        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.ICE);
        this.addWeakness(ElementType.IMAGINARY);

        this.sequence.addAction(this::Disciplined, this::TeamBuilding);
        this.sequence.addAction(this::DegreeReview, this::InTraining);
        this.sequence.addAction(this::WorkCultureShock, this::CostReduction);
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    @Subscribe
    public void onWeaknessBreak(WeaknessBreakEvent event) {
        this.chargeState.set(false);
    }

    private void Disciplined() {
        this.doAttack(dl -> {
            dl.logic(this.getRandomTarget(), (e, al) -> {
                al.hit(e, 2001, 10);
            });
        });
    }

    private void TeamBuilding() {
        int size = getBattle().maxEnemiesOnField() - getBattle().enemiesSize();
        if (size == 0 ) {
            return;
        }

        Function<Integer, AbstractEnemy> sup = i -> i%2==0
                ? new GruntSecurityPersonnel(60239, 718, 1150, 132)
                : new GruntFieldPersonnel(43810, 718, 1150, 155.76f);

        for (int i = 0; i < size; i++) {
            AbstractEnemy enemy = sup.apply(i);
            enemy.addPower(new PerformancePoint());

            getBattle().addEnemy(enemy);
        }
    }

    private void DegreeReview() {
        PerformancePoint point = new PerformancePoint();

        getBattle().getEnemies()
                .stream()
                .filter(e -> e instanceof Grunt)
                .forEach(enemy -> {
                    PerformancePoint otherPoint = (PerformancePoint) enemy.getPower(PerformancePoint.NAME);
                    if (otherPoint == null) {
                        return;
                    }

                    point.merge(otherPoint);
                    enemy.removePower(otherPoint);
                });

        this.addPower(point);
    }

    private void InTraining() {
        this.chargeState.set(true);
    }

    private void CostReduction() {
        this.doAttack(dl -> {
            dl.logic(this.getRandomTarget(), (e, al) -> {
                al.hit(e, 2376, 15);
            });
        });
    }

    private void WorkCultureShock() {
        if (!this.chargeState.value()) {
            return;
        }


        this.doAttack(dl -> {
            this.chargeState.set(false);
            dl.logic(getBattle().getPlayers(), (e, al) -> {
                al.hit(e, 2251, 20);
            });
        });
    }

    public static class PerformancePoint extends PermPower {
        public static final String NAME = "Performance Point";

        public PerformancePoint() {
            super(NAME);
            this.maxStacks = 3;
        }

        @Subscribe
        public void onDeath(DeathEvent e) {
            if (e.getSource() instanceof AbstractCharacter<?> character) {
                character.addPower(this);
                this.owner.removePower(this);
            }
        }

        @Subscribe
        public void onWeaknessBreak(WeaknessBreakEvent e) {
            if (e.getSource() instanceof AbstractCharacter<?> character) {
                character.addPower(this);
                this.owner.removePower(this);
            }
        }

        @Subscribe
        public void afterAttack(PostAllyAttack e) {
            if (this.owner instanceof AbstractCharacter<?> character) {
                character.removePower(this);
            }
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 0.5f * this.stacks;
        }
    }

    public interface Grunt {}
}
