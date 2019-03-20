package hr.fer.zemris.java.hw02;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Represents an unmodifiable complex number. Provides many methods for creating
 * complex numbers from different arguments, as well as for performing various
 * mathematical calculations.
 * 
 * @author Luka Mesaric
 */
public class ComplexNumber {

	/**
	 * Constant with value <code>0</code>.
	 */
	public static final ComplexNumber ZERO = fromReal(0.0);

	/**
	 * Constant with value <code>1</code>.
	 */
	public static final ComplexNumber ONE = fromReal(1.0);

	/**
	 * Constant with value <code>i</code>.
	 */
	public static final ComplexNumber I = fromImaginary(1.0);

	/**
	 * Constant with value <code>-i</code>.
	 */
	public static final ComplexNumber MINUS_I = fromImaginary(-1.0);

	/**
	 * Tolerance for determining equality of two double values, and consequently two
	 * <code>ComplexNumbers</code>. Value is {@value}.
	 */
	private static final double TOLERANCE = 1E-7;

	/**
	 * Real part of stored complex number, finite double.
	 */
	private final double real;

	/**
	 * Imaginary part of stored complex number, finite double.
	 */
	private final double imaginary;

	/**
	 * Default constructor.
	 * 
	 * @param real      real part of complex number
	 * @param imaginary imaginary part of complex number
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> with real part set to
	 * <code>real</code> and imaginary part set to <code>0</code>.
	 * 
	 * @param real real part of complex number
	 * @return <code>ComplexNumber</code> with real part set to <code>real</code>
	 *         and imaginary part set to <code>0</code>
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0.0);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> with real part set to
	 * <code>0</code> and imaginary part set to <code>imaginary</code>.
	 * 
	 * @param imaginary imaginary part of complex number
	 * @return <code>ComplexNumber</code> with real part set to <code>0</code> and
	 *         imaginary part set to <code>imaginary</code>
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0.0, imaginary);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> using its magnitude and angle.
	 * 
	 * @param magnitude magnitude of complex number, non-negative
	 * @param angle     angle of complex number, in radians
	 * @return <code>ComplexNumber</code> constructed from magnitude and angle
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		if (magnitude < 0.0) {
			throw new IllegalArgumentException("Magnitude must be a non-negative double.");
		}

		double re = magnitude * cos(angle);
		double im = magnitude * sin(angle);
		return new ComplexNumber(re, im);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> by parsing <code>s</code>.
	 * <p>
	 * Examples of <b>valid</b> input:
	 * </p>
	 * 
	 * <pre>
	 * 1; +351; -317; 3.51; +3.52; -3.17
	 * i; +i; -i; 351i; -351i; 3.51i; -3.17i
	 * 
	 * 1+i; 1-i; -1+i; -1-i; 31+24i; +2.71+3.15i;
	 * </pre>
	 * 
	 * <p>
	 * Examples of <b>invalid</b> input:
	 * </p>
	 * 
	 * <pre>
	 * -+2.71; --2.71; -+2.71; -2.71+-3.15i; .i
	 * i351; -i317; -i3.17
	 * 
	 * i-1; +3.15i+2.71
	 * </pre>
	 * 
	 * @param s String to parse into a <code>ComplexNumber</code>
	 * @return <code>ComplexNumber</code> parsed from <code>s</code>
	 * 
	 * @throws NullPointerException     if <code>s</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>s</code> is blank, or if
	 *                                  <code>s</code> cannot be interpreted as a
	 *                                  valid complex number
	 */
	public static ComplexNumber parse(String s) {

		Util.validateNotNull(s, "s");

		if (s.isBlank()) {
			throw new IllegalArgumentException("Expression cannot be blank.");
		} else if (s.indexOf('i') != s.lastIndexOf('i')) {
			throw new IllegalArgumentException("Expression can contain at most one letter 'i'.");
		}

		ComplexNumberParser parser = new ComplexNumberParser(s);

		// if any of the following three lines throw an IllegalArgumentException, input
		// was not valid to begin with.
		ComplexNumber c1 = parser.getNextComplexNumber();
		ComplexNumber c2 = parser.getNextComplexNumber();
		ComplexNumber c3 = parser.getNextComplexNumber();

		// c1 cannot be null because input was not blank
		if (c1.getImaginary() != 0) {
			if (c2 != null) {
				throw new IllegalArgumentException("Imaginary part must be the last part of the expression.");
			}
			return c1;
		} else {
			if (c2 == null) {
				return c1;
			} else if (c2.getReal() != 0) {
				throw new IllegalArgumentException("Expression cannot have two real parts.");
			} else if (c3 != null) {
				throw new IllegalArgumentException("Expression can contain at most one real and one imaginary part.");
			}
			return c1.add(c2);
		}

		// Please note that the original implementation was capable of parsing and
		// calculating arbitrarily long input (e.g. "1.-3i+12.4-.3+i").
		// It was only short before deadline when I learned that was not acceptable
		// behaviour for this method and had to modify my code. Because of that, I left
		// ComplexNumberParser as it was before, but modified this method so that it
		// limits ComplexNumberParser's capabilities and meets wanted criteria.
		// Therefore, this parser is quite 'overkill'.
		// Original implementation of method ComplexNumber#parse(String) is commented
		// below.

//		Util.validateNotNull(s, "s");
//
//		if (s.isBlank()) {
//			throw new IllegalArgumentException("Expression cannot be blank.");
//		}
//
//		ComplexNumberParser parser = new ComplexNumberParser(s);
//		ComplexNumber result = ComplexNumber.ZERO;
//
//		while (true) {
//			ComplexNumber parsedPart = parser.getNextComplexNumber();
//			if (parsedPart == null) {
//				return result;
//			}
//			result = result.add(parsedPart);
//		}

	}

	/**
	 * Utility class for parsing {@link ComplexNumber}s from Strings.
	 * 
	 * Pure real numbers and pure imaginary numbers are valid. <br>
	 * Letter "i" can not be placed before the magnitude of imaginary part. Multiple
	 * signs in a row are not supported. Leading plus sign is allowed.
	 * 
	 * <p>
	 * This parser can understand arbitrarily long input with no limit to the number
	 * of real and imaginary parts, as long as it is a valid expression.
	 * </p>
	 * <p>
	 * Examples of <b>valid</b> input that can be fully parsed:
	 * </p>
	 * 
	 * <pre>
	 * 1; +351; -317; 3.51; +3.52; -3.17
	 * i; +i; -i; 351i; -351i; 3.51i; -3.17i
	 * 
	 * 1+i; 1-i; -1+i; -1-i; 31+24i; +2.71+3.15i; +3.15i+2.71; i-1
	 * 
	 * 1.-3i+12.4-.3+i <-- this is a single expression
	 * </pre>
	 * 
	 * <p>
	 * Examples of <b>invalid</b> input:
	 * </p>
	 * 
	 * <pre>
	 * -+2.71; --2.71; -+2.71; -2.71+-3.15i; .i
	 * i351; -i317; -i3.17
	 * </pre>
	 * 
	 * @author Luka Mesaric
	 */
	public static class ComplexNumberParser {

		/**
		 * String to parse, without any blanks or spaces.
		 */
		private final String data;

		/**
		 * Length of <code>data</code>.
		 */
		private final int dataLength;

		/**
		 * Position of first character that has not been used up.<br>
		 * From range <code>[0, data.length()]</code>.
		 */
		private int currentPosition = 0;

		/**
		 * Flag to indicate if number that is currently being parsed is the first
		 * requested number.
		 */
		private boolean first = true;

		/**
		 * Default constructor.
		 * 
		 * @param input data for parsing
		 * 
		 * @throws NullPointerException if <code>input</code> is <code>null</code>.
		 */
		public ComplexNumberParser(String input) {
			Util.validateNotNull(input, "input");
			data = input.replaceAll("\\s+", "");
			dataLength = data.length();
		}

		/**
		 * Parses and returns first unused <code>ComplexNumber</code>. This could be
		 * just the real part of a complex number with both real and imaginary
		 * parts.<br>
		 * After first thrown IllegalArgumentException, next calls to this method may
		 * result in unpredictable and invalid output, or the same exception may be
		 * thrown.
		 * 
		 * @return first unused <code>ComplexNumber</code> parsed from input; <br>
		 *         <code>null</code> if there is nothing left to parse
		 * 
		 * @throws IllegalArgumentException if part of input that will be parsed next is
		 *                                  not a valid complex number
		 */
		public ComplexNumber getNextComplexNumber() {
			if (currentPosition >= dataLength) {
				return null;
			}

			int sign = 1;
			switch (data.charAt(currentPosition)) {
			case '-':
				sign = -1;
				// fall through
			case '+':
				currentPosition++;
				break;
			default:
				if (!first) {
					throw new IllegalArgumentException("Sign ('+' or '-') exprected at index " + currentPosition + ".");
				}
				break;
			}
			first = false;

			int numberStartIndex = currentPosition;
			if (skipDigits()) {
				// e.g. "3." is a valid double
				if (isCurrentChar('.')) {
					currentPosition++;
					skipDigits();
				}
			} else {
				// e.g. ".5" is a valid double
				if (isCurrentChar('.')) {
					currentPosition++;
					if (!skipDigits()) {
						throw new IllegalArgumentException(
								"A decimal point (position " + (currentPosition - 1) + ") cannot stand on its own.");
					}
				}
			}

			if (numberStartIndex == currentPosition) {
				if (isCurrentChar('i')) {
					currentPosition++;
					return (sign > 0) ? ComplexNumber.I : ComplexNumber.MINUS_I;
				} else {
					throw new IllegalArgumentException("Number or 'i' expected at position " + currentPosition + ".");
				}
			} else {
				String doubleForParsing = data.substring(numberStartIndex, currentPosition);
				double result;
				try {
					result = sign * Double.parseDouble(doubleForParsing);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Number starting at position " + numberStartIndex
							+ "cannot be represented as double value.", e);
				}

				if (isCurrentChar('i')) {
					currentPosition++;
					return fromImaginary(result);
				} else {
					return fromReal(result);
				}
			}
		}

		/**
		 * Returns true if <code>currentPosition</code> points to char <code>c</code>.
		 * 
		 * @param c char to test
		 * @return <code>true</code> if <code>currentPosition</code> points to char
		 *         <code>c</code>, <code>false</code> if it points to some other char or
		 *         if points outside of <code>data</code>
		 */
		private boolean isCurrentChar(char c) {
			return (currentPosition < dataLength) && data.charAt(currentPosition) == c;
		}

		/**
		 * Increases <code>currentPosition</code> until it no longer points to a digit,
		 * or all data is used up.
		 * 
		 * @return <code>true</code> if at least one digit was skipped,
		 *         <code>false</code> otherwise.
		 */
		private boolean skipDigits() {
			int start = currentPosition;
			while ((currentPosition < dataLength) && Character.isDigit(data.charAt(currentPosition))) {
				currentPosition++;
			}
			return start != currentPosition;
		}
	}

	/**
	 * Getter for <code>real</code>.
	 *
	 * @return real part of this complex number
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Getter for <code>imaginary</code>.
	 *
	 * @return imaginary part of this complex number
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Calculates the magnitude of this complex number as
	 * <code>sqrt(re</code><sup>2</sup><code>+im</code><sup>2</sup><code>)</code>.
	 * 
	 * @return magnitude of this complex number
	 */
	public double getMagnitude() {
		return hypot(real, imaginary);
	}

	/**
	 * Calculates the angle of this complex number, in radians.
	 * 
	 * @return angle of this complex number, in radians, from range
	 *         <code>[0, 2pi)</code>. Can also be NaN.
	 */
	public double getAngle() {
		double angle = atan2(imaginary, real);
		if (angle < 0) {
			angle += 2 * PI;
		}
		return angle;
	}

	/**
	 * Calculates complex conjugate of this complex number.
	 * 
	 * @return complex conjugate
	 */
	public ComplexNumber conj() {
		return new ComplexNumber(this.real, -this.imaginary);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> equivalent to the sum of this
	 * complex number and argument <code>c</code>.
	 * 
	 * @param c <code>ComplexNumber</code> to add
	 * @return sum of this and <code>c</code>
	 * 
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber add(ComplexNumber c) {
		Util.validateNotNull(c, "c");
		double re = this.real + c.real;
		double im = this.imaginary + c.imaginary;
		return new ComplexNumber(re, im);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> equivalent to the difference of
	 * this complex number and argument <code>c</code>.
	 * 
	 * @param c <code>ComplexNumber</code> to subtract
	 * @return difference of this and <code>c</code>
	 * 
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber sub(ComplexNumber c) {
		Util.validateNotNull(c, "c");
		double re = this.real - c.real;
		double im = this.imaginary - c.imaginary;
		return new ComplexNumber(re, im);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> equivalent to the product of this
	 * complex number and argument <code>c</code>.
	 * 
	 * @param c <code>ComplexNumber</code> to multiply with
	 * @return product of this and <code>c</code>
	 * 
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber mul(ComplexNumber c) {
		Util.validateNotNull(c, "c");
		double re = this.real * c.real - this.imaginary * c.imaginary;
		double im = this.imaginary * c.real + this.real * c.imaginary;
		return new ComplexNumber(re, im);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> equivalent to the quotient of
	 * this complex number and argument <code>c</code>.
	 * 
	 * @param c <code>ComplexNumber</code> to divide by, can also represent zero.
	 * @return quotient of this and <code>c</code>
	 * 
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber div(ComplexNumber c) {
		Util.validateNotNull(c, "c");
		double scaleInverse = c.real * c.real + c.imaginary * c.imaginary;
		return this.mul(c.conj()).scale(1.0 / scaleInverse);
	}

	/**
	 * Constructs a new <code>ComplexNumber</code> with both real and imaginary part
	 * multiplied by <code>d</code>.
	 * 
	 * @param d scale to multiply with
	 * @return original scaled by <code>d</code>
	 */
	public ComplexNumber scale(double d) {
		return this.mul(fromReal(d));
	}

	/**
	 * Calculates the value of this complex number raised to the power of
	 * <code>n</code>.
	 * 
	 * @param n the power to raise to
	 * @return complex number raised to <code>n</code>
	 * 
	 * @throws IllegalArgumentException if <code>n</code> is less than
	 *                                  <code>0</code>
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Cannot raise complex number to negative power.");
		}
		double magnitude = pow(getMagnitude(), n);
		double angle = getAngle() * n;
		return fromMagnitudeAndAngle(magnitude, angle);
	}

	/**
	 * Calculates all <code>n-th</code> roots of this complex number by using
	 * <a href= "https://en.wikipedia.org/wiki/De_Moivre's_formula">De Moivre's
	 * formula</a>.
	 *
	 * @param n root to take
	 * @return array of all roots
	 * 
	 * @throws IllegalArgumentException if <code>n</code> is <code>0</code> or less
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Cannot take n-th root of complex number if n is not greater than 0.");
		}

		ComplexNumber[] roots = new ComplexNumber[n];

		double magnitude = pow(getMagnitude(), 1.0 / n);
		double angle = getAngle();

		for (int k = 0; k < n; k++) {
			double angleCurr = (angle + 2 * PI * k) / n;
			roots[k] = fromMagnitudeAndAngle(magnitude, angleCurr);
		}

		return roots;
	}

	/**
	 * Returns a string representation of this <code>ComplexNumber</code> in a
	 * format that {@link #parse(String)} can accept as valid.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Double.toString(real));

		if (Double.isNaN(imaginary)) {
			sb.append("+");
		} else {
			sb.append(Math.signum(imaginary) >= 0 ? "+" : "-");
		}
		sb.append(Math.abs(imaginary)).append("i");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(real, imaginary);
	}

	/**
	 * For tolerance, see {@link #TOLERANCE}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ComplexNumber)) {
			return false;
		}
		ComplexNumber other = (ComplexNumber) obj;
		if (Double.doubleToLongBits(this.real) == Double.doubleToLongBits(other.real)
				&& Double.doubleToLongBits(this.imaginary) == Double.doubleToLongBits(other.imaginary)) {
			return true;
		}
		return Math.abs(real - other.real) < TOLERANCE && Math.abs(imaginary - other.imaginary) < TOLERANCE;
	}

}
