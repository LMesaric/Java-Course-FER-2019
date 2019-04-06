package hr.fer.zemris.java.custom.collections;

/**
 * Represents a collection of objects stored in an ordered list.
 * 
 * @param <E> the type of elements stored in this list
 * 
 * @author Luka Mesaric
 * 
 * @see Collection
 */
public interface List<E> extends Collection<E> {

	/**
	 * Returns the object that is stored in the list at position <code>index</code>.
	 * Valid indices are <code>0</code> to <code>size-1</code>.
	 * 
	 * @param index index from range <code>[0, size-1]</code>
	 * @return object stored at position <code>index</code>
	 * 
	 * @throws IndexOutOfBoundsException if <code>index</code> is less than
	 *                                   <code>0</code> or greater than
	 *                                   <code>size-1</code>
	 */
	E get(int index);

	/**
	 * Inserts the given <code>value</code> at the given <code>position</code>.
	 * Legal positions are from <code>0</code> to <code>size</code> (both included).
	 * 
	 * @param value    object to insert
	 * @param position position from <code>0</code> to <code>size</code> (both
	 *                 included)
	 * 
	 * @throws IndexOutOfBoundsException if <code>position</code> is less than
	 *                                   <code>0</code> or greater than
	 *                                   <code>size</code>
	 */
	void insert(E value, int position);

	/**
	 * Searches the list and returns the index of the first occurrence of the given
	 * <code>value</code>, or <code>-1</code> if the <code>value</code> is not
	 * found. Equality is determined by {@link Object#equals(Object)}.
	 * 
	 * @param value value to search for; can be <code>null</code>
	 * @return index of the first occurrence of the given <code>value</code>; <br>
	 *         <code>-1</code> if the <code>value</code> is not found
	 */
	int indexOf(Object value);

	/**
	 * Removes element at specified <code>index</code> from list. Element that was
	 * previously at location <code>index+1</code> will now be on location
	 * <code>index</code>, etc. Legal indices are <code>0</code> to
	 * <code>size-1</code>.
	 * <p>
	 * This method is different from {@link Collection#remove(Object)}.
	 * </p>
	 * 
	 * @param index index from which to remove an object
	 * 
	 * @throws IndexOutOfBoundsException if <code>index</code> is less than
	 *                                   <code>0</code> or greater than
	 *                                   <code>size-1</code>
	 */
	void remove(int index);

}
