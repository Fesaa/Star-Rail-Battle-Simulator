package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.List;

public class DuranDynastyOfRunningWolves extends AbstractRelicSetBonus {
    public DuranDynastyOfRunningWolves(AbstractCharacter<?> owner) {
        super(owner);
    }

    public DuranDynastyOfRunningWolves(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(c -> c.addPower(new DuranTrackerPower()));
    }

    public static class DuranTrackerPower extends PermPower {
        public DuranTrackerPower() {
            this.setName(this.getClass().getSimpleName());
        }

        @Subscribe
        public void beforeAttack(PreAllyAttack event) {
            if (event.getAttack().getTypes().contains(DamageType.FOLLOW_UP)) {
                for (AbstractCharacter<?> character : getBattle().getPlayers()) {
                    for (AbstractRelicSetBonus relicSetBonus : character.relicSetBonus) {
                        if (relicSetBonus instanceof DuranDynastyOfRunningWolves) {
                            character.addPower(new DuranStackPower());
                        }
                    }
                }
            }
        }
    }

    public static class DuranStackPower extends PermPower {
        public DuranStackPower() {
            this.setName(this.getClass().getSimpleName());
            this.maxStacks = 5;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 5 * stacks;
                }
            }
            return 0;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP && stacks == maxStacks) {
                    return 25;
                }
            }
            return 0;
        }
    }

}