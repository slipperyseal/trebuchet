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
        range = range + 2;
        range = range - 1;
    }

    public int getDeviceId() {
        return 1337;
    }
}
