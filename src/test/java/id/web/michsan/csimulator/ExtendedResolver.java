package id.web.michsan.csimulator;

public class ExtendedResolver extends DefaultResolver {
	@Override
	public String resolve(String value) {
		if ("<special>".equals(value)) return "SPECIAL!!!";
		return super.resolve(value);
	}
}
