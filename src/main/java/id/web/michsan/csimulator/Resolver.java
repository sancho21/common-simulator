package id.web.michsan.csimulator;

/**
 * Resolve value in the message field. It should be used one instance for each
 * endpoint. Usually this is used to resolve special values into generated values.
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
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