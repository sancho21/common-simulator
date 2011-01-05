package id.web.michsan.csimulator;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Basic template which can be a response template or a request template
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class BaseTemplate implements Template {
	private final Map<String, String> fields;
	private final String code;
	private Properties properties = new Properties(); // Other properties
	private String name;

	/**
	 * Create a base template
	 * @param code Unique identifier of the template which has no empty chars.
	 * The value should simple; e.g. bill_payment
	 * @param fields List of pairs of field name and value
	 */
	public BaseTemplate(String code, Map<String, String> fields) {
		this.code = code;
		this.fields = Collections.unmodifiableMap(fields);
	}

	@Override
	public Map<String, String> getFields() {
		return fields;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Set properties that apply to this template; e.g. response_delay
	 * @param properties Properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Set a human friendly name to this template; e.g. Bill Payment
	 * @param name Human friendly name of the template
	 */
	public void setName(String name) {
		this.name = name;
	}
}
