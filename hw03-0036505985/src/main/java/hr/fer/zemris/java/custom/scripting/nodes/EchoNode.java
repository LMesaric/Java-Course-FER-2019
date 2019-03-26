package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * A node representing a command which dynamically generates some textual
 * output.
 * 
 * @author Luka Mesaric
 */
public class EchoNode extends Node {

	/**
	 * Elements of this node.
	 */
	private final Element[] elements;

	/**
	 * Default constructor.
	 * 
	 * @param elements elements of this node
	 * 
	 * @throws NullPointerException if <code>elements</code> is <code>null</code>
	 */
	public EchoNode(Element[] elements) {
		this.elements = Util.validateNotNull(elements, "elements");
	}

	/**
	 * Getter for <code>elements</code>.
	 *
	 * @return <code>elements</code>
	 */
	public Element[] getElements() {
		return elements;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{$= ");
		for (Element element : elements) {
			builder.append(element.asText()).append(" ");
		}
		builder.append("$}");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof EchoNode)) {
			return false;
		}
		EchoNode other = (EchoNode) obj;
		return Arrays.equals(elements, other.elements);
	}

}
