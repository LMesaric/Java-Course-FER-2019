package hr.fer.zemris.java.custom.collections.demo;

import static hr.fer.zemris.java.custom.collections.demo.SimpleHashtableDemo1.generateMap;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

/**
 * Fourth demo program for {@link SimpleHashtable}.
 * 
 * @author Luka Mesaric
 */
public class SimpleHashtableDemo4 {

	public static void main(String[] args) {

		// create collection:
		SimpleHashtable<String, Integer> examMarks = generateMap();

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}

		System.out.println(examMarks);
	}
}
