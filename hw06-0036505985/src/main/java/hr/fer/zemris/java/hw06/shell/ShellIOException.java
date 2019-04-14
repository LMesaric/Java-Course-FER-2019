package hr.fer.zemris.java.hw06.shell;

/**
 * Custom exception thrown when reading or writing to console fails.
 * 
 * @author Luka Mesaric
 */
public class ShellIOException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1357452440229521032L;

	/**
	 * Constructs a new <code>ShellIOException</code> with <code>null</code> as its
	 * detail message.
	 */
	public ShellIOException() {
		super();
	}

	/**
	 * Constructs a new <code>ShellIOException</code> with the specified detail
	 * message.
	 * 
	 * @param message the detail message
	 */
	public ShellIOException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>ShellIOException</code> with the specified cause and a
	 * detail message of <code>(cause==null ? null : cause.toString())</code>.
	 * 
	 * @param cause the cause
	 */
	public ShellIOException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new <code>ShellIOException</code> with the specified detail
	 * message and cause.
	 * 
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public ShellIOException(String message, Throwable cause) {
		super(message, cause);
	}

}
