package hr.fer.zemris.java.hw07.observer1;

/**
 * <i>Concrete Observer</i> which counts the number of times the value stored
 * has been changed since this observer’s registration. Number (counter) is
 * written to the standard output.
 * 
 * @author Luka Mesaric
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * Number of times the stored value has been changed since the time of this
	 * observer’s registration.
	 */
	private int counter = 0;

	/**
	 * {@inheritDoc}<br>
	 * Writes the number of times the stored value has been changed to the standard
	 * output.
	 * 
	 * @param istorage observed object, ignored
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		counter++;
		System.out.println("Number of value changes since tracking: " + counter);
	}

}
