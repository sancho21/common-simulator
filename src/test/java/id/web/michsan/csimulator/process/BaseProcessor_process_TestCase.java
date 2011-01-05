package id.web.michsan.csimulator.process;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import id.web.michsan.csimulator.ResponseTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class BaseProcessor_process_TestCase {
	private BaseProcessor baseProcessor;

	private boolean matchedReceived;
	private boolean unmatchedReceived;
	private Date replyDate;
	private Date receiveDate;

	private Sender responseSender;

	@Before
	public void before() {
		baseProcessor = new BaseProcessor() {
			@Override
			protected void matchedMessageReceived(
					Map<String,String> requestFields, Date receiveDate,
					ResponseTemplate template) {
				matchedReceived = true;
				BaseProcessor_process_TestCase.this.receiveDate = receiveDate;
			};

			@Override
			protected void unmatchedMessageReceived(
					Map<String,String> requestFields, Date receiveDate) {
				unmatchedReceived = true;
			};

			@Override
			protected void replySent(Map<String, String> responseFields) {
				replyDate = new Date();
			}
		};

		responseSender = createMock(Sender.class);
	}

	@SuppressWarnings("unchecked")
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
		responseSender.send(anyObject(Map.class));
		replay(responseSender);

		// When this matched message comes
		Map<String, String> incomingMessageFields = new HashMap<String, String>();
		incomingMessageFields.put("fieldOne", "Hello");
		incomingMessageFields.put("fieldTwo", "World");

		baseProcessor.process(incomingMessageFields, templates, responseSender);

		// Then we expect that matched information is shown
		assertTrue(matchedReceived);
		assertFalse(unmatchedReceived);
		// And sender is used to send
		verify(responseSender);
	}

	@Test
	public void shouldTellForUnmatchedMessages() {
		// Given 2 templates
		List<ResponseTemplate> templates = new ArrayList<ResponseTemplate>();

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("fieldOne", "Halo");
		fields.put("fieldTwo", "<echo>");
		templates.add(new ResponseTemplate("c1", "n1", fields, "fieldOne:Guten"));
		templates.add(new ResponseTemplate("c2", "n2", fields, "fieldOne:Baik"));

		// With this expectation (no call to response sender)
		replay(responseSender);

		// When this unmatched message comes
		Map<String, String> incomingMessageFields = new HashMap<String, String>();
		incomingMessageFields.put("fieldOne", "Hello");

		baseProcessor.process(incomingMessageFields, templates, responseSender);

		// Then we expect that matched information is shown
		assertTrue(unmatchedReceived);
		assertFalse(matchedReceived);
		// And sender is used to send
		verify(responseSender);
	}

	@Test
	public void shouldFollowGlobalDelayRule() {
		// Given processor has response delay set
		baseProcessor.setResponseDelay(200);

		// And a template
		List<ResponseTemplate> templates = new ArrayList<ResponseTemplate>();

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("fieldOne", "Halo");
		fields.put("fieldTwo", "<echo>");
		ResponseTemplate template = new ResponseTemplate("c1", "n1", fields, "fieldOne:Hello");
		templates.add(template);

		// When this matched message comes
		Map<String, String> incomingMessageFields = new HashMap<String, String>();
		incomingMessageFields.put("fieldOne", "Hello");

		baseProcessor.process(incomingMessageFields, templates, responseSender);

		// Then we expect the delay is correct
		assertTrue(replyDate.getTime() - receiveDate.getTime() > 100);
	}

	@Test
	public void shouldFollowOverridenDelayRule() {
		// Given processor has response delay set
		baseProcessor.setResponseDelay(200);

		// And a template with specified delay
		List<ResponseTemplate> templates = new ArrayList<ResponseTemplate>();

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("fieldOne", "Halo");
		fields.put("fieldTwo", "<echo>");
		ResponseTemplate template = new ResponseTemplate("c1", "n1", fields, "fieldOne:Hello");
		template.getProperties().setProperty("response_delay", "400"); // This overrides global delay
		templates.add(template);

		// When this matched message comes
		Map<String, String> incomingMessageFields = new HashMap<String, String>();
		incomingMessageFields.put("fieldOne", "Hello");

		baseProcessor.process(incomingMessageFields, templates, responseSender);

		// Then we expect the delay is correct
		assertTrue(replyDate.getTime() - receiveDate.getTime() > 300);
	}


}
