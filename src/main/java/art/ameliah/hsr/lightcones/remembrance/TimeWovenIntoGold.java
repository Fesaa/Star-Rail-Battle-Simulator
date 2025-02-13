package art.ameliah.hsr.lightcones.remembrance;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.remembrance.Memomaster;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class TimeWovenIntoGold extends AbstractLightcone {
    public TimeWovenIntoGold(AbstractCharacter<?> owner) {
        super(1058, 635, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.baseSpeed += 12;
    }

    @Override
    public void onCombatStart() {
        this.owner.addPower(new TimeWovenIntoGoldPower());
    }

    public static class TimeWovenIntoGoldPower extends PermPower {
        public static final String NAME = "TimeWovenIntoGoldPower";

        public TimeWovenIntoGoldPower() {
            super(NAME);

            this.setConditionalStat(PowerStat.CRIT_DAMAGE, _ -> 9f * this.stacks);
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (!(damageTypes.contains(DamageType.BASIC))) {
                return 0;
            }

            return 9f * this.stacks;
        }

        @Override
        public void afterAttack(AttackLogic attack) {
            if (!(this.owner instanceof Memomaster<?> memomaster)) {
                return;
            }

            Memosprite<?> memo = memomaster.getMemo();
            if (memo == null) {
                return;
            }

             boolean wasDualAttack = attack.getAttack().getPastHits().stream()
                     .anyMatch(hit -> hit.getSource().equals(memo));

            if (!wasDualAttack) {
                return;
            }

            this.stacks = Math.min(this.stacks+1, 6);
        }
    }
}
