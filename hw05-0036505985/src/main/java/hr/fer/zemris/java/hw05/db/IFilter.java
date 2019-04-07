package hr.fer.zemris.java.hw05.db;

/**
 * Represents a filter (boolean-valued function) of one argument.
 * 
 * @author Luka Mesaric
 */
@FunctionalInterface
public interface IFilter {

	/**
	 * Evaluates this filter on the given argument.
	 * 
	 * @param record the input argument
	 * @return <code>true</code> if filter accepts given <code>record</code>,
	 *         <code>false</code> otherwise
	 * 
	 * @throws NullPointerException if <code>record</code> is <code>null</code>.
	 */
	public boolean accepts(StudentRecord record);

}
