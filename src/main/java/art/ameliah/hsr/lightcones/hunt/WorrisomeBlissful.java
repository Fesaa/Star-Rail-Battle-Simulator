package art.ameliah.hsr.lightcones.hunt;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class WorrisomeBlissful extends AbstractLightcone {

    public WorrisomeBlissful(AbstractCharacter<?> owner) {
        super(1058, 582, 463, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(new WorrisomeBlissfulPower());
    }

    @Override
    public void onAttack(Attack attack) {
        if (attack.getSource() != owner) return;
        if (attack.getTargets().isEmpty()) return;

        if (attack.getTypes().contains(DamageType.FOLLOW_UP)) {
            for (AbstractEnemy enemy : attack.getTargets()) {
                enemy.addPower(new TameState());
            }
        }
    }

    public static class WorrisomeBlissfulPower extends PermPower {
        public WorrisomeBlissfulPower() {
            this.name = this.getClass().getSimpleName();
            this.setStat(PowerStat.CRIT_CHANCE, 18);
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!damageTypes.contains(DamageType.FOLLOW_UP)) return 0;

            return 30;
        }
    }

    public class TameState extends PermPower {
        public TameState() {
            this.name = this.getClass().getSimpleName()+ "-" + WorrisomeBlissful.this.owner.name;
            this.maxStacks = 2;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public float receiveConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 12 * this.stacks;
        }
    }
}
