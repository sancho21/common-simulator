package id.web.michsan.csimulator;

import static id.web.michsan.csimulator.util.StringHelper.q;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class DefaultResolver implements Resolver {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(DefaultResolver.class);

	private int counter;

	@Override
	public String resolve(String value) {
		String result = value;

		if (value.startsWith("<date:")) {
			String format = value.replaceFirst("<date:", "").replace(">", "");
			result = new SimpleDateFormat(format).format(new Date());
		}
		else if (value.equals("<counter>")) {
			result = String.valueOf(++counter);
		}

		LOGGER.debug("Resolving " + q(value) + " into " + q(result));
		return result;
	}

}
