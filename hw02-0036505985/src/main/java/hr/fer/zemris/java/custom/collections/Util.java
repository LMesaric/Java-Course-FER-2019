package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Utility class.
 * 
 * @author Luka Mesaric
 */
final class Util {

	/**
	 * Checks that the specified object reference is not {@code null} and throws a
	 * customised {@link NullPointerException} if it is.
	 * 
	 * @param object the object reference to check
	 * @param name   name to use in exception message
	 * @return <code>object</code> if not <code>null</code>
	 * 
	 * @throws NullPointerException if <code>object</code> is <code>null</code>
	 */
	static Object validateNotNull(Object object, String name) {
		return Objects.requireNonNull(object, "Argument '" + name + "' must not be null.");
	}
}
