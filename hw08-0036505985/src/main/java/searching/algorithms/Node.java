package searching.algorithms;

import java.util.Objects;

/**
 * Node of search tree. Stores its state, parent and total cost.
 * 
 * @param  <S> type of <code>state</code> stored in this node
 * @author Luka Mesaric
 */
public class Node<S> {

	/**
	 * Reference to current state, never <code>null</code>.
	 */
	private final S state;

	/**
	 * Reference to parent node, never <code>null</code>.
	 */
	private final Node<S> parent;

	/**
	 * Total cost to get to this <code>state</code>.
	 */
	private final double totalCost;

	/**
	 * Default constructor.
	 * 
	 * @param  parent               reference to parent node, <code>null</code> if
	 *                              parent does not exist
	 * @param  state                reference to current state
	 * @param  totalCost            total cost to get to this state
	 * @throws NullPointerException if <code>state</code> is <code>null</code>
	 */
	public Node(Node<S> parent, S state, double totalCost) {
		this.parent = parent;
		this.state = Objects.requireNonNull(state);
		this.totalCost = totalCost;
	}

	/**
	 * Getter for <code>state</code>.
	 *
	 * @return <code>state</code>, never <code>null</code>
	 */
	public S getState() { return state; }

	/**
	 * Getter for <code>parent</code>.
	 *
	 * @return <code>parent</code>, never <code>null</code>
	 */
	public Node<S> getParent() { return parent; }

	/**
	 * Getter for <code>totalCost</code>.
	 *
	 * @return <code>totalCost</code>
	 */
	public double getCost() { return totalCost; }

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
		if (!(obj instanceof Node)) {
			return false;
		}
		Node<?> other = (Node<?>) obj;
		return Objects.equals(state, other.state);
	}

}
