package hr.fer.zemris.java.custom.collections.demo;

import static hr.fer.zemris.java.custom.collections.demo.SimpleHashtableDemo1.generateMap;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

/**
 * Sixth demo program for {@link SimpleHashtable}.
 * 
 * @author Luka Mesaric
 */
public class SimpleHashtableDemo6 {

	public static void main(String[] args) {

		// create collection:
		SimpleHashtable<String, Integer> examMarks = generateMap();

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) { // MUST THROW ConcurrentModificationException
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				examMarks.remove("Ivana");
			}
		}
	}
}
