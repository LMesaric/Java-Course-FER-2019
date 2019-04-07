package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.collections.Util;

/**
 * Models turtle context in a stack-like manner.
 * 
 * @author Luka Mesaric
 */
public class Context {

	/**
	 * Stack used for storing states.
	 */
	private ObjectStack<TurtleState> stack = new ObjectStack<>();

	/**
	 * Returns current state without modifying the stack.
	 * 
	 * @return current state; never <code>null</code>
	 * 
	 * @throws EmptyStackException if there are no states in this context.
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}

	/**
	 * Adds <code>state</code> to the top of the stack.
	 * 
	 * @param state new state
	 * 
	 * @throws NullPointerException if <code>state</code> is <code>null</code>.
	 */
	public void pushState(TurtleState state) {
		stack.push(Util.validateNotNull(state, "state"));
	}

	/**
	 * Removes one state from the top of the stack.
	 * 
	 * @throws EmptyStackException if there are no states in this context.
	 */
	public void popState() {
		stack.pop();
	}

}
