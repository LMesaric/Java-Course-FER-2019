package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node representing an entire document.
 * 
 * @author Luka Mesaric
 */
public class DocumentNode extends Node {

	@Override
	public String toString() {
		StringBuilder children = new StringBuilder();
		for (int i = 0, size = numberOfChildren(); i < size; i++) {
			Node child = getChild(i);
			children.append(child.toString());
		}

		return children.toString();
	}
}
