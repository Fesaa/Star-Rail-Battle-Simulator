package art.ameliah.hsr.characters.abundance.lingsha;

import art.ameliah.hsr.characters.AbstractSummon;

public class FuYuan extends AbstractSummon<Lingsha> {

    public FuYuan(Lingsha owner) {
        super(owner);
        this.baseSpeed = 90;
        this.name = "Fu Yuan";
    }

    public void takeTurn() {
        super.takeTurn();
        this.summoner.FuYuanAttack(true);
    }
}
