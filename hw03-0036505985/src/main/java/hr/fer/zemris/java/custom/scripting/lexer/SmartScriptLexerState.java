package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration for different states {@link SmartScriptLexer} can be in.
 * 
 * @author Luka Mesaric
 */
public enum SmartScriptLexerState {

	/**
	 * Default state. Treat everything as text (like a comment).
	 */
	TEXT,

	/**
	 * Look for a tag name ('=' or variable name).
	 */
	TAG_NAME,

	/**
	 * Use various types of tokens for different type of data.
	 */
	TAG_BODY

}
