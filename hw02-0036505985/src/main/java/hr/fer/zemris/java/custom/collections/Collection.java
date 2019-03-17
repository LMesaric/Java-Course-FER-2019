package hr.fer.zemris.java.custom.collections;

/**
 * Class represents any general collection of objects.
 * 
 * @author Luka Mesaric
 */
public class Collection {

	/**
	 * Default constructor.
	 */
	protected Collection() {
		super();
	}

	/**
	 * Checks if collection is empty.
	 * 
	 * @return <code>true</code> if collection contains no objects,
	 *         <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of elements in this collection.
	 * 
	 * @return number of currently stored objects
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value object to add
	 */
	public void add(Object value) {
		return;
	}

	/**
	 * Checks if given object is contained inside this collection.
	 * 
	 * @param value value to look for; can be <code>null</code>
	 * @return <code>true</code> only if the collection contains the given value, as
	 *         determined by {@link Object#equals(Object)}, <code>false</code>
	 *         otherwise
	 */
	public boolean contains(Object value) {
		return false;
	}

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
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Allocates a new array the size of this collection, fills it with collection
	 * content and returns the array. This method never returns <code>null</code>.
	 * 
	 * @return collection content represented as an array of <code>Objects</code>
	 * @throws UnsupportedOperationException if not overridden
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Calls {@link Processor#process(Object)} for each element of this collection.
	 * The order in which elements will be sent is undefined in this class.
	 * 
	 * @param processor <code>Processor</code> instance used to process elements of
	 *                  this collection
	 */
	public void forEach(Processor processor) {
		return;
	}

	/**
	 * Method adds all elements from the given collection into the current
	 * collection. Other collection <b>remains unchanged</b>.
	 * 
	 * @param other collection whose elements will be added into this collection
	 */
	public void addAll(Collection other) {

		/**
		 * Helper local class for adding all values <code>other</code> into the current
		 * collection.
		 * 
		 * @author Luka Mesaric
		 */
		class AddElementsProcessor extends Processor {

			/**
			 * Adds <code>value</code> to collection.
			 */
			@Override
			public void process(Object value) {
				add(value);
			}
		}

		other.forEach(new AddElementsProcessor());
	}

	/**
	 * Removes all elements from this collection.
	 */
	public void clear() {
		return;
	}

}
