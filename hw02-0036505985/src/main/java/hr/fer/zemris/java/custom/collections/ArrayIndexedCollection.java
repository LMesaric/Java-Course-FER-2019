package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * Models an automatically resizable array-backed collection of objects.
 * 
 * <p>
 * Duplicate elements are allowed. Storage of <code>null</code> references is
 * not allowed.
 * </p>
 * 
 * @author Luka Mesaric
 * 
 * @see Collection
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * Current size of this collection - number of elements actually stored in
	 * <code>elements</code> array.
	 */
	private int size = 0;

	/**
	 * An array of object references whose length is determined by
	 * <code>capacity</code> argument of (some) constructors.
	 */
	private Object[] elements;

	/**
	 * Initial capacity used when no other is provided. Value is {@value}.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Factor by which to increase collection's size when it is full. Value is
	 * {@value}.
	 */
	private static final int RESIZE_FACTOR = 2;

	/**
	 * Default constructor. Creates an empty collection an sets its initial capacity
	 * to {@value #DEFAULT_CAPACITY}.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates an empty collection with initial capacity set to
	 * <code>initialCapacity</code>.
	 * 
	 * @param initialCapacity initial capacity of this collection
	 * 
	 * @throws IllegalArgumentException if <code>initialCapacity</code> is less than
	 *                                  <code>1</code>
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		validateCapacity(initialCapacity);
		elements = new Object[initialCapacity];
	}

	/**
	 * Creates a new collection the size of <code>other</code> and copies its
	 * content. If <code>other</code> had size less than @value #DEFAULT_CAPACITY},
	 * initial capacity will be set to @value #DEFAULT_CAPACITY}.
	 * 
	 * @param other collection whose elements are copied into this collection
	 * 
	 * @throws NullPointerException if <code>other</code> is <code>null</code>, or
	 *                              if any element in <code>other</code> is
	 *                              <code>null</code>
	 */
	public ArrayIndexedCollection(Collection other) {
		/*
		 * Avoiding the problem where 'elements.length' could be 0 after using an empty
		 * collection, and resizing would then have no effect (0*2=0).
		 */
		this(other, DEFAULT_CAPACITY);
	}

	/**
	 * Creates a new collection with capacity set to <code>initialCapacity</code>.
	 * Copies all content from <code>other</code> into the new collection. If
	 * <code>initialCapacity</code> is less than the size of <code>other</code>,
	 * <code>initialCapacity</code> is ignored and size is used instead.
	 * 
	 * @param other           collection whose elements are copied into this
	 *                        collection
	 * @param initialCapacity minimum initial capacity of this collection
	 * 
	 * @throws NullPointerException     if <code>other</code> is <code>null</code>,
	 *                                  or if any element in <code>other</code> is
	 *                                  <code>null</code>
	 * @throws IllegalArgumentException if <code>initialCapacity</code> is less than
	 *                                  <code>1</code>
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		this(validateConstructorInput(other, initialCapacity));
		addAll(other);
	}

	/**
	 * Helper method for validating input for constructor.
	 * 
	 * @param other           collection whose size is used in calculation
	 * @param initialCapacity initial capacity
	 * @return greater value of <code>other</code>'s size and
	 *         <code>initialCapacity</code>
	 * 
	 * @throws NullPointerException     if <code>other</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>initialCapacity</code> is less than
	 *                                  <code>1</code>
	 */
	private static int validateConstructorInput(Collection other, int initialCapacity) {
		Collection validatedCol = (Collection) Util.validateNotNull(other, "other");
		int collectionSize = validatedCol.size();
		int validatedCapacity = validateCapacity(initialCapacity);
		return Math.max(collectionSize, validatedCapacity);
	}

	/**
	 * Helper method for throwing <code>IllegalArgumentException</code> for invalid
	 * <code>initialCapacity</code>.
	 * 
	 * @param initialCapacity initial capacity to test for validity
	 * @return <code>initialCapacity</code> if valid
	 * 
	 * @throws IllegalArgumentException if <code>initialCapacity</code> is less than
	 *                                  <code>1</code>
	 */
	private static int validateCapacity(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException(
					"Initial capacity must be 1 or greater. It was " + initialCapacity + ".");
		}
		return initialCapacity;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc} Adding <code>null</code> is forbidden.
	 * <p>
	 * Adds an element in constant time (might need to reallocate).
	 * </p>
	 * 
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		Util.validateNotNull(value, "value");
		increaseCapacityIfNeeded();

		elements[size++] = value;
	}

	/**
	 * Inserts the given <code>value</code> at the given <code>position</code>.
	 * Inserting <code>null</code> is forbidden. Legal positions are from
	 * <code>0</code> to <code>size</code> (both included).
	 * <p>
	 * Inserts an element in linear time (might need to reallocate).
	 * </p>
	 * 
	 * @param value    object to insert; must not be <code>null</code>
	 * @param position position from <code>0</code> to <code>size</code> (both
	 *                 included)
	 * 
	 * @throws NullPointerException      if <code>value</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if <code>position</code> is less than
	 *                                   <code>0</code> or greater than
	 *                                   <code>size</code>
	 */
	public void insert(Object value, int position) {
		Util.validateNotNull(value, "value");
		Objects.checkIndex(position, size + 1);
		increaseCapacityIfNeeded();

		for (int i = size - 1; i >= position; i--) {
			elements[i + 1] = elements[i];
		}

		elements[position] = value;
		size++;
	}

	/**
	 * Increases capacity by factor {@value #RESIZE_FACTOR} if backing array is
	 * full.
	 */
	private void increaseCapacityIfNeeded() {
		if (size >= elements.length) {
			elements = Arrays.copyOf(elements, elements.length * RESIZE_FACTOR);
		}
	}

	/**
	 * Returns the object that is stored in the backing array at position index.
	 * Valid indices are <code>0</code> to <code>size-1</code>.
	 * <p>
	 * Returns wanted element in constant time.
	 * </p>
	 * 
	 * @param index index from range <code>[0, size-1]</code>
	 * @return object stored at position <code>index</code>
	 * 
	 * @throws IndexOutOfBoundsException if <code>index</code> is less than
	 *                                   <code>0</code> or greater than
	 *                                   <code>size-1</code>
	 */
	public Object get(int index) {
		return elements[Objects.checkIndex(index, size)];
	}

	/**
	 * Searches the collection and returns the index of the first occurrence of the
	 * given <code>value</code>, or <code>-1</code> if the <code>value</code> is not
	 * found. Equality is determined by {@link Object#equals(Object)}.
	 * <p>
	 * Returns wanted index in linear time.
	 * </p>
	 * 
	 * @param value value to search for; can be <code>null</code>
	 * @return index of the first occurrence of the given <code>value</code>; <br>
	 *         <code>-1</code> if the <code>value</code> is not found or it is
	 *         <code>null</code>
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		for (int i = 0; i < size; i++) {
			if (Objects.equals(elements[i], value)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) >= 0;
	}

	/**
	 * Removes element at specified <code>index</code> from collection. Element that
	 * was previously at location <code>index+1</code> will now be on location
	 * <code>index</code>, etc. Legal indices are <code>0</code> to
	 * <code>size-1</code>.
	 * <p>
	 * Removes object from wanted index in linear time.
	 * </p>
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
	public void remove(int index) {
		Objects.checkIndex(index, size);

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}

		elements[--size] = null;
	}

	/**
	 * Removes exactly one object equal to <code>value</code> from this collection,
	 * if such exists (determined by {@link Object#equals(Object)}). If there are
	 * multiple candidates for removal, the first one will be removed.
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);

		if (index < 0) {
			return false;
		}

		remove(index);
		return true;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOfRange(elements, 0, size);
	}

	/**
	 * Calls {@link Processor#process(Object)} for each element of this collection,
	 * in the order they are written in the backing array.
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}

	/**
	 * {@inheritDoc} The allocated array is left at current capacity.
	 */
	@Override
	public void clear() {
		Arrays.fill(elements, 0, size, null);
		size = 0;
	}

	@Override
	public String toString() {
		return "ArrayIndexedCollection [size=" + size + ", elements=" + Arrays.toString(elements) + ", size()=" + size()
				+ ", toArray()=" + Arrays.toString(toArray()) + ", isEmpty()=" + isEmpty() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(toArray());
		result = prime * result + Objects.hash(size);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArrayIndexedCollection)) {
			return false;
		}
		ArrayIndexedCollection other = (ArrayIndexedCollection) obj;
		return (size == other.size) && Arrays.deepEquals(this.toArray(), other.toArray());
	}

}
