package id.web.michsan.csimulator.process;

import java.util.Map;

/**
 * Describe how to send a rendered fields of message
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.1
 */
public interface Sender {
	/**
	 * Send rendered fields
	 *
	 * @param fields
	 *            Rendered fields
	 */
	public void send(Map<String, String> fields);
}
