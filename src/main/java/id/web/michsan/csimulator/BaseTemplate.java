package id.web.michsan.csimulator;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Basic template which can be a response template or a request template
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
public class BaseTemplate implements Template {
	private final Map<String, String> fields;
	private final String name;
	private Properties properties = new Properties(); // Other properties
	private String label;

	/**
	 * Create a base template
	 *
	 * @param name
	 *            Unique identifier of the template which has no empty chars.
	 *            The value should simple; e.g. bill_payment
	 * @param fields
	 *            List of pairs of field name and value
	 */
	public BaseTemplate(String name, Map<String, String> fields) {
		this.name = name;
		this.fields = Collections.unmodifiableMap(fields);
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public String getName() {
		return name;
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * Set properties that apply to this template; e.g. response_delay
	 *
	 * @param properties
	 *            Properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * Set a human friendly name to this template; e.g. Bill Payment
	 *
	 * @param label
	 *            Human friendly name of the template
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}
