package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Element that represents an integer constant.
 * 
 * @author Luka Mesaric
 */
public class ElementConstantInteger extends Element {

	/**
	 * Value of this element.
	 */
	private final int value;

	/**
	 * Default constructor.
	 * 
	 * @param value value of this element
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}

	/**
	 * Getter for <code>value</code>.
	 *
	 * @return <code>value</code>
	 */
	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementConstantInteger)) {
			return false;
		}
		ElementConstantInteger other = (ElementConstantInteger) obj;
		return value == other.value;
	}

}
