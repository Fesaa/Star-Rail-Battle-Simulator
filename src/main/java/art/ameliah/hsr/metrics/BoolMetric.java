package art.ameliah.hsr.metrics;

public class BoolMetric extends AbstractMetric {

    private boolean bool = false;

    public BoolMetric(String key, String desc, boolean init) {
        this(key, desc);

        this.bool = init;
    }

    public BoolMetric(String key, String desc) {
        super(key, desc);
    }

    public void flip() {
        this.bool = !this.bool;
    }

    public boolean value() {
        return this.bool;
    }

    public void set(boolean bool) {
        this.bool = bool;
    }

    @Override
    public String representation() {
        return this.getDescription() + ": " + this.bool;
    }
}
