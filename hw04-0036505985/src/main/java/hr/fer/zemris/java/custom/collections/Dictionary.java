package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Models a map as an adapter over {@link ArrayIndexedCollection}. <br>
 * Stores key-value pairs. Key must not be <code>null</code>, but value may be
 * <code>null</code>.
 * 
 * @param <K> type of key
 * @param <V> type of value
 * 
 * @author Luka Mesaric
 */
public class Dictionary<K, V> {

	private final List<Entry<K, V>> data;

	/**
	 * Default constructor.
	 */
	public Dictionary() {
		this.data = new ArrayIndexedCollection<>();
	}

	/**
	 * Checks if map is empty.
	 * 
	 * @return <code>true</code> if there are no stored pairs, <code>false</code>
	 *         otherwise
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * Returns size of this map.
	 * 
	 * @return number of stored key-value pairs
	 */
	public int size() {
		return data.size();
	}

	/**
	 * Removes all pairs from this map. Does not change its capacity.
	 */
	public void clear() {
		data.clear();
	}

	/**
	 * Associates <code>key</code> with <code>value</code>. If <code>key</code>
	 * already exists in map, its value is changed to <code>value</code>.
	 * 
	 * @param key   key; never <code>null</code>
	 * @param value value; can be <code>null</code>
	 * 
	 * @throws NullPointerException if <code>key</code> is <code>null</code>
	 */
	public void put(K key, V value) {
		Util.validateNotNull(key, "key");

		Entry<K, V> entry = getEntry(key);
		if (entry == null) {
			data.add(new Entry<>(key, value));
		} else {
			entry.value = value;
		}
	}

	/**
	 * Returns value associated with <code>key</code>. If <code>key</code> does not
	 * exist in map, <code>null</code> is returned.
	 * 
	 * @param key key
	 * @return value associated with <code>key</code>; <code>null</code> if
	 *         <code>key</code> does not exist
	 */
	public V get(Object key) {
		Entry<K, V> entry = getEntry(key);
		return entry != null ? entry.value : null;
	}

	/**
	 * Returns a key-value pair for given <code>key</code>, if such exists in map.
	 * 
	 * @param key key
	 * @return key-value pair; <code>null</code> if <code>key</code> is
	 *         <code>null</code> or pair with given <code>key</code> does not exist
	 */
	private Entry<K, V> getEntry(Object key) {
		if (key == null) {
			return null;
		}

		ElementsGetter<Entry<K, V>> getter = data.createElementsGetter();
		while (getter.hasNextElement()) {
			Entry<K, V> entry = getter.getNextElement();
			if (Objects.equals(key, entry.key)) {
				return entry;
			}
		}
		return null;
	}

	/**
	 * Key-value pair (entry) stored in map.
	 * 
	 * @param <K> type of key
	 * @param <V> type of value
	 * 
	 * @author Luka Mesaric
	 */
	private static class Entry<K, V> {

		/**
		 * Key of key-value pair, immutable.
		 */
		private final K key;

		/**
		 * Value of key-value pair, can be changed.
		 */
		private V value;

		/**
		 * Default constructor.
		 * 
		 * @param key   key; never <code>null</code>
		 * @param value value; can be <code>null</code>
		 * 
		 * @throws NullPointerException if <code>key</code> is <code>null</code>
		 */
		private Entry(K key, V value) {
			this.key = Util.validateNotNull(key, "key");
			this.value = value;
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
			if (!(obj instanceof Entry)) {
				return false;
			}
			Entry<?, ?> other = (Entry<?, ?>) obj;
			return Objects.equals(key, other.key) && Objects.equals(value, other.value);
		}

	}

}
