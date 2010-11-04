package id.web.michsan.csimulator;

/**
 * Resolve value in the message field. It should be used one instance for each
 * endpoint.
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.1
 */
public interface Resolver {
	/**
	 * To resolve what a value means for e.g. "date:HHmmss" can be resolved
	 * into 111533
	 * @param value Value to resolve
	 * @return Result
	 */
	public String resolve(String value);
}