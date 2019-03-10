package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program repeatedly asks user to input an integer from range [3, 20] whose
 * factorial will then be calculated. Program stops when the word "kraj" is
 * entered instead of an integer.
 * 
 * @author Luka Mesaric
 */
public class Factorial {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		while (true) {

			System.out.print("Unesite broj > ");

			if (sc.hasNextInt()) {
				int input = sc.nextInt();

				if (input < 3 || input > 20) {
					System.out.printf("'%d' nije broj u dozvoljenom rasponu.%n", input);
					continue;
				}

				System.out.printf("%d! = %d%n", input, calculateFactorial(input));

			} else {
				String input = sc.next();

				if (input.trim().equalsIgnoreCase("kraj")) {
					System.out.println("Doviđenja.");
					break;
				}

				System.out.printf("'%s' nije cijeli broj.%n", input);
			}
		}

		sc.close();

	}

	/**
	 * Calculates factorial of given argument.
	 * 
	 * @param n number whose factorial is to be calculated; must be 0 or greater
	 * @return factorial of n (<code>n!</code>)
	 * 
	 * @throws IllegalArgumentException if <code>n</code> is a negative number or
	 *                                  result was too large for <code>long</code>.
	 */
	public static long calculateFactorial(int n) {

		if (n < 0) {
			throw new IllegalArgumentException("Cannot calculate factorial of a negative number!");
		}

		long fact = 1L;
		for (int i = 2; i <= n; i++) {
			fact *= i;

			if (fact < 0) {
				throw new IllegalArgumentException("Argument too large - result overflowed!");
			}
		}

		return fact;
	}
}
