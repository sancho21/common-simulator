package id.web.michsan.csimulator.resolver;

import id.web.michsan.csimulator.Resolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Request template
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 3.0.1
 */
public class RotatingResolver implements Resolver {

	private final Map<String, String[]> rotatingValues = new HashMap<String, String[]>();
	private final Map<String, Integer> rotatingValueIndexes = new HashMap<String, Integer>();

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
