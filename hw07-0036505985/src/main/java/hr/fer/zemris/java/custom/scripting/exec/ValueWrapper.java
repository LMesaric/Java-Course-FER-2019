package hr.fer.zemris.java.custom.scripting.exec;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;

/**
 * Mutable wrapper for a single <code>Object</code>. Enables simple mathematical
 * operations on the stored object, as long as types are compatible.
 * 
 * @author Luka Mesaric
 */
public class ValueWrapper {

	/**
	 * Value stored by this wrapper. Can be <code>null</code>.
	 */
	private Object value;

	/**
	 * Representation of <code>null</code> value in calculations.
	 */
	private static final Integer NULL_EQUIVALENT = Integer.valueOf(0);

	/**
	 * Default constructor.
	 * 
	 * @param value initial value, can be <code>null</code>
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Getter for <code>value</code>.
	 *
	 * @return <code>value</code>, can be <code>null</code>
	 */
	public Object getValue() { return value; }

	/**
	 * Setter for <code>value</code>.
	 *
	 * @param value the <code>value</code> to set, can be <code>null</code>
	 */
	public void setValue(Object value) { this.value = value; }

	/**
	 * Increases stored value by <code>incValue</code>.
	 * 
	 * @param  incValue         value by which to increase the stored value
	 * @throws RuntimeException if <code>value</code> or <code>incValue</code>
	 *                          cannot be converted to any of the supported types
	 */
	public void add(Object incValue) {
		this.value = performOperation(
				value, incValue,
				Integer::sum,
				Double::sum);
	}

	/**
	 * Decreases stored value by <code>decValue</code>.
	 * 
	 * @param  decValue         value by which to decrease the stored value
	 * @throws RuntimeException if <code>value</code> or <code>decValue</code>
	 *                          cannot be converted to any of the supported types
	 */
	public void subtract(Object decValue) {
		this.value = performOperation(
				value, decValue,
				(i1, i2) -> i1 - i2,
				(d1, d2) -> d1 - d2);
	}

	/**
	 * Multiplies stored value by <code>mulValue</code>.
	 * 
	 * @param  mulValue         value by which to multiply the stored value
	 * @throws RuntimeException if <code>value</code> or <code>mulValue</code>
	 *                          cannot be converted to any of the supported types
	 */
	public void multiply(Object mulValue) {
		this.value = performOperation(
				value, mulValue,
				(i1, i2) -> i1 * i2,
				(d1, d2) -> d1 * d2);
	}

	/**
	 * Divides stored value by <code>divValue</code>.
	 * 
	 * @param  divValue            value by which to divide the stored value
	 * @throws ArithmeticException in case of integer division by <code>0</code>
	 * @throws RuntimeException    if <code>value</code> or <code>divValue</code>
	 *                             cannot be converted to any of the supported types
	 */
	public void divide(Object divValue) {
		this.value = performOperation(
				value, divValue,
				(i1, i2) -> i1 / i2,
				(d1, d2) -> d1 / d2);
	}

	/**
	 * Performs numerical comparison between the currently stored value in this
	 * wrapper and given argument.<br>
	 * This method does not perform any changes to the stored data.
	 * 
	 * @param  withValue        the argument that stored value is compared to
	 * @return                  a value less than <code>0</code> if the currently
	 *                          stored value is smaller than the argument, value
	 *                          greater than <code>0</code> if the currently stored
	 *                          value is greater than the argument, or the value
	 *                          <code>0</code> if they are equal
	 * @throws RuntimeException if <code>value</code> or <code>withValue</code>
	 *                          cannot be converted to any of the supported types
	 */
	public int numCompare(Object withValue) {
		return performOperation(
				value, withValue,
				Integer::compare,
				Double::compare).intValue();
	}

	/**
	 * Performs either <code>intBinary</code> or <code>doubleBinary</code>,
	 * depending on argument types.
	 * 
	 * @param  first            first argument of operation
	 * @param  second           second argument of operation
	 * @param  intBinary        binary operator that will be used for integer
	 *                          calculations
	 * @param  doubleBinary     binary operator that will be used for double
	 *                          calculations
	 * @return                  result of performed calculation as a
	 *                          <code>Number</code>
	 * @throws RuntimeException if <code>first</code> or <code>second</code> cannot
	 *                          be converted to any of the supported types
	 * @see                     #prepareOneArgument(Object)
	 */
	private static Number performOperation(
			Object first,
			Object second,
			IntBinaryOperator intBinary,
			DoubleBinaryOperator doubleBinary) {

		Number n1 = prepareOneArgument(first);
		Number n2 = prepareOneArgument(second);
		if ((n1 instanceof Integer) && (n2 instanceof Integer)) {
			return intBinary.applyAsInt(n1.intValue(), n2.intValue());
		} else {
			return doubleBinary.applyAsDouble(n1.doubleValue(), n2.doubleValue());
		}
	}

	/**
	 * Converts <code>argument</code> into a form suitable for performing arithmetic
	 * or comparison operations.
	 * 
	 * @param  argument         argument to convert
	 * @return                  argument as a <code>Number</code>
	 * @throws RuntimeException if <code>argument</code> cannot be converted to any
	 *                          of the supported types
	 * @see                     #validateType(Object)
	 * @see                     #parseString(String)
	 */
	private static Number prepareOneArgument(Object argument) {
		validateType(argument);
		if (argument == null) {
			return NULL_EQUIVALENT;
		} else if (argument instanceof String) {
			return parseString((String) argument);
		} else {
			return (Number) argument;
		}
	}

	/**
	 * Parses given argument into an <code>Integer</code> or <code>Double</code>
	 * value. If argument is <code>null</code>, {@link #NULL_EQUIVALENT} is
	 * returned.
	 * 
	 * @param  number                number to parse, can be <code>null</code>
	 * @return                       parsed number as an instance of
	 *                               <code>Integer</code> or <code>Double</code>
	 * @throws NumberFormatException if <code>number</code> cannot be parsed to a
	 *                               <code>Double</code> or <code>Integer</code>
	 */
	private static Number parseString(String number) {
		if (number == null) {
			return NULL_EQUIVALENT;
		}
		number = number.strip().toUpperCase();
		if (number.contains(".") || number.contains("E")) {
			return Double.parseDouble(number);
		} else {
			return Integer.parseInt(number);
		}
	}

	/**
	 * Checks if <code>object</code> is of correct type for performing arithmetic or
	 * comparison operations. Throws an exception if it is not.
	 * 
	 * @param  object           object to check
	 * @throws RuntimeException if <code>object</code> is not <code>null</code>, nor
	 *                          is of type <code>Integer</code>, <code>Double</code>
	 *                          or <code>String</code>
	 * @see                     #chechType(Object)
	 */
	private static void validateType(Object object) {
		if (!chechType(object)) {
			throw new RuntimeException("Invalid type: " + object.getClass());
		}
	}

	/**
	 * Checks if <code>object</code> is of correct type for performing arithmetic or
	 * comparison operations.
	 * 
	 * @param  object object to check
	 * @return        <code>true</code> if <code>object</code> is <code>null</code>,
	 *                or is of type <code>Integer</code>, <code>Double</code> or
	 *                <code>String</code>
	 */
	private static boolean chechType(Object object) {
		return object == null
				|| object instanceof Integer
				|| object instanceof Double
				|| object instanceof String;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ValueWrapper)) {
			return false;
		}
		ValueWrapper other = (ValueWrapper) obj;
		return Objects.equals(value, other.value);
	}

}
