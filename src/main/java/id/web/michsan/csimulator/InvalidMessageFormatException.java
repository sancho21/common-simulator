package id.web.michsan.csimulator;

/**
 * Exception which is thrown when message format is invalid
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 3.2.0
 */
public class InvalidMessageFormatException extends RuntimeException {

	private static final long serialVersionUID = -5610501517633522650L;

	public InvalidMessageFormatException(String ruleName, String problem) {
		super("Invalid message format for rule '" + ruleName + "'. " + problem + "!");
	}
}
