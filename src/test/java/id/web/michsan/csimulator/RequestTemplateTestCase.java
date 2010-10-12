package id.web.michsan.csimulator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class RequestTemplateTestCase {
	private static Resolver resolver;

	@BeforeClass
	public static void beforeClass() {
		resolver = createMock(Resolver.class);
	}

	@Test
	public void shouldRender() {
		// Give we have this request template
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F55", "<counting>");
		RequestTemplate template = new RequestTemplate("echo", "Echo", fields);

		template.setResolver(resolver);
		expect(resolver.resolve("<counting>")).andReturn("1400");
		replay(resolver);

		// When we render the template
		Map<String, String> renderedFields = template.render();

		// We should get this assertation correct
		assertEquals("1400", renderedFields.get("F55"));
	}
}
