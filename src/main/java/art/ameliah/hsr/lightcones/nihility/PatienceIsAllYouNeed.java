package art.ameliah.hsr.lightcones.nihility;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class PatienceIsAllYouNeed extends AbstractLightcone {

    public PatienceIsAllYouNeed(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
        throw new UnsupportedOperationException("Not implemented, stacking speed buffs currently doesn't work.");
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.DAMAGE_BONUS, 24, "Patience Is All You Need DMG Boost"));
    }

    @Override
    public void beforeAttack(AttackLogic attack) {
        if (attack.getSource() == null) return;
        attack.getSource().addPower(new PatienceIsAllYouNeedSpeedBoost());
    }

    public static class PatienceIsAllYouNeedSpeedBoost extends PermPower {
        public PatienceIsAllYouNeedSpeedBoost() {
            this.setName(this.getClass().getSimpleName());
            this.maxStacks = 3;
            this.setStat(PowerStat.SPEED_PERCENT, 4.8f);
        }
    }
}
