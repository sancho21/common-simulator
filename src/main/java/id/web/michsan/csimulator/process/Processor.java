package id.web.michsan.csimulator.process;

import id.web.michsan.csimulator.RequestTemplate;
import id.web.michsan.csimulator.ResponseTemplate;

import java.util.List;
import java.util.Map;

/**
 * To process an incoming message. One instance per endpoint.
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.1
 */
public interface Processor {
	/**
	 * Process an incoming message
	 * @param incomingMessageFields
	 * @param templates List of response templates
	 * @param responseSender How to reply the message
	 */
	public void process(Map<String, String> incomingMessageFields,
			List<ResponseTemplate> templates, Sender responseSender);

	/**
	 * Construct request template into a rendered request and directly send it
	 * using request sender
	 * @param template A request template
	 * @param requestSender Request sender
	 */
	public void processRequest(RequestTemplate template, Sender requestSender);
}
