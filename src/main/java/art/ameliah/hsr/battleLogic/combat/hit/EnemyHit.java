package art.ameliah.hsr.battleLogic.combat.hit;

import art.ameliah.hsr.characters.AbstractCharacter;

public record EnemyHit(AbstractCharacter<?> target, int energy, float dmg) {
}
