package trebuchet;

import trebuchet.craft.Spaceship;
import trebuchet.equipment.LongRangeScanner;

public class Universe {
	public static void main(String[] args) {
		new Spaceship(new LongRangeScanner());
	}
}
