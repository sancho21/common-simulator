package id.web.michsan.csimulator.process;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import id.web.michsan.csimulator.RequestTemplate;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
public class BaseProcessor_missingResolverConfigFile_TestCase {
	private BaseProcessor processor;
	private Sender sender;

	@Before
	public void before() throws Exception {
		processor = new BaseProcessor();
		sender = mock(Sender.class);
	}

	@Test
	public void shouldLoadDefaultIfConfigFileIsMissing() {
		// Given there is no resolver.properties file

		// We have this request template with special key
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F65", "other value");
		fields.put("F75", "<special>"); // this will not be modified by DEFAULT resolver
		RequestTemplate template = new RequestTemplate("echo", "Echo", fields);

		// When we send the template
		processor.processRequest(template, sender);

		// Then we'll receive correct format
		Map<String, String> expectedRenderedFields = new HashMap<String, String>();
		expectedRenderedFields.put("F65", "other value");
		expectedRenderedFields.put("F75", "<special>"); // stay the same.

		verify(sender, times(1)).send(expectedRenderedFields);
	}
}
