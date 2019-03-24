package hr.fer.zemris.java.custom.scripting.elems;

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

}
