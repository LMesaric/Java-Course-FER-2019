package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Fifth demo program.
 * 
 * @author Luka Mesaric
 */
public class CollectionDemo5 {

	public static void main(String[] args) {

		demo(new ArrayIndexedCollection<String>(), new ArrayIndexedCollection<String>());
		System.out.println();
		demo(new LinkedListIndexedCollection<String>(), new LinkedListIndexedCollection<String>());
	}

	public static void demo(Collection<String> col1, Collection<String> col2) {
		System.out.println(col1.getClass().getName() + ":");
		col1.add("Ivo");
		col1.add("Ana");
		col1.add("Jasna");
		col2.add("Jasmina");
		col2.add("Štefanija");
		col2.add("Karmela");
		ElementsGetter<String> getter1 = col1.createElementsGetter();
		ElementsGetter<String> getter2 = col1.createElementsGetter();
		ElementsGetter<String> getter3 = col2.createElementsGetter();
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
	}

}
