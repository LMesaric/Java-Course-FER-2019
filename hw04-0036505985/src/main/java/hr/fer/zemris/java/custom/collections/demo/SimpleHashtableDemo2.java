package hr.fer.zemris.java.custom.collections.demo;

import static hr.fer.zemris.java.custom.collections.demo.SimpleHashtableDemo1.generateMap;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

/**
 * Second demo program for {@link SimpleHashtable}.
 * 
 * @author Luka Mesaric
 */
public class SimpleHashtableDemo2 {

	public static void main(String[] args) {

		// create collection:
		SimpleHashtable<String, Integer> examMarks = generateMap();

		for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}
	}
}
