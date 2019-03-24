package hr.fer.zemris.java.hw03.prob1;

/**
 * Custom exception thrown by {@link Lexer} when it cannot correctly tokenize
 * its input.
 * 
 * @author Luka Mesaric
 */
public class LexerException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -838417587195269999L;

	/**
	 * Constructs a new <code>LexerException</code> with <code>null</code> as its
	 * detail message.
	 */
	public LexerException() {
		super();
	}

	/**
	 * Constructs a new <code>LexerException</code> with the specified detail
	 * message.
	 * 
	 * @param message the detail message
	 */
	public LexerException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>LexerException</code> with the specified cause and a
	 * detail message of <code>(cause==null ? null : cause.toString())</code>.
	 * 
	 * @param cause the cause
	 */
	public LexerException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new <code>LexerException</code> with the specified detail
	 * message and cause.
	 * 
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}

}
