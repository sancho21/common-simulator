package id.web.michsan.csimulator.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.3
 */
public class ConditionCheckerTestCase {
	private static Map<String, String> fields;

	@BeforeClass
	public static void beforeClass() {
		fields = new HashMap<String, String>();
		fields.put("f0", "000");
		fields.put("f3", "300");
		fields.put("f6", "600");
		fields.put("f9", "900");
		fields.put("f12", "");
	}

	@Test
	public void shouldBeMatched() {
		assertTrue(ConditionChecker.match("f0:000", fields));
	}

	@Test
	public void shouldBeUnmatched() {
		assertFalse(ConditionChecker.match("f0:005", fields));
	}

	@Test
	public void shouldBeMatchedInComplex() {
		assertTrue(ConditionChecker.match("f0:000 && f3:300", fields));
		assertTrue(ConditionChecker.match("f0:000 && (f3:300 || f6:700)", fields));
	}

	@Test
	public void shouldBeUnmatchedInComplex() {
		assertFalse(ConditionChecker.match("f0:005 && (f3:300 || f6:700)", fields));
	}

	@Test
	public void shouldBeUnmatchedForMissingFields() {
		assertFalse(ConditionChecker.match("f1:005", fields));
	}

	@Test
	public void shouldHandleNegation() {
		assertTrue(ConditionChecker.match("f1:!005", fields));
	}

	@Test
	public void shouldHandleEmptyString() {
		assertTrue(ConditionChecker.match("f12:", fields));
		assertTrue(ConditionChecker.match("f0:!", fields));
	}
}
