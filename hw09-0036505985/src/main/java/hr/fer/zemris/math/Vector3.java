package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Models an immutable 3D vector in Cartesian coordinate system.
 * 
 * @author Luka Mesaric
 */
public class Vector3 {

	/** Vector's first component. */
	private final double x;

	/** Vector's second component. */
	private final double y;

	/** Vector's third component. */
	private final double z;

	/**
	 * Default constructor.
	 * 
	 * @param x vector's first component
	 * @param y vector's second component
	 * @param z vector's third component
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns vector's norm (i.e. its length).
	 * 
	 * @return norm of this vector
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns a new vector which represents the normalized vector of the current
	 * vector.
	 * 
	 * @return unit vector
	 */
	public Vector3 normalized() {
		return scale(1.0 / norm());
	}

	/**
	 * Returns a new vector which represents the sum of the current vector and
	 * <code>other</code>.
	 * 
	 * @param  other                other vector
	 * @return                      <code>this + other</code>
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(
				x + other.x,
				y + other.y,
				z + other.z);
	}

	/**
	 * Returns a new vector which represents the difference between the current
	 * vector and <code>other</code>.
	 * 
	 * @param  other                other vector
	 * @return                      <code>this - other</code>
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other);
		return add(other.scale(-1.0));
	}

	/**
	 * Returns the dot product (scalar product) of the current vector and
	 * <code>other</code>.
	 * 
	 * @param  other                other vector
	 * @return                      dot product of vectors
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	public double dot(Vector3 other) {
		Objects.requireNonNull(other);
		return x * other.x
				+ y * other.y
				+ z * other.z;
	}

	/**
	 * Returns a new vector which represents the cross product of the current vector
	 * and <code>other</code>.
	 * 
	 * @param  other                other vector
	 * @return                      cross product of vectors
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	public Vector3 cross(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(
				y * other.z - z * other.y,
				z * other.x - x * other.z,
				x * other.y - y * other.x);
	}

	/**
	 * Returns a new vector which represents the current vector scaled by
	 * <code>s</code>.
	 * 
	 * @param  s value by which to scale
	 * @return   scaled vector, never <code>null</code>
	 */
	public Vector3 scale(double s) {
		return new Vector3(
				x * s,
				y * s,
				z * s);
	}

	/**
	 * Returns the cosine of the angle between this vector and <code>other</code>.
	 * 
	 * @param  other                other vector
	 * @return                      cosine of the angle between vectors
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	public double cosAngle(Vector3 other) {
		Objects.requireNonNull(other);
		final double norms = norm() * other.norm();
		return dot(other) / norms;
	}

	/**
	 * Getter for <code>x</code> (first component).
	 *
	 * @return <code>x</code> (first component)
	 */
	public double getX() { return x; }

	/**
	 * Getter for <code>y</code> (second component).
	 *
	 * @return <code>y</code> (second component)
	 */
	public double getY() { return y; }

	/**
	 * Getter for <code>z</code> (third component).
	 *
	 * @return <code>z</code> (third component)
	 */
	public double getZ() { return z; }

	/**
	 * Returns an array of vector's components, ordered as <code>[x, y, z]</code>.
	 * 
	 * @return array of vector's components
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	/**
	 * Returns a string representation of this vector in the form
	 * <code>(x, y, z)</code>.
	 */
	@Override
	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vector3)) {
			return false;
		}
		Vector3 other = (Vector3) obj;
		// Using doubleToLongBits because of compatibility with hashCode
		// and because of possible non-finite values.
		return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
				&& Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y)
				&& Double.doubleToLongBits(z) == Double.doubleToLongBits(other.z);
	}

}
