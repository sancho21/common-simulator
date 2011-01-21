package id.web.michsan.csimulator;

import static id.web.michsan.csimulator.util.StringHelper.q;

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

	public String resolve(String value) {
		String result = value;

		if (value.startsWith("<date:")) {
			String format = value.replaceFirst("<date:", "").replace(">", "");
			result = FastDateFormat.getInstance(format).format(new Date());
		}
		else if (value.equals("<counter>")) {
			result = String.valueOf(++counter);
		}

		LOGGER.debug("Resolving " + q(value) + " into " + q(result));
		return result;
	}

}
