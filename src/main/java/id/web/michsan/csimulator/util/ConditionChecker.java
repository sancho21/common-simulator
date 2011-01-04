package id.web.michsan.csimulator.util;

import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import id.web.michsan.csimulator.util.grammar.*;

/**
 * To check condition using expressions
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.3
 */
public class ConditionChecker {

	/**
	 * To check if expression reflects the fields
	 * @param expression String expression which is based on field:value pattern
	 * e.g. (f40:Hello || f50:500) && f43:Ichsan
	 * @param fields Fields and its corresponding values
	 * @return True if it is matched with the fields
	 */
	public static boolean match(String expression, Map<String, String> fields) {
		ANTLRStringStream in = new ANTLRStringStream(expression);
		ConditionLexer lexer = new ConditionLexer(in);
		CommonTokenStream input = new CommonTokenStream(lexer);

		ConditionParser parser = new ConditionParser(input);
		parser.setFields(fields);

		try {
			return parser.eval();
		} catch (RecognitionException e) {
			throw new InvalidExpressionException("This expression is invalid: " + expression, e);
		}
	}
}
