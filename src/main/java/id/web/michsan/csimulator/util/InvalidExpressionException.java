package id.web.michsan.csimulator.util;

/**
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.3
 */
public class InvalidExpressionException extends RuntimeException {
	private static final long serialVersionUID = -4754306036392446974L;

	public InvalidExpressionException(String message, Throwable e) {
		super(message, e);
	}
}
