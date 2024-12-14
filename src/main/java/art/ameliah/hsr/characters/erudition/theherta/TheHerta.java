package art.ameliah.hsr.characters.erudition.theherta;

import art.ameliah.hsr.battleLogic.BattleEvents;
import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.DoMove;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysSkillGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.ult.DontUltMissingPowerGoal;
import art.ameliah.hsr.characters.goal.shared.ult.UltAtEndOfBattle;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TheHerta extends AbstractCharacter<TheHerta> {

    public static final String NAME = "The Herta";

    protected CounterMetric<Integer> inspiration = metricRegistry.register(CounterMetric.newIntegerCounter("the-herta-inspiration", "Inspiration stacks"));
    protected CounterMetric<Integer> interpretation = metricRegistry.register(CounterMetric.newIntegerCounter("the-herta-interpretation", "Interpretation stacks"));

    private boolean eruditionGoal = false;

    public TheHerta() {
        super(NAME, 1164, 679, 485, 99, 80, ElementType.ICE, 220, 75, Path.ERUDITION);
        this.isDPS = true;

        this.addPower(new TracePower()
                .setStat(PowerStat.ICE_DMG_BOOST, 22.4f)
                .setStat(PowerStat.ATK_PERCENT, 18)
                .setStat(PowerStat.FLAT_SPEED, 5)
        );

        this.registerGoal(0, new UltAtEndOfBattle<>(this));
        this.registerGoal(10, DontUltMissingPowerGoal.robin(this));
        this.registerGoal(15, new HertaUltGoal(this));
        this.registerGoal(20, new AlwaysUltGoal<>(this));

        this.registerGoal(0, new AlwaysSkillGoal<>(this));

        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    @Override
    public void onCombatStart() {
        int erudition = getBattle().getPlayers().stream().filter(p -> p.getPath().equals(Path.ERUDITION)).toList().size();
        if (erudition > 1) {
            this.eruditionGoal = true;
            getBattle().getPlayers().forEach(p -> p.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 80, "Message From Beyond the Veil")));
        }

        getBattle().getPlayers().forEach(p -> p.addPower(new AlooflyHonest()));
    }

    @Override
    public void onWaveStart() {
        getBattle().getEnemies().forEach(e -> e.addPower(new Interpretation(4)));
    }

    @Override
    public void afterAttack(AttackLogic attack) {
        AbstractEnemy target = attack.getTargets()
                .stream()
                .max(Comparator.comparingInt(e -> e.getPowerStacks(Interpretation.NAME)))
                .orElseThrow();

        target.addPower(new Interpretation());
    }

    @Override
    public void onUseUltimate() {
        this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 80, 2, "Told Ya! Magic Happens ATK boost"));
    }

    @Override
    public void onEnemyJoinCombat(AbstractEnemy enemy) {
        enemy.addPower(new Interpretation());
    }

    @Override
    public void onEnemyRemove(AbstractEnemy enemy, int idx) {
        int tally = enemy.getPowerStacks(Interpretation.NAME);
        List<AbstractEnemy> targets = getBattle().getEnemies().stream()
                .sorted(Comparator.comparingInt(Comparators::CompareRarity)).toList();

        for (var target : targets) {
            int copy = Math.min(42-target.getPowerStacks(Interpretation.NAME), tally);
            target.addPower(new Interpretation(copy));

            tally = Math.max(tally-copy, 0);
            if (tally == 0) {
                break;
            }
        }
    }

    @Override
    protected void skillSequence() {
        if (this.inspiration.get() > 0) {
            this.emit(BattleEvents::onUseSkill);
            this.enhancedSkill();
            this.emit(BattleEvents::afterUseSkill);
            return;
        }

        super.skillSequence();
    }

    @Override
    public void useTechnique() {
        this.addPower(TempPower.create(PowerStat.ATK_PERCENT, 40, 2, "The Herta Technique ATK buff"));
    }

    // TODO: Check toughness dmg when Hakush updates
    // Homdgcat:  Single 15 | All 5 | Other 10 idk what this means
    @Override
    protected void useSkill() {
        this.doAttack(DamageType.SKILL, da -> {
            AbstractEnemy target = this.getTarget(MoveType.SKILL);
            int idx = getBattle().getEnemies().indexOf(target);

            da.logic(idx, (e, al) -> {
                al.hit(e, 0.7f, 15);
                e.addPower(new Interpretation());
            });

            da.logic(idx-1, (e, al) -> al.hit(e, 0.7f));
            da.logic(idx, (e, al) -> al.hit(e, 0.7f));
            da.logic(idx+1, (e, al) -> al.hit(e, 0.7f));

            // Copy list so we don't get concurrency error
            for (var t : new ArrayList<>(da.getTargets())) {
                idx = getBattle().getEnemies().indexOf(t); // Not 100% sure if this is correct
                da.logic(idx-1, (e, al) -> al.hit(e, 0.7f));
                da.logic(idx, (e, al) -> al.hit(e, 0.7f));
                da.logic(idx+1, (e, al) -> al.hit(e, 0.7f));
            }
        });
    }

    protected void enhancedSkill()  {
        this.inspiration.decrement();
        this.actionMetric.record(MoveType.ENHANCED_SKILL);
        getBattle().addToLog(new DoMove(this, MoveType.ENHANCED_SKILL));
        getBattle().useSkillPoint(this, 1);
        increaseEnergy(skillEnergyGain, SKILL_ENERGY_GAIN);

        this.startAttack().handle(DamageType.SKILL, da -> {
            AbstractEnemy target = this.getTarget(MoveType.ENHANCED_SKILL);
            int idx = getBattle().getEnemies().indexOf(target);

            float primMul = target.getPowerStacks(Interpretation.NAME) * (this.eruditionGoal ? 0.16f : 0.08f);
            float adjMul = target.getPowerStacks(Interpretation.NAME) * (this.eruditionGoal ? 0.1f : 0.05f);
            da.logic(target, al -> {
                al.hit(target, 0.8f + primMul);
                target.addPower(new Interpretation());
            });

            da.logic(idx-1, (e, al) -> al.hit(e, 0.8f + adjMul));
            da.logic(idx, (e, al) -> al.hit(e, 0.8f + primMul));
            da.logic(idx+1, (e, al) -> al.hit(e, 0.8f + adjMul));

            // Copy list so we don't get concurrency error
            for (var t : new ArrayList<>(da.getTargets())) {
                idx = getBattle().getEnemies().indexOf(t); // Not 100% sure if this is correct
                da.logic(idx-1, (e, al) -> al.hit(e, 0.8f + adjMul));
                da.logic(idx, (e, al) -> al.hit(e, 0.8f+ primMul));
                da.logic(idx+1, (e, al) -> al.hit(e, 0.8f + adjMul));
            }

            da.logic(getBattle().getEnemies().stream().filter(e -> !e.equals(target)).toList(), (e, al) -> {
                al.hit(e, 0.4f + adjMul);
            });

            target.getPower(Interpretation.NAME).stacks = 1;
        }).execute();
    }

    @Override
    protected void useBasic() {
        this.doAttack(DamageType.BASIC, da -> {
            da.logic(this.getTarget(MoveType.BASIC), (e, al) -> {
                al.hit(e, 1f, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        });
    }

    @Override
    protected void useUltimate() {
        int interpretationTally = getBattle().getEnemies()
                .stream()
                .mapToInt(e -> e.getPowerStacks(Interpretation.NAME))
                .sum();

        this.interpretation.increase(interpretationTally, 99);
        getBattle().addToLog(new GainCharge(this, interpretationTally, this.interpretation, "Interpretation Ult charges"));

        getBattle().getEnemies().forEach(e -> e.removePower(Interpretation.NAME));

        var enemies = getBattle().getEnemies().stream().collect(Collectors.groupingBy(AbstractEnemy::getType));

        Map<EnemyType, Integer> tallyMap = new HashMap<>();

        EnemyType[] types = {EnemyType.Boss, EnemyType.Elite, EnemyType.Minion};

        for (EnemyType type : types) {
            List<AbstractEnemy> enemyList = enemies.getOrDefault(type, Collections.emptyList());
            if (enemyList.isEmpty()) continue;

            int tally = Math.min(interpretationTally, enemyList.size() * 42);
            interpretationTally = Math.max(interpretationTally-tally, 0);
            tallyMap.put(type, tally);
        }

        for (EnemyType type : types) {
            List<AbstractEnemy> enemyList = enemies.getOrDefault(type, Collections.emptyList());
            if (enemyList.isEmpty()) continue;

            int totalTally = tallyMap.get(type);
            int baseTally = totalTally / enemyList.size();
            int remainder = totalTally % enemyList.size();

            for (int i = 0; i < enemyList.size(); i++) {
                int tally = baseTally + (i == enemyList.size() - 1 ? remainder : 0);
                enemyList.get(i).addPower(new Interpretation(tally));
            }
        }

        float extraMul = this.interpretation.get() / 100f;
        this.startAttack().handle(DamageType.ULTIMATE, da -> {
            da.logic(getBattle().getEnemies(), (e, al) -> {
                al.hit(e, 2f + extraMul, TOUGHNESS_DAMAGE_TWO_UNITS);
            });
        }).afterAttackHook(() -> {
            this.inspiration.increment();
            getBattle().AdvanceEntity(this, 100);
        }).execute();
    }

    public class AlooflyHonest extends PermPower {

        public static final String NAME = "Aloofly Honest";

        public AlooflyHonest() {
            super(NAME);
        }

        @Override
        public void afterAttack(AttackLogic attack) {
            attack.getTargets().forEach(e -> e.addPower(new Interpretation()));

            // Max 5, at least 3
            int energy = Math.max(Math.min(attack.getTargets().size(), 5), 3) * 3;
            TheHerta.this.increaseEnergy(energy, false, "Aloofly Honest");

        }
    }

    public static class Interpretation extends PermPower {

        public static final String NAME = "Interpretation";

        public Interpretation(int stacks) {
            this();
            this.stacks = stacks;
        }

        public Interpretation() {
            super(NAME);
            this.maxStacks = 42;
        }

    }
}
