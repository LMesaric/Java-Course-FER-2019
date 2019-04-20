package hr.fer.zemris.java.hw07.observer2;

import hr.fer.zemris.java.hw07.ExceptionUtil;

/**
 * Class which models a change of an <code>IntegerStorage</code> instance. It
 * holds a reference to the <code>IntegerStorage</code>, old value, and new
 * value.
 * 
 * @author Luka Mesaric
 */
public class IntegerStorageChange {

	/**
	 * Reference to the IntegerStorage. Never <code>null</code>.
	 */
	private final IntegerStorage istorage;

	/**
	 * The value of stored integer before the change has occurred.
	 */
	private final int oldValue;

	/**
	 * The new value of currently stored integer
	 */
	private final int newValue;

	/**
	 * Default constructor.
	 * 
	 * @param  istorage             observed object, source of state
	 * @param  oldValue             value before the change
	 * @throws NullPointerException if <code>istorage</code> is <code>null</code>
	 */
	public IntegerStorageChange(IntegerStorage istorage, int oldValue) {
		this.istorage = ExceptionUtil.validateNotNull(istorage, "istorage");
		this.oldValue = oldValue;
		this.newValue = istorage.getValue();
	}

	/**
	 * Getter for <code>istorage</code>.
	 *
	 * @return <code>istorage</code>, never <code>null</code>
	 */
	public IntegerStorage getIstorage() { return istorage; }

	/**
	 * Getter for <code>oldValue</code>.
	 *
	 * @return <code>oldValue</code>
	 */
	public int getOldValue() { return oldValue; }

	/**
	 * Getter for <code>newValue</code>.
	 *
	 * @return <code>newValue</code>
	 */
	public int getNewValue() { return newValue; }

}
