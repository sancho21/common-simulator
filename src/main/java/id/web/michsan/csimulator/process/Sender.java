package id.web.michsan.csimulator.process;

import java.util.Map;

/**
 * Describe how to send a rendered fields of message
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.1
 */
public interface Sender {
	/**
	 * Send rendered fields
	 * @param fields Rendered fields
	 */
	public void send(Map<String, String> fields);
}
