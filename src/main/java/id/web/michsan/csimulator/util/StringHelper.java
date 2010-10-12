package id.web.michsan.csimulator.util;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class StringHelper {

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

		// Pad
		if (isLeftAligned) {
			for (int i = padded.length(); i < length; i++) padded = padded + filler;
		} else {
			for (int i = padded.length(); i < length; i++) padded = filler + padded;
		}

		return padded;
	}

	public static String chop(String string, int length, boolean isLeftAligned) {
		if (string.length() < length) return string;

		if (isLeftAligned) 	return string.substring(0, length);
		else				return string.substring(string.length() - length, string.length());
	}

	// Created by Glo?
	public static boolean isEmpty(String input, boolean trim){
		if (input == null) return true;

		if (trim) input = input.trim();
		return input.isEmpty();
	}

	public static String q(String value) {
		return "'" + value + "'";
	}
}

