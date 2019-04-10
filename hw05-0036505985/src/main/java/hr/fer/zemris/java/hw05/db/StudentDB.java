package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-like application for querying a student database.
 * 
 * @author Luka Mesaric
 */
public class StudentDB {

	/**
	 * Console prompt.
	 */
	private static final String PROMPT = "> ";

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {

		String filePath = "src/main/resources/database.txt";
		List<String> lines;

		try {
			lines = Files.readAllLines(
					Paths.get(filePath),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Could not read data from " + filePath);
			System.out.println("Exiting the program.");
			return;
		}

		StudentDatabase db;
		try {
			db = new StudentDatabase(lines);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.println("Exiting the program.");
			return;
		}

		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.print(PROMPT);
				String input = sc.nextLine().strip();

				if ("exit".equals(input)) {
					System.out.println("Goodbye!");
					return;
				} else if (input.isEmpty()) {
					System.out.println("Input cannot be blank.");
					continue;
				}

				String[] split = input.split("\\s+", 2);
				if (!"query".equals(split[0])) {
					System.out.println("'" + split[0] + "' is not a supported command.");
					continue;
				} else if (split.length == 1) {
					System.out.println("Query command must have a body.");
					continue;
				}

				QueryParser parser;
				try {
					parser = new QueryParser(split[1]);
				} catch (ParserException e) {
					System.out.println(e.getMessage());
					continue;
				}
				List<StudentRecord> queriedRecords;
				if (parser.isDirectQuery()) {
					System.out.println("Using index for record retrieval.");
					StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
					queriedRecords = new ArrayList<>();
					if (r != null) {
						queriedRecords.add(r);
					}
				} else {
					queriedRecords = db.filter(new QueryFilter(parser.getQuery()));
				}
				RecordFormatter.format(queriedRecords).forEach(System.out::println);
				System.out.println("Records selected: " + queriedRecords.size());
				System.out.println();
			}
		}

	}

}
