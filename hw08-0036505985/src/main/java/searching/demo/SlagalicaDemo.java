package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;

/**
 * Demo program for solving puzzles.
 * 
 * @author Luka Mesaric
 */
public class SlagalicaDemo {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {

		// 6 moves
//		Slagalica slagalica = new Slagalica(
//				new KonfiguracijaSlagalice(new int[] { 2, 3, 0, 1, 4, 6, 7, 5, 8 }));

		// impossible
		Slagalica slagalica = new Slagalica(
				new KonfiguracijaSlagalice(new int[] { 1, 6, 4, 5, 0, 2, 8, 7, 3 }));

//		Node<KonfiguracijaSlagalice> rjesenje = SearchUtil.bfs(
//				slagalica, slagalica, slagalica);
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
		}
	}

}
