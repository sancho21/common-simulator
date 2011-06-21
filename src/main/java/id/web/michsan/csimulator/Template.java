package id.web.michsan.csimulator;

import java.util.Map;
import java.util.Properties;

/**
 * Template
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.1
 */
public interface Template {
	/**
	 * To get template's name without whitespace character. E.g. "down_payment" instead of "down payment"
	 * @return Template name
	 */
	public String getName();

	/**
	 * To get template's human friendly name
	 * @return Human friendly name
	 */
	public String getLabel();

	/**
	 * To get template's list of field-value
	 * @return Fields
	 */
	public Map<String, String> getFields();

	/**
	 * Other properties that this template may have
	 * @return Properties
	 */
	public Properties getProperties();
}