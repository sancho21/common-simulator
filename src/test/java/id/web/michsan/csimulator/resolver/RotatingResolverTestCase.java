package id.web.michsan.csimulator.resolver;

import static org.junit.Assert.assertEquals;
import id.web.michsan.csimulator.Resolver;

import org.junit.Test;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class RotatingResolverTestCase {

	@Test
	public void shouldResolve() {
		Resolver resolver = new RotatingResolver();
		String value = "<rotate:Good Morning|Bukittinggi|Cilacap>";

		assertEquals("Good Morning", resolver.resolve(value));
		assertEquals("Bukittinggi", resolver.resolve(value));
		assertEquals("Cilacap", resolver.resolve(value));
		assertEquals("Good Morning", resolver.resolve(value));
	}
}
