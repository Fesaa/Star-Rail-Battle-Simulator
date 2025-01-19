package art.ameliah.hsr.powers;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.log.lines.entity.RefreshPower;
import art.ameliah.hsr.battleLogic.log.lines.entity.StackPower;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractPower implements BattleEvents, BattleParticipant {

    public final boolean durationBasedOnSelfTurns = true;
    private final Map<PowerStat, Float> stats = new HashMap<>();
    public int turnDuration;
    public PowerType type = PowerType.BUFF;
    public boolean lastsForever = false;
    public boolean justApplied = false;
    public int maxStacks = 0;
    public int stacks = 1;
    @Setter
    protected String name;
    @Getter
    private AbstractEntity owner;

    public AbstractPower() {
    }

    public AbstractPower(String name) {
        this.name = name;
    }

    public void setOwner(AbstractEntity owner) {
        this.owner = owner;
        // The owner received the buff in their own turn
        if (getBattle() != null && getBattle().getCurrentUnit() != null) {
            this.justApplied = getBattle().getCurrentUnit().equals(this.owner);
        }
    }

    public String getName() {
        if (this.name == null) {
            return this.getClass().getSimpleName();
        }
        return this.name;
    }

    public void merge(AbstractPower other) {
        if (this.maxStacks > 0) {
            this.stacks = Math.min(this.stacks + 1, this.maxStacks);
        }

        this.turnDuration = other.turnDuration;
        if (getBattle() != null && getBattle().getCurrentUnit() != null) {
            this.justApplied = getBattle().getCurrentUnit().equals(this.owner);
        }

        if (getBattle().getLessMetrics()) {
            return;
        }

        if (this.maxStacks > 0) {
            getBattle().addToLog(new StackPower(this.owner, this));
        } else {
            getBattle().addToLog(new RefreshPower(this.owner, this));
        }

    }

    @Override
    public IBattle getBattle() {
        return this.getOwner().getBattle();
    }

    public float getConditionalAtkBonus(AbstractCharacter<?> character) {
        return 0;
    }

    public float getConditionalERR(AbstractCharacter<?> character) {
        return 0;
    }

    /**
     * Increase damage dealt by the character when attacking the enemy
     */
    public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
        return 0;
    }

    /**
     * Increases incoming damage
     */
    public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
        return 0;
    }

    /**
     * Increase damage dealt by the character when attacking the enemy
     * Use this for DMG Boosts, that are applied as a debug on targets
     */
    public float receiveConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
        return 0;
    }

    public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
        return 0;
    }

    public float getConditionalCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
        return 0;
    }

    public float receiveConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
        return 0;
    }

    public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
        return 0;
    }

    public float setFixedCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes, float currentCrit) {
        return currentCrit;
    }

    public float setFixedCritDmg(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes, float currentCritDmg) {
        return currentCritDmg;
    }

    /**
     * @return A def% bonus
     */
    public float getConditionalDefenseBonus(AbstractCharacter<?> character) {
        return 0;
    }

    /**
     *
     * @return A SPD% bonus
     */
    public float getConditionalSpeedBoost(AbstractCharacter<?> character) {
        return 0;
    }

    /**
     * @return A break% bonus
     */
    public float getConditionalBreakEffectBonus(AbstractCharacter<?> character) {
        return 0;
    }

    /**
     * @return A flat HP bonus
     */
    public float getConditionalFlatHpBonus(AbstractCharacter<?> character) {
        return 0;
    }

    @Override
    public void onEndTurn() {
        if (!lastsForever && durationBasedOnSelfTurns) {
            if (justApplied) {
                justApplied = false;
            } else {
                turnDuration--;
            }
        }

        if (!lastsForever && turnDuration <= 0) {
            if (getStat(PowerStat.SPEED_PERCENT) > 0 || getStat(PowerStat.FLAT_SPEED) > 0) {
                getBattle().DecreaseSpeed(this.getOwner(), this);
            } else {
                this.getOwner().removePower(this);
            }
        }
    }

    public void onRemove() {

    }

    /**
     * Get the value of a stat
     *
     * @param stat The stat to get
     * @return The value of the stat, 0 if the stat is not set
     */
    // TODO: Should be * this by the amount of stacks?
    // dunno if that would break anything
    public float getStat(PowerStat stat) {
        return this.stats.getOrDefault(stat, 0f);
    }

    /**
     * Increase the value of a stat, calls getStat internally
     *
     * @param stat  The stat to increase
     * @param value The value to increase by
     */
    public void increaseStat(PowerStat stat, float value) {
        this.setStat(stat, this.getStat(stat) + value);
    }

    /**
     * Set the value of a stat, this overwrites the previous value
     *
     * @param stat  The stat to set
     * @param value The value to set
     * @return The power object
     */
    public AbstractPower setStat(PowerStat stat, float value) {
        this.stats.put(stat, value);
        return this;
    }

    @Override
    public String toString() {
        String boosts = this.stats.entrySet().stream()
                .map(e -> String.format("%s=%,.2f", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", "));
        return String.format("%s(turnDuration=%d, stacks=%d, maxStacks=%d, justApplied=%b, type=%s, durationBasedOnSelfTurns=%b, lastsForever=%b, %s)",
                this.getName(), this.turnDuration, this.stacks, this.maxStacks, this.justApplied, this.type, this.durationBasedOnSelfTurns, this.lastsForever, boosts);
    }

    // TODO: Implement DOT
    public enum PowerType {
        BUFF, DEBUFF, DOT
    }

}
