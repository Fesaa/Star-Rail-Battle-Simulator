package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class TaliaKingdomOfBanditry extends AbstractRelicSetBonus {
    public TaliaKingdomOfBanditry(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public TaliaKingdomOfBanditry(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.BREAK_EFFECT, 16, "Talia Kingdom of Banditry Break bonus"));
    }

    @Override
    public void onCombatStart() {
        this.owner.addPower(new TaliaKingdomOfBanditryPower());
    }

    public static class TaliaKingdomOfBanditryPower extends PermPower {
        public TaliaKingdomOfBanditryPower() {
            super("Talia Kingdom of Banditry extra break bonus");

            this.setConditionalStat(PowerStat.BREAK_EFFECT, this::breakEffectBonus);
        }

        public float breakEffectBonus(AbstractCharacter<?> character) {
            if (character.getFinalSpeed() >= 145) {
                return 20;
            }

            return 0;
        }
    }

}
