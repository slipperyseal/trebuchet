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
    }

    public int getDeviceId() {
        return 1337;
    }

    private class SubspaceScanner implements Scanner {
        private long subspaceFrequency;

        public SubspaceScanner() {
            this.subspaceFrequency = 10483298;
        }
        public void scan() {
            subspaceFrequency = subspaceFrequency + 2;
        }
    }

    private class TachyonScanner implements Scanner {
        private long tachyonDopplerCalibration;

        TachyonScanner() {
            this.tachyonDopplerCalibration = -22340;
        }

        public void scan() {
            this.tachyonDopplerCalibration = this.tachyonDopplerCalibration + 4;
        }
    }
}
