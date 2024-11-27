package art.ameliah.hsr.battleLogic.combat.enemy;

import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.base.AbstractAttack;
import art.ameliah.hsr.battleLogic.combat.hit.EnemyHit;
import art.ameliah.hsr.battleLogic.combat.result.EnemyHitResult;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnemyAttack extends AbstractAttack<AbstractEnemy, AbstractCharacter<?>, EnemyAttackLogic, EnemyDelayAttack> {

    public EnemyAttack(AbstractEnemy source) {
        super(source);
    }

    @Override
    protected EnemyDelayAttack newDelayAttack(AbstractEnemy source) {
        return new EnemyDelayAttack(source);
    }

    @Override
    protected void attack(EnemyDelayAttack dh) {
        EnemyAttackLogic al = new EnemyAttackLogic(this.source, this.targets, this.types, this::handleHit);

        this.source.emit(l -> l.beforeAttack(al));
        this.targets.forEach(t -> t.emit(l -> l.beforeAttack(al)));

        dh.getLogic().accept(al);

        this.targets.forEach(t -> t.emit(l -> l.afterAttacked(al)));

        this.source.emit(l -> l.afterAttacked(al));
    }

    private EnemyHitResult handleHit(EnemyHit hit) {
        if (hit.target().invincible()) {
            return new EnemyHitResult(0);
        }

        var res = hit.target().hit(hit);
        getBattle().addToLog(new Attacked(source, hit.target(), hit.dmg(), List.of()));
        return res;
    }

}
