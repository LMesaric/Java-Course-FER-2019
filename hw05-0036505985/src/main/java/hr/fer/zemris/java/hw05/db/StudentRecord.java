package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Represents a record for one student.
 * 
 * @author Luka Mesaric
 */
public class StudentRecord {

	/**
	 * Student's JMBAG. Unique for each student. Never <code>null</code>.
	 */
	private final String jmbag;

	/**
	 * Student's last name. Never <code>null</code>.
	 */
	private final String lastName;

	/**
	 * Student's first name. Never <code>null</code>.
	 */
	private final String firstName;

	/**
	 * Student's final grade, between 1 and 5 (inclusive).
	 */
	private final int finalGrade;

	/**
	 * Lowest legal grade.
	 */
	public static final int GRADE_MIN = 1;

	/**
	 * Highest legal grade.
	 */
	public static final int GRADE_MAX = 5;

	/**
	 * Default constructor.
	 * 
	 * @param jmbag      jmbag
	 * @param lastName   last name
	 * @param firstName  first name
	 * @param finalGrade final grade
	 * 
	 * @throws NullPointerException     if any argument is <code>null</code>.
	 * @throws IllegalArgumentException if <code>finalGrade</code> is not from
	 *                                  interval <code>[1, 5]</code>.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = Util.validateNotNull(jmbag, "jmbag");
		this.lastName = Util.validateNotNull(lastName, "lastName");
		this.firstName = Util.validateNotNull(firstName, "firstName");
		this.finalGrade = validateFinalGrade(finalGrade);
	}

	/**
	 * Validates that <code>grade</code> is from interval <code>[1, 5]</code>.
	 * 
	 * @param grade grade to validate
	 * @return given <code>grade</code>
	 * 
	 * @throws IllegalArgumentException if <code>grade</code> is not from interval
	 *                                  <code>[1, 5]</code>.
	 */
	private static int validateFinalGrade(int grade) {
		if (grade < GRADE_MIN || grade > GRADE_MAX) {
			throw new IllegalArgumentException("Final grade must be from interval [1, 5].");
		}
		return grade;
	}

	/**
	 * Getter for <code>jmbag</code>.
	 *
	 * @return <code>jmbag</code>
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for <code>lastName</code>.
	 *
	 * @return <code>lastName</code>
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for <code>firstName</code>.
	 *
	 * @return <code>firstName</code>
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter for <code>finalGrade</code>.
	 *
	 * @return <code>finalGrade</code>
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	@Override
	public String toString() {
		return "StudentRecord [jmbag=" + jmbag + ", lastName=" + lastName + ", firstName=" + firstName + ", finalGrade="
				+ finalGrade + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StudentRecord)) {
			return false;
		}
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}

}
