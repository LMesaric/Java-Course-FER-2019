package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeration for different states {@link Lexer} can be in.
 * 
 * @author Luka Mesaric
 */
public enum LexerState {

	/**
	 * Default state.
	 */
	BASIC,

	/**
	 * All data is considered to be text (like a comment).
	 */
	EXTENDED

}
