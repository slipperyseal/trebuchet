package net.catchpole.trebuchet;

import org.junit.Test;

public class TrebuchetTest {
	@Test
	public void testTrebuchet() throws Exception {
		Trebuchet trebuchet = new Trebuchet();
		trebuchet.process("universe", "src/test/resources/test1/");
	}
}
