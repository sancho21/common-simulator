package id.web.michsan.csimulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Load templates from properties file. The important keys are rule_names
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.1
 */
public class TemplateLoader {

	private final Properties props;
	private final String listingField;
	private final String fieldIdentifier;

	/**
	 * Constructor
	 * @param props Properties containing templates
	 * @param listingField Field of properties which contains the template names
	 * @param fieldIdentifier Field identifier, e.g. response in rule.echo.response.39=00
	 */
	public TemplateLoader(Properties props, String listingField, String fieldIdentifier) {
		this.props = props;
		this.listingField = listingField;
		this.fieldIdentifier = fieldIdentifier;
	}

	/**
	 * Load all templates form properties file
	 * @param templateCollectionFile Properties file containing templates
	 * @param listingField Field of properties which contains the template codes
	 * @param fieldIdentifier Field identifier, e.g. response in rule.echo.response.39=00
	 * @throws IOException Failed to read file
	 */
	public TemplateLoader(String templateCollectionFile, String listingField, String fieldIdentifier) throws IOException {
		this.props = new Properties();
		InputStream in = new FileInputStream(templateCollectionFile);
		props.load(in);
		in.close();

		this.listingField = listingField;
		this.fieldIdentifier = fieldIdentifier;
	}

	/**
	 * Load all templates from properties
	 * @return List of templates
	 */
	public List<Template> load() {
		List<Template> templates = new ArrayList<Template>();

		String[] ruleCodes = props.getProperty(listingField).split(",");
		for (int i = 0; i < ruleCodes.length; i++) {
			String ruleCode = ruleCodes[i].trim();
			if (ruleCode.length() != 0) {
				templates.add(loadTemplate(props, ruleCode));
			}
		}

		return templates;
	}

	private Template loadTemplate(Properties props, String ruleCode) {
		Map<String, String> templateFields = new HashMap<String, String>();

		Properties properties = new Properties();

		for (Entry<Object, Object> entry : props.entrySet()) {
			String keyStr = (String) entry.getKey();

			// Field
			if (keyStr.matches("rule\\." + ruleCode + "\\." + fieldIdentifier + "\\..+")) {
				String field = keyStr.replace(
						"rule." + ruleCode + "." + fieldIdentifier + ".", "");
				templateFields.put(field, entry.getValue().toString());

			// Other properties
			} else if (keyStr.matches("rule\\." + ruleCode + ".+")) {
				properties.setProperty(
						keyStr.replace("rule." + ruleCode + ".", ""),
						entry.getValue().toString());

			}
		}

		BaseTemplate template = new BaseTemplate(ruleCode, templateFields);
		template.setName((String) properties.remove("name"));
		template.setProperties(properties); // Set the rest values

		return template;
	}
}
