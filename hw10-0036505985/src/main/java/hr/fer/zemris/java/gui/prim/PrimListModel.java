package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * <code>ListModel</code> for generating prime numbers.
 * 
 * @author Luka Mesaric
 */
public class PrimListModel implements ListModel<Integer> {

	/** Generated prime numbers. Never empty. */
	private List<Integer> primes = new ArrayList<>();
	/** Registered listeners. */
	private List<ListDataListener> listeners = new ArrayList<>();

	/** Default constructor. Initializes list of primes with <code>1</code>. */
	public PrimListModel() {
		primes.add(1);
	}

	@Override
	public int getSize() { return primes.size(); }

	@Override
	public Integer getElementAt(int index) {
		return primes.get(index);
	}

	/** @throws NullPointerException if <code>l</code> is <code>null</code> */
	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	/** @throws NullPointerException if <code>l</code> is <code>null</code> */
	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(Objects.requireNonNull(l));
	}

	/** Adds next prime to this model and notifies listeners. */
	public void next() {
		int position = getSize();
		primes.add(getNextPrime());

		ListDataEvent event = new ListDataEvent(
				this,
				ListDataEvent.INTERVAL_ADDED,
				position,
				position);
		listeners.forEach(l -> l.intervalAdded(event));
	}

	/**
	 * Finds first prime after the last stored in <code>primes</code>.
	 * 
	 * @return next prime number
	 */
	private int getNextPrime() {
		for (int n = 1 + primes.get(primes.size() - 1);; n++) {
			if (isPrime(n)) {
				return n;
			}
		}
	}

	/**
	 * Tests if given number is a prime. Uses <code>primes</code> list to speed up
	 * testing, so there are no guarantees this method will work properly if numbers
	 * are not tested sequentially.
	 * 
	 * @param  num number to test
	 * @return     <code>true</code> if <code>num</code> is prime,
	 *             <code>false</code> otherwise
	 */
	private boolean isPrime(int num) {
		int limit = (int) Math.sqrt(num) + 1;
		for (int prime : primes) {
			if (prime == 1) continue;
			if (prime > limit) break;
			if (num % prime == 0) return false;
		}
		return true;
	}
}
