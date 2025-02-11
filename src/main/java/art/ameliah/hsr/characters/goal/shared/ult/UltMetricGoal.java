package art.ameliah.hsr.characters.goal.shared.ult;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.goal.UltGoal;
import art.ameliah.hsr.metrics.CounterMetric;

import java.util.function.Predicate;

public class UltMetricGoal<C extends AbstractCharacter<C>, T> extends UltGoal<C> {

    private final CounterMetric<T> counter;
    private final Predicate<CounterMetric<T>> predicate;

    /**
     * Ult goal that returns UltGoalResult.DONT if the predicate fails
     * @param character character
     * @param metric metric counter
     * @param predicate predicate to test against
     */
    public UltMetricGoal(C character, CounterMetric<T> metric, Predicate<CounterMetric<T>> predicate) {
        super(character);

        this.counter = metric;
        this.predicate = predicate;
    }

    /**
     * Ult goal, that returns UltGoalResult.DONT if the metric counter is not equal to the value
     * @param character character
     * @param metric metric counter
     * @param value value to equal
     */
    public UltMetricGoal(C character, CounterMetric<T> metric, T value) {
        super(character);

        this.counter = metric;
        this.predicate = m -> m.get() == value;
    }

    @Override
    public UltGoalResult determineAction() {
        if (!this.predicate.test(counter)) {
            return UltGoalResult.DONT;
        }

        return UltGoalResult.PASS;
    }
}
