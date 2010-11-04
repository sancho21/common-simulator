package id.web.michsan.csimulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
*
* @author Muhammad Ichsan (ichsan@gmail.com)
*
*/
public class PackagerTestCase {
	private Packager packager;

	@Before
	public void before() {
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("fieldOne", 0, 10, null, false, null));
		fields.add(new Field("fieldTwo", 10, 5, null, false, null));
		fields.add(new Field("fieldThree", 15, 10, null, false, null));
		packager = new Packager("MYCODE", fields);
	}

	@Test
	public void shouldPack() {
		Map<String, String> valueHolder = new HashMap<String, String>();
		valueHolder.put("fieldOne", "Ichsan");
		valueHolder.put("fieldTwo", "is");
		valueHolder.put("fieldThree", "the man!");

		assertEquals("Ichsan    " +
				"is   " +
				"the man!  ", packager.pack(valueHolder));
	}

	@Test
	public void shouldIgnoreMissingValues() {
		Map<String, String> valueHolder = new HashMap<String, String>();
		valueHolder.put("fieldOne", "Ichsan");
		// IGNORE THIS: valueHolder.put("fieldTwo", "is");
		valueHolder.put("fieldThree", "the man!");

		assertEquals("Ichsan    " +
				"     " +
				"the man!  ", packager.pack(valueHolder));
	}

	@Test
	public void shouldThrowExceptionForInvalidFields() {
		// Given fieldOne requires numeric
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("fieldOne", 0, 10, null, false, "[0-9]{2}"));
		packager = new Packager("MYCODE", fields);

		Map<String, String> valueHolder = new HashMap<String, String>();
		valueHolder.put("fieldOne", "Ichsan");

		try {
			packager.pack(valueHolder);
			assertTrue(false);
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void shouldLoadFromFile() throws Exception {
		packager = new Packager("my", "src/test/files/packager.xml");
		List<Field> fields = packager.getSortedFields();

		assertTrue(fields.contains(
				new Field("ACC", 0, 4, "Account Number", false, null)));
		assertTrue(fields.contains(
				new Field("AMOUNT", 4, 10, "Balance", true, null)));
	}
}
