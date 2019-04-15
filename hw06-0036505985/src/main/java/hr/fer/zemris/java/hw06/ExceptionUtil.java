package hr.fer.zemris.java.hw06;

import java.util.Objects;

/**
 * Utility class for easily generating customised exceptions.
 *
 * @author Luka Mesaric
 */
public final class ExceptionUtil {

	/**
	 * Checks that the specified object reference is not {@code null} and throws a
	 * customised {@link NullPointerException} if it is.
	 *
	 * @param  object               the object reference to check
	 * @param  name                 name to use in exception message
	 * @param                       <T> the type of the reference
	 * @return                      <code>object</code> if not <code>null</code>
	 * @throws NullPointerException if <code>object</code> is <code>null</code>
	 */
	public static <T> T validateNotNull(T object, String name) {
		return Objects.requireNonNull(object,
				"Argument '" + name + "' must not be null.");
	}

	/** Disable creating instances. */
	private ExceptionUtil() {}

}
