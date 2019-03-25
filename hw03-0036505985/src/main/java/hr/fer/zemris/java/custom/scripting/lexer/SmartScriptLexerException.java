package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Custom exception thrown by {@link SmartScriptLexer} when it cannot correctly
 * tokenize its input.
 * 
 * @author Luka Mesaric
 */
public class SmartScriptLexerException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -838417687195269999L;

	/**
	 * Constructs a new <code>SmartScriptLexerException</code> with
	 * <code>null</code> as its detail message.
	 */
	public SmartScriptLexerException() {
		super();
	}

	/**
	 * Constructs a new <code>SmartScriptLexerException</code> with the specified
	 * detail message.
	 * 
	 * @param message the detail message
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>SmartScriptLexerException</code> with the specified
	 * cause and a detail message of
	 * <code>(cause==null ? null : cause.toString())</code>.
	 * 
	 * @param cause the cause
	 */
	public SmartScriptLexerException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new <code>SmartScriptLexerException</code> with the specified
	 * detail message and cause.
	 * 
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public SmartScriptLexerException(String message, Throwable cause) {
		super(message, cause);
	}

}
