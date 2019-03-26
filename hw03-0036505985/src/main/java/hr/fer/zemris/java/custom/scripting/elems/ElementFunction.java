package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Element that represents a function.
 * 
 * @author Luka Mesaric
 */
public class ElementFunction extends Element {

	/**
	 * Name of represented function.
	 */
	private final String name;

	/**
	 * Default constructor.
	 * 
	 * @param name name of represented function
	 * 
	 * @throws NullPointerException if <code>name</code> is <code>null</code>
	 */
	public ElementFunction(String name) {
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
		if (!(obj instanceof ElementFunction)) {
			return false;
		}
		ElementFunction other = (ElementFunction) obj;
		return Objects.equals(name, other.name);
	}

}
