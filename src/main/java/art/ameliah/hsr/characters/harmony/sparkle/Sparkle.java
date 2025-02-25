package art.ameliah.hsr.characters.harmony.sparkle;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreSkill;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;

public class Sparkle extends AbstractCharacter<Sparkle> {

    public static final String NAME = "Sparkle";
    public static final String SKILL_POWER_NAME = "SparkleSkillPower";
    public static final String ULT_POWER_NAME = "SparkleUltPower";

    public Sparkle() {
        super(NAME, 1397, 524, 485, 101, 80, ElementType.QUANTUM, 110, 100, Path.HARMONY);

        this.basicEnergyGain = 30;
        this.addPower(new TracePower()
                .setStat(PowerStat.CRIT_DAMAGE, 24)
                .setStat(PowerStat.HP_PERCENT, 28)
                .setStat(PowerStat.EFFECT_RES, 10));

        this.registerGoal(0, new AlwaysSkillGoal<>(this));
        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public void useSkill() {
        AbstractPower skillPower = new SparkleSkillPower();
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character.isDPS) {
                character.removePower(skillPower.getName()); // remove the old power in case sparkle's crit damage changed so we get new snapshot of her buff
                character.addPower(skillPower);
                getBattle().AdvanceEntity(character, 50);
                lightcone.onSpecificTrigger(character, null);
            }
        }
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC, MoveType.BASIC, (e, al) -> al.hit(e, 1, TOUGHNESS_DAMAGE_SINGLE_UNIT));
    }

    public void useUltimate() {
        getBattle().generateSkillPoint(this, 4);
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            AbstractPower ultPower = new SparkleUltPower();
            character.addPower(ultPower);
        }
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        getBattle().increaseMaxSkillPoints(2);
        int numQuantumAllies = 0;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            PermPower nocturne = PermPower.create(PowerStat.ATK_PERCENT, 15, "Sparkle Atk Bonus");
            character.addPower(nocturne);
            if (character.elementType == ElementType.QUANTUM) {
                numQuantumAllies++;
            }
        }
        int quantumAtkBonus;
        if (numQuantumAllies == 3) {
            quantumAtkBonus = 30;
        } else if (numQuantumAllies == 2) {
            quantumAtkBonus = 15;
        } else {
            quantumAtkBonus = 5;
        }

        getBattle().registerForPlayers(p -> {
            if (p.elementType == ElementType.QUANTUM) {
                PermPower quantumNocturne = PermPower.create(PowerStat.ATK_PERCENT, quantumAtkBonus, "Sparkle Quantum Atk Bonus");
                p.addPower(quantumNocturne);
            }

            p.addPower(new SparkleTalentPowerTracker());
        });
    }

    public void useTechnique() {
        getBattle().generateSkillPoint(this, 3);
    }

    public static class SparkleTalentPowerTracker extends PermPower {
        public SparkleTalentPowerTracker() {
            super("SparkleTalentPowerTracker");
        }

        @Subscribe
        public void onUseSkill(PreSkill e) {
            getBattle().getPlayers().forEach(c -> c.addPower(new SparkleTalentPower()));
        }
    }

    public static class SparkleTalentPower extends AbstractPower {
        public SparkleTalentPower() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 2;
            this.maxStacks = 3;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            AbstractPower ultPower = new SparkleUltPower();
            if (character.hasPower(ultPower.getName())) {
                return stacks * 16;
            } else {
                return stacks * 6;
            }
        }
    }

    public static class SparkleUltPower extends AbstractPower {
        public SparkleUltPower() {
            this.setName(ULT_POWER_NAME);
            this.turnDuration = 2;
        }
    }

    public class SparkleSkillPower extends PermPower {
        public SparkleSkillPower() {
            super(SKILL_POWER_NAME);
            this.justApplied = true;
            this.setStat(PowerStat.CRIT_DAMAGE, (getTotalCritDamage() * 0.24f) + 45);
        }

        @Subscribe
        public void onTurnStart(TurnStartEvent e) {
            if (justApplied) {
                justApplied = false;
            } else {
                getOwner().removePower(this);
            }
        }
    }
}
