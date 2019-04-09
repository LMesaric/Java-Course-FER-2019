package hr.fer.zemris.java.hw05.db;

import java.util.List;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Implementation of <code>IFilter</code> which requires one student record to
 * satisfy an entire list of conditions in order to satisfy this filter.
 * 
 * @author Luka Mesaric
 */
public class QueryFilter implements IFilter {

	/**
	 * List of conditional expressions that need to be satisfied.
	 */
	private final List<ConditionalExpression> conditionalExpressions;

	/**
	 * Default constructor.
	 * 
	 * @param conditionalExpressions list of conditional expressions
	 * 
	 * @throws NullPointerException if <code>conditionalExpressions</code> is
	 *                              <code>null</code>
	 */
	public QueryFilter(List<ConditionalExpression> conditionalExpressions) {
		this.conditionalExpressions = Util.validateNotNull(
				conditionalExpressions, "conditionalExpressions");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expression : conditionalExpressions) {
			if (!expression.satisfies(record)) {
				return false;
			}
		}
		return true;
	}

}
