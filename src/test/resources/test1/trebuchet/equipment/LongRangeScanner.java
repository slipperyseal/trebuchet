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

    private class YourInnerScanner {
        private long someNumber;

        public YourInnerScanner() {
            this.someNumber = 10;
        }
        public void scanAhoy() {
            someNumber = someNumber + 2;
        }
    }

    private class MyInnerScanner {
        private long anotherNumber;

        MyInnerScanner() {
            this.anotherNumber = 20;
        }

        public void scanAhoy() {
            this.anotherNumber = this.anotherNumber + 4;
        }
    }
}
