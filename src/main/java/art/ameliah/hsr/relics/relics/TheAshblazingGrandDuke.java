package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

public class TheAshblazingGrandDuke extends AbstractRelicSetBonus {
    public TheAshblazingGrandDuke(AbstractCharacter<?> owner) {
        super(owner);
    }
    public TheAshblazingGrandDuke(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }
    AbstractPower atkBonus;

    @Override
    public void onEquip() {
        owner.addPower(new DukeDamagePower());
    }
    @Override
    public void onBeforeUseAttack(ArrayList<DamageType> damageTypes) {
        if (damageTypes.contains(DamageType.FOLLOW_UP) && atkBonus != null && isFullSet) {
            owner.removePower(atkBonus.name);
        }
    }

    @Override
    public void onBeforeHitEnemy(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
        atkBonus = new DukeAtkBonus();
        if (damageTypes.contains(DamageType.FOLLOW_UP) && isFullSet) {
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

    private static class DukeDamagePower extends AbstractPower {
        public DukeDamagePower() {
            this.name = this.getClass().getSimpleName();
            this.lastsForever = true;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 20;
                }
            }
            return 0;
        }
    }

    private static class DukeAtkBonus extends AbstractPower {
        public DukeAtkBonus() {
            this.name = this.getClass().getSimpleName();
            this.turnDuration = 3;
            this.maxStacks = 8;
        }
        @Override
        public float getConditionalAtkBonus(AbstractCharacter<?> character) {
            return stacks * 6.0f;
        }
    }

}