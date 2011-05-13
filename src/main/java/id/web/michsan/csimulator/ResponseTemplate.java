package id.web.michsan.csimulator;

import static id.web.michsan.csimulator.util.StringHelper.pad;
import static id.web.michsan.csimulator.util.StringHelper.q;
import id.web.michsan.csimulator.util.ConditionChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Template for response which also can compose response message
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.1
 */
public class ResponseTemplate implements Template {
	// e.g. <echo|*,10|f:34>
	private static final Pattern PRIVATE_ECHO_PATTERN =
		Pattern.compile("<echo\\|([0-9]+|\\*),[0-9]+(\\|f:\\S+){0,1}>");

	// e.g. <echo|f:34>
	private static final Pattern OTHER_ECHO_PATTERN =
		Pattern.compile("<echo\\|f:\\S+>");

	private final String condition;
	private final String code;
	private final Map<String, String> fields;
	private Properties properties = new Properties();
	private final String name;
	private Resolver resolver;

	/**
	 * Create response template from template. The template must have properties
	 * named 'condition'
	 * @param template Input template
	 */
	public ResponseTemplate(Template template) {
		this(template.getCode(), template.getName(), template.getFields(),
				template.getProperties().getProperty("condition"));

		properties = template.getProperties();
	}

	public ResponseTemplate(String code, String name,
			Map<String, String> fields, String condition) {
		this.code = code;
		this.name = name;
		this.fields = fields;
		this.condition = condition;
	}

	/**
	 * Check if the fields match with this template
	 * @param fields Request fields
	 * @return result
	 */
	public boolean match(Map<String, String> fields) {
		return ConditionChecker.match(condition, fields);
	}

	/**
	 * Create response fields for request fields
	 * @param requestFields The fields and values of request message
	 * @return Response fields
	 */
	public Map<String, String> createResponse(Map<String, String> requestFields) {
		Map<String, String> rendered = new HashMap<String, String>();

		Map<String, String> resolvedValues = new HashMap<String, String>();

		for (Entry<String, String> entry : fields.entrySet()) {
			String tField = entry.getKey(); // template field
			String tValue = entry.getValue(); // template value

			try {
				if (tValue.equals("<echo>")) {
					tValue = requestFields.get(tField);
				}

				else if (OTHER_ECHO_PATTERN.matcher(tValue).find()) {
					tValue = otherEcho(requestFields, tValue);
				}

				else if (tValue.contains("<echo|")) {
					tValue = complexValueReplace(requestFields, tField, tValue);
				}

				else {
					String value = resolvedValues.get(tValue);
					if (value == null) {
						value = resolver.resolve(tValue);
						resolvedValues.put(entry.getValue(), value);
					}

					tValue = value;
				}

				rendered.put(tField, tValue);

			} catch (Exception e) {
				throw new RuntimeException("Failed to create response for field: " + tField, e);
			}
		}


		return rendered;
	}

	// Apply to <echo|f:34>
	private String otherEcho(Map<String, String> requestFields, String tValue) {
		String otherField = tValue.substring(8/*<echo|f:*/, tValue.length() - 1).trim();
		return requestFields.get(otherField);
	}

	private String complexValueReplace(Map<String, String> requestFields,
			String field, String value) {
		// 1234567890<echo|10>Jakarta
		Matcher m = PRIVATE_ECHO_PATTERN.matcher(value);

		int totalLastEchoLength = 0;
		int totalLastTextLength = 0;

		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String echoPart = value.substring(m.start(), m.end());
			String[] echoAttrs = echoPart
			.substring(1, echoPart.length() - 1) // remove < and >
			.split("\\|");

			String sourceValue = readSourceValue(requestFields, field, echoAttrs);

			// Determine the beginning of copy and length to cpy
			String[] positionAttrs = echoAttrs[1].split(",");
			int textLength = Integer.parseInt(positionAttrs[1]);

			int sourceBegin = m.start() - totalLastEchoLength + totalLastTextLength;
			if (!"*".equals(positionAttrs[0])) {
				sourceBegin = Integer.parseInt(positionAttrs[0]);
			}

			String replacement = pad(sourceValue, sourceBegin + textLength)
			.substring(sourceBegin, sourceBegin + textLength);
			m.appendReplacement(sb, replacement);

			totalLastEchoLength += echoPart.length();
			totalLastTextLength += textLength;
		}
		m.appendTail(sb);

		return sb.toString();
	}

	private String readSourceValue(Map<String, String> requestFields,
			String rField, String[] templateParts) {
		// Change source field
		String sourceField = rField;
		if (templateParts.length == 3) {
			sourceField = templateParts[2].substring(2/*f:*/);
		}

		String sourceValue = requestFields.get(sourceField);
		if (sourceValue == null) {
			throw new RuntimeException("Can not read from request field " +
					q(sourceField) + " as it doesn't exist!");
		}
		return sourceValue;
	}

	public String getCondition() {
		return condition;
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

	public Properties getProperties() {
		return properties;
	}

	public static List<ResponseTemplate> convert(List<Template> templates) {
		List<ResponseTemplate> list = new ArrayList<ResponseTemplate>();

		for (Template template : templates) {
			list.add(new ResponseTemplate(template));
		}
		return list;
	}

	public void setResolver(Resolver resolver) {
		this.resolver = resolver;
	}
}
