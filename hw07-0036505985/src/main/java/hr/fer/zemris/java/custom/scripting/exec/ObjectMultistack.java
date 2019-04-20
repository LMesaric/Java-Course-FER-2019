package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.java.hw07.ExceptionUtil;

/**
 * Stores multiple values for same key. Values are stored in a stack-like
 * manner. Keys are Strings and must not be <code>null</code>. Stored values are
 * instances of {@link ValueWrapper} and are also never null (although wrapped
 * value may be <code>null</code>). All operations are performed in
 * <code>O(1)</code>.
 * 
 * @author Luka Mesaric
 */
public class ObjectMultistack {

	/**
	 * Maps names of stacks to head nodes of linked lists that simulate stacks.
	 */
	private final Map<String, MultistackEntry> map = new HashMap<>();

	/**
	 * Pushes the given <code>valueWrapper</code> on top of the stack mapped to
	 * <code>keyName</code>. Pushing <code>null</code> is forbidden. Stack name
	 * cannot be <code>null</code>.<br>
	 * Pushes value in <code>O(1)</code>.
	 * 
	 * @param  keyName              name of stack
	 * @param  valueWrapper         value to push to stack
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		ExceptionUtil.validateNotNull(keyName, "keyName");
		ExceptionUtil.validateNotNull(valueWrapper, "valueWrapper");

		MultistackEntry oldHead = map.get(keyName);	// can be null
		MultistackEntry newHead = new MultistackEntry(valueWrapper, oldHead);
		map.put(keyName, newHead);
	}

	/**
	 * Returns the last value pushed on stack from stack mapped to
	 * <code>keyName</code> and removes it from stack. If the stack is empty when
	 * method <code>pop</code> is called, {@link EmptyStackException} is thrown.<br>
	 * Returns and removes value in <code>O(1)</code>.
	 * 
	 * @param  keyName             name of stack
	 * @return                     last object pushed on stack mapped to
	 *                             <code>keyName</code>, never <code>null</code>
	 * @throws EmptyStackException if stack is empty
	 */
	public ValueWrapper pop(String keyName) {
		// If keyName is null peek will throw an exception because 'null'
		// cannot be a key of any stack, and thus the stack is empty.
		ValueWrapper value = peek(keyName);
		// At this point, mapped stack cannot be null.
		// If head.next is null, key will be removed from the map.
		// Removing the key is not necessary, but it keeps the map nice and clean.
		map.compute(keyName, (key, head) -> head.next);
		return value;
	}

	/**
	 * Returns the last value pushed on stack mapped to <code>keyName</code>, but
	 * does not remove it. If the stack is empty when method <code>peek</code> is
	 * called, {@link EmptyStackException} is thrown. <br>
	 * Returns value in <code>O(1)</code>.
	 * 
	 * @param  keyName             name of stack
	 * @return                     last object pushed on stack mapped to
	 *                             <code>keyName</code>, never <code>null</code>
	 * @throws EmptyStackException if stack is empty
	 */
	public ValueWrapper peek(String keyName) {
		MultistackEntry head = map.get(keyName);
		if (head == null) {
			throw new EmptyStackException(
					"Cannot return value from empty stack. Key: " + keyName);
		}
		return head.value;
	}

	/**
	 * Checks if stack mapped to <code>keyName</code> is empty.
	 * 
	 * @param  keyName name of stack
	 * @return         <code>true</code> if stack contains no objects,
	 *                 <code>false</code> otherwise
	 */
	public boolean isEmpty(String keyName) {
		return map.get(keyName) == null;
	}

	/**
	 * Models a node of a single-linked list used to simulate a stack in
	 * {@link ObjectMultistack}.
	 * 
	 * @author Luka Mesaric
	 */
	private static class MultistackEntry {

		/**
		 * Value stored in this node. Never <code>null</code>.
		 */
		private final ValueWrapper value;

		/**
		 * Reference to next node in stack. Can be <code>null</code>.
		 */
		private final MultistackEntry next;

		/**
		 * Default constructor.
		 * 
		 * @param  value                stored value, never <code>null</code>
		 * @param  next                 next node, may be <code>null</code>
		 * @throws NullPointerException if <code>value</code> is <code>null</code>
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = ExceptionUtil.validateNotNull(value, "value");
			this.next = next;
		}

	}

}
