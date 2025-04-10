package art.ameliah.hsr.enemies.action;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.enemy.WeaknessBreakEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: Allow this to be a LOT more complex, dunno how...
public class EnemyActionSequence implements BattleParticipant {

    private final AbstractEnemy enemy;

    private final List<List<Runnable>> actions = new ArrayList<>();
    private int idx = 0;

    public EnemyActionSequence(AbstractEnemy enemy) {
        this.enemy = enemy;
        this.enemy.addListener(this);
    }

    public void addAction(Runnable... actions) {
        this.actions.add(Arrays.asList(actions));
    }

    public void runNext() {
        List<Runnable> actions = this.actions.get(idx);
        actions.forEach(Runnable::run);

        this.idx = (idx + 1) % this.actions.size();
    }

    @Subscribe
    public void onWeaknessBreak(WeaknessBreakEvent event) {
        this.idx = 0;
    }

    @Override
    public IBattle getBattle() {
        return this.enemy.getBattle();
    }
}
