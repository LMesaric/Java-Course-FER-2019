package hr.fer.zemris.java.hw07.demo4;

import java.util.StringJoiner;

import hr.fer.zemris.java.hw07.ExceptionUtil;

/**
 * Represents a record for one student.
 * 
 * @author Luka Mesaric
 */
public class StudentRecord {

	/**
	 * Student's JMBAG. Unique for each student. Never <code>null</code>, always has
	 * length 10.
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
	 * Student's score on the midterm exam.
	 */
	private final double scoreMidterm;

	/**
	 * Student's score on the final exam.
	 */
	private final double scoreFinals;

	/**
	 * Student's score on the laboratory exercises.
	 */
	private final double scoreLaboratory;

	/**
	 * Student's final grade, between 1 and 5 (inclusive).
	 */
	private final int finalGrade;

	/**
	 * Default constructor.
	 * 
	 * @param  jmbag                jmbag
	 * @param  lastName             last name
	 * @param  firstName            first name
	 * @param  scoreMidterm         score on the midterm exam
	 * @param  scoreFinals          score on the final exam
	 * @param  scoreLaboratory      score on the laboratory exercises
	 * @param  finalGrade           final grade
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public StudentRecord(String jmbag, String lastName, String firstName,
			double scoreMidterm, double scoreFinals, double scoreLaboratory,
			int finalGrade) {
		this.jmbag = ExceptionUtil.validateNotNull(jmbag, "jmbag");
		this.lastName = ExceptionUtil.validateNotNull(lastName, "lastName");
		this.firstName = ExceptionUtil.validateNotNull(firstName, "firstName");
		this.scoreMidterm = scoreMidterm;
		this.scoreFinals = scoreFinals;
		this.scoreLaboratory = scoreLaboratory;
		this.finalGrade = finalGrade;
	}

	/**
	 * Creates an instance of <code>StudentRecord</code> from given
	 * <code>data</code>. Each element must be separated by a <code>'\t'</code>.
	 * 
	 * @param  data                     data to parse
	 * @return                          parsed student record
	 * @throws NullPointerException     if <code>data</code> is null
	 * @throws IllegalArgumentException if <code>data</code> cannot be parsed into a
	 *                                  <code>StudentRecord</code> for any reason
	 */
	public static StudentRecord fromRawData(String data) {
		ExceptionUtil.validateNotNull(data, "data");
		String[] parts = data.strip().split("\t");
		if (parts.length != 7) {
			throw new IllegalArgumentException(
					"Incorrect number of parts: " + parts.length);
		}

		try {
			return new StudentRecord(
					parts[0],
					parts[1],
					parts[2],
					Double.parseDouble(parts[3]),
					Double.parseDouble(parts[4]),
					Double.parseDouble(parts[5]),
					Integer.parseInt(parts[6]));

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Could not parse number: " + e.getMessage(), e);
		}
	}

	/**
	 * Getter for <code>jmbag</code>.
	 *
	 * @return <code>jmbag</code>
	 */
	public String getJmbag() { return jmbag; }

	/**
	 * Getter for <code>lastName</code>.
	 *
	 * @return <code>lastName</code>
	 */
	public String getLastName() { return lastName; }

	/**
	 * Getter for <code>firstName</code>.
	 *
	 * @return <code>firstName</code>
	 */
	public String getFirstName() { return firstName; }

	/**
	 * Getter for <code>scoreMidterm</code>.
	 *
	 * @return <code>scoreMidterm</code>
	 */
	public double getScoreMidterm() { return scoreMidterm; }

	/**
	 * Getter for <code>scoreFinals</code>.
	 *
	 * @return <code>scoreFinals</code>
	 */
	public double getScoreFinals() { return scoreFinals; }

	/**
	 * Getter for <code>scoreLaboratory</code>.
	 *
	 * @return <code>scoreLaboratory</code>
	 */
	public double getScoreLaboratory() { return scoreLaboratory; }

	/**
	 * Getter for <code>finalGrade</code>.
	 *
	 * @return <code>finalGrade</code>
	 */
	public int getFinalGrade() { return finalGrade; }

	/**
	 * Returns student's total score.
	 * 
	 * @return sum of score on midterm exam, finals and laboratory exercises
	 */
	public double getTotalScore() {
		return scoreMidterm + scoreFinals + scoreLaboratory;
	}

	@Override
	public String toString() {
		return new StringJoiner("\t")
				.add(jmbag)
				.add(lastName)
				.add(firstName)
				.add(String.valueOf(scoreMidterm))
				.add(String.valueOf(scoreFinals))
				.add(String.valueOf(scoreLaboratory))
				.add(String.valueOf(finalGrade))
				.toString();
	}

}
