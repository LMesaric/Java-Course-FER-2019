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
	 * @param  istorageChange       wrapper of observed object
	 * @throws NullPointerException if <code>istorageChange</code> is
	 *                              <code>null</code>
	 */
	public void valueChanged(IntegerStorageChange istorageChange);

}
