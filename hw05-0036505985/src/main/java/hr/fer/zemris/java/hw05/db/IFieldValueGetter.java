package hr.fer.zemris.java.hw05.db;

/**
 * Represents a getter for some string field of a <code>StudentRecord</code>
 * instance.
 * 
 * @author Luka Mesaric
 */
@FunctionalInterface
public interface IFieldValueGetter {

	/**
	 * Returns wanted field value from <code>record</code>.
	 * 
	 * @param record record from which the return value is extracted
	 * @return extracted value
	 * 
	 * @throws NullPointerException if <code>record</code> is <code>null</code>
	 */
	String get(StudentRecord record);

}
