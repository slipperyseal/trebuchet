package trebuchet.craft;

import trebuchet.equipment.LongRangeScanner;

public class Spaceship {
	public static final int FIRSTSHIP = 100;
	private String name = "Nostromo";
	private int serialNumber = 120;
	private LongRangeScanner longRangeScanner;

	public Spaceship(LongRangeScanner longRangeScanner) {
		this.longRangeScanner = longRangeScanner;
	}

	public LongRangeScanner getLongRangeScanner() {
		return longRangeScanner;
	}

	public void reset(LongRangeScanner longRangeScanner, int serialNumber) {
		this.longRangeScanner = longRangeScanner;
		this.serialNumber = serialNumber;
	}
}
