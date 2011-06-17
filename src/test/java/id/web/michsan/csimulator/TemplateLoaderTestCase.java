package id.web.michsan.csimulator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
public class TemplateLoaderTestCase {

	private static TemplateLoader loader;

	@BeforeClass
	public static void beforeClass() throws IOException {
		loader = new TemplateLoader("src/test/files/templates.txt", "rule_codes", "response");
	}

	@Test
	public void shouldLoad() throws Exception {
		List<Template> templates = loader.load();

		assertEquals(3, templates.size());

		Template template1 = templates.get(0);

		assertEquals("Network Control", template1.getName());

		assertEquals(2, template1.getFields().size());
		assertEquals("0810", template1.getFields().get("0"));
		assertEquals("00", template1.getFields().get("39"));
		assertEquals("false", template1.getProperties().get("verbose"));
	}
}
