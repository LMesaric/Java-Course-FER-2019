package hr.fer.zemris.java.custom.collections.demo;

import java.lang.reflect.InvocationTargetException;

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

	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		demo(ArrayIndexedCollection.class);
		System.out.println();
		demo(LinkedListIndexedCollection.class);
	}

	public static void demo(Class<? extends Collection> colClass) throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		Collection col1 = colClass.getDeclaredConstructor().newInstance();
		Collection col2 = colClass.getDeclaredConstructor().newInstance();
		System.out.println(col1.getClass().getName() + ":");
		col1.add("Ivo");
		col1.add("Ana");
		col1.add("Jasna");
		col2.add("Jasmina");
		col2.add("Štefanija");
		col2.add("Karmela");
		ElementsGetter getter1 = col1.createElementsGetter();
		ElementsGetter getter2 = col1.createElementsGetter();
		ElementsGetter getter3 = col2.createElementsGetter();
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
	}

}
