package hr.fer.zemris.java.custom.collections.demo;

import java.util.ConcurrentModificationException;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Sixth demo program.
 * 
 * @author Luka Mesaric
 */
public class CollectionDemo6 {

	public static void main(String[] args) {
		demo(new ArrayIndexedCollection());
		System.out.println();
		demo(new LinkedListIndexedCollection());
	}

	public static void demo(Collection col) {
		System.out.println(col.getClass().getName() + ":");
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		ElementsGetter getter = col.createElementsGetter();
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		col.clear();

		try {
			System.out.println("Jedan element: " + getter.getNextElement());
		} catch (ConcurrentModificationException e) {
			System.out.println("MODIFICIRANO!");
		}
	}

}
