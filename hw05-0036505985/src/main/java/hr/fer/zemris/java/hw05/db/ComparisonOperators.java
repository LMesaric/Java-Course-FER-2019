package hr.fer.zemris.java.hw05.db;

/**
 * Utility class that offers implementations of common operators.
 * 
 * @author Luka Mesaric
 */
public class ComparisonOperators {

	/**
	 * Returns <code>true</code> if the first argument is lexicographically smaller
	 * than the second argument.
	 */
	public static final IComparisonOperator LESS = (v1, v2) -> v1.compareTo(v2) < 0;

	/**
	 * Returns <code>true</code> if the first argument is lexicographically greater
	 * than the second argument.
	 */
	public static final IComparisonOperator GREATER = (v1, v2) -> v1.compareTo(v2) > 0;

	/**
	 * Returns <code>true</code> if arguments are equal.
	 */
	public static final IComparisonOperator EQUALS = String::equals;

	/**
	 * Returns <code>true</code> if the first argument is lexicographically smaller
	 * or equal than the second argument.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = GREATER.negate();

	/**
	 * Returns <code>true</code> if the first argument is lexicographically greater
	 * or equal than the second argument.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = LESS.negate();

	/**
	 * Returns <code>true</code> if arguments are not equal.
	 */
	public static final IComparisonOperator NOT_EQUALS = EQUALS.negate();

	/**
	 * Returns <code>true</code> if the first argument is <i>like</i> the second
	 * argument (pattern). Wildcard <code>*</code> replaces any substring, including
	 * empty string.
	 */
	public static final IComparisonOperator LIKE = (v1, v2) -> v1.matches(v2.replace("*", ".*"));

	/**
	 * Private constructor disables creating instances of this class.
	 */
	private ComparisonOperators() {
		// No ComparisonOperators for you :)
	}

}
