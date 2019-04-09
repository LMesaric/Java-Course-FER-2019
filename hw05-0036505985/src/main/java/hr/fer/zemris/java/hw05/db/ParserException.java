package hr.fer.zemris.java.hw05.db;

/**
 * Custom exception thrown by {@link QueryParser} when it cannot correctly parse
 * its input.
 * 
 * @author Luka Mesaric
 */
public class ParserException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -1178475093760978784L;

	/**
	 * Constructs a new <code>ParserException</code> with <code>null</code> as its
	 * detail message.
	 */
	public ParserException() {
		super();
	}

	/**
	 * Constructs a new <code>ParserException</code> with the specified detail
	 * message.
	 * 
	 * @param message the detail message
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>ParserException</code> with the specified cause and a
	 * detail message of <code>(cause==null ? null : cause.toString())</code>.
	 * 
	 * @param cause the cause
	 */
	public ParserException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new <code>ParserException</code> with the specified detail
	 * message and cause.
	 * 
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
