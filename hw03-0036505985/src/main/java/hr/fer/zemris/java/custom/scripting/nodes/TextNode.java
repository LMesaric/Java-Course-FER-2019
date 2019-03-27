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

	/**
	 * Escapes any backslashes and open curly braces located directly before a
	 * dollar sign. <code>\n</code>, <code>\r</code> and <code>\t</code> are not
	 * escaped because it cannot be determined whether they were originally escaped
	 * characters or direct ASCII values.
	 */
	@Override
	public String toString() {
		// I am deeply sorry, but it was fun to play with.
		// "\\\\" in plain text is "\\" which regex treats as if it were "\"
		// "\\\\\\\\" in plain text is "\\\\" which regex treats as if it were "\\"
		// "\\{\\$" in plain text is "\{\$" which regex treats as if it were "{$"
		// "\\\\{\\$" in plain text is "\\{\$" which regex treats as if it were "\{$"
		return text.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\{\\$", "\\\\{\\$");
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
