package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program uses rectangle's width and height to output a message about
 * rectangle's area and perimeter.
 * 
 * <p>
 * If two command line arguments are provided, they are used as width and
 * height. If no arguments are provided, interactive user input is required. If
 * any other number of arguments is given, program is terminated.
 * </p>
 * 
 * @author Luka Mesaric
 */
public class Rectangle {

	/**
	 * Program entry point.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		if (args.length == 2) {
			try {
				processInput(validateAndParseInput(args[0]), validateAndParseInput(args[1]));
			} catch (IllegalArgumentException ex) {
				System.exit(1);
			}

		} else if (args.length == 0) {
			try (Scanner sc = new Scanner(System.in)) {
				processInput(getInputFromUser(sc, "širinu"), getInputFromUser(sc, "visinu"));
			}

		} else {
			System.out.println("Program se mora pokrenuti bez argumenata ili s točno 2 argumenta!");
			System.exit(1);
		}

	}

	/**
	 * Displays a formatted message about rectangle's area and perimeter.
	 * 
	 * @param width  rectangle width
	 * @param height rectangle height
	 */
	public static void processInput(double width, double height) {
		System.out.printf("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.", width, height,
				width * height, 2 * (width + height));
	}

	/**
	 * Asks user to input a positive decimal number (decimal point is the only valid
	 * decimal separator). Method will not return until a valid input is given.
	 * 
	 * @param sc   Scanner to be used for reading user input
	 * @param name name of the parameter for which input is needed
	 * @return user's input; positive <code>double</code>
	 */
	public static double getInputFromUser(Scanner sc, String name) {

		while (true) {
			System.out.printf("Unesite %s > ", name);
			String input = sc.next();

			try {
				return validateAndParseInput(input);
			} catch (IllegalArgumentException ex) {
				continue;
			}
		}
	}

	/**
	 * If <code>input</code> represents a positive double, it is parsed and
	 * returned. Otherwise an appropriate message is displayed and
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param input String to be parsed
	 * @return positive double parsed from <code>input</code>
	 * @throws IllegalArgumentException if input cannot be parsed, or if represents
	 *                                  a negative number
	 */
	public static double validateAndParseInput(String input) {

		double num;

		try {
			num = Double.parseDouble(input);
		} catch (NumberFormatException ex) {
			System.out.printf("'%s' se ne može protumačiti kao broj.%n", input);
			throw new IllegalArgumentException();
		}

		if (num < 0) {
			System.out.println("Unijeli ste negativnu vrijednost.");
			throw new IllegalArgumentException();
		}

		return num;
	}

}
