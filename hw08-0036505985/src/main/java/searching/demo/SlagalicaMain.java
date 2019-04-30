package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

/**
 * Demo program for solving puzzles. Shows results in GUI.<br>
 * Program takes exactly one command line argument: starting puzzle
 * configuration.
 * 
 * @author Luka Mesaric
 */
public class SlagalicaMain {

	/**
	 * Number of elements in a puzzle.
	 */
	private static final int NUMBER_OF_ELEMENTS = 9;

	/**
	 * Difference between digit value and its encoding.
	 */
	private static final int DIGIT_SHIFT = 48;

	/**
	 * Converts <code>input</code> to an array of integers. Throws an exception
	 * input does not represent a valid puzzle configuration.
	 * 
	 * @param  input                    char array to inspect
	 * @return                          <code>input</code> converted to an array of
	 *                                  integers
	 * @throws NullPointerException     if <code>input</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>input</code> is not a permutation
	 *                                  of <code>{0,1,2,3,4,5,6,7,8}</code>
	 */
	private static int[] sanitizeInput(char[] input) {
		if (input.length != NUMBER_OF_ELEMENTS) {
			throw new IllegalArgumentException("Puzzle must have exactly "
					+ NUMBER_OF_ELEMENTS + " elements.");
		}

		int[] configuration = new int[NUMBER_OF_ELEMENTS];
		for (int i = 0; i < configuration.length; i++) {
			configuration[i] = input[i] - DIGIT_SHIFT;
		}

		outer: for (int digit = 0; digit < NUMBER_OF_ELEMENTS; digit++) {
			for (int i = 0; i < configuration.length; i++) {
				if (configuration[i] == digit)
					continue outer;
			}
			throw new IllegalArgumentException(
					"Input does not contain digit " + digit);
		}

		return configuration;
	}

	/**
	 * Program entry point.
	 * 
	 * @param args starting puzzle configuration
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Expected exactly one argument.");
			return;
		}

		int[] configuration;
		try {
			configuration = sanitizeInput(args[0].toCharArray());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}

		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(configuration));

		Node<KonfiguracijaSlagalice> rjesenje = SearchUtil.bfsv(
				slagalica, slagalica, slagalica);

		if (rjesenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
		} else {
			System.out.println("Imam rješenje. Broj poteza je: " + rjesenje.getCost());

			List<KonfiguracijaSlagalice> lista = new ArrayList<>();
			Node<KonfiguracijaSlagalice> trenutni = rjesenje;
			while (trenutni != null) {
				lista.add(trenutni.getState());
				trenutni = trenutni.getParent();
			}
			Collections.reverse(lista);
			lista.stream().forEach(k -> {
				System.out.println(k);
				System.out.println();
			});
			SlagalicaViewer.display(rjesenje);
		}
	}

}
