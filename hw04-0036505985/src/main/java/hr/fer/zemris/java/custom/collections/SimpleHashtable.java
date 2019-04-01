package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Models a hash table capable of storing key-value pairs.<br>
 * Keys must not be <code>null</code>, but values may be <code>null</code>.
 * 
 * @param <K> the type of keys in this hash table
 * @param <V> the type of values in this hash table
 * 
 * @author Luka Mesaric
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * Array of references to first elements of linked lists constructed from
	 * <code>TableEntry</code> nodes. At all times, length of this array must be a
	 * power of 2.
	 */
	private TableEntry<K, V>[] table;

	/**
	 * Number of key-value pairs currently stored in this hash table.
	 */
	private int size = 0;

	/**
	 * Counter of modifications of data stored in this hash table. Must be
	 * incremented for each change to stored data.
	 */
	private long modificationCount = 0L;

	/**
	 * Initial capacity used when no other is provided. Value is {@value}.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Factor used to trigger resizing upon table overpopulation. Value is {@value}.
	 */
	private static final double DEFAULT_OVERPOPULATION_FACTOR = 0.75;

	/**
	 * Factor by which to increase hash table's size when it is getting
	 * overpopulated. Value is {@value}.
	 */
	private static final int RESIZE_MULTIPLIER = 2;

	/**
	 * Default constructor. Creates an empty hash table an sets its initial capacity
	 * to {@value #DEFAULT_CAPACITY}.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructor with initial capacity.<br>
	 * Real initial capacity is calculated as the first integer greater or equal to
	 * <code>capacity</code> that is a power of <code>2</code>.
	 * 
	 * @param capacity minimum initial capacity
	 * 
	 * @throws IllegalArgumentException if <code>capacity</code> is <code>0</code>
	 *                                  or negative, or next power of two is greater
	 *                                  than {@link Integer#MAX_VALUE}
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity must be 1 or greater.");
		}
		int realCapacity = nextPowerOfTwo(capacity);
		this.table = (TableEntry<K, V>[]) new TableEntry[realCapacity];
	}

	/**
	 * Calculates first integer greater or equal to <code>original</code> that is a
	 * power of <code>2</code>.<br>
	 * Method should be called only for values <code>1</code> and higher.
	 * 
	 * @param original starting point
	 * @return power of two closest to <code>original</code>, always <code>1</code>
	 *         or greater
	 * 
	 * @throws IllegalArgumentException if result overflowed, or
	 *                                  <code>original</code> is negative or
	 *                                  <code>0</code>
	 */
	private static int nextPowerOfTwo(int original) {
		if (original <= 0) {
			throw new IllegalArgumentException("Can only look for power of 2 for positive numbers.");
		}
		if (Integer.bitCount(original) == 1) {
			return original;
		}
		int result = Integer.highestOneBit(original) << 1;
		if (result <= 0) {
			// Overflow into negative values
			throw new IllegalArgumentException("Cannot represent power of 2 greater than " + original + ".");
		}
		return result;
	}

	/**
	 * Returns the number of key-value pairs currently stored in this hash table.
	 * 
	 * @return number of key-value pairs
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if hash table is empty.
	 * 
	 * @return <code>true</code> if hash table contains no key-value pairs,
	 *         <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Calculates index of the table slot in which <code>object</code> should go.
	 * 
	 * @param object object
	 * @return index of table slot
	 */
	private int tableSlotIndex(Object object) {
		// Taking absolute value of 'hash' might return a negative number in case of
		// Integer#MIN_VALUE, so absolute value of modulo is used instead. That way the
		// final result cannot overflow.
		int hash = Objects.hashCode(object);
		return Math.abs(hash % table.length);
	}

	/**
	 * Returns <code>TableEntry</code> for given <code>key</code>, if such exists in
	 * hash table.
	 * 
	 * @param key key
	 * @return <code>TableEntry</code> for <code>key</code>; <code>null</code> if
	 *         <code>key</code> is <code>null</code> or it does not exist
	 */
	private TableEntry<K, V> getEntry(Object key) {
		if (key == null) {
			return null;
		}
		int slot = tableSlotIndex(key);
		for (TableEntry<K, V> e = table[slot]; e != null; e = e.next) {
			if (key.equals(e.key)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Associates given <code>key</code> with given <code>value</code>.<br>
	 * If <code>key</code> already exists in this hash table, its value is changed
	 * to <code>value</code>. If <code>key</code> is not present, a new key-value
	 * pair is created.
	 * 
	 * @param key   key; never <code>null</code>
	 * @param value value; can be <code>null</code>
	 * 
	 * @throws NullPointerException if <code>key</code> is <code>null</code>
	 */
	public void put(K key, V value) {
		Util.validateNotNull(key, "key");
		resizeIfNeeded();

		int slot = tableSlotIndex(key);
		TableEntry<K, V> entry = table[slot];
		if (entry == null) {
			table[slot] = new TableEntry<>(key, value, null);
			size++;
			modificationCount++;
			return;
		}
		for (;; entry = entry.next) {
			if (key.equals(entry.key)) {
				entry.setValue(value);
				// do NOT change modificationCount
				return;
			} else if (entry.next == null) {
				break;
			}
		}
		entry.next = new TableEntry<>(key, value, null);
		size++;
		modificationCount++;
	}

	/**
	 * Returns value associated with <code>key</code>. If <code>key</code> does not
	 * exist in hash table (or it is <code>null</code>), <code>null</code> is
	 * returned.
	 * <p>
	 * If method returns <code>null</code>, it is not conclusive that
	 * <code>key</code> does not exist in hash table, as mapped value could have
	 * been <code>null</code> itself. See {@link #containsKey(Object)}.
	 * </p>
	 * 
	 * @param key key
	 * @return value associated with <code>key</code>; <code>null</code> if
	 *         <code>key</code> does not exist
	 */
	public V get(Object key) {
		TableEntry<K, V> entry = getEntry(key);
		return entry != null ? entry.value : null;
	}

	/**
	 * Checks if given <code>key</code> is contained inside this hash table.
	 * <code>null</code> is never contained.
	 * 
	 * @param key key to look for, can be <code>null</code>
	 * @return <code>true</code> if this hash table contains an entry for
	 *         <code>key</code>, <code>false</code> otherwise
	 */
	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}

	/**
	 * Checks if given <code>value</code> is contained inside this hash table.
	 * 
	 * @param value value to look for, can be <code>null</code>
	 * @return <code>true</code> if this hash table maps any key to
	 *         <code>value</code>, <code>false</code> otherwise
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> entry : this) {
			if (Objects.equals(entry.value, value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes key-value pair from this hash table.<br>
	 * If <code>key</code> does not exist (or it is <code>null</code>), nothing is
	 * changed.
	 * 
	 * @param key key whose mapping should be removed
	 */
	public void remove(Object key) {
		if (key == null) {
			return;
		}

		int slot = tableSlotIndex(key);
		TableEntry<K, V> entry = table[slot];
		if (entry == null) {
			return;
		} else if (entry.key.equals(key)) {
			table[slot] = entry.next;
			size--;
			modificationCount++;
			return;
		}
		for (; entry.next != null; entry = entry.next) {
			if (entry.next.key.equals(key)) {
				entry.next = entry.next.next;
				size--;
				modificationCount++;
				return;
			}
		}
	}

	/**
	 * Removes all key-value pairs from this hash table. Capacity is not changed.
	 */
	public void clear() {
		if (size == 0) {
			return;
		}
		Arrays.fill(table, null);
		size = 0;
		modificationCount++;
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Increases table capacity by factor {@value #RESIZE_MULTIPLIER} in case
	 * <code>size</code> is greater than <code>capacity</code> *
	 * {@value #DEFAULT_OVERPOPULATION_FACTOR}.
	 */
	@SuppressWarnings("unchecked")
	private void resizeIfNeeded() {
		if (size < table.length * DEFAULT_OVERPOPULATION_FACTOR) {
			return;
		}

		TableEntry<K, V>[] oldTable = table;
		int newCapacity = table.length * RESIZE_MULTIPLIER;
		table = (TableEntry<K, V>[]) new TableEntry[newCapacity];

		for (int i = 0, len = oldTable.length; i < len; i++) {
			TableEntry<K, V> entry = oldTable[i];
			if (entry == null) {
				continue;
			}
			oldTable[i] = null;

			while (entry != null) {
				TableEntry<K, V> next = entry.next;
				addEntryToList(entry, tableSlotIndex(entry.key));
				entry = next;
			}
		}
		modificationCount++;
	}

	/**
	 * Adds given <code>entry</code> to the beginning of linked list in table slot
	 * at position <code>index</code>. Entry's attribute <code>next</code> is likely
	 * to be changed.<br>
	 * This is a helper method for resizing the hash table and it does <b>not</b>
	 * increase <code>modificationCount</code> on its own.
	 * 
	 * @param entry entry to add; must not be <code>null</code>
	 * @param index index of table slot
	 * 
	 * @throws NullPointerException if <code>entry</code> is <code>null</code>
	 */
	private void addEntryToList(TableEntry<K, V> entry, int index) {
		Util.validateNotNull(entry, "entry");
		if (table[index] == null) {
			table[index] = entry;
			entry.next = null;
		} else {
			entry.next = table[index];
			table[index] = entry;
		}
	}

	/**
	 * Generates a list of key-value pairs in the following format:<br>
	 * <code>"[key1=value1, key2=value2, key3=value3]"</code><br>
	 * Pairs are ordered from first to last slot in table, and from first element of
	 * list to last one.
	 */
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
		for (TableEntry<K, V> entry : this) {
			joiner.add(entry.toString());
		}
		return joiner.toString();
	}

	/**
	 * Models a single key-value pair stored in hash table.<br>
	 * Key must not be <code>null</code>, but value may be <code>null</code>.
	 * 
	 * @param <K> type of key
	 * @param <V> type of value
	 * 
	 * @author Luka Mesaric
	 */
	public static class TableEntry<K, V> {

		/**
		 * Key of key-value pair, immutable.
		 */
		private final K key;

		/**
		 * Value of key-value pair, can be changed.
		 */
		private V value;

		/**
		 * Reference to next entry in the same table slot as this one.
		 */
		private TableEntry<K, V> next;

		/**
		 * Default constructor.
		 * 
		 * @param key   key; never <code>null</code>
		 * @param value value; can be <code>null</code>
		 * @param next  next entry in the same slot; can be <code>null</code>
		 * 
		 * @throws NullPointerException if <code>key</code> is <code>null</code>
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = Util.validateNotNull(key, "key");
			this.value = value;
			this.next = next;
		}

		/**
		 * Getter for <code>key</code>.
		 *
		 * @return <code>key</code>
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Getter for <code>value</code>.
		 *
		 * @return <code>value</code>
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Setter for <code>value</code>.
		 *
		 * @param value the <code>value</code> to set
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns a String formatted as <code>"key=value"</code>.
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(key, value);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof TableEntry)) {
				return false;
			}
			TableEntry<?, ?> other = (TableEntry<?, ?>) obj;
			return Objects.equals(key, other.key) && Objects.equals(value, other.value);
		}

	}

	/**
	 * Returns key-value pairs in order, from first table slot to last, and from
	 * first element in overflow list to last.
	 * 
	 * @author Luka Mesaric
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/**
		 * Index of slot of <code>nextEntry</code>.
		 */
		private int currentIndex = 0;

		/**
		 * Reference to current entry.
		 */
		private TableEntry<K, V> currentEntry = null;

		/**
		 * Reference to next entry.
		 */
		private TableEntry<K, V> nextEntry = null;

		/**
		 * Hash table's modification count at the time of creating this
		 * <code>Iterator</code>. Updated when {@link #remove()} is called.
		 */
		private long savedModificationCount;

		/**
		 * Default constructor.
		 */
		public IteratorImpl() {
			this.savedModificationCount = modificationCount;
			skipToNextFilledSlot();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException if content of hash table was changed
		 *                                         after the moment when this
		 *                                         <code>Iterator</code> was created
		 */
		@Override
		public boolean hasNext() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Hash table was changed since this iterator was created.");
			}
			return nextEntry != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws NoSuchElementException          {@inheritDoc}
		 * @throws ConcurrentModificationException if content of hash table was changed
		 *                                         after the moment when this
		 *                                         <code>Iterator</code> was created
		 */
		@Override
		public TableEntry<K, V> next() {
			if (!hasNext()) {
				throw new NoSuchElementException("All elements of this collection have been used.");
			}
			currentEntry = nextEntry;
			if (nextEntry.next != null) {
				nextEntry = nextEntry.next;
			} else {
				currentIndex++;
				skipToNextFilledSlot();
			}
			return currentEntry;
		}

		/**
		 * Changes <code>currentIndex</code> to point to slot with non-null content.
		 * Updates <code>nextEntry</code>.
		 */
		private void skipToNextFilledSlot() {
			while (currentIndex < table.length && table[currentIndex] == null) {
				currentIndex++;
			}
			if (currentIndex < table.length) {
				nextEntry = table[currentIndex];
			} else {
				nextEntry = null;
			}
		}

		/**
		 * Removes from the underlying collection the last element returned by this
		 * iterator. This method can be called only once per call to {@link #next()}.
		 * 
		 * @throws IllegalStateException           {@inheritDoc}
		 * @throws ConcurrentModificationException if content of hash table was changed
		 *                                         after the moment when this
		 *                                         <code>Iterator</code> was created
		 */
		@Override
		public void remove() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Hash table was changed since this iterator was created.");
			}
			if (currentEntry == null) {
				throw new IllegalStateException("There is no element to remove.");
			}
			SimpleHashtable.this.remove(currentEntry.key);
			currentEntry = null;
			savedModificationCount = modificationCount;
		}

	}

}
