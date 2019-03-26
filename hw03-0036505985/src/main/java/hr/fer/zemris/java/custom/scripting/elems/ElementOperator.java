package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Element that represents an operator.
 * 
 * @author Luka Mesaric
 */
public class ElementOperator extends Element {

	/**
	 * Symbol of represented operator.
	 */
	private final String symbol;

	/**
	 * Default constructor.
	 * 
	 * @param symbol symbol of represented operator
	 * 
	 * @throws NullPointerException if <code>symbol</code> is <code>null</code>
	 */
	public ElementOperator(String symbol) {
		this.symbol = Util.validateNotNull(symbol, "symbol");
	}

	@Override
	public String asText() {
		return symbol;
	}

	/**
	 * Getter for <code>symbol</code>.
	 *
	 * @return <code>symbol</code>
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementOperator)) {
			return false;
		}
		ElementOperator other = (ElementOperator) obj;
		return Objects.equals(symbol, other.symbol);
	}

}
