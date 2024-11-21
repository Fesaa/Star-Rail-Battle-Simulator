package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.List;
import java.util.Set;

public interface BattleEvents {

    /**
     * Called when combat starts.
     */
    default void onCombatStart() {}

    /**
     * Called when a character is attacked, or when an enemy is attacked.
     *
     * @param character          The character that was attacked/is attacking
     * @param enemy              The enemy that attacked/is being attacked
     * @param types              The types of damage dealt
     * @param energyFromAttacked The energy gained from being attacked
     * @param totalDmg           The total dmg dealt to the character
     */
    default void onAttacked(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> types, int energyFromAttacked, float totalDmg) {}

    /**
     * Called when the enemy has their weakness broken
     */
    default void onWeaknessBreak() {}

    /**
     * Called before dmg is calculated.
     * Use this to apply buffs, debuffs, extra hits, etc. that need to happen in this attack
     * Add an attack to queue if you need to start a new one.
     * @param attack the attack
     */
    default void onAttack(Attack attack) {}

    /**
     * Called before being attacked
     * @param attack the attack going to happen
     */
    default void beforeAttacked(Attack attack) {}

    /**
     * Called before an enemy is hit by an attack
     * @param character The character that is attacking
     * @param enemy The enemy that is being attacked
     * @param damageTypes The types of damage that will be dealt
     */
    default void onBeforeHitEnemy(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {}

    /**
     * Called after enemies have received the onAttack event
     * @param character The character that attacked
     * @param enemiesHit The enemies that were hit
     * @param types The types of damage dealt
     */
    default void afterAttackFinish(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, List<DamageType> types) {}

    /**
     * Called before AbstractCharacter#takeTurn has been called.
     */
    default void onTurnStart() {}

    /**
     * Called after AbstractCharacter#takeTurn has been called.
     */
    default void onEndTurn() {}

    /**
     * Called when a character uses a basic attack, before the attack is executed.
     */
    default void onUseBasic() {}

    /**
     * Called after a character uses a basic attack.
     */
    default void afterUseBasic() {}

    /**
     * Called when a character uses a skill, before the skill is executed.
     */
    default void onUseSkill() {}

    /**
     * Called after a character uses a skill.
     */
    default void afterUseSkill() {}

    /**
     * Called when a character uses an ultimate, before the ultimate is executed.
     */
    default void onUseUltimate() {}

    /**
     * Called after a character uses an ultimate.
     */
    default void afterUseUltimate() {}

    /**
     * Called when the owner dies
     */
    default void onDeath() {}
}
