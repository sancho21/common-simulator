package id.web.michsan.csimulator.process;

import id.web.michsan.csimulator.RequestTemplate;
import id.web.michsan.csimulator.ResponseTemplate;

import java.util.List;
import java.util.Map;

/**
 * To process an incoming message. One instance per endpoint.
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public interface Processor {
	/**
	 * Process an incoming message
	 * @param incomingMessageFields
	 * @param templates
	 * @param responseSender How to reply the message
	 */
	public void process(Map<String, String> incomingMessageFields,
			List<ResponseTemplate> templates, Sender responseSender);

	/**
	 * Process a message
	 * @param fields
	 * @param template
	 * @param requestSender
	 */
	public void processRequest(RequestTemplate template, Sender requestSender);
}
