package hr.fer.zemris.java.hw07.observer2;

/**
 * <i>Abstract Observer</i> for class <code>IntegerStorage</code>.
 * 
 * @author Luka Mesaric
 */
@FunctionalInterface
public interface IntegerStorageObserver {

	/**
	 * Method called when state of observed object has changed.
	 * 
	 * @param  istorage             observed object
	 * @throws NullPointerException if <code>istorage</code> is <code>null</code>
	 */
	public void valueChanged(IntegerStorage istorage);

}
