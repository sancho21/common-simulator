package id.web.michsan.csimulator.resolver;

import static org.junit.Assert.assertTrue;
import id.web.michsan.csimulator.Resolver;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class RandomizingResolverTestCase {

	@Test
	public void shouldResolve() {
		Resolver resolver = new RandomizingResolver();
		String value = "<random:Good Morning|Bukittinggi|Cilacap>";

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			list.add(resolver.resolve(value) + resolver.resolve(value)
					+ resolver.resolve(value));
		}

		boolean foundDifferentAppend = false;
		String prev = list.get(0);
		for (String appended : list) {
			if (!prev.equals(appended)) {
				foundDifferentAppend = true;
				break;
			}
		}

		assertTrue(foundDifferentAppend);
	}
}
