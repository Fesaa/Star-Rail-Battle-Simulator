package art.ameliah.hsr.battleLogic;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.EnemyAttack;
import art.ameliah.hsr.battleLogic.combat.hit.Hit;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

public interface BattleEvents {

    /**
     * Called when combat starts.
     */
    default void onCombatStart() {
    }

    /**
     * Called when an enemy join the battle while it has already started
     */
    default void onEnemyJoinCombat(AbstractEnemy enemy) {
    }

    /**
     * Hook for {@link AbstractCharacter<>}, after attack ends
     *
     * @param attack the attack
     */
    default void afterAttacked(EnemyAttack attack) {
    }

    /**
     * Hook for {@link AbstractEnemy}, after attack ends
     *
     * @param attack the attack
     */
    default void afterAttacked(AttackLogic attack) {
    }

    /**
     * Called when the enemy has their weakness broken
     */
    default void onWeaknessBreak() {
    }

    /**
     * Hook for {@link AbstractCharacter<>}, before attack starts
     *
     * @param attack the attack
     */
    default void beforeAttack(AttackLogic attack) {
    }

    /**
     * Hook for {@link AbstractEnemy}, before attack starts
     *
     * @param attack the attack
     */
    default void beforeAttack(EnemyAttack attack) {
    }

    /**
     * Hook for {@link AbstractEnemy}, before attack starts
     *
     * @param attack the attack
     */
    default void beforeAttacked(AttackLogic attack) {
    }

    /**
     * Hook for {@link AbstractCharacter<>}, before attack starts
     *
     * @param attack the attack
     */
    default void beforeAttacked(EnemyAttack attack) {
    }

    /**
     * Hook for {@link AbstractCharacter<>}, before hit happens
     *
     * @param hit the hit going to happen
     */
    default void beforeDoHit(Hit hit) {
    }

    /**
     * Hook for {@link AbstractEnemy}, before hit happens
     *
     * @param hit the hit going to happen
     */
    default void beforeReceiveHit(Hit hit) {
    }

    /**
     * Hook for {@link AbstractCharacter<>}, called after the attack has finished
     *
     * @param attack the performed attack
     */
    default void afterAttack(AttackLogic attack) {
    }

    /**
     * Hook for {@link AbstractEnemy}, called after the attack has finished
     *
     * @param attack the attack
     */
    default void afterAttack(EnemyAttack attack) {
    }

    /**
     * Called before AbstractCharacter#takeTurn has been called.
     */
    default void onTurnStart() {
    }

    /**
     * Called after AbstractCharacter#takeTurn has been called.
     */
    default void onEndTurn() {
    }

    /**
     * Called when a character uses a basic attack, before the attack is executed.
     */
    default void onUseBasic() {
    }

    /**
     * Called after a character uses a basic attack.
     */
    default void afterUseBasic() {
    }

    /**
     * Called when a character uses a skill, before the skill is executed.
     */
    default void onUseSkill() {
    }

    /**
     * Called after a character uses a skill.
     */
    default void afterUseSkill() {
    }

    /**
     * Called when a character uses an ultimate, before the ultimate is executed.
     */
    default void onUseUltimate() {
    }

    /**
     * Called after a character uses an ultimate.
     */
    default void afterUseUltimate() {
    }

    /**
     * Called when the owner dies
     */
    default void onDeath() {
    }
}
