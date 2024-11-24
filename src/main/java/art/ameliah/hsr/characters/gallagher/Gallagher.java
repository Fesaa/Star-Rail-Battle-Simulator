package art.ameliah.hsr.characters.gallagher;

import art.ameliah.hsr.battleLogic.combat.Attack;
import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.turn.AlwaysBasicGoal;
import art.ameliah.hsr.characters.goal.shared.ult.AlwaysUltGoal;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;

public class Gallagher extends AbstractCharacter<Gallagher> {
    public static final String NAME = "Gallagher";

    private boolean isEnhanced = false;

    public Gallagher() {
        super(NAME, 1305, 529, 441, 98, 80, ElementType.FIRE, 110, 100, Path.ABUNDANCE);

        this.addPower(new TracePower()
                .setStat(PowerStat.HP_PERCENT, 18)
                .setStat(PowerStat.BREAK_EFFECT, 13.3f)
                .setStat(PowerStat.EFFECT_RES, 28));
        this.hasAttackingUltimate = true;

        this.registerGoal(0, new AlwaysBasicGoal<>(this));
        this.registerGoal(0, new AlwaysUltGoal<>(this));
    }

    @Override
    public void useSkill() {
        // Healing is not implemented
    }

    public void useBasic() {
        Attack attack = this.startAttack();

        AbstractEnemy enemy = getBattle().getMiddleEnemy();
        if (isEnhanced) {
            attack.hitEnemy(enemy, 2.75f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT * 3, DamageType.BASIC);
            AbstractPower atkDebuff = new TempPower();
            atkDebuff.type = AbstractPower.PowerType.DEBUFF;
            atkDebuff.turnDuration = 2;
            atkDebuff.setName("Gallagher Atk Debuff");
            enemy.addPower(atkDebuff);
            isEnhanced = false;
        } else {
            attack.hitEnemy(enemy, 1.1f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_SINGLE_UNIT, DamageType.BASIC);
        }
        attack.execute();
    }

    public void useUltimate() {
        this.startAttack()
                .hitEnemies(getBattle().getEnemies(), 1.65f, MultiplierStat.ATK, TOUGHNESS_DAMAGE_TWO_UNITS, DamageType.ULTIMATE)
                .execute();

        for (AbstractEnemy enemy : getBattle().getEnemies()) {
            AbstractPower besotted = new Besotted();
            enemy.addPower(besotted);
            isEnhanced = true;
        }
        getBattle().AdvanceEntity(this, 100);
    }

    public void onCombatStart() {
        increaseEnergy(20, "from E1");
        PermPower e6buff = new PermPower("Gallagher E6 Buff");
        e6buff.setStat(PowerStat.BREAK_EFFECT, 20);
        e6buff.setStat(PowerStat.WEAKNESS_BREAK_EFF, 20);
        addPower(e6buff);
    }

    private static class Besotted extends AbstractPower {
        public Besotted() {
            this.setName(this.getClass().getSimpleName());
            this.turnDuration = 3;
            this.type = PowerType.DEBUFF;
        }

        @Override
        public float getConditionalDamageTaken(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.BREAK)) {
                return 13.2f;
            }
            return 0;
        }
    }
}
