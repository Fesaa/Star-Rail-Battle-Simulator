package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class HunterOfTheGlacialForest extends AbstractRelicSetBonus {
    public HunterOfTheGlacialForest(AbstractCharacter<?> owner) {
        super(owner);
    }

    public HunterOfTheGlacialForest(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.ICE_DMG_BOOST, 10, "Hunter of the Glacial Forest Ice Boost"));
    }

    // TODO: onAfterUseUltimate
    @Override
    public void afterAttackFinish(Attack attack) {
        if (!attack.getTypes().contains(DamageType.ULTIMATE)) return;

        TempPower ultPower = TempPower.create(PowerStat.CRIT_DAMAGE, 25, 2, "Hunter of the Glacial Forest Ultimate CD Boost");
        ultPower.justApplied = true;
        this.owner.addPower(ultPower);
    }
}
