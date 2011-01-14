package id.web.michsan.csimulator.process;

import static org.easymock.EasyMock.createNiceMock;
import id.web.michsan.csimulator.ResponseTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class BaseProcessorStressTest {
	private BaseProcessor baseProcessor;
	private Sender responseSender;

	@Before
	public void before() {
		baseProcessor = new BaseProcessor();
		responseSender = createNiceMock(Sender.class);
	}

	@Test
	public void shouldTellForMatchedMessages() {
		// Given 2 templates
		List<ResponseTemplate> templates = new ArrayList<ResponseTemplate>();

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("fieldOne", "Halo");
		fields.put("fieldTwo", "<echo>");
		templates.add(new ResponseTemplate("c1", "n1", fields, "fieldOne:Hello"));
		templates.add(new ResponseTemplate("c2", "n2", fields, "fieldOne:Good"));

		// With this expectation
//		responseSender.send(anyObject(Map.class));
//		replay(responseSender);

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

	}
}
