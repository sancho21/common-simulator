package id.web.michsan.csimulator;

import static id.web.michsan.csimulator.util.StringHelper.pad;
import id.web.michsan.csimulator.util.ConditionChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Template for response which also can compose response message
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.1
 */
public class ResponseTemplate implements Template {
	private static final Pattern PRIVATE_ECHO_PATTERN = Pattern.compile("<echo\\|([0-9]+|\\*),[0-9]+>");
	private final String condition;
	private final String code;
	private final Map<String, String> fields;
	private Properties properties = new Properties();
	private final String name;

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
		Map<String, String> response = new HashMap<String, String>();
		response.putAll(fields);

		for (String field : response.keySet()) {
			try {
				String value = response.get(field);

				if (value.equals("<echo>")) {
					value = requestFields.get(field);
				}

				else if (value.contains("<echo|")) {
					value = complexValueReplace(requestFields, field, value);
				}

				response.put(field, value);

			} catch (Exception e) {
				throw new RuntimeException("Failed to create response for field: " + field, e);
			}
		}


		return response;
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
			String[] split = echoPart.split("\\|");

			int sourceBegin = m.start() - totalLastEchoLength + totalLastTextLength;

			String position = split[1].replace(">", "");
			split = position.split(",");
			int textLength = Integer.parseInt(split[1]);

			if (!"*".equals(split[0])) {
				sourceBegin = Integer.parseInt(split[0]);
			}

			String replacement = pad(requestFields.get(field), sourceBegin + textLength)
			.substring(sourceBegin, sourceBegin + textLength);
			m.appendReplacement(sb, replacement);

			totalLastEchoLength += echoPart.length();
			totalLastTextLength += textLength;
		}
		m.appendTail(sb);

		return sb.toString();
	}

	public String getCondition() {
		return condition;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, String> getFields() {
		return fields;
	}

	@Override
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
}
