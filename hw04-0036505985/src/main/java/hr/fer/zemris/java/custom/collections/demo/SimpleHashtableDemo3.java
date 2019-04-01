package hr.fer.zemris.java.custom.collections.demo;

import static hr.fer.zemris.java.custom.collections.demo.SimpleHashtableDemo1.generateMap;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

/**
 * Third demo program for {@link SimpleHashtable}.
 * 
 * @author Luka Mesaric
 */
public class SimpleHashtableDemo3 {

	public static void main(String[] args) {

		// create collection:
		SimpleHashtable<String, Integer> examMarks = generateMap();

		for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
			for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
				System.out.printf("(%s => %d) - (%s => %d)%n", 
						pair1.getKey(), pair1.getValue(), 
						pair2.getKey(),	pair2.getValue()
				);
			}
		}
	}
}
