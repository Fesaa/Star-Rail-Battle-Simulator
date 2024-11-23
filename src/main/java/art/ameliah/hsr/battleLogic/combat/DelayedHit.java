package art.ameliah.hsr.battleLogic.combat;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
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

    @Override
    public IBattle getBattle() {
        return this.source.getBattle();
    }

    public void hitEnemy(float multiplier, MultiplierStat stat, float toughnessDamage, List<DamageType> types) {
        this.hitEnemy(this.source, this.target, multiplier, stat, toughnessDamage, false, types);
    }

    public void hitEnemy(float multiplier, MultiplierStat stat, float toughnessDamage, DamageType ...types) {
        this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, List.of(types));
    }

    public void hitEnemy(float multiplier, MultiplierStat stat, float toughnessDamage) {
        this.hitEnemy(this.source, target, multiplier, stat, toughnessDamage, false, this.types.stream().toList());
    }

    private void hitEnemy(AbstractCharacter<?> source, AbstractEnemy target, float multiplier, MultiplierStat stat, float toughnessDamage, boolean ignoreWeakness, List<DamageType> types) {
        AllyHit hit = new AllyHit(source, target, multiplier, stat, types, toughnessDamage, source.elementType, ignoreWeakness);
        this.hits.add(hit);
    }


    @Override
    public List<Hit> getHits() {
        this.logic.accept(this);
        return this.hits;
    }
}
