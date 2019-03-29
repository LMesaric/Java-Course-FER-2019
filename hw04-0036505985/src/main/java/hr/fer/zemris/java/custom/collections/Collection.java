package hr.fer.zemris.java.custom.collections;

/**
 * Interface represents any general collection of objects.
 * 
 * @author Luka Mesaric
 */
public interface Collection {

	/**
	 * Checks if collection is empty.
	 * 
	 * @return <code>true</code> if collection contains no objects,
	 *         <code>false</code> otherwise
	 */
	default boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return number of currently stored objects
	 */
	int size();

	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value object to add
	 */
	void add(Object value);

	/**
	 * Checks if given object is contained inside this collection.
	 * 
	 * @param value value to look for; can be <code>null</code>
	 * @return <code>true</code> only if the collection contains the given value, as
	 *         determined by {@link Object#equals(Object)}, <code>false</code>
	 *         otherwise
	 */
	boolean contains(Object value);

	/**
	 * Removes exactly one object equal to <code>value</code> from this collection,
	 * if such exists (determined by {@link Object#equals(Object)}). If there are
	 * multiple candidates for removal, it is not defined which one will be removed.
	 * 
	 * @param value value to remove, if it exists inside this collection; can be
	 *              <code>null</code>
	 * @return <code>true</code> only if an object was removed, <code>false</code>
	 *         otherwise
	 */
	boolean remove(Object value);

	/**
	 * Allocates a new array the size of this collection, fills it with collection
	 * content and returns the array. This method never returns <code>null</code>.
	 * 
	 * @return collection content represented as an array of <code>Objects</code>
	 */
	Object[] toArray();

	/**
	 * Calls {@link Processor#process(Object)} for each element of this collection.
	 * The order in which elements will be sent is undefined in this class.
	 * 
	 * @param processor <code>Processor</code> instance used to process elements of
	 *                  this collection
	 * 
	 * @throws NullPointerException if <code>processor</code> is <code>null</code>
	 */
	default void forEach(Processor processor) {
		Util.validateNotNull(processor, "processor");
		createElementsGetter().processRemaining(processor);
	}

	/**
	 * Method adds all elements from the given collection into the current
	 * collection. Other collection <b>remains unchanged</b>.
	 * 
	 * @param other collection whose elements will be added into this collection
	 * 
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	default void addAll(Collection other) {
		Util.validateNotNull(other, "other");
		other.forEach(this::add);
	}

	/**
	 * Removes all elements from this collection.
	 */
	void clear();

	/**
	 * Returns an instance of <code>ElementsGetter</code> that can be used to
	 * iterate over a collection in linear time.
	 * 
	 * @return instance of <code>ElementsGetter</code> used for iteration
	 */
	ElementsGetter createElementsGetter();

	/**
	 * All elements from <code>col</code> that <code>tester</code> accepts are added
	 * into this collection.
	 * 
	 * @param col    collection whose elements are tested
	 * @param tester function used for testing elements
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		Util.validateNotNull(col, "col");
		Util.validateNotNull(tester, "tester");

		ElementsGetter getter = col.createElementsGetter();
		getter.processRemaining(value -> {
			if (tester.test(value)) {
				this.add(value);
			}
		});
	}

}
