package id.web.michsan.csimulator;

import static id.web.michsan.csimulator.util.StringHelper.q;
import id.web.michsan.csimulator.resolver.RandomizingResolver;
import id.web.michsan.csimulator.resolver.RotatingResolver;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default resolver which can handle &lt;date:HHmmss&gt;, &lt;counter&gt;,
 * &lt;rotate|value 1|value 2&gt;, &lt;random|value 1|value 2&gt;
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.1
 */
public class DefaultResolver implements Resolver {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(DefaultResolver.class);

	private int counter;

	private final RotatingResolver rotatingResolver = new RotatingResolver();
	private final RandomizingResolver randomizingResolver = new RandomizingResolver();

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
		else if (value.startsWith("<random:")) {
			result = randomizingResolver.resolve(value);
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Resolving " + q(value) + " into " + q(result));

		return result;
	}
}
