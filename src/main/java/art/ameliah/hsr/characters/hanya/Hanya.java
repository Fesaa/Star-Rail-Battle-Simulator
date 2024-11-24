package art.ameliah.hsr.characters.hanya;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.log.lines.character.hanya.BurdenLog;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

public class Hanya extends AbstractCharacter<Hanya> {

    public static final String NAME = "Hanya";
    public static final String ULT_BUFF_NAME = "Hanya Ult Buff";

    public Hanya() {
        super(NAME, 917, 564, 353, 110, 80, ElementType.PHYSICAL, 140, 100, Path.HARMONY);

        this.addPower(new TracePower()
                .setStat(PowerStat.ATK_PERCENT, 28)
                .setStat(PowerStat.FLAT_SPEED, 9)
                .setStat(PowerStat.HP_PERCENT, 10));

        this.registerGoal(0, new AlwaysSkillGoal<>(this));
        this.registerGoal(0, new AlwaysUltGoal<>(this));
    }

    public void useSkill() {
        Attack attack = this.startAttack();
        AbstractEnemy enemy = getBattle().getEnemyWithHighestHP();
        attack.hitEnemy(enemy, 2.64f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.SKILL);

        AbstractPower burden = new BurdenPower();
        burden.setOwner(enemy);
        if (enemy.hasPower(burden.getName())) {
            enemy.removePower(burden.getName());
        }
        enemy.addPower(burden);

        TempPower speedPower = TempPower.create(PowerStat.SPEED_PERCENT, 20, 1, "Hanya Skill Speed Power");
        speedPower.justApplied = true;
        getBattle().IncreaseSpeed(this, speedPower);

        attack.execute();
    }

    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
    }

    public void useUltimate() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS) {
                AbstractPower existingPower = character.getPower(ULT_BUFF_NAME);
                if (existingPower != null) {
                    getBattle().DecreaseSpeed(character, existingPower);
                }
                TempPower ultBuff = new TempPower(3, ULT_BUFF_NAME);
                ultBuff.setStat(PowerStat.ATK_PERCENT, 65);
                ultBuff.setStat(PowerStat.FLAT_SPEED, this.getFinalSpeed() * 0.21f);
                getBattle().IncreaseSpeed(character, ultBuff);
                break;
            }
        }
    }

    private class BurdenPower extends AbstractPower {

        private final int hitsToTrigger = 2;
        private int triggersLeft = 2;
        private int hitCount = 0;

        public BurdenPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
        }

        @Override
        public void beforeAttacked(Attack attack) {
            if (attack.getTypes().contains(DamageType.BASIC) || attack.getTypes().contains(DamageType.SKILL) || attack.getTypes().contains(DamageType.ULTIMATE)) {
                TempPower talentPower = TempPower.create(PowerStat.DAMAGE_BONUS, 43, 2, "Hanya Talent Power");
                talentPower.justApplied = true;
                attack.getSource().addPower(talentPower);
            }
        }

        @Override
        public void afterAttacked(Attack attack) {
            if (attack.getTypes().contains(DamageType.BASIC) || attack.getTypes().contains(DamageType.SKILL) || attack.getTypes().contains(DamageType.ULTIMATE)) {
                hitCount++;
                getBattle().addToLog(new BurdenLog(hitCount, hitsToTrigger));

                if (hitCount >= hitsToTrigger) {
                    triggersLeft--;
                    getBattle().generateSkillPoint(attack.getSource(), 1);
                    Hanya.this.increaseEnergy(2, TALENT_ENERGY_GAIN);

                    TempPower tracePower = TempPower.create(PowerStat.ATK_PERCENT, 10, 1, "Hanya Trace Atk Power");
                    attack.getSource().addPower(tracePower);

                    hitCount = 0;
                    if (triggersLeft <= 0) {
                        getOwner().removePower(this);
                    }
                }
            }
        }
    }
}
