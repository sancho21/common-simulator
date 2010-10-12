package id.web.michsan.csimulator;

/**
 * Resolve value in the message field. It should be used one instance for each
 * endpoint.
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public interface Resolver {
	public String resolve(String value);
}