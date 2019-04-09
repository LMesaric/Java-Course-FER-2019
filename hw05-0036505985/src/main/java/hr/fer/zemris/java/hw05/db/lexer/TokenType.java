package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Enumeration for different types of tokens used by {@link Token} and
 * {@link Lexer}.
 * 
 * @author Luka Mesaric
 */
public enum TokenType {

	/**
	 * Signals that there are no more tokens.
	 */
	EOF,

	/**
	 * Token is word in plain text, type <code>String</code>.
	 */
	WORD,

	/**
	 * Token is a String, type <code>String</code>. Quotation marks are removed.
	 */
	STRING,

	/**
	 * Token is a symbolic operator, type <code>String</code>. E.g. {@code =},
	 * {@code !=}, {@code <=}, {@code >} etc.
	 */
	OPERATOR

}
