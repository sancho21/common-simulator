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

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
