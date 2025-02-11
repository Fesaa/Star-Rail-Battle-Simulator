package art.ameliah.hsr.enemies.game.luofu.moonrage;

import art.ameliah.hsr.battleLogic.AbstractEntity;

public class MoonRageEntity extends AbstractEntity {

    public MoonRageEntity() {
        this.name = "Moon Rage Entity";
    }

    @Override
    public void takeTurn() {
        getBattle().getActionValueMap().remove(this);

        getBattle().getEnemies()
                .stream()
                .filter(e -> e instanceof MoonRageAble)
                .map(e -> (MoonRageAble) e)
                .forEach(MoonRageAble::dispelMoonRage);
    }

    @Override
    public float getBaseAV() {
        return 100; // TODO: Check
    }
}
