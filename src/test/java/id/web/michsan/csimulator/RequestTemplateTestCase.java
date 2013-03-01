package id.web.michsan.csimulator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
/**
 *m
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
public class RequestTemplateTestCase {
	private Resolver resolver;

	@Before
	public void beforeClass() {
		resolver = mock(Resolver.class);
	}

	@Test
	public void shouldRender() {
		// Given: we have this request template
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F55", "<counting>");
		RequestTemplate template = new RequestTemplate("echo", "Echo", fields);

		// And: a template resolver
		template.setResolver(resolver);
		when(resolver.resolve("<counting>")).thenReturn("1400");

		// When: we render the template
		Map<String, String> renderedFields = template.render();

		// Then: we should get rendered value
		assertEquals("1400", renderedFields.get("F55"));
	}

	@Test
	public void shouldRenderTheSameResolvedValueInOneCycle() {
		// Given: we have a request message having the same resolving values
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F55", "<counting>");
		fields.put("F65", "<counting>");
		RequestTemplate template = new RequestTemplate("echo", "Echo", fields);

		// And: a template resolver
		template.setResolver(resolver);
		when(resolver.resolve("<counting>")).thenReturn("500");

		// When: we render the template
		Map<String, String> renderedFields = template.render();

		// Then: we should get the same rendered value
		assertEquals("500", renderedFields.get("F55"));
		assertEquals("500", renderedFields.get("F65"));

		// And: resolver is just called once
		verify(resolver, times(1)).resolve("<counting>");
	}

	@Test
	public void shouldRenderDifferentlyBetweenCycles() {
		// Given: we have a request message having the same resolving values
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F55", "<counting>");
		fields.put("F65", "<counting>");
		RequestTemplate template = new RequestTemplate("echo", "Echo", fields);

		// And: a template resolver
		template.setResolver(resolver);
		when(resolver.resolve("<counting>"))
			.thenReturn("500")
			.thenReturn("550");

		// When: we render the template twice
		Map<String, String> renderedFields1 = template.render();
		Map<String, String> renderedFields2 = template.render();

		// Then: we should get different render result
		assertEquals("500", renderedFields1.get("F55"));
		assertEquals("550", renderedFields2.get("F65"));

		// And: resolver is called twice
		verify(resolver, times(2)).resolve("<counting>");
	}
}
