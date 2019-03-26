package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Element that represents a variable.
 * 
 * @author Luka Mesaric
 */
public class ElementVariable extends Element {

	/**
	 * Name of represented variable.
	 */
	private final String name;

	/**
	 * Default constructor.
	 * 
	 * @param name name of represented variable.
	 * 
	 * @throws NullPointerException if <code>name</code> is <code>null</code>
	 */
	public ElementVariable(String name) {
		this.name = Util.validateNotNull(name, "name");
	}

	@Override
	public String asText() {
		return name;
	}

	/**
	 * Getter for <code>name</code>.
	 *
	 * @return <code>name</code>
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementVariable)) {
			return false;
		}
		ElementVariable other = (ElementVariable) obj;
		return Objects.equals(name, other.name);
	}

}
