package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.ExceptionUtil;

/**
 * Stores a single integer value which is mutable. Supports registering and
 * notifying observers.<br>
 * <i>Subject</i> in the <i>Observer</i> design pattern.
 * 
 * @author Luka Mesaric
 * @see    IntegerStorageObserver
 */
public class IntegerStorage {

	/**
	 * Value stored by this object.
	 */
	private int value;

	/**
	 * List of all currently registered observers.
	 */
	// ArrayList is used. Reference may be null. Values are never null.
	private List<IntegerStorageObserver> observers;

	/**
	 * Default constructor.
	 * 
	 * @param initialValue initially stored value
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds <code>observer</code> to internal list of observers if it is not already
	 * present.
	 * 
	 * @param  observer             observer to register
	 * @throws NullPointerException if <code>observer</code> is <code>null</code>
	 */
	public void addObserver(IntegerStorageObserver observer) {
		ExceptionUtil.validateNotNull(observer, "observer");
		if (observers == null) {
			observers = new ArrayList<>();
		}
		if (!observers.contains(observer)) {
			copyObservers();
			observers.add(observer);
		}
	}

	/**
	 * Removes <code>observer</code> from internal list of observers if it is
	 * present.
	 * 
	 * @param observer observer to remove
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if (observers != null) {
			copyObservers();
			observers.remove(observer);
		}
	}

	/**
	 * Removes all currently registered observers.
	 */
	public void clearObservers() {
		if (observers != null) {
			copyObservers();
			observers.clear();
		}
	}

	/**
	 * Getter for <code>value</code>.
	 *
	 * @return <code>value</code>
	 */
	public int getValue() { return value; }

	/**
	 * Setter for <code>value</code>.<br>
	 * If new value is different from the old value, all currently registered
	 * observers are notified.
	 * 
	 * @param value new value
	 */
	public void setValue(int value) {
		if (this.value != value) {
			int oldValue = this.value;
			this.value = value;
			notifyObservers(new IntegerStorageChange(this, oldValue));
		}
	}

	/**
	 * Notifies all currently registered observers about last change.
	 * 
	 * @param change reference to a wrapper of integer storage
	 */
	private void notifyObservers(IntegerStorageChange change) {
		if (observers == null) {
			return;
		}
		// it is crucial that an iterator is used!
		for (IntegerStorageObserver observer : observers) {
			observer.valueChanged(change);
		}
	}

	/**
	 * Helper method for replacing list of observers with its copy. Used to prevent
	 * <code>ConcurrentModificationExceptions</code>.
	 */
	private void copyObservers() {
		if (observers != null) {
			this.observers = new ArrayList<>(observers);
		}
	}

}
