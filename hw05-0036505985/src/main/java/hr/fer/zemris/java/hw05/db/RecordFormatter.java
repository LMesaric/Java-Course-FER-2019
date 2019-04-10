package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.ToIntFunction;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Utility class for formatting a collection of student records.
 * 
 * @author Luka Mesaric
 */
public class RecordFormatter {

	/**
	 * Produces a list of strings, with each element representing one line of
	 * formatted output.
	 * 
	 * @param records records to format
	 * @return formatted records that can be used to print to console or file etc.
	 * 
	 * @throws NullPointerException if <code>records</code> is <code>null</code>
	 */
	public static List<String> format(List<? extends StudentRecord> records) {
		Util.validateNotNull(records, "records");
		if (records.isEmpty()) {
			return Collections.emptyList();
		}

		final int extra = 2;

		final int jmbagSize = extra + calculateMaxValue(records, r -> r.getJmbag().length());
		final int lastNameSize = extra + calculateMaxValue(records, r -> r.getLastName().length());
		final int firstNameSize = extra + calculateMaxValue(records, r -> r.getFirstName().length());
		final int gradeSize = extra + 1;

		List<String> formatted = new ArrayList<>(records.size() + 2);
		String header = new StringBuilder().append('+')
				.append("=".repeat(jmbagSize)).append('+')
				.append("=".repeat(lastNameSize)).append('+')
				.append("=".repeat(firstNameSize)).append('+')
				.append("=".repeat(gradeSize)).append('+')
				.toString();

		formatted.add(header);

		for (StudentRecord record : records) {
			String format = "| %1$-" + (jmbagSize - 1) + "s"
					+ "| %2$-" + (lastNameSize - 1) + "s"
					+ "| %3$-" + (firstNameSize - 1) + "s"
					+ "| %4$-" + (gradeSize - 1) + "s"
					+ "|";

			String line = String.format(format,
					record.getJmbag(), record.getLastName(),
					record.getFirstName(), record.getFinalGrade());

			formatted.add(line);
		}

		formatted.add(header);

		return formatted;
	}

	/**
	 * Helper method for calculating maximum of values extracted from
	 * <code>records</code> by using <code>mapper</code>.
	 * 
	 * @param records records on which to apply the function
	 * @param mapper  function for extracting wanted integer value
	 * @return maximum of extracted values
	 * 
	 * @throws NullPointerException   if any argument is <code>null</code>
	 * @throws NoSuchElementException if <code>records</code> is empty
	 */
	private static int calculateMaxValue(
			Collection<? extends StudentRecord> records,
			ToIntFunction<? super StudentRecord> mapper) {
		Util.validateNotNull(records, "records");
		Util.validateNotNull(mapper, "mapper");

		return records.stream()
				.mapToInt(mapper)
				.max()
				.orElseThrow();
	}

	/**
	 * Private constructor disables creating instances of this class.
	 */
	private RecordFormatter() {
		// No RecordFormatter for you :)
	}

}
