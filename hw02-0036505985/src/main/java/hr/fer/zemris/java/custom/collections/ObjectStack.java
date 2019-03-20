package hr.fer.zemris.java.custom.collections;

/**
 * Models an automatically resizable stack-like collection of objects.
 * 
 * <p>
 * Duplicate elements are allowed. Storage of <code>null</code> references is
 * not allowed.
 * </p>
 * 
 * @author Luka Mesaric
 * 
 * @see ArrayIndexedCollection
 */
public class ObjectStack {

	/**
	 * Data structure used for storage and delegating method calls.
	 */
	private ArrayIndexedCollection storage = new ArrayIndexedCollection();

	/**
	 * Default constructor. Creates an empty stack.
	 */
	public ObjectStack() {
		super();
	}

	/**
	 * Checks if stack is empty.
	 * 
	 * @return <code>true</code> if stack contains no objects, <code>false</code>
	 *         otherwise
	 */
	public boolean isEmpty() {
		return storage.isEmpty();
	}

	/**
	 * Returns the number of elements in this stack.
	 * 
	 * @return number of currently stored objects
	 */
	public int size() {
		return storage.size();
	}

	/**
	 * Pushes the given object on top of this stack. Pushing <code>null</code> is
	 * forbidden.
	 * <p>
	 * Pushes an element in constant time (might need to reallocate).
	 * </p>
	 * 
	 * @param value object to push
	 * 
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	public void push(Object value) {
		storage.add(value);
	}

	/**
	 * Removes last value pushed on stack from stack and returns it. If the stack is
	 * empty when method <code>pop</code> is called, {@link EmptyStackException} is
	 * thrown.
	 * <p>
	 * Returns and removes the value in constant time.
	 * </p>
	 * 
	 * @return last object pushed on stack
	 * 
	 * @throws EmptyStackException if stack was empty
	 */
	public Object pop() {
		return lastElement(true);
	}

	/**
	 * Returns the last value pushed on stack, but does not remove it. If the stack
	 * is empty when method <code>peek</code> is called, {@link EmptyStackException}
	 * is thrown.
	 * <p>
	 * Returns the value in constant time.
	 * </p>
	 * 
	 * @return last object pushed on stack
	 * 
	 * @throws EmptyStackException if stack was empty
	 */
	public Object peek() {
		return lastElement(false);
	}

	/**
	 * Returns last value pushed on stack. Depending on <code>shouldRemove</code>,
	 * the same value might be removed from stack. If the stack is empty when method
	 * <code>lastElement</code> is called, {@link EmptyStackException} is thrown.
	 * <p>
	 * Returns (and removes) the value in constant time.
	 * </p>
	 * 
	 * @param shouldRemove if <code>true</code>, returned element will also be
	 *                     removed from stack
	 * @return last object pushed on stack
	 * 
	 * @throws EmptyStackException if stack was empty
	 */
	private Object lastElement(boolean shouldRemove) {
		try {
			int index = storage.size() - 1;
			Object result = storage.get(index);
			if (shouldRemove) {
				storage.remove(index);
			}
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new EmptyStackException("Cannot return value from empty stack.", e);
		}
	}

	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		storage.clear();
	}

}
