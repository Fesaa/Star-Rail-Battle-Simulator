package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.characters.nihility.pela.Pela;
import art.ameliah.hsr.enemies.AbstractEnemy;

public class DontUltMissingDebuffGoal<C extends AbstractCharacter<C>> extends UltGoal<C> {

    private final String other;
    private final String[] debuffNames;

    public DontUltMissingDebuffGoal(C character, String other, String... debuffName) {
        super(character);
        this.other = other;
        this.debuffNames = debuffName;
    }

    public static <C extends AbstractCharacter<C>> DontUltMissingDebuffGoal<C> pela(C character) {
        return new DontUltMissingDebuffGoal<>(character, Pela.NAME, Pela.ULT_DEBUFF_NAME);
    }

    @Override
    public UltGoalResult determineAction() {
        if (!getBattle().hasCharacter(other)) {
            return UltGoalResult.PASS;
        }

        AbstractEnemy enemy = getBattle().getMiddleEnemy();
        for (String debuffName : debuffNames) {
            if (!enemy.hasPower(debuffName)) {
                return UltGoalResult.DONT;
            }
        }

        return UltGoalResult.PASS;
    }
}
