package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.character.PreDoHit;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class TheAshblazingGrandDuke extends AbstractRelicSetBonus {
    AbstractPower atkBonus;

    public TheAshblazingGrandDuke(AbstractCharacter<?> owner) {
        super(owner);
    }

    public TheAshblazingGrandDuke(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }

    @Override
    public void onEquip() {
        owner.addPower(new DukeDamagePower());
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack event) {
        if (event.getAttack().getTypes().contains(DamageType.FOLLOW_UP) && atkBonus != null && isFullSet) {
            owner.removePower(atkBonus.getName());
        }
    }

    @Subscribe
    public void beforeDoHit(PreDoHit e) {
        atkBonus = new DukeAtkBonus();
        if (e.getHit().getTypes().contains(DamageType.FOLLOW_UP) && isFullSet) {
            owner.addPower(atkBonus);
        }
    }

    public String toString() {
        if (isFullSet) {
            return "4 PC Duke";
        } else {
            return "2 PC Duke";
        }
    }

    public static class DukeDamagePower extends AbstractPower {
        public DukeDamagePower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 20;
                }
            }
            return 0;
        }
    }

    public static class DukeAtkBonus extends AbstractPower {
        public DukeAtkBonus() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 3;
            this.maxStacks = 8;

            this.setConditionalStat(PowerStat.ATK_PERCENT, (_) -> this.stacks * 6.0f);
        }
    }

}
