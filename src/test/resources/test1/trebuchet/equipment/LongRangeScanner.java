package trebuchet.equipment;

public class LongRangeScanner extends Device implements Scanner {
    private int range;

    public int getRange() {
        return this.range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void scan() {
    }

    public int getDeviceId() {
        return 1;
    }
}
