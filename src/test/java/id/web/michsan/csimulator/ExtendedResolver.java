package id.web.michsan.csimulator;

/**
*
* @author Muhammad Ichsan (ichsan@gmail.com)
*
*/
public class ExtendedResolver extends DefaultResolver {
	@Override
	public String resolve(String value) {
		if ("<special>".equals(value)) return "SPECIAL!!!";
		return super.resolve(value);
	}
}
