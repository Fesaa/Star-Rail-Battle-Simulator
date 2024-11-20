package art.ameliah.hsr.characters.goal.shared;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.bronya.Bronya;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.characters.hanya.Hanya;
import art.ameliah.hsr.characters.robin.Robin;
import art.ameliah.hsr.characters.ruanmei.RuanMei;
import art.ameliah.hsr.characters.sparkle.Sparkle;

public class DontUltMissingPowerGoal<C extends AbstractCharacter<C>> extends UltGoal<C> {

    private final String other;
    private final String[] powerNames;

    public DontUltMissingPowerGoal(C character, String other, String ...powerName) {
        super(character);
        this.other = other;
        this.powerNames = powerName;
    }

    public static<C extends AbstractCharacter<C>> DontUltMissingPowerGoal<C> robin(C character) {
        return new DontUltMissingPowerGoal<>(character, Robin.NAME, Robin.ULT_POWER_NAME);
    }

    public static<C extends AbstractCharacter<C>> DontUltMissingPowerGoal<C> sparkle(C character) {
        return new DontUltMissingPowerGoal<>(character, Sparkle.NAME, Sparkle.SKILL_POWER_NAME, Sparkle.ULT_POWER_NAME);
    }

    public static<C extends AbstractCharacter<C>> DontUltMissingPowerGoal<C> bronya(C character) {
        return new DontUltMissingPowerGoal<>(character, Bronya.NAME, Bronya.SKILL_POWER_NAME, Bronya.ULT_POWER_NAME);
    }

    public static<C extends AbstractCharacter<C>> DontUltMissingPowerGoal<C> ruanmei(C character) {
        return new DontUltMissingPowerGoal<>(character, RuanMei.NAME, RuanMei.ULT_POWER_NAME);
    }

    public static<C extends AbstractCharacter<C>> DontUltMissingPowerGoal<C> hanya(C character) {
        return new DontUltMissingPowerGoal<>(character, Hanya.NAME, Hanya.ULT_BUFF_NAME);
    }

    @Override
    public UltGoalResult determineAction() {
        if (getBattle().hasCharacter(other)) {

            for (String powerName : powerNames) {
                if (!this.character.hasPower(powerName)) {
                    return UltGoalResult.DONT;
                }
            }


        }

        return UltGoalResult.PASS;
    }

}
