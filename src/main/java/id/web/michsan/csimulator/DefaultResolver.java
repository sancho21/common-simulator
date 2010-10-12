package id.web.michsan.csimulator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class DefaultResolver implements Resolver {
	private int counter;

	@Override
	public String resolve(String value) {
		if (value.startsWith("<date:")) {
			String format = value.replaceFirst("<date:", "").replace(">", "");
			return new SimpleDateFormat(format).format(new Date());
		}
		if (value.startsWith("<counter:")) {
			int length = Integer.parseInt(value.replaceFirst("<counter:", "").replace(">", ""));
			String format = "";
			while (length > 0) {
				format += "0";
				length--;
			}

			return new DecimalFormat(format).format(counter++);
		}

		// Assume it as simple value
		return value;
	}

}
