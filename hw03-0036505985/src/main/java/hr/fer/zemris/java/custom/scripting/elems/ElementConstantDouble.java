package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Element that represents a double constant.
 * 
 * @author Luka Mesaric
 */
public class ElementConstantDouble extends Element {

	/**
	 * Tolerance for comparing values of two <code>ElementConstantDoubles</code>.
	 */
	public static final double TOLERANCE = 1E-5;

	/**
	 * Value of this element.
	 */
	private final double value;

	/**
	 * Default constructor.
	 * 
	 * @param value value of this element
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}

	/**
	 * Getter for <code>value</code>.
	 *
	 * @return <code>value</code>
	 */
	public double getValue() {
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
		if (!(obj instanceof ElementConstantDouble)) {
			return false;
		}
		ElementConstantDouble other = (ElementConstantDouble) obj;
		return Math.abs(value - other.value) < TOLERANCE;
	}

}
