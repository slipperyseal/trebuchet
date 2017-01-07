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
        range = range + 234;
        range = range - 123;
    }

    public int getDeviceId() {
        return 1337;
    }
}
