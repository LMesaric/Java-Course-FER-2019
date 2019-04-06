package hr.fer.zemris.math;

import java.util.Objects;

import hr.fer.zemris.util.Util;

/**
 * Models a 2D vector in Cartesian coordinate system.
 * 
 * @author Luka Mesaric
 */
public class Vector2D {

	/**
	 * Vector's x-coordinate.
	 */
	private double x;

	/**
	 * Vector's y-coordinate.
	 */
	private double y;

	/**
	 * Tolerance for determining equality of two double values, and consequently two
	 * instances of <code>Vector2D</code>. Value is {@value}.
	 */
	public static final double TOLERANCE = 1E-7;

	/**
	 * Default constructor.
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for <code>x</code>.
	 *
	 * @return <code>x</code>
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter for <code>y</code>.
	 *
	 * @return <code>y</code>
	 */
	public double getY() {
		return y;
	}

	/**
	 * Translates this vector by <code>offset</code>.
	 * 
	 * @throws NullPointerException if <code>offset</code> is <code>null</code>
	 */
	public void translate(Vector2D offset) {
		replace(translated(Util.validateNotNull(offset, "offset")));
	}

	/**
	 * Returns a new vector which represents the current vector translated by
	 * <code>offset</code>. Original vector is not modified.
	 * 
	 * @param offset
	 * @return translated vector
	 * 
	 * @throws NullPointerException if <code>offset</code> is <code>null</code>
	 */
	public Vector2D translated(Vector2D offset) {
		Util.validateNotNull(offset, "offset");
		return new Vector2D(x + offset.x, y + offset.y);
	}

	/**
	 * Rotates this vector by <code>angle</code>.
	 * 
	 * @param angle, in radians
	 */
	public void rotate(double angle) {
		replace(rotated(angle));
	}

	/**
	 * Returns a new vector which represents the current vector rotated by
	 * <code>angle</code>. Original vector is not modified.
	 * 
	 * @param angle, in radians
	 * @return rotated vector
	 */
	public Vector2D rotated(double angle) {
		double cosAng = Math.cos(angle);
		double sinAng = Math.sin(angle);
		return new Vector2D(x * cosAng - y * sinAng, x * sinAng + y * cosAng);
	}

	/**
	 * Scales this vector by <code>scaler</code>.
	 * 
	 * @param scaler value by which to scale
	 */
	public void scale(double scaler) {
		replace(scaled(scaler));
	}

	/**
	 * Returns a new vector which represents the current vector scaled by
	 * <code>scaler</code>. Original vector is not modified.
	 * 
	 * @param scaler value by which to scale
	 * @return scaled vector
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}

	/**
	 * Creates and returns an exact copy of this vector.
	 * 
	 * @return a copy of this vector, never <code>null</code>
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	/**
	 * Helper method for changing this vector to be equal to <code>v</code>.
	 * 
	 * @param v vector whose coordinates are used as replacement
	 * 
	 * @throws NullPointerException if <code>v</code> is <code>null</code>
	 */
	private void replace(Vector2D v) {
		Util.validateNotNull(v, "v");
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * Compares two double values with precision <code>epsilon</code>, while taking
	 * into account non-finite values.
	 * 
	 * @param d1      first value
	 * @param d2      second value
	 * @param epsilon tolerance
	 * @return <code>true</code> if doubles are equal
	 */
	private boolean doubleEqualsEpsilon(double d1, double d2, double epsilon) {
		// takes into account non-finite values
		if (Double.compare(d1, d2) == 0) {
			return true;
		}
		// at this point, values cannot be both non-finite and equal
		return Math.abs(d1 - d2) < epsilon;
	}

	@Override
	public String toString() {
		return "Vector2D [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	/**
	 * For tolerance, see {@link #TOLERANCE}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Vector2D)) {
			return false;
		}
		Vector2D other = (Vector2D) obj;

		if (Double.compare(x, other.x) == 0) {

		}
		// this is needed to compare non-finite values
		return doubleEqualsEpsilon(x, other.x, TOLERANCE) 
				&& doubleEqualsEpsilon(y, other.y, TOLERANCE);
	}

}
