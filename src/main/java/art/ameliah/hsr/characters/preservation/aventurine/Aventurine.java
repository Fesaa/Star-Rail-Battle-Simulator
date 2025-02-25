package art.ameliah.hsr.characters.preservation.aventurine;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.enemy.EnemyAttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.character.aventurine.UseBlindBet;
import art.ameliah.hsr.battleLogic.log.lines.entity.GainCharge;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.TurnStartEvent;
import art.ameliah.hsr.events.enemy.PostEnemyAttack;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;
import art.ameliah.hsr.utils.Randf;

import java.util.List;

public class Aventurine extends AbstractCharacter<Aventurine> {
    public static final String NAME = "Aventurine";
    private static final int BLIND_BET_CAP = 10;

    protected CounterMetric<Integer> blindBetGained = metricRegistry.register(CounterMetric.newIntegerCounter("blind-bet-gained", "Blind bet gained"));
    protected CounterMetric<Integer> blindBetGainedFUA = metricRegistry.register(CounterMetric.newIntegerCounter("blind-bet-fua-gained", "Blind bet gained from FUA"));
    protected CounterMetric<Integer> blindBetCounter = metricRegistry.register(CounterMetric.newIntegerCounter("blind-bet-counter", "Left over blind bet"));

    final AventurineTalentPower talentPower = new AventurineTalentPower();
    final boolean SPNeutral;
    private final int BLIND_BET_THRESHOLD = 7;
    private final int blindBetFollowUpPerTurn = 3;
    private int blindBetFollowUpCounter = blindBetFollowUpPerTurn;

    public Aventurine(boolean SPNeutral) {
        super(NAME, 1203, 446, 655, 106, 80, ElementType.IMAGINARY, 110, 150, Path.PRESERVATION);

        this.SPNeutral = SPNeutral;
        this.addPower(new TracePower()
                .setStat(PowerStat.DEF_PERCENT, 35)
                .setStat(PowerStat.IMAGINARY_DMG_BOOST, 14.4f)
                .setStat(PowerStat.EFFECT_RES, 10));
        this.hasAttackingUltimate = true;

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new AventurineTurnGoal(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
    }

    public Aventurine() {
        this(true);
    }

    @Override
    protected void useSkill() {
        // Shields are not implemented
    }

    public void useBasic() {
        this.startAttack()
                .handle(dh -> {
                    AbstractEnemy target = this.getTarget(MoveType.BASIC);
                    dh.addEnemies(target);
                    dh.addTypes(DamageType.BASIC);
                    dh.logic(al -> al.hit(this.getTarget(MoveType.BASIC), 1, MultiplierStat.DEF, TOUGHNESS_DAMAGE_SINGLE_UNIT));
                }).execute();
    }

    public void useUltimate() {
        this.startAttack()
                .handle(dh -> {
                    AbstractEnemy target = this.getTarget(MoveType.ULTIMATE);
                    target.addPower(new AventurineUltDebuff());
                    dh.addTypes(DamageType.ULTIMATE);
                    dh.logic(target, al -> al.hit(this.getTarget(MoveType.ULTIMATE), 2.7f, MultiplierStat.DEF, TOUGHNESS_DAMAGE_THREE_UNITs));
                }).execute();

        int blindBetGain = getBattle().getGambleChanceRng().nextInt(7) + 1;
        increaseBlindBet(blindBetGain);
    }

    public void useFollowUp() {
        this.actionMetric.record(MoveType.FOLLOW_UP);

        int initialBlindBet = this.blindBetCounter.get();
        this.blindBetCounter.decrease(BLIND_BET_THRESHOLD);

        getBattle().addToLog(new UseBlindBet(this, initialBlindBet, this.blindBetCounter.get()));
        increaseEnergy(7, FUA_ENERGY_GAIN);

        this.startAttack()
                .handle(dh -> {
                    var targets = Randf.rand(getBattle().getEnemies(), 7, getBattle().getGetRandomEnemyRng());

                    dh.addEnemies(targets);
                    dh.addTypes(DamageType.FOLLOW_UP);
                    dh.logic(al -> {
                        targets.forEach(t -> al.hit(t, 0.25f, MultiplierStat.DEF, 3.3333333333333335f));
                    });
                }).execute();
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(p -> p.addPower(talentPower));
        getBattle().registerForEnemy(e -> e.addPower(talentPower));
        addPower(PermPower.create(PowerStat.CRIT_CHANCE, 48, "Aventurine Crit Chance Bonus"));
    }

    @Subscribe
    public void onTurnStart(TurnStartEvent e) {
        blindBetFollowUpCounter = blindBetFollowUpPerTurn;
    }

    public void increaseBlindBet(int amount) {
        this.blindBetGained.increase(amount);

        int initialBlindBet = this.blindBetCounter.get();
        this.blindBetCounter.increase(amount, BLIND_BET_CAP);

        getBattle().addToLog(new GainCharge(this, amount, initialBlindBet, this.blindBetCounter.get(), "Blind Bet"));
        if (this.blindBetCounter.get() >= BLIND_BET_THRESHOLD) {
            useFollowUp();
        }
    }

    public static class AventurineUltDebuff extends AbstractPower {
        public AventurineUltDebuff() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 3;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public float receiveConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            return 15;
        }
    }

    public class AventurineTalentPower extends AbstractPower {
        public AventurineTalentPower() {
            this.setName(this.getClass().getSimpleName());
            this.lastsForever = true;
            this.setStat(PowerStat.EFFECT_RES, 50); //assume 100% shield uptime
        }

        @Subscribe
        public void afterAttack(PostEnemyAttack event) {
            int count = event.getAttack().getTargets().stream().mapToInt(t -> {
                if (t == Aventurine.this) {
                    return 2;
                }
                return 1;
            }).sum();

            Aventurine.this.increaseBlindBet(count);
        }

        @Subscribe
        public void afterAttack(PostAllyAttack event) {
            if (event.getAttack().getSource() != Aventurine.this
                    && event.getAttack().getTypes().contains(DamageType.FOLLOW_UP) && blindBetFollowUpCounter > 0) {
                increaseBlindBet(1);
                blindBetFollowUpCounter--;
                Aventurine.this.blindBetGainedFUA.increment();
            }
        }
    }
}
