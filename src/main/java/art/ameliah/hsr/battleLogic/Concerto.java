package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.log.lines.character.ConcertoEnd;
import art.ameliah.hsr.characters.robin.Robin;
import art.ameliah.hsr.powers.PermPower;

public class Concerto extends AbstractEntity {
    final Robin owner;

    public Concerto(Robin owner) {
        this.baseSpeed = 90;
        this.name = "Concerto";
        this.owner = owner;
        this.addPower(new ConcertoResetPower());
    }

    @Override
    public IBattle getBattle() {
        return this.owner.getBattle();
    }

    public class ConcertoResetPower extends PermPower {
        public ConcertoResetPower() {
            super("Concerto Reset Power");
        }

        @Override
        public void onTurnStart() {
            getBattle().addToLog(new ConcertoEnd());
            getBattle().getActionValueMap().remove(this.getOwner());
            Concerto.this.owner.onConcertoEnd();
            getBattle().setCurrentUnit(Concerto.this.owner);
            Concerto.this.owner.emit(BattleEvents::onTurnStart);
        }
    }
}
