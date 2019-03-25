package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration for different types of tokens used by {@link SmartScriptToken}
 * and {@link SmartScriptLexer}.
 * 
 * @author Luka Mesaric
 */
public enum SmartScriptTokenType {

	/**
	 * Signals that there are no more tokens.
	 */
	EOF,

	/**
	 * Token is an open tag, type <code>String</code>.
	 */
	OPEN_TAG,

	/**
	 * Token is a closing tag, type <code>String</code>.
	 */
	CLOSE_TAG,

	/**
	 * Token is a plain text, type <code>String</code>.
	 */
	PLAIN_TEXT,

	/**
	 * Token is a tag name, type <code>String</code>.
	 */
	TAG_NAME,
	
	/**
	 * Token is a variable name, type <code>String</code>.
	 */
	VARIABLE,
	
	/**
	 * Token is a function name, type <code>String</code>.
	 */
	FUNCTION,

	/**
	 * Token is a String, type <code>String</code>.
	 */
	STRING,

	/**
	 * Token is a number, type <code>Integer</code>.
	 */
	INTEGER,

	/**
	 * Token is a number, type <code>Double</code>.
	 */
	DOUBLE,

	/**
	 * Token is an operator (symbol), type <code>Character</code>.
	 */
	OPERATOR

}
