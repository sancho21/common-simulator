package id.web.michsan.csimulator.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
public class StringHelperTestCase {
	@Test
	public void shouldPad() {
		assertEquals("0000000500", StringHelper.pad(500, 10));
		assertEquals("500       ", StringHelper.pad("500", 10));
	}

	@Test
	public void shouldTrim() {
		assertEquals("12345", StringHelper.pad("123456", 5));
	}

	@Test
	public void shouldTellLastNChars() {
		assertEquals("san", StringHelper.lastNChars("Muhammad Ichsan", 3));
	}

	@Test
	public void shouldReturnTheTextIfShorterForLastNChars() {
		assertEquals("Hello", StringHelper.lastNChars("Hello", 10));
	}

	@Test
	public void shouldTellNullAsEmpty() {
		assertTrue(StringHelper.isEmpty(null, true));
	}

	@Test
	public void shouldTellEmptyStringAsEmpty() {
		assertTrue(StringHelper.isEmpty("", true));
		assertTrue(StringHelper.isEmpty(" ", true));
	}
}
