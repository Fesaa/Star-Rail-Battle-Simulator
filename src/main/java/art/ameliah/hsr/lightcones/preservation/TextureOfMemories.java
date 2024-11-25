package art.ameliah.hsr.lightcones.preservation;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class TextureOfMemories extends AbstractLightcone {

    public TextureOfMemories(AbstractCharacter<?> owner) {
        super(1058, 423, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.EFFECT_RES, 16, "Texture Of Memories Effect Resistance Boost"));
    }

    @Override
    public void afterAttacked(AttackLogic attack) {
        // TODO: Check shield
        // TODO: Reduce dmg taken power
    }
}
