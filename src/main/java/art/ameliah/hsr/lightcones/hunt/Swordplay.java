package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import java.util.ArrayList;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;

public class Swordplay extends AbstractLightcone {

    private AbstractEnemy target;

    public Swordplay(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    public void onBeforeHitEnemy(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> types) {
        AbstractPower swordPlayDamagePower = new SwordplayDamagePower();
        if (target != enemy) {
            owner.removePower(swordPlayDamagePower.name);
            target = enemy;
        }
        owner.addPower(swordPlayDamagePower);
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    private static class SwordplayDamagePower extends PermPower {
        public SwordplayDamagePower() {
            this.name = this.getClass().getSimpleName();
            this.maxStacks = 5;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            return 16 * stacks;
        }
    }
}
