package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Iterator over a collection. Implementations must allow iteration over entire
 * collection in linear time. Changing the collection during iteration is not
 * allowed.
 * 
 * @author Luka Mesaric
 */
public interface ElementsGetter {

	/**
	 * Checks if there are any elements that have not yet been used (visited).
	 * 
	 * @return <code>true</code> if there is at least one element that was not used,
	 *         <code>false</code> otherwise
	 * 
	 * @throws ConcurrentModificationException if collection was changed after the
	 *                                         moment when this
	 *                                         <code>ElementsGetter</code> was
	 *                                         created
	 */
	boolean hasNextElement();

	/**
	 * Returns the next element in the iteration.
	 * 
	 * @return first object that was not used before
	 * 
	 * @throws NoSuchElementException          if all elements have already been
	 *                                         used
	 * @throws ConcurrentModificationException if collection was changed after the
	 *                                         moment when this
	 *                                         <code>ElementsGetter</code> was
	 *                                         created
	 */
	Object getNextElement();

	/**
	 * Processes all remaining elements using the supplied <code>Processor</code>.
	 * 
	 * @param p <code>Processor</code> used to process elements
	 * 
	 * @throws ConcurrentModificationException if collection was changed after the
	 *                                         moment when this
	 *                                         <code>ElementsGetter</code> was
	 *                                         created
	 */
	default void processRemaining(Processor p) {
		while (this.hasNextElement()) {
			p.process(this.getNextElement());
		}
	}

}
