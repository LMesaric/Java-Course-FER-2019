package hr.fer.zemris.java.hw07.observer1;

import hr.fer.zemris.java.hw07.ExceptionUtil;

/**
 * <i>Concrete Observer</i> which writes a square of the integer stored in the
 * <code>IntegerStorage</code> to the standard output, without modifying the
 * stored integer.
 * 
 * @author Luka Mesaric
 */
public class SquareValue implements IntegerStorageObserver {

	/**
	 * {@inheritDoc}<br>
	 * Writes a square of the stored integer to the standard output.
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		ExceptionUtil.validateNotNull(istorage, "istorage");
		int newValue = istorage.getValue();
		int square = newValue * newValue;
		System.out.format("Provided new value: %d, square is %d%n",
				newValue, square);
	}

}
