package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Represents a single token with defined type and value.
 * 
 * @author Luka Mesaric
 * 
 * @see Lexer
 * @see TokenType
 */
public class Token {

	/**
	 * Type of this token.
	 */
	private final TokenType type;

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
	public Token(TokenType type, Object value) {
		this.type = Util.validateNotNull(type, "type");
		this.value = value;
	}

	/**
	 * Getter for <code>type</code>.
	 *
	 * @return type
	 */
	public TokenType getType() {
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
		if (!(obj instanceof Token)) {
			return false;
		}
		Token other = (Token) obj;
		return type == other.type && Objects.equals(value, other.value);
	}

}
