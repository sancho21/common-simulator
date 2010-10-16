package id.web.michsan.csimulator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultResolverTestCase {

	@Test
	public void shouldResolveCounter() {
		DefaultResolver resolver = new DefaultResolver();
		assertEquals("1",resolver.resolve("<counter>"));
		assertEquals("2",resolver.resolve("<counter>"));
	}
}
