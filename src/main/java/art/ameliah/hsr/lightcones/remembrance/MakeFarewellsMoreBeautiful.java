package art.ameliah.hsr.lightcones.remembrance;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.remembrance.Memomaster;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.HPLost;
import art.ameliah.hsr.events.character.MemospriteDeath;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

public class MakeFarewellsMoreBeautiful extends AbstractLightcone {

    private boolean onCooldown = false;

    public MakeFarewellsMoreBeautiful(AbstractCharacter<?> owner) {
        super(1270, 529, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.HP_PERCENT, 30, "Make Farewells More Beautiful HP Boost"));
    }

    @Subscribe
    public void onHPLoss(HPLost event) {
        if (getBattle().getCurrentUnit() == this.owner) {
            this.owner.addPower(TempPower.create(PowerStat.DEFENSE_IGNORE, 24, 2, "Make Farewells More Beautiful Def Ignore"));
            if (this.owner instanceof Memomaster<?> memomaster) {
                var memo = memomaster.getMemo();
                if (memo != null) {
                    memo.addPower(TempPower.create(PowerStat.DEFENSE_IGNORE, 24, 2, "Make Farewells More Beautiful Def Ignore"));
                }
            }
        }
    }

    @Subscribe
    public void onMemospriteSummon(PostSummon event) {
        this.onCooldown = false;
    }

    @Subscribe
    public void onMemospriteDeparture(MemospriteDeath event) {
        if (this.onCooldown) {
            return;
        }

        this.onCooldown = true;
        getBattle().AdvanceEntity(this.owner, 12);
    }
}
