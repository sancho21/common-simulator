package id.web.michsan.csimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Request template
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.1
 */
public class RequestTemplate implements Template {
	private final String code;
	private final String name;
	private final Map<String, String> fields;
	private Properties properties = new Properties();
	private Resolver resolver;

	public RequestTemplate(Template template) {
		this(template.getCode(), template.getName(), template.getFields());
		properties = template.getProperties();
	}

	public RequestTemplate(String code, String name, Map<String, String> fields) {
		this.code = code;
		this.name = name;
		this.fields = fields;
	}

	/**
	 * Render this template into real message
	 * @return Rendered message fields
	 */
	public Map<String, String> render() {
		HashMap<String, String> rendered = new HashMap<String, String>();

		Map<String, String> resolvedValues = new HashMap<String, String>();

		for (Entry<String, String> entry : fields.entrySet()) {
			String field = entry.getKey();

			String value = resolvedValues.get(entry.getValue());
			if (value == null) {
				value = resolver.resolve(entry.getValue());
				resolvedValues.put(entry.getValue(), value);
			}

			rendered.put(field, value);
		}

		return rendered;
	}

	public static List<RequestTemplate> convert(List<Template> templates) {
		List<RequestTemplate> list = new ArrayList<RequestTemplate>();

		for (Template template : templates) {
			list.add(new RequestTemplate(template));
		}
		return list;
	}

	/* Accessors **************************************************************/

	public void setResolver(Resolver resolver) {
		this.resolver = resolver;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getFields() {
		return fields;
	}
}
