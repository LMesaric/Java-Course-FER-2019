package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class for searching algorithms.
 * 
 * @author Luka Mesaric
 */
public class SubspaceExploreUtil {

	/**
	 * Implementation of Breadth-First Search (BFS).
	 * 
	 * @param  s0                   supplier of starting state
	 * @param  process              performs wanted task using given state
	 * @param  succ                 maps state to its direct successors
	 * @param  acceptable           test if given state belongs to wanted subspace
	 * @param                       <S> type of states used in BFS
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public static <S> void bfs(
			Supplier<S> s0,
			Consumer<S> process,
			Function<S, List<S>> succ,
			Predicate<S> acceptable) {

		// add successors to the end
		performSearch(s0, process, succ, acceptable,
				(toSearch, successors) -> toSearch.addAll(successors));
	}

	/**
	 * Implementation of Depth-First Search (DFS).
	 * 
	 * @param  s0                   supplier of starting state
	 * @param  process              performs wanted task using given state
	 * @param  succ                 maps state to its direct successors
	 * @param  acceptable           test if given state belongs to wanted subspace
	 * @param                       <S> type of states used in DFS
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public static <S> void dfs(
			Supplier<S> s0,
			Consumer<S> process,
			Function<S, List<S>> succ,
			Predicate<S> acceptable) {

		// add successors to the beginning
		performSearch(s0, process, succ, acceptable,
				(toSearch, successors) -> toSearch.addAll(0, successors));
	}

	/**
	 * Performs searching without storing visited states. Determining the way
	 * successors are added to the list of states that need to searched is left to a
	 * concrete strategy.
	 * 
	 * @param  s0                   supplier of starting state
	 * @param  process              performs wanted task using given state
	 * @param  succ                 maps state to its direct successors
	 * @param  acceptable           test if given state belongs to wanted subspace
	 * @param  addingSuccs          strategy for adding successors to the list of
	 *                              states to search
	 * @param                       <S> type of states used in this search
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         #dfs(Supplier, Consumer, Function, Predicate)
	 * @see                         #bfs(Supplier, Consumer, Function, Predicate)
	 */
	private static <S> void performSearch(
			Supplier<S> s0,
			Consumer<S> process,
			Function<S, List<S>> succ,
			Predicate<S> acceptable,
			BiConsumer<List<S>, List<S>> addingSuccs) {

		Objects.requireNonNull(s0);
		Objects.requireNonNull(process);
		Objects.requireNonNull(succ);
		Objects.requireNonNull(acceptable);
		Objects.requireNonNull(addingSuccs);

		List<S> toSearch = new LinkedList<>();
		toSearch.add(s0.get());

		while (!toSearch.isEmpty()) {
			S current = toSearch.remove(0);	// remove from the beginning
			if (!acceptable.test(current))
				continue;
			process.accept(current);
			addingSuccs.accept(toSearch, succ.apply(current));
		}
	}

	/**
	 * Implementation of optimized Breadth-First Search (BFS) which stores visited
	 * states.
	 * 
	 * @param  s0                   supplier of starting state
	 * @param  process              performs wanted task using given state
	 * @param  succ                 maps state to its direct successors
	 * @param  acceptable           test if given state belongs to wanted subspace
	 * @param                       <S> type of states used in BFS
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public static <S> void bfsv(
			Supplier<S> s0,
			Consumer<S> process,
			Function<S, List<S>> succ,
			Predicate<S> acceptable) {

		Objects.requireNonNull(s0);
		Objects.requireNonNull(process);
		Objects.requireNonNull(succ);
		Objects.requireNonNull(acceptable);

		List<S> toSearch = new LinkedList<>();
		Set<S> visited = new HashSet<>();
		S starting = s0.get();
		toSearch.add(starting);
		visited.add(starting);

		while (!toSearch.isEmpty()) {
			S current = toSearch.remove(0);	// remove from the beginning
			if (!acceptable.test(current))
				continue;
			process.accept(current);
			List<S> children = succ.apply(current);
			children.removeAll(visited);
			toSearch.addAll(children); 		// add children to the end
			visited.addAll(children);
		}
	}

	/** Disable creating instances. */
	private SubspaceExploreUtil() {}

}
