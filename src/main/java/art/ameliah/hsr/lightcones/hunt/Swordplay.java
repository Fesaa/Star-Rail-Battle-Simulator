package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreDoHit;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;

public class Swordplay extends AbstractLightcone {

    private AbstractEnemy target;

    public Swordplay(AbstractCharacter<?> owner) {
        super(953, 476, 331, owner);
    }

    @Subscribe
    public void beforeDoHit(PreDoHit e) {
        AbstractPower swordPlayDamagePower = new SwordplayDamagePower();
        if (this.target != e.getHit().getTarget()) {
            owner.removePower(swordPlayDamagePower.getName());
            this.target = e.getHit().getTarget();
        }
        owner.addPower(swordPlayDamagePower);
    }

    public static class SwordplayDamagePower extends PermPower {
        public SwordplayDamagePower() {
            this.setName(this.getClass().getSimpleName());
            this.maxStacks = 5;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 16 * stacks;
        }
    }
}
