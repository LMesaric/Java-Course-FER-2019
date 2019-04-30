package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class for searching algorithms.
 * 
 * @author Luka Mesaric
 */
public class SearchUtil {

	/**
	 * Implementation of Breadth-First Search (BFS).
	 * 
	 * @param  s0                   supplier of starting state
	 * @param  succ                 maps state to its direct successors
	 * @param  goal                 test if given state is acceptable
	 * @param                       <S> type of states used in BFS
	 * @return                      final node in search tree
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public static <S> Node<S> bfs(
			Supplier<S> s0,
			Function<S, List<Transition<S>>> succ,
			Predicate<S> goal) {

		Objects.requireNonNull(s0);
		Objects.requireNonNull(succ);
		Objects.requireNonNull(goal);

		List<Node<S>> toSearch = new LinkedList<>();
		toSearch.add(new Node<>(null, s0.get(), 0));

		while (!toSearch.isEmpty()) {
			Node<S> current = toSearch.remove(0);	// remove from the beginning
			S currentState = current.getState();
			if (goal.test(currentState)) {
				return current;
			}
			List<Node<S>> children = succ.apply(currentState)
					.stream()
					.map(t -> new Node<S>(current, t.getState(),
							current.getCost() + t.getCost()))
					.collect(Collectors.toList());
			toSearch.addAll(children);				// add children to the end
		}

		return null;
	}

	/**
	 * Implementation of optimized Breadth-First Search (BFS) which stores visited
	 * states.
	 * 
	 * @param  s0                   supplier of starting state
	 * @param  succ                 maps state to its direct successors
	 * @param  goal                 test if given state is acceptable
	 * @param                       <S> type of states used in BFS
	 * @return                      final node in search tree
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public static <S> Node<S> bfsv(
			Supplier<S> s0,
			Function<S, List<Transition<S>>> succ,
			Predicate<S> goal) {

		Objects.requireNonNull(s0);
		Objects.requireNonNull(succ);
		Objects.requireNonNull(goal);

		List<Node<S>> toSearch = new LinkedList<>();
		Set<S> visited = new HashSet<>();
		S starting = s0.get();
		toSearch.add(new Node<>(null, starting, 0));
		visited.add(starting);

		while (!toSearch.isEmpty()) {
			Node<S> current = toSearch.remove(0);	// remove from the beginning
			S currentState = current.getState();
			if (goal.test(currentState)) {
				return current;
			}
			List<Node<S>> children = succ.apply(currentState)
					.stream()
					.map(t -> new Node<S>(current, t.getState(),
							current.getCost() + t.getCost()))
					.collect(Collectors.toList());

			children.removeIf(node -> visited.contains(node.getState()));
			children.forEach(node -> visited.add(node.getState()));
			toSearch.addAll(children);				// add children to the end
		}

		return null;
	}

	/** Disable creating instances. */
	private SearchUtil() {}

}
