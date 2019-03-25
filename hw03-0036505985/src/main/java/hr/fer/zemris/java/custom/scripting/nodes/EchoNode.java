package hr.fer.zemris.java.custom.scripting.nodes;

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

}
