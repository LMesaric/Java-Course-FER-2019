package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that represents a double constant.
 * 
 * @author Luka Mesaric
 */
public class ElementConstantDouble extends Element {

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

}
