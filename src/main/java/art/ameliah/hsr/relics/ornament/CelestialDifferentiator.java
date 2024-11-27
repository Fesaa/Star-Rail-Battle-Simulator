package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class CelestialDifferentiator extends AbstractRelicSetBonus {
    public CelestialDifferentiator(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public CelestialDifferentiator(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 16, "Celestial Differentiator CD bonus"));
    }

    @Override
    public void onCombatStart() {
        this.owner.addPower(new CelestialDifferentiatorCRBonus());
    }

    public static class CelestialDifferentiatorCRBonus extends PermPower {
        public CelestialDifferentiatorCRBonus() {
            super("Celestial Differentiator CR bonus");
            this.setStat(PowerStat.CRIT_CHANCE, 60);
        }

        @Override
        public void afterAttack(AttackLogic attack) {
            this.getOwner().removePower(this);
        }
    }
}
