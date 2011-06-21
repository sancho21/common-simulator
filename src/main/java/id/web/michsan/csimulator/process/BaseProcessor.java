package id.web.michsan.csimulator.process;

import static id.web.michsan.csimulator.util.StringHelper.q;
import id.web.michsan.csimulator.DefaultResolver;
import id.web.michsan.csimulator.RequestTemplate;
import id.web.michsan.csimulator.Resolver;
import id.web.michsan.csimulator.ResponseTemplate;
import id.web.michsan.csimulator.Template;
import id.web.michsan.csimulator.util.ObjectViewer;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of a Processor
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.1
 */
public class BaseProcessor implements Processor {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseProcessor.class);

	private FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMM yyyy HH:mm:ss,SSS");
	private long responseDelay;
	private static Resolver resolver;
	private static Class<? extends Resolver> resolverClass;

	public void process(Map<String, String> incomingMessageFields, List<ResponseTemplate> templates,
			Sender responseSender) {

		Date receiveDate = new Date();
		for (ResponseTemplate template : templates) {

			if (template.match(incomingMessageFields)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Match found!");
				}

				template.setResolver(loadResolver());
				boolean isVerbose = isVerbose(template);

				if (isVerbose) {
					matchedMessageReceived(incomingMessageFields, receiveDate, template);
				}

				delayIfNecessary(template);

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

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("No match found!");
		}
		unmatchedMessageReceived(incomingMessageFields, receiveDate);
	}

	private void delayIfNecessary(ResponseTemplate template) {
		long responseDelay = this.responseDelay;
		String delayStr = template.getProperties().getProperty("response_delay");

		if (delayStr != null)
			responseDelay = Long.parseLong(delayStr);

		if (responseDelay != 0)
			sleepFor(responseDelay);
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
				(template.getLabel() != null ? template.getLabel() : template.getName()));
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

			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
	}

	public void processRequest(RequestTemplate template, Sender requestSender) {
		if (LOGGER.isDebugEnabled())
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
	 * @param ruleNameOrCode Rule name the request message belongs to
	 * @param requestFields Rendered request fields
	 */
	protected void requestSent(String ruleNameOrCode, Map<String, String> requestFields) {
		System.out.println("Sending " + q(ruleNameOrCode) + " on " + dateFormat.format(new Date()));
		System.out.println(viewOrderedContents(requestFields));
	}

	private String readNameOrCode(RequestTemplate template) {
		return template.getLabel() != null ? template.getLabel() : template.getName();
	}

	/**
	 * Load resolver from runtime
	 * @return Field value resolver
	 */
	private static Resolver loadResolver() {
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
		InputStream in = null;
		try {
			in = ClassLoader.getSystemClassLoader().getResourceAsStream("resolver.properties");
			if (in == null) {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("Failed to find resolver.properties. Using default resolver implementation.");
				resolverClass = DefaultResolver.class;
				return;
			}

			props.load(in);
			if (LOGGER.isDebugEnabled()) LOGGER.debug("resolver.properties is found");

		} catch (IOException e) {
			throw new RuntimeException("Failed to load resolver.properties file.", e);

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {}
		}

		String resolverClassName = props.getProperty("class");
		try {
			resolverClass = Class.forName(resolverClassName).asSubclass(Resolver.class);
			if (LOGGER.isInfoEnabled()) LOGGER.info("Resolver " + resolverClassName + " is loaded");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/* Accessors **************************************************************/

	/**
	 * Set pattern of date format of information when a message is processed.
	 * @param pattern Date pattern as in {@link SimpleDateFormat}.
	 */
	public void setDateFormatPattern(String pattern) {
		this.dateFormat = FastDateFormat.getInstance(pattern);
	}

	/**
	 * Global response delay
	 * @param responseDelay Delay in milliseconds
	 */
	public void setResponseDelay(long responseDelay) {
		this.responseDelay = responseDelay;
	}
}
