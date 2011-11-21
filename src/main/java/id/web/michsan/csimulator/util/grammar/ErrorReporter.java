package id.web.michsan.csimulator.util.grammar;

import id.web.michsan.csimulator.util.InvalidExpressionException;

public class ErrorReporter {
	public static void report(String error) {
		throw new InvalidExpressionException(error, null);
	}
}