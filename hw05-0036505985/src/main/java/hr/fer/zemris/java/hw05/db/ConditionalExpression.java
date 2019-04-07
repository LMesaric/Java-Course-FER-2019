package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Models a complete conditional expression.
 * 
 * @author Luka Mesaric
 */
public class ConditionalExpression {

	/**
	 * Reference to a <code>IFieldValueGetter</code> strategy. Never
	 * <code>null</code>.
	 */
	private final IFieldValueGetter fieldGetter;

	/**
	 * Reference to a string literal. Never <code>null</code>.
	 */
	private final String stringLiteral;

	/**
	 * Reference to a <code>IComparisonOperator</code> strategy. Never
	 * <code>null</code>.
	 */
	private final IComparisonOperator comparisonOperator;

	/**
	 * Default constructor.
	 * 
	 * @param fieldGetter        reference to a <code>IFieldValueGetter</code>
	 *                           strategy
	 * @param stringLiteral      reference to a string literal
	 * @param comparisonOperator reference to a <code>IComparisonOperator</code>
	 *                           strategy
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter,
			String stringLiteral, IComparisonOperator comparisonOperator) {

		this.fieldGetter = Util.validateNotNull(fieldGetter, "fieldGetter");
		this.stringLiteral = Util.validateNotNull(stringLiteral, "stringLiteral");
		this.comparisonOperator = Util.validateNotNull(comparisonOperator, "comparisonOperator");
	}

	/**
	 * Tests whether given <code>record</code> satisfies this conditional
	 * expression.
	 * 
	 * @param record record to evaluate
	 * @return <code>true</code> if <code>record</code> satisfies this conditional
	 *         expression, <code>false</code> otherwise
	 * 
	 * @throws NullPointerException if <code>record</code> is <code>null</code>
	 */
	public boolean satisfies(StudentRecord record) {
		Util.validateNotNull(record, "record");
		return comparisonOperator.satisfied(
				fieldGetter.get(record),
				stringLiteral);
	}

	/**
	 * Getter for <code>fieldGetter</code>.
	 *
	 * @return <code>fieldGetter</code>
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Getter for <code>stringLiteral</code>.
	 *
	 * @return <code>stringLiteral</code>
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Getter for <code>comparisonOperator</code>.
	 *
	 * @return <code>comparisonOperator</code>
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comparisonOperator, fieldGetter, stringLiteral);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ConditionalExpression)) {
			return false;
		}
		ConditionalExpression other = (ConditionalExpression) obj;
		// Most likely two out of three 'equals' will resort to comparing references.
		// That is fine because static final objects should be used.
		return Objects.equals(comparisonOperator, other.comparisonOperator)
				&& Objects.equals(fieldGetter, other.fieldGetter)
				&& Objects.equals(stringLiteral, other.stringLiteral);
	}

}
