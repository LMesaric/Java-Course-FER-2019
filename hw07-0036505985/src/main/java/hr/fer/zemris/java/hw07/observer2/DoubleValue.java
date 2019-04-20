package hr.fer.zemris.java.hw07.observer2;

import hr.fer.zemris.java.hw07.ExceptionUtil;

/**
 * <i>Concrete Observer</i> which calculates the doubled value of stored integer
 * (<code>value * 2</code>) and writes it to standard output. Doubled value is
 * written for <code>n</code> times (constructor argument), after which this
 * observer is automatically deregistered.
 * 
 * @author Luka Mesaric
 */
public class DoubleValue implements IntegerStorageObserver {

	/**
	 * Remaining number of times the stored value has to be changed before this
	 * observer stops observing by deregistering itself from the subject.
	 */
	private int counter;

	/**
	 * Default constructor.
	 * 
	 * @param  counter                  number of times this observer will be
	 *                                  triggered
	 * @throws IllegalArgumentException if <code>counter</code> less than
	 *                                  <code>1</code>
	 */
	public DoubleValue(int counter) {
		if (counter < 1) {
			throw new IllegalArgumentException("Counter must be 1 or greater.");
		}
		this.counter = counter;
	}

	/**
	 * {@inheritDoc}<br>
	 * Writes doubled value of stored integer to the standard output. Possibly
	 * deregisters itself.
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		ExceptionUtil.validateNotNull(istorageChange, "istorageChange");

		int doubled = 2 * istorageChange.getNewValue();
		System.out.println("Double value: " + doubled);

		counter--;
		if (counter <= 0) {
			istorageChange.getIstorage().removeObserver(this);
		}
	}

}
