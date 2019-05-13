package hr.fer.zemris.java.gui.layouts;

/**
 * Custom exception thrown by {@link CalcLayout}.
 * 
 * @author Luka Mesaric
 */
public class CalcLayoutException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -5028233227535697092L;

	/**
	 * Constructs a new <code>CalcLayoutException</code> with <code>null</code> as
	 * its detail message.
	 */
	public CalcLayoutException() {
		super();
	}

	/**
	 * Constructs a new <code>CalcLayoutException</code> with the specified detail
	 * message.
	 * 
	 * @param message the detail message
	 */
	public CalcLayoutException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>CalcLayoutException</code> with the specified cause
	 * and a detail message of <code>(cause==null ? null : cause.toString())</code>.
	 * 
	 * @param cause the cause
	 */
	public CalcLayoutException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new <code>CalcLayoutException</code> with the specified detail
	 * message and cause.
	 * 
	 * @param message the detail message
	 * @param cause   the cause
	 */
	public CalcLayoutException(String message, Throwable cause) {
		super(message, cause);
	}

}
