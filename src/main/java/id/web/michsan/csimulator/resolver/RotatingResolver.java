package id.web.michsan.csimulator.resolver;

import id.web.michsan.csimulator.Resolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolver which returns value by rotating defined values
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 3.0.1
 */
public class RotatingResolver implements Resolver {

	private final Map<String, String[]> rotatingValues = new HashMap<String, String[]>();
	private final Map<String, Integer> rotatingValueIndexes = new HashMap<String, Integer>();

	/**
	 * Can resolve "&lt;rotate|value 1|value 2&gt;" into "value 1" and "value 2"
	 * for the first and second method invocation. The next value will be back
	 * to "value 1"
	 */
	public String resolve(String value) {
		String key = value;

		if (!rotatingValues.containsKey(key)) {
			rotatingValues.put(key, value.substring(8, value.length() - 1)
					.split("\\|"));
			rotatingValueIndexes.put(value, 0);
		}
		return handleRotation(value);
	}

	private String handleRotation(String fieldValue) {
		String key = fieldValue;

		String[] values = rotatingValues.get(key);
		int idx = rotatingValueIndexes.get(key);
		fieldValue = values[idx];

		int laterIdx = calcLaterIndex(idx, values.length);
		rotatingValueIndexes.put(key, laterIdx);
		return fieldValue;
	}

	private int calcLaterIndex(int idx, int numOfData) {
		if (idx < numOfData - 1)
			return idx + 1;
		return 0;
	}
}
