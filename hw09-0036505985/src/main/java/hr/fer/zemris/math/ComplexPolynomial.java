package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents an immutable polynomial with complex coefficients.
 * 
 * <pre>
 * f(z) = z<sub>n</sub>*z<sup>n</sup>+z<sub>n-1</sub>*z<sup>n-1</sup>+...+z<sub>2</sub>*z<sup>2</sup>+z<sub>1</sub>*z+z<sub>0</sub>
 * </pre>
 * 
 * @author Luka Mesaric
 */
public class ComplexPolynomial {

	/**
	 * Complex coefficients <code>z<sub>i</sub></code>. Array must have at least one
	 * element (<code>z<sub>0</sub></code>), after which may follow
	 * <code>z<sub>1</sub>,z<sub>2</sub></code>, etc.<br>
	 * Array itself is never <code>null</code> and no element is <code>null</code>.
	 */
	private final Complex[] factors;

	/**
	 * Default constructor.
	 * 
	 * @param  factors                  complex coefficients of this polynomial, in
	 *                                  order <code>z<sub>0</sub>, z<sub>1</sub>,
	 *                                  z<sub>2</sub>, ..., z<sub>n</sub></code>
	 * @throws NullPointerException     if <code>factors</code> itself is
	 *                                  <code>null</code>, or any element of
	 *                                  <code>factors</code> is <code>null</code>
	 * @throws IllegalArgumentException if there are no <code>factors</code>
	 */
	public ComplexPolynomial(Complex... factors) {
		Objects.requireNonNull(factors);
		final int len = factors.length;
		if (len <= 0) {
			throw new IllegalArgumentException("At least one argument expected.");
		}
		for (Complex factor : factors) {
			Objects.requireNonNull(factor);
		}

		int maxNonZero = len - 1;
		for (; maxNonZero > 0; maxNonZero--) {
			if (!factors[maxNonZero].equals(Complex.ZERO)) {
				break;
			}
		}
		this.factors = Arrays.copyOf(factors, maxNonZero + 1);
	}

	/**
	 * Returns the order of this polynomial.
	 * 
	 * @return the order of this polynomial, always non-negative
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiplies this polynomial by <code>p</code>.
	 * 
	 * @param  p                    multiplier
	 * @return                      <code>this * p</code>
	 * @throws NullPointerException if <code>p</code> is <code>null</code>
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p);
		final int lenThis = factors.length;
		final int lenP = p.factors.length;

		final Complex[] newFactors = new Complex[lenThis + lenP - 1];
		for (int i = 0, lenNew = newFactors.length; i < lenNew; i++) {
			newFactors[i] = Complex.ZERO;
		}

		for (int i = 0; i < lenThis; i++) {
			for (int j = 0; j < lenP; j++) {
				newFactors[i + j] = factors[i]
						.multiply(p.factors[j])
						.add(newFactors[i + j]);
			}
		}
		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Computes first derivative of this polynomial.
	 * 
	 * @return polynomial representing the first derivative of this polynomial
	 */
	public ComplexPolynomial derive() {
		if (factors.length <= 1) {
			return new ComplexPolynomial(Complex.ZERO);
		}
		final Complex[] newFactors = new Complex[factors.length - 1];
		for (int i = 0, len = newFactors.length; i < len; i++) {
			newFactors[i] = factors[i + 1].multiply(new Complex(i + 1, 0));
		}
		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Computes polynomial value at given point <code>z</code> by using
	 * <a href="https://en.wikipedia.org/wiki/Horner's_method">Horner's method</a>.
	 * 
	 * @param  z                    point at which to compute value
	 * @return                      computed value
	 * @throws NullPointerException if <code>z</code> is <code>null</code>
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z);
		int n = factors.length - 1;
		Complex res = factors[n--];
		for (; n >= 0; n--) {
			res = res.multiply(z).add(factors[n]);
		}
		return res;
	}

	/**
	 * Returns a string representation of this polynomial in the form
	 * <code>(zn)*(z^n)+...+(z2)*z^2+(z1)*z^1+(z0)</code>.
	 */
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("+");
		for (int n = factors.length - 1; n > 0; n--) {
			sj.add("(" + factors[n] + ")*z^" + n);
		}
		sj.add("(" + factors[0] + ")");
		return sj.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(factors);
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
		if (!(obj instanceof ComplexPolynomial)) {
			return false;
		}
		ComplexPolynomial other = (ComplexPolynomial) obj;
		return Arrays.equals(factors, other.factors);
	}

}
