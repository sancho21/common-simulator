package id.web.michsan.csimulator.process;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import id.web.michsan.csimulator.ResponseTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
public class BaseProcessorStressTest {
	private BaseProcessor baseProcessor;
	private Sender responseSender;

	@Before
	public void before() {
		baseProcessor = new BaseProcessor();
		responseSender = mock(Sender.class);
	}

	@Test
	public void shouldTellForMatchedMessages() {
		// Given 2 templates
		List<ResponseTemplate> templates = new ArrayList<ResponseTemplate>();

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("fieldOne", "Halo");
		fields.put("fieldTwo", "<echo>");
		templates.add(new ResponseTemplate("c1", "n1", fields, "fieldOne == \"Hello\""));
		templates.add(new ResponseTemplate("c2", "n2", fields, "fieldOne == \"Good\""));

		// When this matched message comes
		Map<String, String> incomingMessageFields = new HashMap<String, String>();
		incomingMessageFields.put("fieldOne", "Hello");
		incomingMessageFields.put("fieldTwo", "World");

		long period = 10000; // in ms
		long endTime = System.currentTimeMillis() + period;

		int processed = 0;
		while (endTime > System.currentTimeMillis()) {
			baseProcessor.process(incomingMessageFields, templates, responseSender);
			processed++;
		}

		System.out.println("Processed " + processed + " times.");

		// Then reponseSender should be called as much as number of message that comes
		fields = new HashMap<String, String>();
		fields.put("fieldOne", "Halo");
		fields.put("fieldTwo", "World");

		verify(responseSender, times(processed)).send(fields);
	}
}
