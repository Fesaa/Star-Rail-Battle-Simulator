package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class ChampionOfStreetwiseBoxing extends AbstractRelicSetBonus {
    public ChampionOfStreetwiseBoxing(AbstractCharacter<?> owner, boolean fullSet) {
        super(owner, fullSet);
    }

    public ChampionOfStreetwiseBoxing(AbstractCharacter<?> owner) {
        super(owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.PHYSICAL_DMG_BOOST, 10, "Champion of Streetwise Boxing Physical Boost"));
    }

    @Override
    public void onCombatStart() {
        if (!this.isFullSet) return;

        this.owner.addPower(new ChampionOfStreetwiseBoxing4PCPower());
    }

    public static class ChampionOfStreetwiseBoxing4PCPower extends PermPower {
        public ChampionOfStreetwiseBoxing4PCPower() {
            super("Champion of Streetwise Boxing 4PC Power");

            this.setConditionalStat(PowerStat.ATK_PERCENT, _ -> 5f * this.stacks);
        }

        @Override
        public void afterAttacked(AttackLogic attack) {
            this.stacks = Math.min(this.stacks + 1, 5);
        }

        @Override
        public void beforeAttack(AttackLogic attack) {
            this.stacks = Math.min(this.stacks + 1, 5);
        }
    }

}
