package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.math.Vector2D;

/**
 * Models a simple turtle used in turtle graphics.
 * 
 * @author Luka Mesaric
 */
public class TurtleState {

	/**
	 * Turtle's current position, radius vector. Never <code>null</code>.
	 */
	private Vector2D position;

	/**
	 * Direction in which the turtle is looking. Vector of length <code>1</code>.
	 * Never <code>null</code>.
	 */
	private Vector2D direction;

	/**
	 * Color of drawn lines. Never <code>null</code>.
	 */
	private Color color;

	/**
	 * Effective length. Always positive and finite.
	 */
	private double effectiveLength;

	/**
	 * Default constructor.
	 * 
	 * @param position        current position
	 * @param direction       direction
	 * @param color           drawing color
	 * @param effectiveLength effective length
	 * 
	 * @throws NullPointerException     if <code>position</code>,
	 *                                  <code>direction</code> or <code>color</code>
	 *                                  is <code>null</code>.
	 * @throws IllegalArgumentException if <code>effectiveLength</code> is
	 *                                  <code>0</code> or negative, or is not
	 *                                  finite. If length of <code>direction</code>
	 *                                  is not <code>1</code>.
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double effectiveLength) {
		setPosition(position);
		setDirection(direction);
		setColor(color);
		setEffectiveLength(effectiveLength);
	}

	/**
	 * Returns a deep copy of current state.
	 * 
	 * @return deep copy of current state; never <code>null</code>
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), direction.copy(), color, effectiveLength);
	}

	/**
	 * Getter for <code>position</code>.
	 *
	 * @return <code>position</code>
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Setter for <code>position</code>.
	 *
	 * @param position the <code>position</code> to set
	 * 
	 * @throws NullPointerException if <code>position</code> is <code>null</code>.
	 */
	public void setPosition(Vector2D position) {
		this.position = Util.validateNotNull(position, "position");
	}

	/**
	 * Getter for <code>direction</code>.
	 *
	 * @return <code>direction</code>
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * Private setter for <code>direction</code>.
	 *
	 * @param direction the <code>direction</code> to set
	 * 
	 * @throws IllegalArgumentException if length of <code>direction</code> is not
	 *                                  <code>1</code>.
	 */
	public void setDirection(Vector2D direction) {
		Util.validateNotNull(direction, "direction");
		double length = Math.hypot(direction.getX(), direction.getY());
		if (Math.abs(length - 1) >= Vector2D.TOLERANCE) {
			throw new IllegalArgumentException("'direction' must be of length 1.");
		}
		this.direction = direction;
	}

	/**
	 * Getter for <code>color</code>.
	 *
	 * @return <code>color</code>
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter for <code>color</code>.
	 *
	 * @param color the <code>color</code> to set
	 * 
	 * @throws NullPointerException if <code>color</code> is <code>null</code>.
	 */
	public void setColor(Color color) {
		this.color = Util.validateNotNull(color, "color");
	}

	/**
	 * Getter for <code>effectiveLength</code>.
	 *
	 * @return <code>effectiveLength</code>
	 */
	public double getEffectiveLength() {
		return effectiveLength;
	}

	/**
	 * Setter for <code>effectiveLength</code>.
	 *
	 * @param effectiveLength the <code>effectiveLength</code> to set
	 * 
	 * @throws IllegalArgumentException if <code>effectiveLength</code> is
	 *                                  <code>0</code> or negative, or is not
	 *                                  finite.
	 */
	public void setEffectiveLength(double effectiveLength) {
		if (effectiveLength <= 0 || !Double.isFinite(effectiveLength)) {
			throw new IllegalArgumentException("'effectiveLength' must be a finite positive value.");
		}
		this.effectiveLength = effectiveLength;
	}

	@Override
	public String toString() {
		return "TurtleState [position=" + position + ", direction=" + direction + ", color=" + color
				+ ", effectiveLenght=" + effectiveLength + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, direction, effectiveLength, position);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TurtleState)) {
			return false;
		}
		TurtleState other = (TurtleState) obj;
		return Objects.equals(color, other.color) && Objects.equals(direction, other.direction)
				&& Double.doubleToLongBits(effectiveLength) == Double.doubleToLongBits(other.effectiveLength)
				&& Objects.equals(position, other.position);
	}

}
