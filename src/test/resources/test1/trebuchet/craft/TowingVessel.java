package trebuchet.craft;

import trebuchet.equipment.LongRangeScanner;

/*
  The USCSS Nostromo (reg. 180924609) was a $42 million (and adjusted) commercial
  towing vessel owned by the Weyland-Yutani Corporation, a Lockmart CM 88B
  Bison M-Class starfreighter used as a commercial hauler between Thedus and Earth.
*/

public class TowingVessel {
	public static final int REGISTRATION = 180924609;
	// We set down on LV-426
	private String name = "Nostromo";
	private char starfreighterClass = 'M';
	private LongRangeScanner longRangeScanner;

	public TowingVessel() {
	}

	public TowingVessel(LongRangeScanner longRangeScanner) {
		this.longRangeScanner = longRangeScanner;
	}

	public LongRangeScanner getLongRangeScanner() {
		return longRangeScanner;
	}

	public void reset(LongRangeScanner longRangeScanner, char starfreighterClass) {
		// Don't call this method when above light speed.
		this.longRangeScanner = longRangeScanner;
		this.starfreighterClass = starfreighterClass;
	}

	public String getName() {
		return name;
	}
}
