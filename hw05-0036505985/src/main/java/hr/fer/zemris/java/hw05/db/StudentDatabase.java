package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Models a simple database for storing student records.
 * 
 * @author Luka Mesaric
 */
public class StudentDatabase {

	/**
	 * List of student records stored in this instance of database. Ordered the same
	 * way as data given through constructor. Elements must not be
	 * <code>null</code>.
	 */
	private final List<StudentRecord> studentRecords;

	/**
	 * Index of all student records, accessible by JMBAG. Keys and values must not
	 * be <code>null</code>.
	 */
	private final Map<String, StudentRecord> index;

	/**
	 * Default constructor. Execution without exceptions means that all data is
	 * valid and there are no duplicates.
	 * 
	 * @param data list of strings containing student records
	 * 
	 * @throws NullPointerException     if <code>data</code> is <code>null</code>
	 * @throws IllegalArgumentException if any element of given data does not
	 *                                  represent a valid student record, or any
	 *                                  student record appears twice
	 */
	public StudentDatabase(List<String> data) {
		Util.validateNotNull(data, "data");
		int size = data.size();
		this.studentRecords = new ArrayList<>(size);
		this.index = new HashMap<>(size << 1);

		for (String line : data) {
			if (line == null || (line = line.strip()).isEmpty()) {
				continue;
			}
			StudentRecord record = parseRecordFromLine(line);
			if (index.put(record.getJmbag(), record) != null) {
				throw new IllegalArgumentException(
						"Student appeared more than once: " + line);
			}
			studentRecords.add(record);
		}
	}

	/**
	 * Helper method for constructing a <code>StudentRecord</code> from given line
	 * of textual data.
	 * 
	 * @param line line to parse
	 * @return constructed student record, never <code>null</code>
	 * 
	 * @throws IllegalArgumentException if given data does not represent a valid
	 *                                  student record
	 */
	private StudentRecord parseRecordFromLine(String line) {
		String[] parts = line.split("\\t");
		if (parts.length != 4) {
			throw new IllegalArgumentException(
					"Database entry must have exactly 4 items: " + line);
		}
		try {
			int grade = Integer.parseInt(parts[3]);
			return new StudentRecord(parts[0], parts[1], parts[2], grade);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Grade must be an integer: " + line, e);
		}
	}

	/**
	 * Returns student record for given <code>jmbag</code> in O(1).
	 * 
	 * @param jmbag jmbag to look for
	 * @return student record for given <code>jmbag</code> or <code>null</code> if
	 *         such record does not exist
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}

	/**
	 * Returns a list of all student records which given <code>filter</code>
	 * accepts.
	 * 
	 * @param filter filter
	 * @return filtered records
	 */
	public List<StudentRecord> filter(IFilter filter) {
		return studentRecords.stream()
				.filter(filter::accepts)
				.collect(Collectors.toList());
	}

}
