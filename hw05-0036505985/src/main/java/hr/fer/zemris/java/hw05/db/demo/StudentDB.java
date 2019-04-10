package hr.fer.zemris.java.hw05.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.java.hw05.db.ParserException;
import hr.fer.zemris.java.hw05.db.QueryFilter;
import hr.fer.zemris.java.hw05.db.QueryParser;
import hr.fer.zemris.java.hw05.db.RecordFormatter;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

/**
 * Console-like application for querying a student database.<br>
 * Supported commands are <code>query</code> and <code>exit</code>.
 * 
 * @author Luka Mesaric
 */
public class StudentDB {

	/**
	 * Console prompt.
	 */
	private static final String PROMPT = "> ";

	/**
	 * Exit command keyword.
	 */
	private static final String COMMAND_EXIT = "exit";

	/**
	 * Query command keyword.
	 */
	private static final String COMMAND_QUERY = "query";

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {

		StudentDatabase db;
		try {
			String filePath = "src/main/resources/database.txt";
			List<String> lines = readData(filePath);
			db = new StudentDatabase(lines);
		} catch (IOException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.println("Exiting the program.");
			return;
		}

		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.print(PROMPT);
				String input = sc.nextLine().strip();
				boolean terminate = performOneQuery(input, db);
				if (terminate) {
					break;
				}
			}
		}
	}

	/**
	 * Returns a list of all lines written in file with path <code>filePath</code>,
	 * or throws an exception with a formatted message.
	 * 
	 * @param filePath path to file
	 * @return list of read lines
	 * 
	 * @throws IOException          if data could not be read
	 * @throws InvalidPathException if <code>filePath</code> does not represent a
	 *                              path
	 */
	private static List<String> readData(String filePath) throws IOException {
		try {
			return Files.readAllLines(
					Paths.get(filePath),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IOException("Could not read data from " + filePath, e);
		}
	}

	/**
	 * Performs one database query. Retrieves data and prints it to standard output.
	 * 
	 * @param input user's command line input
	 * @param db    instance of database used to get queried student records
	 * @return <code>true</code> if program should be terminated, <code>false</code>
	 *         otherwise
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	private static boolean performOneQuery(String input, StudentDatabase db) {
		Util.validateNotNull(input, "input");
		Util.validateNotNull(db, "db");

		if (COMMAND_EXIT.equals(input)) {
			System.out.println("Goodbye!");
			return true;
		} else if (input.isEmpty()) {
			System.out.println("Input cannot be blank.");
			return false;
		}

		String[] split = input.split("\\s+", 2);
		if (!COMMAND_QUERY.equals(split[0])) {
			System.out.println("'" + split[0] + "' is not a supported command.");
			return false;
		} else if (split.length == 1) {
			System.out.println("Query command must have a body.");
			return false;
		}

		QueryParser parser;
		try {
			parser = new QueryParser(split[1]);
		} catch (ParserException e) {
			System.out.println(e.getMessage());
			return false;
		}

		List<StudentRecord> queriedRecords;
		if (parser.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");
			StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
			queriedRecords = new ArrayList<>(1);
			if (r != null) {
				queriedRecords.add(r);
			}
		} else {
			queriedRecords = db.filter(new QueryFilter(parser.getQuery()));
		}

		RecordFormatter.format(queriedRecords).forEach(System.out::println);
		System.out.format("Records selected: %d%n%n", queriedRecords.size());
		return false;
	}

}
