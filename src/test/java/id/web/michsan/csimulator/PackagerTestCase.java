package id.web.michsan.csimulator;

import static org.junit.Assert.assertEquals;

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
}
