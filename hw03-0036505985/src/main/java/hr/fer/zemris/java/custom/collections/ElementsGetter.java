package hr.fer.zemris.java.custom.collections;

import java.util.NoSuchElementException;

/**
 * Iterator over a collection. Implementations must allow iteration over entire
 * collection in linear time.
 * 
 * @author Luka Mesaric
 */
public interface ElementsGetter {

	/**
	 * Checks if there are any elements that have not yet been used (visited).
	 * 
	 * @return <code>true</code> if there is at least one element that was not used,
	 *         <code>false</code> otherwise
	 */
	boolean hasNextElement();

	/**
	 * Returns the next element in the iteration.
	 * 
	 * @return first object that was not used before
	 * 
	 * @throws NoSuchElementException if all elements have already been used
	 */
	Object getNextElement();

}
