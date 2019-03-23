package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Seventh demo program.
 * 
 * @author Luka Mesaric
 */
public class CollectionDemo7 {

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
		getter.getNextElement();
		getter.processRemaining((System.out::println);

	}

}
