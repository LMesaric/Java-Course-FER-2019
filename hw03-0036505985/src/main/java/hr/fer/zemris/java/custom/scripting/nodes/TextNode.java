package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * A node representing a piece of textual data.
 * 
 * @author Luka Mesaric
 */
public class TextNode extends Node {

	/**
	 * Textual data stored in this node.
	 */
	private final String text;

	/**
	 * Default constructor.
	 * 
	 * @param text textual data to be stored in this node
	 * 
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 */
	public TextNode(String text) {
		this.text = Util.validateNotNull(text, "text");
	}

	/**
	 * Getter for <code>text</code>.
	 *
	 * @return <code>text</code>
	 */
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(text);
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
		if (!(obj instanceof TextNode)) {
			return false;
		}
		TextNode other = (TextNode) obj;
		return Objects.equals(text, other.text);
	}

}
