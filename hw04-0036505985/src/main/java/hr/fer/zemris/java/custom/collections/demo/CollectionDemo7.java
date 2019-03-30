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
		demo(new ArrayIndexedCollection<String>());
		System.out.println();
		demo(new LinkedListIndexedCollection<String>());
	}

	public static void demo(Collection<String> col) {
		System.out.println(col.getClass().getName() + ":");
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		ElementsGetter<String> getter = col.createElementsGetter();
		getter.getNextElement();
		getter.processRemaining(System.out::println);
	}

}
