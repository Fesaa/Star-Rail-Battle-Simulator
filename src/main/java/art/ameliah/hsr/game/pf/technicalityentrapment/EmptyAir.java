package art.ameliah.hsr.game.pf.technicalityentrapment;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.wave.pf.PfBattle;
import art.ameliah.hsr.battleLogic.wave.pf.PureFictionBuff;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;

public class EmptyAir implements PureFictionBuff {

    @Override
    public void applyGritMechanic(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.addPower(new PermPower("Empty Air Grit Mechanic") {
            @Override
            public void afterAttackFinish(Attack attack) {
                if (attack.getTypes().contains(DamageType.ULTIMATE)) {
                    battle.increaseGridAmount(3 * attack.getTargets().size());
                }
            }
        }));
    }

    @Override
    public void applySurgingGritBuff(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.addPower(new EmptyAirPower()));
    }

    @Override
    public void removeSurgingGritBuff(PfBattle battle) {
        battle.getPlayers().forEach(player -> player.removePower(EmptyAirPower.NAME));
    }

    public static class EmptyAirPower extends PermPower {
        public static final String NAME = "EmptyAirPower";

        public EmptyAirPower() {
            super(NAME);
        }

        @Override
        public void afterUseSkill() {
            getBattle().generateSkillPoint((AbstractCharacter<?>) this.owner, 1);
        }

        @Override
        public void afterAttackFinish(Attack attack) {
            attack.getSource().addPower(new FeverishSurge());
        }
    }

    public static class FeverishSurge extends PermPower {
        public static final String NAME = "FeverishSurgePower";

        public FeverishSurge() {
            super(NAME);

            this.setStat(PowerStat.SPEED_PERCENT, 4);
            this.maxStacks = 10;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.SKILL) || damageTypes.contains(DamageType.ULTIMATE)) {
                return 4 * this.stacks;
            }

            return 0;
        }
    }
}
