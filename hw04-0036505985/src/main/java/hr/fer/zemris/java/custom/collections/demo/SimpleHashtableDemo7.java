package hr.fer.zemris.java.custom.collections.demo;

import static hr.fer.zemris.java.custom.collections.demo.SimpleHashtableDemo1.generateMap;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

/**
 * Sixth demo program for {@link SimpleHashtable}.
 * 
 * @author Luka Mesaric
 */
public class SimpleHashtableDemo7 {

	public static void main(String[] args) {

		// create collection:
		SimpleHashtable<String, Integer> examMarks = generateMap();

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter.remove();
		}

		// map must be empty
		System.out.println();
		System.out.println(examMarks);
		System.out.printf("Veličina: %d%n", examMarks.size());
	}
}
