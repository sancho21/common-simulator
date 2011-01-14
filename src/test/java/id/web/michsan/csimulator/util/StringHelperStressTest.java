package id.web.michsan.csimulator.util;

import org.junit.Test;

public class StringHelperStressTest {

	@Test
	public void testPadding() {
		long period = 10000; // in ms
		long endTime = System.currentTimeMillis() + period;

		int processed = 0;
		while (endTime > System.currentTimeMillis()) {
			StringHelper.pad("Hello Bandung!!! Hello Jakarta!!!", 200);
			processed++;
		}

		System.out.println("Processed " + processed + " times.");
	}
}
