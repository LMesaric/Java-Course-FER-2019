package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.collections.Util;

/**
 * Command-line application which accepts a single command-line argument: an
 * expression that will be evaluated. The expression must be in postfix
 * representation. <br>
 * When starting the program from the console, enclose the whole expression in
 * quotation marks.
 * <p>
 * In the expression, everything must be separated by one (or more) spaces.<br>
 * Each operator takes two preceding numbers and replaces them with operation
 * result. Only <code>+, -, /, *</code> and <code>%</code> (remainder of integer
 * division) are supported. All operators work with and produce integer results
 * (<code>3/2=1</code>).
 * </p>
 * 
 * Examples:
 * 
 * <pre>
 * 8 -2 / -1 * --> 4
 * -1 8 2 / +  --> 3
 * 1 0 * 7 +   --> 7
 * 1 0 / 7 +   --> ERROR (division by 0)
 * 8 -2 / -1   --> ERROR (not enough operators)
 * 3 2 ^       --> ERROR (invalid operator)
 * 
 * 12 15 - -7 * 3 8 3 % + / --> 4 because 
 * (12 - 15) * -7 / (3 + 8 % 3) = -3 * -7 / (3 + 2) = -3 * -7 / 5 = 21 / 5 = 4
 * </pre>
 * 
 * @author Luka Mesaric
 */
public class StackDemo {

	/**
	 * Program entry point.
	 * 
	 * @param args a single postfix expression
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Exactly one argument is expected!");
			System.exit(1);
		}

		try {
			String[] elements = args[0].trim().split("\\s+");
			int finalResult = evaluatePostfixExpression(elements);
			System.out.format("Expression evaluates to %d.%n", finalResult);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Evaluates postfix expression whose elements are given through argument
	 * <code>elements</code>.
	 * 
	 * @param elements elements of postfix expression
	 * @return expression value
	 * 
	 * @throws IllegalArgumentException if expression is not a valid postfix
	 *                                  expression, or if there is an attempt to
	 *                                  divide by <code>0</code>
	 * @throws NullPointerException     if <code>elements</code> is
	 *                                  <code>null</code>
	 */
	private static int evaluatePostfixExpression(String[] elements) {
		Util.validateNotNull(elements, "elements");

		// all elements pushed on stack are Integers!
		ObjectStack stack = new ObjectStack();

		for (String element : elements) {
			try {
				int num = Integer.parseInt(element);
				stack.push(num);
				continue;
			} catch (NumberFormatException ignorable) {
			}

			int firstOperand;
			int secondOperand;
			try {
				secondOperand = (int) stack.pop();
				firstOperand = (int) stack.pop();
			} catch (EmptyStackException e) {
				throw new IllegalArgumentException("Expression is incorrect, wrong number of operators.", e);
			}

			int result = performOperation(firstOperand, secondOperand, element);
			stack.push(result);
		}

		if (stack.size() != 1) {
			throw new IllegalArgumentException("Expression is incorrect, wrong number of operators.");
		}

		return (int) stack.pop();
	}

	/**
	 * Calculates wanted result as <code>firstOperand operator secondOperand</code>.
	 * 
	 * @param firstOperand  first operator
	 * @param secondOperand second operator
	 * @param operator      any of <code>+, -, /, *</code> or <code>%</code>
	 *                      (remainder of integer division) operators, represented
	 *                      as a String
	 * @return result of requested calculation
	 * 
	 * @throws IllegalArgumentException if <code>operator</code> is not a valid
	 *                                  operator, or there is an attempt to divide
	 *                                  by <code>0</code>
	 */
	private static int performOperation(int firstOperand, int secondOperand, String operator) {
		switch (operator) {
		case "+":
			return firstOperand + secondOperand;
		case "-":
			return firstOperand - secondOperand;
		case "*":
			return firstOperand * secondOperand;
		case "/":
			if (secondOperand == 0) {
				throw new IllegalArgumentException("Cannot divide by zero!");
			}
			return firstOperand / secondOperand;
		case "%":
			if (secondOperand == 0) {
				throw new IllegalArgumentException("Cannot calculate 'mod 0'!");
			}
			return firstOperand % secondOperand;
		default:
			throw new IllegalArgumentException(String.format("Invalid operator: '%s'", operator));
		}
	}

}
