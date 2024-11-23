package art.ameliah.hsr.relics.relics;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.Hit;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

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
    public void onAttack(Attack attack) {
        if (attack.getTypes().contains(DamageType.FOLLOW_UP) && atkBonus != null && isFullSet) {
            owner.removePower(atkBonus.getName());
        }
    }

    @Override
    public void onBeforeHitEnemy(Hit hit) {
        atkBonus = new DukeAtkBonus();
        if (hit.getTypes().contains(DamageType.FOLLOW_UP) && isFullSet) {
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

    private static class DukeAtkBonus extends AbstractPower {
        public DukeAtkBonus() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 3;
            this.maxStacks = 8;
        }
        @Override
        public float getConditionalAtkBonus(AbstractCharacter<?> character) {
            return stacks * 6.0f;
        }
    }

}
