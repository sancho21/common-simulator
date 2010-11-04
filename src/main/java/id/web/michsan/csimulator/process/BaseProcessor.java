package id.web.michsan.csimulator.process;

import static id.web.michsan.csimulator.util.StringHelper.q;
import id.web.michsan.csimulator.RequestTemplate;
import id.web.michsan.csimulator.Resolver;
import id.web.michsan.csimulator.ResponseTemplate;
import id.web.michsan.csimulator.Template;
import id.web.michsan.csimulator.util.ObjectViewer;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of a Processor
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.1
 */
public class BaseProcessor implements Processor {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseProcessor.class);

	private DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	private Resolver resolver;
	private static Class<? extends Resolver> resolverClass;

	@Override
	public void process(Map<String, String> incomingMessageFields, List<ResponseTemplate> templates,
			Sender responseSender) {

		Date receiveDate = new Date();
		for (ResponseTemplate template : templates) {

			if (template.match(incomingMessageFields)) {
				boolean isVerbose = isVerbose(template);

				if (isVerbose) {
					matchedMessageReceived(incomingMessageFields, receiveDate, template);
				}

				String delayStr = template.getProperties().getProperty("response_delay");
				if (delayStr != null) {
					sleepFor(Long.parseLong(delayStr));
				}

				Map<String, String> responseFields = template.createResponse(incomingMessageFields);
				if (!responseFields.isEmpty()) {
					responseSender.send(responseFields);

					if (isVerbose) {
						replySent(responseFields);
					}
				}
				return;
			}
		}

		unmatchedMessageReceived(incomingMessageFields, receiveDate);
	}

	private void sleepFor(long length) {
		try {
			Thread.sleep(length);
		} catch (InterruptedException e) {}
	}

	private boolean isVerbose(Template template) {
		boolean isVerbose = true;
		String verboseStr = template.getProperties().getProperty("verbose");
		if (verboseStr != null) isVerbose = Boolean.parseBoolean(verboseStr);
		return isVerbose;
	}

	/**
	 * This is called when an unmatched message has come. Current implementation
	 * only prints when the message come and what are its field contents.
	 * @param requestFields Fields of the request message
	 * @param receiveDate Date when the message is received
	 */
	protected void unmatchedMessageReceived(Map<String, String> requestFields,
			Date receiveDate) {
		System.out.println("Received on " + dateFormat.format(receiveDate) + ":");
		System.out.println(viewOrderedContents(requestFields));
	}

	/**
	 * This is called when a matched message is received. Current implementation
	 * only prints when the message come, what are its field contents and also
	 * the rule which matches.
	 * @param requestFields Fields of the request message
	 * @param receiveDate Date when the message is received
	 * @param template Response template which matches
	 */
	protected void matchedMessageReceived(Map<String, String> requestFields,
			Date receiveDate, ResponseTemplate template) {
		System.out.println("Received on " + dateFormat.format(receiveDate) + ":");
		System.out.println(viewOrderedContents(requestFields));
		System.out.println("Match to rule: " +
				(template.getName() != null ? template.getName() : template.getCode()));
	}

	/**
	 * This is called when a response message is sent back to the requester.
	 * Current implementation only prints when the message is sent and what are
	 * its field contents.
	 * @param responseFields Fields of the response message
	 */
	protected void replySent(Map<String, String> responseFields) {
		String replyDate = dateFormat.format(new Date());
		System.out.println("Replied on " + replyDate + ":");
		System.out.println(viewOrderedContents(responseFields));
	}

	protected String viewOrderedContents(Map<String, String> fields) {
		return ObjectViewer.view(fields, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
	}

	@Override
	public void processRequest(RequestTemplate template, Sender requestSender) {
		LOGGER.debug("Sending template " + readNameOrCode(template));
		template.setResolver(loadResolver());

		Map<String, String> rendered = template.render();

		if (isVerbose(template)) {
			requestSent(readNameOrCode(template), rendered);
		}
		requestSender.send(rendered);
	}

	/**
	 * This is called when a request message is rendered. This is only for
	 *  information reason.
	 * @param ruleName Rule name the request message belongs to
	 * @param requestFields Rendered request fields
	 */
	protected void requestSent(String ruleName, Map<String, String> requestFields) {
		System.out.println("Sending " + q(ruleName) + " on " + dateFormat.format(new Date()));
		System.out.println(viewOrderedContents(requestFields));
	}

	private String readNameOrCode(RequestTemplate template) {
		return template.getName() != null ? template.getName() : template.getCode();
	}

	public Resolver loadResolver() {
		if (resolver == null) {
			if (resolverClass == null) {
				loadClass();
			}
			try {
				resolver = resolverClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return resolver;
	}

	private static void loadClass() {
		Properties props = new Properties();
		try {
			InputStream in =
				ClassLoader.getSystemClassLoader().getResourceAsStream("resolver.properties");
			props.load(in);
			LOGGER.debug("resolver.properties is found");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String resolverClassName = props.getProperty("class");
		try {
			resolverClass = Class.forName(resolverClassName).asSubclass(Resolver.class);
			LOGGER.info("Resolver " + resolverClassName + " is loaded");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/* Accessors **************************************************************/

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
}
