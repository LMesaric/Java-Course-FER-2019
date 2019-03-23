package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * Models a linked list-backed collection of objects.
 * <p>
 * Duplicate elements are allowed. Storage of <code>null</code> references is
 * not allowed.
 * </p>
 * 
 * @author Luka Mesaric
 * 
 * @see Collection
 */
public class LinkedListIndexedCollection implements Collection {

	/**
	 * Current size of this collection - number of elements actually stored; number
	 * of nodes in list.
	 */
	private int size = 0;

	/**
	 * Reference to the first node of this linked list.
	 */
	private ListNode first = null;

	/**
	 * Reference to the last node of this linked list.
	 */
	private ListNode last = null;

	/**
	 * Models a single node used for storing data in
	 * {@link LinkedListIndexedCollection}.
	 * 
	 * @author Luka Mesaric
	 */
	private static class ListNode {

		/**
		 * Reference to previous node in list.
		 */
		private ListNode previous;

		/**
		 * Reference to next node in list.
		 */
		private ListNode next;

		/**
		 * Value stored in this node.
		 */
		private Object value;

		/**
		 * Default constructor.
		 * 
		 * @param previous previous node
		 * @param next     next node
		 * @param value    value stored in node
		 */
		private ListNode(ListNode previous, ListNode next, Object value) {
			this.previous = previous;
			this.next = next;
			this.value = value;
		}

		/**
		 * Simple constructor, sets <code>previous</code> and <code>next</code> to
		 * <code>null</code>.
		 * 
		 * @param value value stored in node
		 */
		private ListNode(Object value) {
			this(null, null, value);
		}

	}

	/**
	 * Data class for storing <code>ListNode - index</code> pairs.
	 * 
	 * @author Luka Mesaric
	 */
	private static class IndexNodePair {

		/**
		 * Node from <code>ListNode - index</code> pair.
		 */
		private ListNode node;

		/**
		 * Node index from <code>ListNode - index</code> pair.
		 */
		private int nodeIndex;

		/**
		 * Default constructor.
		 * 
		 * @param node      node
		 * @param nodeIndex node index
		 */
		private IndexNodePair(ListNode node, int nodeIndex) {
			this.node = node;
			this.nodeIndex = nodeIndex;
		}
	}

	/**
	 * Default constructor. Creates an empty collection.
	 */
	public LinkedListIndexedCollection() {
	}

	/**
	 * Creates a new collection the size of <code>other</code> and copies its
	 * content.
	 * 
	 * @param other collection whose elements are copied into this collection
	 * 
	 * @throws NullPointerException if <code>other</code> is <code>null</code>, or
	 *                              if any element in <code>other</code> is
	 *                              <code>null</code>
	 */
	public LinkedListIndexedCollection(Collection other) {
		addAll(other);
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc} Adding <code>null</code> is forbidden.
	 * <p>
	 * Adds an element in exactly constant time - O(1).
	 * </p>
	 * 
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		Util.validateNotNull(value, "value");

		ListNode newNode = new ListNode(value);

		if (first == null) {
			first = last = newNode;
		} else {
			last.next = newNode;
			newNode.previous = last;
			last = newNode;
		}
		size++;
	}

	/**
	 * Inserts the given <code>value</code> at the given <code>position</code>.
	 * Inserting <code>null</code> is forbidden. Legal positions are from
	 * <code>0</code> to <code>size</code> (both included).
	 * <p>
	 * Inserts an element in linear time.
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

		ListNode newNode = new ListNode(value);

		if (first == null) {
			first = last = newNode;
		} else if (position == 0) {
			newNode.next = first;
			first.previous = newNode;
			first = newNode;
		} else if (position == size) {
			newNode.previous = last;
			last.next = newNode;
			last = newNode;
		} else {
			ListNode current = getNode(position).node;

			newNode.previous = current.previous;
			newNode.next = current;

			current.previous.next = newNode;
			current.previous = newNode;
		}

		size++;
	}

	/**
	 * Returns the object that is stored in the linked list at position index. Valid
	 * indices are <code>0</code> to <code>size-1</code>.
	 * <p>
	 * Returns wanted element in linear time - O(n/2).
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
		return getNode(index).node.value;
	}

	/**
	 * Finds and returns <code>IndexNodePair</code> of node at specified
	 * <code>index</code>. Does that in O(n/2).
	 * 
	 * @param index index of wanted node
	 * @return <code>IndexNodePair</code> of node at specified <code>index</code>
	 * 
	 * @throws IndexOutOfBoundsException if <code>index</code> is less than
	 *                                   <code>0</code> or greater than
	 *                                   <code>size-1</code>
	 */
	private IndexNodePair getNode(int index) {
		Objects.checkIndex(index, size);

		ListNode current;
		if (index <= size / 2) {
			current = first;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
		} else {
			current = last;
			for (int i = size - 1; i > index; i--) {
				current = current.previous;
			}
		}
		return new IndexNodePair(current, index);
	}

	/**
	 * Searches the collection and returns the <code>IndexNodePair</code> of node
	 * that contained the first occurrence of the given <code>value</code>, or
	 * <code>null</code> if the <code>value</code> is not found. Equality is
	 * determined by {@link Object#equals(Object)}.
	 * <p>
	 * Returns wanted <code>IndexNodePair</code> in linear time.
	 * </p>
	 * 
	 * @param value value to search for; can be <code>null</code>
	 * @return <code>IndexNodePair</code> of node that contained the first
	 *         occurrence of the given <code>value</code>; <br>
	 *         <code>null</code> if the <code>value</code> was not found or
	 *         <code>value</code> was <code>null</code>
	 */
	private IndexNodePair getNode(Object value) {
		if (value == null) {
			return null;
		}

		ListNode current = first;
		for (int i = 0; current != null; i++) {
			if (Objects.equals(current.value, value)) {
				return new IndexNodePair(current, i);
			}
			current = current.next;
		}
		return null;
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
		IndexNodePair pair = getNode(value);
		return pair != null ? pair.nodeIndex : -1;
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

		if (size == 1) {
			first = last = null;
		} else if (index == 0) {
			first.value = null;
			first = first.next;
			first.previous = null;
		} else if (index == size - 1) {
			last.value = null;
			last = last.previous;
			last.next = null;
		} else {
			ListNode current = getNode(index).node;
			current.previous.next = current.next;
			current.next.previous = current.previous;

			current.value = null;
			current.previous = null;
			current.next = null;
		}

		size--;
	}

	/**
	 * Removes exactly one object equal to <code>value</code> from this collection,
	 * if such exists (determined by {@link Object#equals(Object)}). If there are
	 * multiple candidates for removal, the first one will be removed.
	 */
	@Override
	public boolean remove(Object value) {
		IndexNodePair pair = getNode(value);

		if (pair == null) {
			return false;
		}

		remove(pair.nodeIndex);
		return true;
	}

	@Override
	public Object[] toArray() {

		var toArrayProcessor = new Processor() {
			private Object[] elements = new Object[size];
			private int index = 0;

			/**
			 * Adds <code>value</code> to backing array.
			 */
			@Override
			public void process(Object value) {
				elements[index++] = value;
			}
		};

		forEach(toArrayProcessor);
		return toArrayProcessor.elements;
	}

	/**
	 * Calls {@link Processor#process(Object)} for each element of this collection,
	 * in the order they are linked in the linked list, from <code>first</code> to
	 * <code>last</code>.
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void forEach(Processor processor) {
		Util.validateNotNull(processor, "processor");

		ListNode current = first;
		while (current != null) {
			processor.process(current.value);
			current = current.next;
		}
	}

	@Override
	public void clear() {
		ListNode current = first;

		while (current != null) {
			ListNode next = current.next;
			current.value = null;
			current.previous = null;
			current.next = null;
			current = next;
		}

		first = last = null;
		size = 0;
	}

	@Override
	public String toString() {
		return "LinkedListIndexedCollection [size=" + size + ", first=" + first + ", last=" + last + ", size()="
				+ size() + ", toArray()=" + Arrays.toString(toArray()) + ", isEmpty()=" + isEmpty() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		ListNode current = first;
		while (current != null) {
			result = prime * result + Objects.hash(current.value);
			current = current.next;
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LinkedListIndexedCollection)) {
			return false;
		}
		LinkedListIndexedCollection other = (LinkedListIndexedCollection) obj;
		return (size == other.size) && Arrays.deepEquals(this.toArray(), other.toArray());
	}

}
