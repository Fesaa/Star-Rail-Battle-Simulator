package art.ameliah.hsr.relics.ornament;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.relics.AbstractRelicSetBonus;

import java.util.ArrayList;

public class DuranDynastyOfRunningWolves extends AbstractRelicSetBonus {
    public DuranDynastyOfRunningWolves(AbstractCharacter<?> owner) {
        super(owner);
    }
    public DuranDynastyOfRunningWolves(AbstractCharacter<?> owner, boolean isFullSet) {
        super(owner, isFullSet);
    }

    @Override
    public void onCombatStart() {
        getBattle().getPlayers().forEach(c -> c.addPower(new DuranTrackerPower()));
    }

    private static class DuranTrackerPower extends PermPower {
        public DuranTrackerPower() {
            this.name = this.getClass().getSimpleName();
        }
        @Override
        public void onBeforeUseAttack(ArrayList<DamageType> damageTypes) {
            if (damageTypes.contains(DamageType.FOLLOW_UP)) {
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

    private static class DuranStackPower extends PermPower {
        public DuranStackPower() {
            this.name = this.getClass().getSimpleName();
            this.maxStacks = 5;
        }
        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP) {
                    return 5 * stacks;
                }
            }
            return 0;
        }

        @Override
        public float getConditionalCritDamage(AbstractCharacter<?> character, AbstractEnemy enemy, ArrayList<DamageType> damageTypes) {
            for (DamageType type : damageTypes) {
                if (type == DamageType.FOLLOW_UP && stacks == maxStacks) {
                    return 25;
                }
            }
            return 0;
        }
    }

}