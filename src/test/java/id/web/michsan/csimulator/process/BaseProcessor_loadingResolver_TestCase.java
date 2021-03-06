package id.web.michsan.csimulator.process;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import id.web.michsan.csimulator.RequestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
@Ignore("Do not know how to test this")
public class BaseProcessor_loadingResolver_TestCase {
	private static BaseProcessor processor;
	private static Sender sender;
	private final static File configFile = new File("src/test/resources/resolver.properties");

	@BeforeClass
	public static void beforeClass() throws Exception {
		processor = new BaseProcessor();
		sender = mock(Sender.class);

		Properties props = new Properties();
		props.setProperty("class", "id.web.michsan.csimulator.ExtendedResolver");
		OutputStream os = new FileOutputStream(configFile);
		props.store(os, "");
		os.close();
	}

	@AfterClass
	public static void afterClass() {
		// Not letting other test is affected by the file
		configFile.delete();
	}

	@Test
	public void shouldLoadDifferentImplementationIfSpecifiedInConfigFile() throws Exception {
		// In src/test/resources there is resolver.properties which use ExtendedResolver.java

		// Given we have this request template with special key
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F65", "other value");
		fields.put("F75", "<special>"); // this will be modified by extended resolver
		RequestTemplate template = new RequestTemplate("echo", "Echo", fields);

		// When we send the template
		processor.processRequest(template, sender);

		// We should meet the expectation
		Map<String, String> expectedRenderedFields = new HashMap<String, String>();
		expectedRenderedFields.put("F65", "other value");
		expectedRenderedFields.put("F75", "SPECIAL!!!");

		verify(sender, times(1)).send(expectedRenderedFields);
	}
}
