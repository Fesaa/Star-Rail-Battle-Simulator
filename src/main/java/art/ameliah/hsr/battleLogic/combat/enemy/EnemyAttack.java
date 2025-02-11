package art.ameliah.hsr.battleLogic.combat.enemy;

import art.ameliah.hsr.battleLogic.combat.base.AbstractAttack;
import art.ameliah.hsr.battleLogic.combat.hit.EnemyHit;
import art.ameliah.hsr.battleLogic.combat.result.EnemyHitResult;
import art.ameliah.hsr.battleLogic.log.lines.character.Attacked;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;

import java.util.List;

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
        EnemyAttackLogic al = new EnemyAttackLogic(this.source, this.targets, this.types, this, this::handleHit);

        this.source.emit(l -> l.beforeAttack(al));
        this.targets.forEach(t -> t.emit(l -> l.beforeAttacked(al)));

        dh.getLogic().accept(al);

        this.targets.forEach(t -> t.emit(l -> l.afterAttacked(al)));

        this.source.emit(l -> l.afterAttack(al));
    }

    private EnemyHitResult handleHit(EnemyHit hit) {
        if (hit.target().invincible()) {
            return new EnemyHitResult(0);
        }

        var res = hit.target().hit(hit);
        this.dmgDealt += res.dmgDealt();
        getBattle().addToLog(new Attacked(source, hit.target(), hit.dmg(), List.of()));
        return res;
    }

}
