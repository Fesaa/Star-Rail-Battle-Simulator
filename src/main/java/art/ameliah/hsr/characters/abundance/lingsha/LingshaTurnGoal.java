package art.ameliah.hsr.characters.abundance.lingsha;

import art.ameliah.hsr.characters.goal.TurnGoal;

public class LingshaTurnGoal extends TurnGoal<Lingsha> {
    public LingshaTurnGoal(Lingsha character) {
        super(character);
    }

    @Override
    public TurnGoalResult determineAction() {
        boolean fuYuanGood = this.character.getFuYuanHitCount().get() <= Lingsha.FUYUAN_MAX_HIT_COUNT - Lingsha.SKILL_HIT_COUNT_GAIN;
        if (getBattle().getSkillPoints() > 0 && fuYuanGood) {
            return TurnGoalResult.SKILL;
        }

        return TurnGoalResult.BASIC;
    }
}
