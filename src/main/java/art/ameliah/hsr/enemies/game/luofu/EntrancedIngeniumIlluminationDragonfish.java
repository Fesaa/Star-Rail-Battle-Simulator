package art.ameliah.hsr.enemies.game.luofu;

import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class EntrancedIngeniumIlluminationDragonfish extends AbstractEnemy {

    public EntrancedIngeniumIlluminationDragonfish() {
        super("Dragonfish", EnemyType.Minion, 26263, 608, 1050, 120, 20, 85);

        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.LIGHTNING);
        this.addWeakness(ElementType.IMAGINARY);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 28, "Base stat EHR"));
        this.addPower(PermPower.create(PowerStat.EFFECT_RES, 20, "Base stat ER"));
    }

    @Override
    public void onDeath() {

        // Both of these require big changes
        // TODO: deal dmg to other enemies. Currently not really possible as it expects a character I think?
        // TODO: Make it roll for inflicting def down

        getBattle().getEnemies().forEach(e -> e.addPower(TempPower.createDebuff(
                PowerStat.DEFENSE_REDUCTION, 30, 2,
                "Entranced Ingenium: Illumination Dragonfish death def down")));
    }

    @Override
    protected void act() {
        this.startAttack().handle(da -> {
            da.logic(getBattle().getPlayers(), (c, al) -> al.hit(c, 5, 326));
        }).afterAttackHook(() -> {
            var targets = this.getRandomTargets(getBattle().getPlayers().size() > 2 ? 2 : 1);

            for (var target : targets) {
                if (this.successfulHit(target, 80)) {
                    target.addPower(TempPower.createDebuff(PowerStat.DEFENSE_REDUCTION, 30, 2, "Entranced Ingenium: Illumination Dragonfish def down"));
                }
            }
        }).execute();
    }
}
