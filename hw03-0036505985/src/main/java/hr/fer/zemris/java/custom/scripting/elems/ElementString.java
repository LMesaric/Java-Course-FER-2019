package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Element that represents a String.
 * 
 * @author Luka Mesaric
 */
public class ElementString extends Element {

	/**
	 * Value of this element.
	 */
	private final String value;

	/**
	 * Default constructor.
	 * 
	 * @param value value of this element
	 * 
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	public ElementString(String value) {
		this.value = Util.validateNotNull(value, "value");
	}

	/**
	 * {@inheritDoc} Quotation marks are added around its value.
	 */
	@Override
	public String asText() {
		return "\"" + value + "\"";
	}

	/**
	 * Getter for <code>value</code>.
	 *
	 * @return <code>value</code>
	 */
	public String getValue() {
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
		if (!(obj instanceof ElementString)) {
			return false;
		}
		ElementString other = (ElementString) obj;
		return Objects.equals(value, other.value);
	}

}
