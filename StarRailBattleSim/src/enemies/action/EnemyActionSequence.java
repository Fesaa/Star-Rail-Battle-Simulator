package enemies.action;

import battleLogic.BattleEvents;
import battleLogic.BattleParticipant;
import battleLogic.IBattle;
import battleLogic.log.lines.enemy.EnemyAction;
import enemies.AbstractEnemy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnemyActionSequence implements BattleParticipant, BattleEvents {

    private final AbstractEnemy enemy;

    private final List<List<Runnable>> actions = new ArrayList<>();
    private int idx = 0;

    public EnemyActionSequence(AbstractEnemy enemy) {
        this.enemy = enemy;
        this.enemy.addListener(this);
    }

    public void addAction(Runnable...actions) {
        this.actions.add(Arrays.asList(actions));
    }

    public void runNext() {
        List<Runnable> actions = this.actions.get(idx);
        actions.forEach(Runnable::run);

        this.idx = (idx + 1) % actions.size();
    }

    @Override
    public void onWeaknessBreak() {
        this.idx = 0;
    }

    @Override
    public IBattle getBattle() {
        return this.enemy.getBattle();
    }
}
