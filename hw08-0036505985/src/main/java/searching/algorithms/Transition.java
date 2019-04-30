package searching.algorithms;

import java.util.Objects;

/**
 * Models an ordered pair <code>(state, cost)</code>.
 * 
 * @param  <S> type of <code>state</code>
 * @author Luka Mesaric
 */
public class Transition<S> {

	/**
	 * Reference to current state, never <code>null</code>.
	 */
	private final S state;

	/**
	 * Cost of this transition.
	 */
	private final double cost;

	/**
	 * Default constructor.
	 * 
	 * @param  state                reference to current state
	 * @param  cost                 cost of this transition
	 * @throws NullPointerException if <code>state</code> is <code>null</code>
	 */
	public Transition(S state, double cost) {
		this.state = Objects.requireNonNull(state);
		this.cost = cost;
	}

	/**
	 * Getter for <code>state</code>.
	 *
	 * @return <code>state</code>, never <code>null</code>
	 */
	public S getState() { return state; }

	/**
	 * Getter for <code>cost</code>.
	 *
	 * @return <code>cost</code>
	 */
	public double getCost() { return cost; }

	@Override
	public int hashCode() {
		return Objects.hash(state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Transition)) {
			return false;
		}
		Transition<?> other = (Transition<?>) obj;
		return Objects.equals(state, other.state);
	}

}
