package art.ameliah.hsr.lightcones.remembrance;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.remembrance.Memomaster;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostSummon;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class TimeWovenIntoGold extends AbstractLightcone {
    public TimeWovenIntoGold(AbstractCharacter<?> owner) {
        super(1058, 635, 397, owner);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        AbstractPower power = new TimeWovenIntoGoldPower();
        power.stacks = 0; // starts at 0
        this.owner.addPower(power);
    }

    @Override
    public void onEquip() {
        this.owner.baseSpeed += 12;
    }

    public static class TimeWovenIntoGoldPower extends PermPower {
        public static final String NAME = "TimeWovenIntoGoldPower";

        public TimeWovenIntoGoldPower() {
            super(NAME);
            this.setConditionalStat(PowerStat.CRIT_DAMAGE, _ -> 9f * this.stacks);
            this.maxStacks = 6;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.BASIC) && stacks == maxStacks) {
                return 9f * this.stacks;
            }

            return 0;
        }

        @Subscribe
        public void beforeAttack(PreAllyAttack e) {
            this.owner.addPower(new TimeWovenIntoGoldPower());
            if (owner instanceof Memomaster<?> memomaster) {
                if (memomaster.getMemo() != null) {
                    memomaster.getMemo().addPower(new TimeWovenIntoGoldPower());
                }
            }
            if (owner instanceof Memosprite<?, ?> memosprite) {
                if (memosprite.getMaster() != null) {
                    memosprite.getMaster().addPower(new TimeWovenIntoGoldPower());
                }
            }
        }

        @Subscribe
        public void afterSummon(PostSummon e) {
            AbstractPower power = new TimeWovenIntoGoldPower();
            power.stacks = this.stacks;
            e.getMemosprite().addPower(power);
        }
    }
}
