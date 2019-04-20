package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Custom exception used for notifications about access to an empty stack.
 * 
 * @author Luka Mesaric
 */
public class EmptyStackException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 7148337426672220561L;

	/**
	 * Constructs a new <code>EmptyStackException</code> with <code>null</code> as
	 * its detail message.
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * 
	 * @param message the detail message
	 */
	public EmptyStackException(String message) {
		super(message);
	}

	/**
	 * Constructs a new runtime exception with the specified cause and a detail
	 * message of <code>(cause==null ? null : cause.toString())</code>.
	 * 
	 * @param cause the cause
	 */
	public EmptyStackException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message and
	 * cause.
	 * 
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public EmptyStackException(String message, Throwable cause) {
		super(message, cause);
	}

}
