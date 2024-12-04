package art.ameliah.hsr.battleLogic.log.lines.entity;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.log.Loggable;
import art.ameliah.hsr.battleLogic.log.Logger;
import art.ameliah.hsr.metrics.CounterMetric;

public class GainCharge implements Loggable {

    public final AbstractEntity character;
    public final int amount;
    public final int initialCharge;
    public final int chargeCount;
    public final String charge;

    public GainCharge(AbstractEntity character, int amount, int initialCharge, int chargeCount) {
        this(character, amount, initialCharge, chargeCount, "Charge");
    }

    public GainCharge(AbstractEntity character, int amount, int initialCharge, int chargeCount, String charge) {
        this.character = character;
        this.amount = amount;
        this.initialCharge = initialCharge;
        this.chargeCount = chargeCount;
        this.charge = charge;
    }

    public GainCharge(AbstractEntity entity, int amount, CounterMetric<?> metric, String charge) {
        this.character = entity;
        this.initialCharge = (int) metric.last();
        this.chargeCount = (int) metric.get();
        this.amount = amount;
        this.charge = charge;
    }

    public GainCharge(AbstractEntity entity, CounterMetric<?> metric, String charge) {
        this.character = entity;
        this.initialCharge = (int) metric.last();
        this.chargeCount = (int) metric.get();
        this.amount = this.chargeCount - this.initialCharge;
        this.charge = charge;
    }

    @Override
    public String asString() {
        return String.format("%s gained %,d %s (%,d -> %,d)", character.getName(), amount, charge, initialCharge, chargeCount);
    }

    @Override
    public void handle(Logger logger) {
        logger.handle(this);
    }
}
