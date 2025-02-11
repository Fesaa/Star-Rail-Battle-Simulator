package art.ameliah.hsr.enemies.game.jarilovi.fragmentum;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FrigidProwler extends AbstractEnemy {

    private final List<AbstractEnemy> otherlings = new ArrayList<>();
    private AbstractCharacter<?> lockedIn = null;

    public FrigidProwler() {
        super("Frigid Prowler", EnemyType.Elite, 448625, 718, 1000, 132, 100, 92);

        this.addWeakness(ElementType.FIRE);
        this.addWeakness(ElementType.LIGHTNING);
        this.addWeakness(ElementType.QUANTUM);
        this.setRes(ElementType.ICE, 40);

        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 30, "Frigid Prowler ER Boost"));
        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 33.6f, "Frigid Prowler EHR Boost"));

        this.sequence.addAction(this::SummonOtherling, this::FrozenStorm);
        this.sequence.addAction(this::IceWheelFist, this::DevourOtherling);
        // TODO: Make more complete sequence.

    }

    @Override
    protected void act() {
        if (this.lockedIn != null) {
            this.IceWheelFist(this.lockedIn);
            this.lockedIn = null;
            // TODO: See what second action is
            return;
        }

        this.sequence.runNext();
    }

    @Override
    public void onDeath(BattleParticipant source) {
        super.onDeath(source);
        this.otherlings.forEach(getBattle()::removeEnemy);
    }

    private void IceWheelFist() {
        this.IceWheelFist(this.getRandomTarget());
    }

    private void IceWheelFist(AbstractCharacter<?> target) {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> {
            da.logic(target, al -> al.hit(target, 10, 976));
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Ice Wheel Fist"));
        });
    }

    private void SummonOtherling() {
        for (int i = 0; i < 2; i++) {
            AbstractEnemy otherling = new EverwinterShadewalker();
            this.otherlings.add(otherling);
            getBattle().addEnemy(otherling);
        }
    }

    private void FrozenStorm() {
        this.actionMetric.record(EnemyAttackType.AOE);
        this.doAttack(da -> da.logic(getBattle().getPlayers(), (c, al) -> {
            for (var player : c) {
                al.hit(player, 5, 325);
                if (this.successfulHit(player, 100)) {
                    player.addPower(new DeepFreeze());
                }
                getBattle().addToLog(new EnemyAction(this, EnemyAttackType.AOE, "Frozen Storm"));
            }
        }));
    }

    private void DevourOtherling() {
        Optional<AbstractEnemy> optionalOtherling = getBattle().getEnemies()
                .stream()
                .filter(e -> e.getName().equals(EverwinterShadewalker.NAME))
                .findFirst();

        if (optionalOtherling.isPresent()) {
            getBattle().removeEnemy(optionalOtherling.get());
            this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 50, 1, "Devour Otherling ATK Buff"));
            this.lockedIn = this.getRandomTarget();
        } else {
            this.addPower(TempPower.create(PowerStat.DEFENSE_REDUCTION, 50, 1, "Devour Otherling DEFENSE Red"));
        }
    }

    public static class DeepFreeze extends PermPower {

        public static final String NAME = "Frigid Prowler";

        public DeepFreeze() {
            this.name = name;
            this.type = PowerType.DEBUFF;
            this.setStat(PowerStat.SPEED_PERCENT, -12);
            // this.setStat(PowerStat.ICE_RES, -20); I don't think we can do this?
            this.maxStacks = 3;
        }


    }
}
