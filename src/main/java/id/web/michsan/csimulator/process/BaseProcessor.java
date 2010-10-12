package id.web.michsan.csimulator.process;

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

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class BaseProcessor implements Processor {
	private DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	private Resolver resolver;
	private static Class<? extends Resolver> resolverClass;

	@Override
	public void process(Map<String, String> incomingMessageFields, List<ResponseTemplate> templates,
			Sender responseSender) {

		String receiveDate = dateFormat.format(new Date());
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
				responseSender.send(responseFields);

				if (isVerbose) {
					replySent(responseFields);
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

	protected void unmatchedMessageReceived(Map<String, String> fields,
			String receiveDate) {
		System.out.println("Received on " + receiveDate + ":");
		System.out.println(viewOrderedContents(fields));
	}

	protected void matchedMessageReceived(Map<String, String> fields,
			String receiveDate, ResponseTemplate template) {
		System.out.println("Received on " + receiveDate + ":");
		System.out.println(viewOrderedContents(fields));
		System.out.println("Match to rule: " + template.getCode());
	}

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
		template.setResolver(loadResolver());
		requestSender.send(template.render());
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String resolverClassName = props.getProperty("class");
		try {
			resolverClass = Class.forName(resolverClassName).asSubclass(Resolver.class);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/* Accessors **************************************************************/

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
}
