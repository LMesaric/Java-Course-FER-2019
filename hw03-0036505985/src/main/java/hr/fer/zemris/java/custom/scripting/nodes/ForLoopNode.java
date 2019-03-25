package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct.
 * 
 * @author Luka Mesaric
 */
public class ForLoopNode extends Node {

	/**
	 * Variable used by this loop.
	 */
	private final ElementVariable variable;

	/**
	 * Start expression.
	 */
	private final Element startExpression;

	/**
	 * End expression.
	 */
	private final Element endExpression;

	/**
	 * Step expression. Can be <code>null</code>.
	 */
	private final Element stepExpression;

	/**
	 * Default constructor.
	 * 
	 * @param variable        variable
	 * @param startExpression start expression
	 * @param endExpression   end expression
	 * @param stepExpression  step expression; can be <code>null</code>
	 * 
	 * @throws NullPointerException if <code>variable</code>,
	 *                              <code>startExpression</code> or
	 *                              <code>endExpression</code> are <code>null</code>
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		this.variable = Util.validateNotNull(variable, "variable");
		this.startExpression = Util.validateNotNull(startExpression, "startExpression");
		this.endExpression = Util.validateNotNull(endExpression, "endExpression");
		this.stepExpression = stepExpression;
	}

	/**
	 * Constructor. Sets <code>stepExpression</code> to <code>null</code>.
	 * 
	 * @param variable        variable
	 * @param startExpression start expression
	 * @param endExpression   end expression
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
		this(variable, startExpression, endExpression, null);
	}

	/**
	 * Getter for <code>variable</code>.
	 *
	 * @return <code>variable</code>
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Getter for <code>startExpression</code>.
	 *
	 * @return <code>startExpression</code>
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Getter for <code>endExpression</code>.
	 *
	 * @return <code>endExpression</code>
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Getter for <code>stepExpression</code>.
	 *
	 * @return <code>stepExpression</code>; can be <code>null</code>
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

}
