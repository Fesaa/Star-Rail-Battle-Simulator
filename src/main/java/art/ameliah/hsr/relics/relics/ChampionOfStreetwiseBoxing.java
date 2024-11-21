package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

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
        }

        @Override
        public float getConditionalAtkBonus(AbstractCharacter<?> character) {
            return 5 * this.stacks;
        }

        @Override
        public void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> types, int energyFromAttacked, float totalDmg) {
            this.stacks = Math.min(this.stacks + 1, 5);
        }

        @Override
        public void onAttack(Attack attack) {
            this.stacks = Math.min(this.stacks + 1, 5);
        }
    }

}
