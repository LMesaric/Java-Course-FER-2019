package hr.fer.zemris.java.hw05.db;

/**
 * Utility class that offers implementations of field getters.
 * 
 * @author Luka Mesaric
 */
public class FieldValueGetters {

	/**
	 * Extracts student's first name.
	 */
	public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

	/**
	 * Extracts student's last name.
	 */
	public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

	/**
	 * Extracts student's jmbag.
	 */
	public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;

	/**
	 * Private constructor disables creating instances of this class.
	 */
	private FieldValueGetters() {
		// No FieldValueGetters for you :)
	}

}
