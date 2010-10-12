package id.web.michsan.csimulator;

/**
 * Representation of message field
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @version 1.0.0, 10/02/10
 * @since 1.0.0
 */
public class Field implements Comparable<Field> {
	private final String name;
	private final int index;
	private final int length;
	private final String description;
	private final boolean isLeftAligned;
	private final char filler;
	private final String validationRegex;

	public static final String NO_VALIDATION = null;

	public Field(String name, int index, int length,
			String description, boolean isLeftAligned, char filler,
			String validationRegex) {
		this.name = name;
		this.index = index;
		this.length = length;
		this.description = description;
		this.isLeftAligned = isLeftAligned;
		this.filler = filler;
		this.validationRegex = validationRegex;
	}

	public Field(String name, int index, int length,
			String description, boolean isNumeric,
			String validationRegex) {
		this(name, index, length, description, !isNumeric,
				isNumeric ? '0' : ' ', validationRegex);
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public int getLength() {
		return length;
	}

	public String getDescription() {
		return description;
	}

	public boolean isLeftAligned() {
		return isLeftAligned;
	}

	public char getFiller() {
		return filler;
	}

	public String getValidationRegex() {
		return validationRegex;
	}

	public boolean isValid(String value) {
		if (validationRegex != NO_VALIDATION) return value.matches(validationRegex);
		return true;
	}

	@Override
	public String toString() {
		return "Field [description=" + description + ", filler=" + filler
				+ ", index=" + index + ", isLeftAligned=" + isLeftAligned
				+ ", length=" + length + ", name=" + name
				+ ", validationRegex=" + validationRegex + "]";
	}

	@Override
	public int compareTo(Field other) {
		return new Integer(index).compareTo(other.index);
	}
}