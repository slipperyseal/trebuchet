package trebuchet;

import trebuchet.craft.TowingVessel;
import trebuchet.equipment.LongRangeScanner;

public class Universe {
	public static void main(String[] args) {
		new TowingVessel(new LongRangeScanner());
	}
}
