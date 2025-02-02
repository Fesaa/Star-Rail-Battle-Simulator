package art.ameliah.hsr.characters.abundance.huohuo;

import art.ameliah.hsr.battleLogic.BattleParticipant;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.ally.LowestHpGoal;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.goal.shared.turn.SkillCounterTurnGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.metrics.CounterMetric;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

public class Huohuo extends AbstractCharacter<Huohuo> implements SkillCounterTurnGoal.SkillCounterCharacter {

    private static final String NAME = "Huohuo";

    protected CounterMetric<Integer> talentProcs = metricRegistry.register(CounterMetric.newIntegerCounter("hh-talent-proc", "Number of Talent Procs"));
    private int talentCounter = 0;

    public Huohuo() {
        super(NAME, 1358, 602, 509, 98, 80, ElementType.WIND, 140, 100, Path.ABUNDANCE);

        this.addPower(new TracePower()
                .setStat(PowerStat.HP_PERCENT, 28)
                .setStat(PowerStat.FLAT_SPEED, 5)
                .setStat(PowerStat.EFFECT_RES, 18));

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new SkillCounterTurnGoal<>(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));
        this.registerGoal(0, new LowestHpGoal<>(this));
    }

    public void useSkill() {
        talentCounter = 2;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(new HuohuoTalentPower());
        }
        int idx = this.getAllyTargetIdx();

        double main = this.getFinalHP()*0.21 + 560f;
        double adj = this.getFinalHP()*0.168 + 448;

        getBattle().playerCallback(idx-1, c -> c.increaseHealth(this, adj));
        getBattle().playerCallback(idx, c -> c.increaseHealth(this, main));
        getBattle().playerCallback(idx+1, c -> c.increaseHealth(this, adj));
    }

    public void useBasic() {
        this.doAttack(DamageType.BASIC, MoveType.BASIC,
                (e, al) -> al.hit(e, 0.5f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT));
    }

    public void useUltimate() {
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            if (character != this) {
                character.increaseEnergy(character.maxEnergy * 0.2f, false, "from Huohuo Ult");
                character.addPower(TempPower.create(PowerStat.ATK_PERCENT, 40, 2, "Tail Atk Bonus"));
            }
        }
    }

    public void onCombatStart() {
        talentCounter = 1;
        getBattle().registerForPlayers(p -> p.addPower(new HuohuoTalentPower()));
    }

    public void onTurnStart() {

        talentCounter--;
        if (talentCounter <= 0) {
            for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                character.removePower(HuohuoTalentPower.NAME);
            }
        }
        tryUltimate();
    }

    @Override
    public int getSkillCounter() {
        return talentCounter;
    }

    private class HuohuoTalentPower extends AbstractPower {

        public static final String NAME = "Possession: Ethereal Metaflow";

        public HuohuoTalentPower() {
            this.setName(NAME);
            lastsForever = true;
        }

        private void trigger() {
            Huohuo.this.increaseEnergy(1, TALENT_ENERGY_GAIN);
            Huohuo.this.talentProcs.increment();
            double amount = Huohuo.this.getFinalHP()*0.045+120;
            ((AbstractCharacter<?>) this.owner).increaseHealth(Huohuo.this, amount);

            getBattle().getPlayers().stream()
                    .filter(p -> p.getCurrentHp().get() < p.getFinalHP()*0.5)
                    .forEach(p -> p.increaseHealth(Huohuo.this,amount));
        }

        @Override
        public void onTurnStart() {
            this.trigger();
        }

        @Override
        public void onUseUltimate() {
            this.trigger();
        }
    }
}
