package searching.slagalica;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the state of a puzzle.
 * 
 * @author Luka Mesaric
 */
public class KonfiguracijaSlagalice {

	/**
	 * Configuration array of length 9, storing every integer from 0 to 8. Never
	 * <code>null</code>.
	 */
	private final int[] configuration;

	/**
	 * Length of configuration array.
	 */
	private static final int CONFIGURATION_LENGTH = 9;

	/**
	 * Default constructor.
	 * 
	 * @param  configuration            configuration of this puzzle
	 * @throws NullPointerException     if <code>configuration</code> is
	 *                                  <code>null</code>
	 * @throws IllegalArgumentException if <code>configuration</code> length is not
	 *                                  9, or <code>0</code> is not found in
	 *                                  <code>configuration</code>
	 */
	public KonfiguracijaSlagalice(int[] configuration) {
		Objects.requireNonNull(configuration);
		if (configuration.length != CONFIGURATION_LENGTH) {
			throw new IllegalArgumentException(
					"Configuration must have length " + CONFIGURATION_LENGTH);
		}
		this.configuration = Arrays.copyOf(configuration, configuration.length);
		if (indexOfSpace() < 0) {
			throw new IllegalArgumentException("Missing 0 in configuration!");
		}
	}

	/**
	 * Getter for <code>configuration</code>, an array of length 9.
	 *
	 * @return a copy of <code>configuration</code>, never <code>null</code>
	 */
	public int[] getPolje() {
		return Arrays.copyOf(configuration, configuration.length);
	}

	/**
	 * Returns the index of element <code>0</code> in internal array.
	 * 
	 * @return index of element <code>0</code>, always between 0 and 8
	 */
	public int indexOfSpace() {
		for (int i = 0, max = configuration.length; i < max; i++) {
			if (configuration[i] == 0) {
				return i;
			}
		}
		return -1;	// This can only be returned during checks in the constructor.
	}

	/**
	 * Return string representation of wanted position.
	 * 
	 * @param  index index
	 * @return       <code>*</code> if <code>0</code> is at <code>index</code>,
	 *               stored number otherwise
	 */
	private String getValue(int index) {
		int n = configuration[index];
		return n == 0 ? "*" : Integer.toString(n);
	}

	@Override
	public String toString() {
		// Since there are not rules on how the output should look if
		// the puzzle was larger, using for loops would just overcomplicate the code.
		return String.format("%s %s %s%n%s %s %s%n%s %s %s",
				getValue(0), getValue(1), getValue(2),
				getValue(3), getValue(4), getValue(5),
				getValue(6), getValue(7), getValue(8));
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(configuration);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof KonfiguracijaSlagalice)) {
			return false;
		}
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(configuration, other.configuration);
	}

}
