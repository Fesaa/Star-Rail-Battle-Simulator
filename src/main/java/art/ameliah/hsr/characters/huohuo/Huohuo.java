package art.ameliah.hsr.characters.huohuo;

import art.ameliah.hsr.battleLogic.BattleHelpers;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.AlwaysUltGoal;
import art.ameliah.hsr.characters.goal.shared.SkillCounterTurnGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.ArrayList;
import java.util.HashMap;

public class Huohuo extends AbstractCharacter<Huohuo> implements SkillCounterTurnGoal.SkillCounterCharacter {
    private static final String NAME = "Huohuo";
    
    final HuohuoTalentPower talentPower = new HuohuoTalentPower();
    int talentCounter = 0;
    private int numTalentProcs = 0;
    private final String numTalentProcsMetricName = "Number of Talent Procs";

    public Huohuo() {
        super(NAME, 1358, 602, 509, 98, 80, ElementType.WIND, 140, 100, Path.ABUNDANCE);

        this.addPower(new TracePower()
                .setStat(PowerStat.HP_PERCENT, 28)
                .setStat(PowerStat.FLAT_SPEED, 5)
                .setStat(PowerStat.EFFECT_RES, 18));

        this.registerGoal(0, new AlwaysUltGoal<>(this));
        this.registerGoal(0, new SkillCounterTurnGoal<>(this));
    }

    public void useSkill() {
        talentCounter = 2;
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(talentPower);
        }
    }
    public void useBasic() {
        this.startAttack()
                .hitEnemy(getBattle().getEnemyWithHighestHP(), 0.5F, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC)
                .execute();
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
        for (AbstractCharacter<?> character : getBattle().getPlayers()) {
            character.addPower(talentPower);
        }
    }

    public void onTurnStart() {
        
        talentCounter--;
        if (talentCounter <= 0) {
            for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                character.removePower(talentPower);
            }
        }
        tryUltimate();
    }

    public HashMap<String, String> getCharacterSpecificMetricMap() {
        HashMap<String, String> map = super.getCharacterSpecificMetricMap();
        map.put(numTalentProcsMetricName, String.valueOf(numTalentProcs));
        return map;
    }

    public ArrayList<String> getOrderedCharacterSpecificMetricsKeys() {
        ArrayList<String> list = super.getOrderedCharacterSpecificMetricsKeys();
        list.add(numTalentProcsMetricName);
        return list;
    }

    @Override
    public int getSkillCounter() {
        return talentCounter;
    }

    private class HuohuoTalentPower extends AbstractPower {
        public HuohuoTalentPower() {
            this.name = this.getClass().getSimpleName();
            lastsForever = true;
        }

        @Override
        public void onTurnStart() {
            Huohuo.this.increaseEnergy(1, TALENT_ENERGY_GAIN);
            numTalentProcs++;
        }

        @Override
        public void onUseUltimate() {
            Huohuo.this.increaseEnergy(1, TALENT_ENERGY_GAIN);
            numTalentProcs++;
        }
    }
}
