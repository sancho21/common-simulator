package id.web.michsan.csimulator.util;

import id.web.michsan.csimulator.Field;
import id.web.michsan.csimulator.Packager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * To view object in string representation
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.0
 */
public class ObjectViewer {
	private static final Map<String, String> EMPTY_MAP = Collections.emptyMap();

	/**
	 * View message (fields) based on its packager fields
	 *
	 * @param message
	 *            The message (fields) to print
	 * @param packager
	 *            The packager
	 * @return String representation
	 */
	public static String view(Map<String, String> message, Packager packager) {
		StringBuilder result = new StringBuilder();

		result.append("[\n");
		for (Field field : packager.getSortedFields()) {
			String value = message.get(field.getName());
			if (value == null) value = "";

			result.append(field.getName()).append(" = '")
				.append(value).append("'\n");
		}
		result.append(']');

		return result.toString();
	}

	/**
	 * View message based on its own fields. The order of string representation
	 * is decided by the comparator
	 *
	 * @param message
	 *            The message (fields) to print
	 * @param comparator
	 *            Comparator which order the message fields
	 * @return String representation
	 */
	public static String view(Map<String, String> message,
			Comparator<String> comparator) {
		return view(message, comparator, EMPTY_MAP);
	}

	/**
	 * View message based on its own fields. The order of string representation
	 * is decided by the comparator
	 *
	 * @param message
	 *            The message (fields) to print
	 * @param comparator
	 *            Comparator which order the message fields
	 * @param fieldNameReplaces
	 *            Names and corresponding replacement for the fields.
	 * @return String representation
	 */
	public static String view(Map<String, String> message,
			Comparator<String> comparator,
			Map<String, String> fieldNameReplaces) {
		List<String> fields = new ArrayList<String>(message.keySet());
		Collections.sort(fields, comparator);

		StringBuilder result = new StringBuilder();
		result.append("[\n");
		for (String field : fields) {
			String value = message.get(field);
			if (value == null) value = "";

			if (fieldNameReplaces.containsKey(field))
				field = fieldNameReplaces.get(field);

			result.append(field).append(" = '")
				.append(value).append("'\n");
		}
		result.append(']');

		return result.toString();
	}
}