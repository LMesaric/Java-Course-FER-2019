package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Custom exception thrown by {@link SmartScriptParser} when it cannot correctly
 * parse its input.
 * 
 * @author Luka Mesaric
 */
public class SmartScriptParserException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -7143512009280949584L;

	/**
	 * Constructs a new <code>SmartScriptParserException</code> with
	 * <code>null</code> as its detail message.
	 */
	public SmartScriptParserException() {
		super();
	}

	/**
	 * Constructs a new <code>SmartScriptParserException</code> with the specified
	 * detail message.
	 * 
	 * @param message the detail message
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>SmartScriptParserException</code> with the specified
	 * cause and a detail message of
	 * <code>(cause==null ? null : cause.toString())</code>.
	 * 
	 * @param cause the cause
	 */
	public SmartScriptParserException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new <code>SmartScriptParserException</code> with the specified
	 * detail message and cause.
	 * 
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
