package hr.fer.zemris.java.custom.collections;

import java.util.function.Predicate;

/**
 * Represents a boolean-valued function of one argument.
 * 
 * @param <T> the type of the input to tester
 * 
 * @author Luka Mesaric
 * 
 * @see Predicate
 */
@FunctionalInterface
public interface Tester<T> {

	/**
	 * Evaluates this test on given argument.
	 * 
	 * @param obj object to test
	 * @return <code>true</code> if the condition is met, <code>false</code>
	 *         otherwise
	 */
	boolean test(T obj);

}
