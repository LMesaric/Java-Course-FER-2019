package searching.slagalica;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

/**
 * Concrete strategies for solving 3x3 puzzles by moving pieces.
 * 
 * @author Luka Mesaric
 */
public class Slagalica implements
		Supplier<KonfiguracijaSlagalice>,
		Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>,
		Predicate<KonfiguracijaSlagalice> {

	/**
	 * Cost of each transition.
	 */
	private final static double COST = 1.0;

	/**
	 * Initial puzzle configuration, never <code>null</code>
	 */
	private final KonfiguracijaSlagalice initialConfiguration;

	/**
	 * Default constructor.
	 * 
	 * @param  initialConfiguration initial puzzle configuration
	 * @throws NullPointerException if <code>initialConfiguration</code> is
	 *                              <code>null</code>
	 */
	public Slagalica(KonfiguracijaSlagalice initialConfiguration) {
		this.initialConfiguration = Objects.requireNonNull(initialConfiguration);
	}

	/**
	 * Returns starting configuration.
	 * 
	 * @return starting configuration, never <code>null</code>
	 */
	@Override
	public KonfiguracijaSlagalice get() {
		return initialConfiguration;
	}

	/**
	 * Tests if given configuration represents the correct final configuration. The
	 * only acceptable state is <code>{1,2,3,4,5,6,7,8,0}</code>.
	 * 
	 * @param  configuration        the configuration to test
	 * @return                      <code>true</code> if the configuration
	 *                              represents the final configuration,
	 *                              <code>false</code> otherwise
	 * @throws NullPointerException if <code>configuration</code> is
	 *                              <code>null</code>
	 */
	@Override
	public boolean test(KonfiguracijaSlagalice configuration) {
		final int[] conf = Objects.requireNonNull(configuration).getPolje();
		final int last = conf.length - 1;
		for (int i = 0; i < last; i++) {
			if (conf[i] != i + 1) return false;
		}
		if (conf[last] != 0) return false;
		return true;
	}

	/**
	 * Returns a list of transitions to all possible direct successors of
	 * <code>configuration</code>.
	 * 
	 * @param  configuration        configuration whose successors are wanted
	 * @return                      list of transitions to all possible successors,
	 *                              never <code>null</code>
	 * @throws NullPointerException if <code>configuration</code> is
	 *                              <code>null</code>
	 */
	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(
			KonfiguracijaSlagalice configuration) {

		int space = Objects.requireNonNull(configuration).indexOfSpace();
		List<Transition<KonfiguracijaSlagalice>> list = new LinkedList<>();

		// move space right
		if (space % 3 != 2) addSucc(list, configuration, space, space + 1);
		// move space left
		if (space % 3 != 0) addSucc(list, configuration, space, space - 1);
		// move space up
		if (space >= 3) addSucc(list, configuration, space, space - 3);
		// move space down
		if (space <= 5) addSucc(list, configuration, space, space + 3);

		return list;
	}

	/**
	 * Swaps elements at indices <code>i1</code> and <code>i2</code> in copy of
	 * <code>configuration</code>'s array, and then uses that array to construct a
	 * new transition that will be added to <code>list</code>.
	 * 
	 * @param  list                 list to which new transition is appended
	 * @param  configuration        configuration
	 * @param  i1                   first index
	 * @param  i2                   second index
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	private static void addSucc(
			List<Transition<KonfiguracijaSlagalice>> list,
			KonfiguracijaSlagalice configuration,
			int i1, int i2) {

		int[] array = configuration.getPolje();
		inplaceSwap(array, i1, i2);
		list.add(new Transition<>(new KonfiguracijaSlagalice(array), COST));
	}

	/**
	 * Helper method for in-place swapping of elements at indices <code>i1</code>
	 * and <code>i2</code>.
	 * 
	 * @param  array                array whose elements are swapped
	 * @param  i1                   first index
	 * @param  i2                   second index
	 * @throws NullPointerException if <code>array</code> is <code>null</code>
	 */
	private static void inplaceSwap(int[] array, int i1, int i2) {
		int tmp = array[i1];
		array[i1] = array[i2];
		array[i2] = tmp;
	}

}
