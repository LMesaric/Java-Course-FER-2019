package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Represents a single token with defined type and value.
 * 
 * @author Luka Mesaric
 */
public class Token {

	/**
	 * Type of this token. Never <code>null</code>.
	 */
	private final TokenType type;

	/**
	 * Value of this token. May be <code>null</code>.
	 */
	private final String value;

	/**
	 * Default constructor.
	 * 
	 * @param type  token's type
	 * @param value token's value, can be <code>null</code> only for
	 *              {@link TokenType#EOF} type
	 * 
	 * @throws NullPointerException if <code>type</code> is <code>null</code>, or
	 *                              <code>value</code> is <code>null</code> and
	 *                              <code>type</code> is not <code>EOF</code>
	 */
	public Token(TokenType type, String value) {
		this.type = Util.validateNotNull(type, "type");
		if (type != TokenType.EOF) {
			Util.validateNotNull(value, "value");
		}
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
	public String getValue() {
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
