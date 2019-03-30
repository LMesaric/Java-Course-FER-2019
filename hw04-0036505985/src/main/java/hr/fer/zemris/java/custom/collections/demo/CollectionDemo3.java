package hr.fer.zemris.java.custom.collections.demo;

import java.util.NoSuchElementException;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Third demo program.
 * 
 * @author Luka Mesaric
 */
public class CollectionDemo3 {

	public static void main(String[] args) {
		demo(new ArrayIndexedCollection<String>());
		System.out.println();
		demo(new LinkedListIndexedCollection<String>());
	}

	public static void demo(Collection<String> col) {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		ElementsGetter<String> getter = col.createElementsGetter();
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		try {
			System.out.println("Jedan element: " + getter.getNextElement());
		} catch (NoSuchElementException e) {
			System.out.println("PRETJERALI!");
		}
	}

}
