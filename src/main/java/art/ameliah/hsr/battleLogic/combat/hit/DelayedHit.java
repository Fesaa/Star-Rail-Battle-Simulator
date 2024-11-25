package art.ameliah.hsr.battleLogic.combat.hit;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class DelayedHit implements BattleParticipant, HitHolder {

    private final AbstractCharacter<?> source;
    private final AbstractEnemy target;
    private final Set<DamageType> types;
    private final Consumer<DelayedHit> logic;

    private final List<Hit> hits = new ArrayList<>();

    private boolean hasAccepted = false;

    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }

    public void hitEnemies(Collection<AbstractEnemy> targets, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType... types) {
        for (var e : targets) {
            this.hitEnemy(e, multiplier, stat, toughnessDamage, types);
        }
    }

    public void hitEnemy(AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, DamageType... types) {
        this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, List.of(types));
    }

    public void hitEnemy(float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        this.hitEnemy(this.source, this.target, multiplier, stat, toughnessDamage, false, types);
    }

    public void hitEnemy(float multiplier, MultiplierStat stat, float toughnessDamage, DamageType... types) {
        this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, List.of(types));
    }

    public void hitEnemy(float multiplier, MultiplierStat stat, float toughnessDamage) {
        this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, this.types.stream().toList());
    }

    private void hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, boolean ignoreWeakness, List<DamageType> types) {
        if (target == null) {
            throw new NullPointerException("Target is null in delayed hit");
        }

        AllyHit hit = new AllyHit(source, target, multiplier, stat, types, toughnessDamage, source.elementType, ignoreWeakness);
        this.hits.add(hit);
        this.types.addAll(types);
    }


    @Override
    public List<Hit> getHits() {
        if (!this.hasAccepted) {
            this.hasAccepted = true;
            this.logic.accept(this);
        }
        return this.hits;
    }
}
