package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Demonstrative class with methods for various operations on student data.
 * 
 * @author Luka Mesaric
 */
public class StudentDemo {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {

		List<StudentRecord> records;
		try {
			List<String> lines = Files.readAllLines(
					Paths.get("./studenti.txt"),
					StandardCharsets.UTF_8);
			records = convert(lines);
		} catch (IOException e) {
			System.out.println("Could not read data from file: " + e.getMessage());
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Could not parse line: " + e.getMessage());
			return;
		}

		printHeader(1);
		System.out.println(vratiBodovaViseOd25(records));
		System.out.println();

		printHeader(2);
		System.out.println(vratiBrojOdlikasa(records));
		System.out.println();

		printHeader(3);
		printList(vratiListuOdlikasa(records));
		System.out.println();

		printHeader(4);
		printList(vratiSortiranuListuOdlikasa(records));
		System.out.println();

		printHeader(5);
		printList(vratiPopisNepolozenih(records));
		System.out.println();

		printHeader(6);
		printMapOfLists(razvrstajStudentePoOcjenama(records));
		System.out.println();

		printHeader(7);
		printRegularMap(vratiBrojStudenataPoOcjenama(records));
		System.out.println();

		printHeader(8);
		printMapOfLists(razvrstajProlazPad(records));

	}

	/**
	 * Converts a list of strings into a list of student records. Ignores any blank
	 * lines.
	 * 
	 * @param  lines                    list of lines to convert into student
	 *                                  records
	 * @return                          list of student records parsed from strings
	 * @throws IllegalArgumentException if any line from <code>lines</code> cannot
	 *                                  be parsed into a <code>StudentRecord</code>
	 *                                  for any reason
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		return lines.stream()
				.filter(Predicate.not(String::isBlank))
				.map(StudentRecord::fromRawData)
				.collect(Collectors.toList());
	}

	//
	// Helper methods for printing results.
	//

	/**
	 * Helper method for printing task header.
	 * 
	 * @param i header number
	 */
	private static void printHeader(int i) {
		System.out.println("Zadatak " + i);
		System.out.println("=========");
	}

	/**
	 * Prints a list, with each element in a new line.
	 * 
	 * @param list list to print
	 */
	private static void printList(List<?> list) {
		list.forEach(System.out::println);
	}

	/**
	 * Prints a map whose keys and values are simple objects.
	 * 
	 * @param map map to print
	 */
	private static <K, V> void printRegularMap(Map<?, ?> map) {
		map.forEach((k, v) -> System.out.println(k + " -> " + v));
	}

	/**
	 * Prints a map whose values are lists.
	 * 
	 * @param     <K> type of keys
	 * @param     <V> type of values stored in lists
	 * @param map map to print
	 */
	private static <K, V> void printMapOfLists(Map<K, List<V>> map) {
		map.forEach((k, v) -> {
			System.out.println(k + " ->");
			printList(v);
			System.out.println();
		});
	}

	//
	// Solutions for given tasks.
	//

	/**
	 * Returns the number of students whose total score is greater than
	 * <code>25</code>.
	 * 
	 * @param  records list of student records
	 * @return         number of students whose total score is greater than
	 *                 <code>25</code>
	 */
	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
				.filter(s -> s.getTotalScore() > 25)
				.count();
	}

	/**
	 * Returns the number of students whose final grade was <code>5</code>.
	 * 
	 * @param  records list of student records
	 * @return         number of students whose final grade was <code>5</code>
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(s -> s.getFinalGrade() == 5)
				.count();
	}

	/**
	 * Returns a list of students whose final grade was <code>5</code>.
	 * 
	 * @param  records list of student records
	 * @return         list of students whose final grade was <code>5</code>
	 */
	private static List<StudentRecord> vratiListuOdlikasa(
			List<StudentRecord> records) {
		return records.stream()
				.filter(s -> s.getFinalGrade() == 5)
				.collect(Collectors.toList());
	}

	/**
	 * Returns a list of students whose final grade was <code>5</code>, ordered by
	 * total score (descending).
	 * 
	 * @param  records list of student records
	 * @return         list of students whose final grade was <code>5</code>
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(
			List<StudentRecord> records) {
		return records.stream()
				.filter(s -> s.getFinalGrade() == 5)
				.sorted((s1, s2) -> Double.compare(
						s2.getTotalScore(),
						s1.getTotalScore()))
				.collect(Collectors.toList());
	}

	/**
	 * Returns a list of jmbags of students who failed the class.
	 * 
	 * @param  records list of student records
	 * @return         list of jmbags of students who failed the class
	 */
	private static List<String> vratiPopisNepolozenih(
			List<StudentRecord> records) {
		return records.stream()
				.filter(s -> s.getFinalGrade() == 1)
				.map(StudentRecord::getJmbag)
				.sorted()
				.collect(Collectors.toList());
	}

	/**
	 * Returns a mapping of final grades to lists of students with those grades.
	 * 
	 * @param  records list of student records
	 * @return         mapping of final grades to lists of students with those
	 *                 grades
	 */
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.groupingBy(StudentRecord::getFinalGrade));
	}

	/**
	 * Returns a mapping of final grades to number of students with that grade.
	 * 
	 * @param  records list of student records
	 * @return         mapping of final grades to number of students with that grade
	 */
	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.toMap(
						StudentRecord::getFinalGrade,
						v -> 1,
						(v1, v2) -> v1 + v2));
	}

	/**
	 * Partitions students into a group that passed the class and a group that
	 * failed.
	 * 
	 * @param  records list of student records
	 * @return         list of students grouped depending if they failed or passed
	 *                 the class
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.partitioningBy(s -> s.getFinalGrade() > 1));
	}

}
