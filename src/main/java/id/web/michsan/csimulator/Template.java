package id.web.michsan.csimulator;

import java.util.Map;
import java.util.Properties;

/**
 * Template
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.1
 */
public interface Template {
	/**
	 * Template code forbid whitespace character. It returns down_payment instead of payment
	 * @return Code which is has no space
	 */
	public String getCode();

	/**
	 * This template human friendly name
	 * @return Human friendly name
	 */
	public String getName();

	/**
	 * Its fields
	 * @return Fields
	 */
	public Map<String, String> getFields();

	/**
	 * Other properties that this template may have
	 * @return Properties
	 */
	public Properties getProperties();
}
