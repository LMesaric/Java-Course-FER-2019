package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents an immutable polynomial with complex roots.
 * 
 * <pre>
 * f(z) = z<sub>0</sub>*(z-z<sub>1</sub>)*(z-z<sub>2</sub>)*...*(z-z<sub>n</sub>)
 * </pre>
 * 
 * @author Luka Mesaric
 */
public class ComplexRootedPolynomial {

	/** Constant <code>z<sub>0</sub></code>, never <code>null</code>. */
	private final Complex constant;

	/**
	 * Complex roots <code>z<sub>i</sub></code>. Array may have length
	 * <code>0</code>.<br>
	 * Array is never <code>null</code> and no element is <code>null</code>.
	 */
	private final Complex[] roots;

	/**
	 * Default constructor.
	 * 
	 * @param  constant             constant <code>z<sub>0</sub></code>
	 * @param  roots                all complex roots, may be empty
	 * @throws NullPointerException if any argument is <code>null</code>, or any
	 *                              element of <code>roots</code> is
	 *                              <code>null</code>
	 */
	public ComplexRootedPolynomial(Complex constant, Complex... roots) {
		this.constant = Objects.requireNonNull(constant);
		for (Complex root : Objects.requireNonNull(roots)) {
			Objects.requireNonNull(root);
		}
		this.roots = Arrays.copyOf(roots, roots.length);
	}

	/**
	 * Computes polynomial value at given point <code>z</code>.
	 * 
	 * @param  z                    point at which to compute value
	 * @return                      computed value
	 * @throws NullPointerException if <code>z</code> is <code>null</code>
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z);
		Complex result = constant;
		for (Complex root : roots) {
			result = result.multiply(z.sub(root));
		}
		return result;
	}

	/**
	 * Converts this polynomial to a {@link ComplexPolynomial} representation.
	 * 
	 * @return this polynomial represented as a {@link ComplexPolynomial}
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(constant);
		for (Complex root : roots) {
			result = result.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
		}
		return result;
	}

	/**
	 * Finds the index of the closest root for given complex number <code>z</code>
	 * that is within <code>threshold</code>. If there is no such root,
	 * <code>-1</code> is returned.<br>
	 * First root has index <code>0</code>, second has index <code>1</code>, etc.
	 * 
	 * @param  z                    complex number around which to search for roots
	 * @param  treshold             maximum distance from <code>z</code> to a root
	 * @return                      index of closest root within
	 *                              <code>threshold</code>, <code>-1</code> if such
	 *                              root does not exist
	 * @throws NullPointerException if <code>z</code> is <code>null</code>
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		Objects.requireNonNull(z);
		if (roots.length == 0) {
			return -1;
		}

		int minIndex = 0;
		double minValue = z.sub(roots[minIndex]).module();
		for (int i = 1, len = roots.length; i < len; i++) {
			double dist = z.sub(roots[i]).module();
			if (dist < minValue) {
				minValue = dist;
				minIndex = i;
			}
		}
		return (minValue <= treshold) ? minIndex : -1;
	}

	/**
	 * Returns a string representation of this polynomial in the form
	 * <code>(z0)*(z-(z1))*(z-(z2))*...*(z-(zn))</code>.
	 */
	@Override
	public String toString() {
		final StringJoiner sj = new StringJoiner(")*(", "(", ")");
		sj.add(constant.toString());
		for (Complex root : roots) {
			sj.add("z-(" + root + ")");
		}
		return sj.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(roots);
		result = prime * result + Objects.hash(constant);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ComplexRootedPolynomial)) {
			return false;
		}
		ComplexRootedPolynomial other = (ComplexRootedPolynomial) obj;
		return Objects.equals(constant, other.constant)
				&& Arrays.equals(roots, other.roots);
	}

}
