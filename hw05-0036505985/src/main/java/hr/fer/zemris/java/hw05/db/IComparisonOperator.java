package hr.fer.zemris.java.hw05.db;

/**
 * Represents a comparison operator for comparing two strings.
 * 
 * @author Luka Mesaric
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Evaluates this comparison operator on the given arguments.
	 * 
	 * @param value1 first argument
	 * @param value2 second argument
	 * @return <code>true</code> if comparison is satisfied, <code>false</code>
	 *         otherwise
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	boolean satisfied(String value1, String value2);

	/**
	 * Returns a comparison operator that represents the logical negation of this
	 * comparison operator.
	 *
	 * @return negated comparison operator
	 */
	default IComparisonOperator negate() {
		return (value1, value2) -> !satisfied(value1, value2);
	}

}
