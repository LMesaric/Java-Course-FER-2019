package hr.fer.zemris.java.hw03.prob1;

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
	 * Token is a word, type <code>String</code>.
	 */
	WORD,

	/**
	 * Token is a number, type <code>Long</code>.
	 */
	NUMBER,

	/**
	 * Token is a symbol, type <code>Character</code>.
	 */
	SYMBOL

}
