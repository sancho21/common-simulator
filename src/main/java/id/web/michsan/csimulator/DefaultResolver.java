package id.web.michsan.csimulator;

import static id.web.michsan.csimulator.util.StringHelper.q;
import id.web.michsan.csimulator.resolver.RotatingResolver;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default resolver which can handle &lt;date:HHmmss&gt;, &lt;counter&gt;
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.1
 */
public class DefaultResolver implements Resolver {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(DefaultResolver.class);

	private int counter;

	private final RotatingResolver rotatingResolver = new RotatingResolver();

	public String resolve(String value) {
		String result = value;

		if (value.startsWith("<date:")) {
			String format = value.replaceFirst("<date:", "").replace(">", "");
			result = FastDateFormat.getInstance(format).format(new Date());
		}
		else if (value.equals("<counter>")) {
			result = String.valueOf(++counter);
		}
		else if (value.startsWith("<rotate:")) {
			result = rotatingResolver.resolve(value);
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Resolving " + q(value) + " into " + q(result));

		return result;
	}
}
