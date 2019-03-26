package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.List;
import hr.fer.zemris.java.custom.collections.Util;

/**
 * Base class for <code>Node</code> hierarchy.
 * 
 * @author Luka Mesaric
 */
public class Node {

	/**
	 * List of all children of this node. All elements are instances of
	 * <code>Node</code>.
	 */
	private List children = null;

	/**
	 * Initial capacity for list <code>children</code>.
	 */
	private static final int CHILDREN_INITIAL_CAPACITY = 4;

	/**
	 * Adds <code>child</code> to children of this node.
	 * 
	 * @param child node to add to children of this node
	 * 
	 * @throws NullPointerException if <code>child</code> is <code>null</code>
	 */
	public void addChildNode(Node child) {
		Util.validateNotNull(child, "child");
		if (children == null) {
			children = new ArrayIndexedCollection(CHILDREN_INITIAL_CAPACITY);
		}
		children.add(child);
	}

	/**
	 * Returns number of children of this node.
	 * 
	 * @return number of children
	 */
	public int numberOfChildren() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}

	/**
	 * Returns child node stored at <code>index</code>.
	 * 
	 * @param index index of child
	 * @return child node
	 * 
	 * @throws IndexOutOfBoundsException if child at <code>index</code> does not
	 *                                   exist
	 */
	public Node getChild(int index) {
		if (children == null) {
			throw new IndexOutOfBoundsException(0);
		}
		// 'get' will throw wanted exception
		return (Node) children.get(index);
	}

	@Override
	public int hashCode() {
		return Objects.hash(children);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Node)) {
			return false;
		}
		Node other = (Node) obj;
		return Objects.equals(children, other.children);
	}

}
