package id.web.michsan.csimulator.process;

import java.util.Map;

/**
 * Describe how to send a repl
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public interface Sender {
	/**
	 * Send rendered fields
	 * @param fields Rendered fields
	 */
	public void send(Map<String, String> fields);
}
