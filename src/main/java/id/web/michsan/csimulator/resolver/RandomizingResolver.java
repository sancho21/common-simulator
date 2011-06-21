package id.web.michsan.csimulator.resolver;

import id.web.michsan.csimulator.Resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Resolver which returns random value from defined values
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 3.0.1
 */
public class RandomizingResolver implements Resolver {

	private final Map<String, String[]> rotatingValues = new HashMap<String, String[]>();
	private final Random random = new Random();

	/**
	 * Can resolve "&lt;random|value 1|value 2&gt;" into "value 1" or
	 * "value 2" randomly for each method invocation.
	 */
	public String resolve(String value) {
		String key = value;

		if (!rotatingValues.containsKey(key)) {
			rotatingValues.put(key, value.substring(8, value.length() - 1)
				.split("\\|"));
		}
		return handle(value);
	}

	private String handle(String fieldValue) {
		String key = fieldValue;

		String[] values = rotatingValues.get(key);
		int idx = random.nextInt(values.length);
		fieldValue = values[idx];

		return fieldValue;
	}
}
