package art.ameliah.hsr.battleLogic.combat.hit;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;

public record EnemyHit(BattleParticipant source, AbstractCharacter<?> target, int energy, float dmg, EnemyAttackLogic logic) {
}
