package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Represents a single token with defined type and value.
 * 
 * @author Luka Mesaric
 * 
 * @see SmartScriptLexer
 * @see SmartScriptTokenType
 */
public class SmartScriptToken {

	/**
	 * Type of this token.
	 */
	private final SmartScriptTokenType type;

	/**
	 * Value of this token. Its class is determined by {@link #type}.
	 */
	private final Object value;

	/**
	 * Default constructor.
	 * 
	 * @param type  token's type
	 * @param value token's value, can be <code>null</code> for <code>EOF</code>
	 *              type
	 * 
	 * @throws NullPointerException if <code>type</code> is <code>null</code>
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		this.type = Util.validateNotNull(type, "type");
		this.value = value;
	}

	/**
	 * Getter for <code>type</code>.
	 *
	 * @return type
	 */
	public SmartScriptTokenType getType() {
		return type;
	}

	/**
	 * Getter for <code>value</code>.
	 *
	 * @return value; can be <code>null</code> for <code>EOF</code> token
	 */
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "(" + type + ", " + value + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SmartScriptToken)) {
			return false;
		}
		SmartScriptToken other = (SmartScriptToken) obj;
		return type == other.type && Objects.equals(value, other.value);
	}

}
