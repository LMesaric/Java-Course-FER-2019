package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an immutable complex number. Provides methods for performing
 * various mathematical calculations.
 *
 * @author Luka Mesaric
 */
public class Complex {

	/** Real part of complex number. */
	private final double re;

	/** Imaginary part of complex number. */
	private final double im;

	/** Constant with value <code>0</code>. */
	public static final Complex ZERO = new Complex(0, 0);

	/** Constant with value <code>1</code>. */
	public static final Complex ONE = new Complex(1, 0);

	/** Constant with value <code>-1</code>. */
	public static final Complex ONE_NEG = new Complex(-1, 0);

	/** Constant with value <code>i</code>. */
	public static final Complex IM = new Complex(0, 1);

	/** Constant with value <code>-i</code>. */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Default constructor.
	 * 
	 * @param re real part of complex number
	 * @param im imaginary part of complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Calculates module of this complex number.
	 * 
	 * @return module of this complex number
	 */
	public double module() {
		return Math.hypot(re, im);
	}

	/**
	 * Constructs a new <code>Complex</code> equivalent to the product of this
	 * complex number and <code>c</code>.
	 * 
	 * @param  c                    complex number to multiply with
	 * @return                      <code>this * c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(
				re * c.re - im * c.im,
				im * c.re + re * c.im);
	}

	/**
	 * Constructs a new <code>Complex</code> equivalent to the quotient of this
	 * complex number and <code>c</code>.
	 * 
	 * @param  c                    complex number to divide by, can also represent
	 *                              zero
	 * @return                      <code>this / c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c);
		final double scaleInverse = c.re * c.re + c.im * c.im;
		return new Complex(
				(re * c.re + im * c.im) / scaleInverse,
				(im * c.re - re * c.im) / scaleInverse);
	}

	/**
	 * Constructs a new <code>Complex</code> equivalent to the sum of this complex
	 * number and <code>c</code>.
	 * 
	 * @param  c                    complex number to add
	 * @return                      <code>this + c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(
				re + c.re,
				im + c.im);
	}

	/**
	 * Constructs a new <code>Complex</code> equivalent to the difference of this
	 * complex number and <code>c</code>.
	 * 
	 * @param  c                    complex number to subtract
	 * @return                      <code>this - c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c);
		return add(c.negate());
	}

	/**
	 * Constructs a new <code>Complex</code> with both real and imaginary part
	 * negated.
	 * 
	 * @return <code>-this</code>
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/**
	 * Calculates the value of this complex number raised to the <code>n-th</code>
	 * power.
	 * 
	 * @param  n                        the power to raise to, non-negative integer
	 * @return                          <code>this^n</code>
	 * @throws IllegalArgumentException if <code>n</code> is less than
	 *                                  <code>0</code>
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"Cannot raise complex number to negative power.");
		}
		return fromPolar(
				Math.pow(module(), n),
				angle() * n);
	}

	/**
	 * Calculates all <code>n-th</code> roots of this complex number by using
	 * <a href= "https://en.wikipedia.org/wiki/De_Moivre's_formula">De Moivre's
	 * formula</a>.
	 * 
	 * @param  n                        root to take, positive integer
	 * @return                          list of all <code>n-th</code> roots of this
	 *                                  complex number, length is <code>n</code>
	 * @throws IllegalArgumentException if <code>n</code> is <code>0</code> or less
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException(
					"Cannot take n-th root of complex number if n is not greater than 0.");
		}
		List<Complex> roots = new ArrayList<>(n);
		final double magnitude = Math.pow(module(), 1.0 / n);
		final double angle = angle();
		for (int k = 0; k < n; k++) {
			double angleCurr = (angle + 2 * Math.PI * k) / n;
			roots.add(fromPolar(magnitude, angleCurr));
		}
		return roots;
	}

	/**
	 * Calculates the angle of this complex number, in radians.
	 * 
	 * @return the angle of this complex number, in radians
	 */
	private double angle() {
		return Math.atan2(im, re);
	}

	/**
	 * Constructs a new <code>Complex</code> using its magnitude and angle.
	 * 
	 * @param  magnitude magnitude of complex number, non-negative
	 * @param  angle     angle of complex number, in radians
	 * @return           <code>Complex</code> constructed from magnitude and angle
	 */
	private static Complex fromPolar(double magnitude, double angle) {
		return new Complex(
				magnitude * Math.cos(angle),
				magnitude * Math.sin(angle));
	}

	/**
	 * Returns a string representation of this complex number in the form
	 * <code>re + im*i</code> ("*" is left out).
	 */
	@Override
	public String toString() {
		return re + (im < 0 ? "-" : "+") + "i" + Math.abs(im);
	}

	@Override
	public int hashCode() {
		return Objects.hash(im, re);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Complex)) {
			return false;
		}
		Complex other = (Complex) obj;
		// Using doubleToLongBits because of compatibility with hashCode
		// and because of possible non-finite values.
		return Double.doubleToLongBits(im) == Double.doubleToLongBits(other.im)
				&& Double.doubleToLongBits(re) == Double.doubleToLongBits(other.re);
	}

}
