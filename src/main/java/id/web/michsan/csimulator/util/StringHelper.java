package id.web.michsan.csimulator.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper for string related things
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.0
 */
public class StringHelper {

	private static Map<Character, String> padders = new HashMap<Character, String>();
	private static final int PADDER_LENGTH = 100;

	static {
		padders.put(' ', createPadder(' '));
		padders.put('0', createPadder('0'));
	}

	public static String pad(int number, int length) {
		return pad(String.valueOf(number), length, false, '0');
	}

	public static String lastNChars(String string, int n) {
		int length = string.length();
		if (length <= n) return string;

		int startIndex = length - n;
		return string.substring(startIndex);
	}

	/**
	 * Adding trailing space to a string (left justified)
	 * @param string Input string
	 * @param length Length of padded string
	 * @return Padded string
	 */
	public static String pad(String string, int length) {
		return pad(string, length, true, ' ');
	}

	public static String pad(Object object, int length, boolean isLeftAligned, char filler) {
		// Chop into to the specified length
		String padded = chop(object.toString(), length, isLeftAligned);

		if (padded.length() == length) return padded;

		String padder = loadPadder(filler);
		while (padder.length() < length) padder = padder.concat(padder);

		String fillingStr = padder.substring(0, length - padded.length());

		return isLeftAligned ? padded.concat(fillingStr) : fillingStr.concat(padded);
	}

	public static String chop(String string, int length, boolean isLeftAligned) {
		if (string.length() < length) return string;

		if (isLeftAligned) 	return string.substring(0, length);
		else				return string.substring(string.length() - length, string.length());
	}

	// Created by Glo?
	public static boolean isEmpty(String input, boolean trim) {
		if (input == null) return true;

		if (trim) input = input.trim();
		return input.isEmpty();
	}

	public static String q(String value) {
		return "'".concat(value).concat("'");
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

