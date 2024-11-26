package art.ameliah.hsr.enemies.game.luofu.moonrage;

import art.ameliah.hsr.battleLogic.combat.EnemyAttack;
import art.ameliah.hsr.powers.PermPower;

public class MoonRageTracker extends PermPower {
    public final static String NAME = "Lupine Maw";

    private final MoonRageEntity moonRageEntity = new MoonRageEntity();
    private static int bloodLustStacks = 0;

    private boolean noneMoonRageAble() {
        return getBattle().getEnemies()
                .stream()
                .noneMatch(e -> e instanceof MoonRageAble);
    }

    private void enterMoonRage() {
        getBattle().getEnemies()
                .stream()
                .filter(e -> e instanceof MoonRageAble)
                .map(e -> (MoonRageAble)e)
                .forEach(MoonRageAble::enterMoonRage);

        moonRageEntity.setBattle(getBattle());
        getBattle().getActionValueMap().put(moonRageEntity, moonRageEntity.getBaseAV());
    }

    @Override
    public void onEndTurn() {
        if (this.noneMoonRageAble()) {
            bloodLustStacks = 0;
            getBattle().getActionValueMap().remove(this.moonRageEntity);
        }
    }

    @Override
    public void afterAttack(EnemyAttack attack) {
        bloodLustStacks++;

        for (var enemy : getBattle().getEnemies()) {
            if (enemy instanceof MoonRageAble moonRageAble) {

                if (bloodLustStacks >= moonRageAble.maxBloodLustStacks()) {
                    this.enterMoonRage();
                    break;
                }
            }
        }
    }
}
