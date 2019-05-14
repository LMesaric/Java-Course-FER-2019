package hr.fer.zemris.java.gui.calc.model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementation of calculator model - concrete strategy.
 * 
 * @author Luka Mesaric
 */
public class CalcModelImpl implements CalcModel {

	/** Flag indicating whether model is currently editable. */
	private boolean isEditable;
	/** Flag indicating whether currently stored number is negative. */
	private boolean isNegative;
	/** Current calculator input, never <code>null</code>. */
	private String strValue;
	/** Absolute value of current input. */
	private double absoluteValue;
	/** Currently active operand, never <code>null</code>. */
	private OptionalDouble activeOperand;
	/** Pending binary operation, can be <code>null</code>. */
	private DoubleBinaryOperator pendingBinaryOperation;
	/** List of registered listeners, contains no <code>null</code> values. */
	private final ArrayList<CalcValueListener> listeners = new ArrayList<>();

	/** Default constructor. */
	public CalcModelImpl() {
		// This will trigger notifying listeners, but none are registered.
		clearAll();
	}

	/**
	 * Throws exception if model is not editable at the time this method is called.
	 * 
	 * @throws CalculatorInputException if model is not editable
	 */
	private void validateModelIsEditable() {
		if (!isEditable()) {
			throw new CalculatorInputException(
					"Cannot perform this action on uneditable model.");
		}
	}

	/** Notifies all registered listeners that the stored value was changed. */
	private void notifyListeners() {
		for (CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(Objects.requireNonNull(l));
	}

	@Override
	public String toString() {
		if (strValue.isEmpty()) {
			return isNegative ? "-0" : "0";
		}
		return strValue;
	}

	@Override
	public double getValue() { return isNegative ? -absoluteValue : absoluteValue; }

	@Override
	public void setValue(double value) {
		this.absoluteValue = Math.abs(value);
		// Math#signum does not work for negative zero.
		isNegative = Math.copySign(1, value) < 0;
		// value == 0 returns true even for negative zero
		strValue = value == 0 ? "" : Double.toString(getValue());
		isEditable = false;
		notifyListeners();
	}

	@Override
	public boolean isEditable() { return isEditable; }

	@Override
	public void clear() {
		isNegative = false;
		strValue = "";
		absoluteValue = 0;
		isEditable = true;
		notifyListeners();
	}

	@Override
	public void clearAll() {
		clearActiveOperand();
		pendingBinaryOperation = null;
		clear();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		validateModelIsEditable();
		if (!strValue.isEmpty()) {
			strValue = isNegative ? strValue.substring(1) : "-" + strValue;
		}
		isNegative = !isNegative;
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		validateModelIsEditable();
		String possibleNewStrValue = strValue + ".";
		try {
			Double.parseDouble(possibleNewStrValue);
		} catch (NumberFormatException e) {
			throw new CalculatorInputException("Cannot insert decimal point.");
		}
		strValue = possibleNewStrValue;
		notifyListeners();
	}

	@Override
	public void insertDigit(int digit)
			throws CalculatorInputException, IllegalArgumentException {
		validateModelIsEditable();
		if (digit < 0 || digit > 9) {
			throw new IllegalArgumentException("Illegal digit: " + digit);
		}
		String possibleNewStrValue = strValue;
		if (strValue.equals("0") || strValue.equals("-0")) {
			if (digit == 0) {
				return;
			} else {
				possibleNewStrValue = possibleNewStrValue.substring(1);
			}
		} else if (strValue.isEmpty() && isNegative) {
			possibleNewStrValue = "-";
		}
		possibleNewStrValue += digit;
		double possibleNewValue;
		CalculatorInputException ex = new CalculatorInputException(
				"Cannot represent resulting number as a double.");
		try {
			possibleNewValue = Double.parseDouble(possibleNewStrValue);
		} catch (NumberFormatException e) {
			throw ex;
		}
		if (!Double.isFinite(possibleNewValue)) {
			throw ex;
		}
		strValue = possibleNewStrValue;
		absoluteValue = Math.abs(possibleNewValue);
		notifyListeners();
	}

	@Override
	public boolean isActiveOperandSet() { return activeOperand.isPresent(); }

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!isActiveOperandSet()) {
			throw new IllegalStateException("Active operand is not set.");
		}
		return activeOperand.orElseThrow();
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = OptionalDouble.of(activeOperand);
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = OptionalDouble.empty();
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingBinaryOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.pendingBinaryOperation = op;
	}

}
