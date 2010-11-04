package id.web.michsan.csimulator.util;

import id.web.michsan.csimulator.Field;
import id.web.michsan.csimulator.Packager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * To view object in string representation
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.0
 */
public class ObjectViewer {

	/**
	 * View message (fields) based on its packager fields
	 * @param message The message (fields) to print
	 * @param packager The packager
	 * @return String representation
	 */
	public static String view(Map<String, String> message, Packager packager) {
		String result = "[\n";
		for (Field field : packager.getSortedFields()) {
			String value = message.get(field.getName());
			if (value == null) value = "";

			result += field.getName() + " = '" +
				value + "'\n";
		}
		result += ']';

		return result;
	}

	/**
	 * View message based on its own fields. The order of string representation
	 * is decided by the comparator
	 * @param message The message (fields) to print
	 * @param comparator Comparator which order the message fields
	 * @return String representation
	 */
	public static String view(Map<String, String> message,
			Comparator<String> comparator) {
		return view(message, comparator, new HashMap<String, String>());
	}

	/**
	 * View message based on its own fields. The order of string representation
	 * is decided by the comparator
	 * @param message The message (fields) to print
	 * @param comparator Comparator which order the message fields
	 * @param fieldNameReplaces Names and corresponding replacement for the fields.
	 * @return String representation
	 */
	public static String view(Map<String, String> message,
			Comparator<String> comparator,
			Map<String, String> fieldNameReplaces) {
		String[] fields = message.keySet().toArray(new String[] {});
		Arrays.sort(fields, comparator);

		String result = "[\n";
		for (String field : fields) {
			String value = message.get(field);
			if (value == null) value = "";

			if (fieldNameReplaces.containsKey(field))
				field = fieldNameReplaces.get(field);

			result += field + " = '" +
				value + "'\n";
		}
		result += ']';

		return result;
	}
}