package id.web.michsan.csimulator.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper for string related things
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 * @since 1.0.0
 */
public class StringHelper {

	private static Map<Character, String> padders = new HashMap<Character, String>();
	private static final int PADDER_LENGTH = 100;

	static {
		padders.put(' ', createPadder(' '));
		padders.put('0', createPadder('0'));
	}

	/**
	 * Pad number with leading zero
	 * @param number Number to pad
	 * @param length Length of final padded text
	 * @return After-pad text
	 */
	public static String pad(int number, int length) {
		return pad(String.valueOf(number), length, false, '0');
	}

	/**
	 * Get last n chars from a string. If the input string is shorter,
	 * then it will return the string itself; e.g. "Sancho", 2 will give "ho"
	 * @param string Input string
	 * @param n Number of char to read
	 * @return Last n chars of a string
	 */
	public static String lastNChars(String string, int n) {
		int length = string.length();
		if (length <= n) return string;

		int startIndex = length - n;
		return string.substring(startIndex);
	}

	/**
	 * Adding trailing space to a string (left justified)
	 * @param string Input string
	 * @param length Length of final padded string
	 * @return Padded string
	 */
	public static String pad(String string, int length) {
		return pad(string, length, true, ' ');
	}

	/**
	 * Add padding string before or after an object.
	 * @param object Object to pad
	 * @param length Length of final padded string
	 * @param isLeftAligned If true then the final string is to-be-padded string + padding string; e.g. 'Hello    '
	 * @param filler Padding character which forms a padding string
	 * @return Padded string
	 */
	public static String pad(Object object, int length, boolean isLeftAligned, char filler) {
		// Chop into to the specified length
		String padded = chop(object.toString(), length, isLeftAligned);

		if (padded.length() == length) return padded;

		String padder = loadPadder(filler);
		while (padder.length() < length) padder = padder.concat(padder);

		String fillingStr = padder.substring(0, length - padded.length());

		return isLeftAligned ? padded.concat(fillingStr) : fillingStr.concat(padded);
	}

	/**
	 * Chop a string so that its final length will not exceed specified length
	 * @param string Input string or string to process
	 * @param length Length of final string
	 * @param isLeftAligned If true then this method will preserve left side of original string
	 * @return Chopping result
	 */
	public static String chop(String string, int length, boolean isLeftAligned) {
		if (string.length() < length) return string;

		if (isLeftAligned) 	return string.substring(0, length);
		else				return string.substring(string.length() - length, string.length());
	}


	// Created by Glo?
	/**
	 * Checking if a string is empty.
	 * @param string Input string
	 * @param trim If true then it will assume whitespace string as empty
	 * @return True if it is null or empty
	 */
	public static boolean isEmpty(String string, boolean trim) {
		if (string == null) return true;

		if (trim) string = string.trim();
		return string.length() == 0;
	}

	/**
	 * Giving quote into a string. E.g. "hello" will be "'hello'"
	 * @param string Input string
	 * @return Quoted string
	 */
	public static String q(String string) {
		return "'".concat(string).concat("'");
	}

	private static String createPadder(char c) {
		StringBuilder builder = new StringBuilder();
		for (int i = PADDER_LENGTH; i > 0; i--) {
			builder.append(c);
		}
		return builder.toString();
	}

	private static String loadPadder(char filler) {
		if (padders.containsKey(filler)) return padders.get(filler);

		String padder = createPadder(filler);
		padders.put(filler, padder);
		return padder;
	}
}

