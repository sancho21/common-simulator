package id.web.michsan.csimulator;

/**
*
* @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
*
*/
public class ExtendedResolver extends DefaultResolver {
	@Override
	public String resolve(String value) {
		if ("<special>".equals(value)) return "SPECIAL!!!";
		return super.resolve(value);
	}
}
