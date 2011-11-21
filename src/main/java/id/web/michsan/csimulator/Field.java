package id.web.michsan.csimulator;

/**
 * Representation of message field
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
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

	public int compareTo(Field other) {
		return new Integer(index).compareTo(other.index);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + filler;
		result = prime * result + index;
		result = prime * result + (isLeftAligned ? 1231 : 1237);
		result = prime * result + length;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((validationRegex == null) ? 0 : validationRegex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (filler != other.filler)
			return false;
		if (index != other.index)
			return false;
		if (isLeftAligned != other.isLeftAligned)
			return false;
		if (length != other.length)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (validationRegex == null) {
			if (other.validationRegex != null)
				return false;
		} else if (!validationRegex.equals(other.validationRegex))
			return false;
		return true;
	}
}