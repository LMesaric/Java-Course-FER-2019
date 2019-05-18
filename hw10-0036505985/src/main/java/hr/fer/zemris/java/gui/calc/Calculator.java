package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * GUI for a simple calculator based on {@link CalcModel} and
 * {@link CalcLayout}.
 * 
 * @author Luka Mesaric
 */
public class Calculator extends JFrame {

	/** Serial version UID. */
	private static final long serialVersionUID = 5016122500695877672L;
	/** Color of all buttons. */
	private static final Color BUTTONS_COLOR = new Color(0xCFD6FF);
	/** Content pane. */
	private Container cp;
	/** Used calculator model. */
	private final CalcModel model = new CalcModelImpl();
	/** Stack used for push and pop operations. */
	private final Deque<Double> stack = new ArrayDeque<>();

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}

	/** Default constructor. */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Java Calculator v1.0");
		initGUI();
		pack();
		setLocationRelativeTo(null);
	}

	/** Initializes calculator GUI. */
	private void initGUI() {
		cp = getContentPane();
		cp.setLayout(new CalcLayout(5));

		JLabel display = new JLabel(model.toString(), SwingConstants.RIGHT);
		display.setOpaque(true);
		display.setBackground(Color.YELLOW);
		display.setFont(display.getFont().deriveFont(30f));
		display.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		cp.add(display, "1,1");

		model.addCalcValueListener(m -> display.setText(model.toString()));

		JCheckBox invertCb = new JCheckBox("Inv");
		cp.add(invertCb, "5,7");

		setUpDigits();
		setUpBinaryOps();
		setUpInvertibleOps(invertCb);

		setUpSimpleButton(".", "5,5", e -> model.insertDecimalPoint());
		setUpSimpleButton("+/-", "5,4", e -> model.swapSign());
		setUpSimpleButton("push", "3,7", e -> stack.push(model.getValue()));
		setUpSimpleButton("pop", "4,7", e -> model.setValue(stack.pop()));
		setUpSimpleButton("clr", "1,7", e -> model.clear());
		setUpSimpleButton("res", "2,7", e -> {
			model.clearAll();
			stack.clear();
		});
		setUpSimpleButton("1/x", "2,1", e -> {
			model.setValue(1.0 / model.getValue());
		});
		setUpSimpleButton("=", "1,6", e -> {
			performCalculation();
			model.setValue(model.getActiveOperand());
			model.setPendingBinaryOperation(null);
		});
	}

	/**
	 * Creates, sets up and adds a simple button to <code>cp</code>.
	 * 
	 * @param  text     button text
	 * @param  position button position
	 * @param  listener action listener to be registered
	 * @return          constructed button
	 */
	private JButton setUpSimpleButton(
			String text, String position, ActionListener listener) {
		JButton btn = new JButton(text);
		btn.setBackground(BUTTONS_COLOR);
		btn.addActionListener(listener);
		cp.add(btn, position);
		return btn;
	}

	/**
	 * Performs pending binary operation, if such exists, and clears it. Sets active
	 * operand to calculated value, or to current value if operation was not
	 * present.
	 */
	private void performCalculation() {
		double value = model.getValue();
		double newValue = value;
		DoubleBinaryOperator pending = model.getPendingBinaryOperation();
		if (pending != null) {
			newValue = pending.applyAsDouble(model.getActiveOperand(), value);
		}
		model.setActiveOperand(newValue);
		model.setPendingBinaryOperation(null);
	}

	/**
	 * Sets up all digits (0 to 9). Creates and registers appropriate listeners and
	 * adds buttons to <code>cp</code>.
	 */
	private void setUpDigits() {
		ActionListener digitListener = e -> {
			DigitButton source = (DigitButton) e.getSource();
			model.insertDigit(source.digitValue);
		};
		for (int num = 0; num <= 9; num++) {
			DigitButton digBtn = new DigitButton(num);
			digBtn.setFont(digBtn.getFont().deriveFont(30f));
			digBtn.addActionListener(digitListener);
			RCPosition pos;
			if (num == 0) {
				pos = new RCPosition(5, 3);
			} else {
				pos = new RCPosition(4 - (num - 1) / 3, 3 + (num - 1) % 3);
			}
			cp.add(digBtn, pos);
		}
	}

	/** Sets up buttons for binary operations: <code>+, -, *, /</code>. */
	private void setUpBinaryOps() {
		ActionListener binOpListener = constructBinaryOpsListener();
		prepareBinaryOperationButton("+", (a, b) -> a + b, binOpListener, "5,6");
		prepareBinaryOperationButton("-", (a, b) -> a - b, binOpListener, "4,6");
		prepareBinaryOperationButton("*", (a, b) -> a * b, binOpListener, "3,6");
		prepareBinaryOperationButton("/", (a, b) -> a / b, binOpListener, "2,6");
	}

	/**
	 * Creates, sets up and adds a binary operation button to <code>cp</code>.
	 * 
	 * @param  text      button text
	 * @param  operation operation performed by this button
	 * @param  listener  action listener to be registered
	 * @param  position  button position
	 * @return           constructed button
	 */
	private BinaryOperationButton prepareBinaryOperationButton(
			String text, DoubleBinaryOperator operation,
			ActionListener listener, String position) {
		BinaryOperationButton binOpBtn = new BinaryOperationButton(text, operation);
		binOpBtn.addActionListener(listener);
		cp.add(binOpBtn, position);
		return binOpBtn;
	}

	/**
	 * Creates a new action listener for binary operations. Listener must only be
	 * registered to components that implement
	 * {@code Supplier<DoubleBinaryOperator>}.
	 * 
	 * @return constructed listener
	 */
	private ActionListener constructBinaryOpsListener() {
		return e -> {
			performCalculation();
			@SuppressWarnings("unchecked")
			var source = (Supplier<DoubleBinaryOperator>) e.getSource();
			model.setPendingBinaryOperation(source.get());
			model.clear();
		};
	}

	/**
	 * Creates a new action listener for unary operations. Listener must only be
	 * registered to components that implement
	 * {@code Supplier<DoubleUnaryOperator>}.
	 * 
	 * @return constructed listener
	 */
	private ActionListener constructUnaryOpsListener() {
		return e -> {
			@SuppressWarnings("unchecked")
			var source = (Supplier<DoubleUnaryOperator>) e.getSource();
			model.setValue(source.get().applyAsDouble(model.getValue()));
		};
	}

	/**
	 * Sets up all invertible buttons, for both unary and binary operations.
	 * 
	 * @param invertCb check box which triggers inverting button operations
	 */
	private void setUpInvertibleOps(JCheckBox invertCb) {
		ActionListener invOpListener = constructUnaryOpsListener();

		prepareInvertibleButton(
				new InvertibleOperationButton<DoubleUnaryOperator>(
						"sin", "arcsin", Math::sin, Math::asin),
				invOpListener, invertCb, "2,2");
		prepareInvertibleButton(
				new InvertibleOperationButton<DoubleUnaryOperator>(
						"cos", "arccos", Math::cos, Math::acos),
				invOpListener, invertCb, "3,2");
		prepareInvertibleButton(
				new InvertibleOperationButton<DoubleUnaryOperator>(
						"tan", "arctan", Math::tan, Math::atan),
				invOpListener, invertCb, "4,2");
		prepareInvertibleButton(
				new InvertibleOperationButton<DoubleUnaryOperator>(
						"ctg", "arcctg", x -> 1 / Math.tan(x), x -> Math.atan(1 / x)),
				invOpListener, invertCb, "5,2");
		prepareInvertibleButton(
				new InvertibleOperationButton<DoubleUnaryOperator>(
						"log", "10^x", Math::log10, x -> Math.pow(10, x)),
				invOpListener, invertCb, "3,1");
		prepareInvertibleButton(
				new InvertibleOperationButton<DoubleUnaryOperator>(
						"ln", "e^x", Math::log, x -> Math.pow(Math.E, x)),
				invOpListener, invertCb, "4,1");
		prepareInvertibleButton(
				new InvertibleOperationButton<DoubleBinaryOperator>(
						"x^n", "x^(1/n)", Math::pow, (x, n) -> Math.pow(x, 1.0 / n)),
				constructBinaryOpsListener(), invertCb, "5,1");

	}

	/**
	 * Registers listeners for an invertible button and adds it to <code>cp</code>.
	 * 
	 * @param button   button to be set up
	 * @param listener action listener to be registered to button
	 * @param invertCb check box which triggers inverting button operations
	 * @param position button position
	 */
	private void prepareInvertibleButton(
			InvertibleButton button, ActionListener listener,
			JCheckBox trigger, String position) {

		button.addActionListener(listener);
		trigger.addItemListener(e -> {
			switch (e.getStateChange()) {
			case ItemEvent.SELECTED:
				button.setToInverted();
				break;
			case ItemEvent.DESELECTED:
				button.setToRegular();
				break;
			}
		});
		cp.add(button, position);
	}

	/**
	 * Custom button representing a digit.
	 * 
	 * @author Luka Mesaric
	 */
	private static class DigitButton extends JButton {
		/** Serial version UID. */
		private static final long serialVersionUID = -41840124659998599L;
		/** Digit represented by this button. */
		private final int digitValue;

		/**
		 * Default constructor.
		 * 
		 * @param digit digit represented by this button
		 */
		private DigitButton(int digit) {
			super(Integer.toString(digit));
			digitValue = digit;
			setBackground(BUTTONS_COLOR);
		}
	}

	/**
	 * Custom button representing a binary operation.
	 * 
	 * @author Luka Mesaric
	 */
	private static class BinaryOperationButton
			extends JButton implements Supplier<DoubleBinaryOperator> {
		/** Serial version UID. */
		private static final long serialVersionUID = 3210788758485717176L;
		/** Binary operation represented by this button. */
		private final DoubleBinaryOperator operation;

		/**
		 * Default constructor.
		 * 
		 * @param text      text written on button
		 * @param operation binary operation triggered by this button
		 */
		private BinaryOperationButton(String text, DoubleBinaryOperator operation) {
			super(text);
			this.operation = operation;
			setBackground(BUTTONS_COLOR);
		}

		@Override
		public DoubleBinaryOperator get() {
			return operation;
		}
	}

	/**
	 * Custom invertible button.
	 * 
	 * @author Luka Mesaric
	 */
	private static class InvertibleButton extends JButton {
		/** Serial version UID. */
		private static final long serialVersionUID = 2936687058478056221L;
		/** Button text in regular state. */
		private final String textRegular;
		/** Button text in inverted state. */
		private final String textInverted;
		/** Button's preferred size. */
		private final Dimension prefSize;

		/**
		 * Default constructor.
		 * 
		 * @param textRegular  button text in regular state
		 * @param textInverted button text in inverted state
		 */
		private InvertibleButton(String textRegular, String textInverted) {
			super(textRegular);
			this.textRegular = textRegular;
			this.textInverted = textInverted;
			setBackground(BUTTONS_COLOR);

			setToInverted();
			Dimension d1 = super.getPreferredSize();
			setToRegular();
			Dimension d2 = super.getPreferredSize();
			if (d1 == null || d2 == null) {
				prefSize = null;
			} else {
				prefSize = new Dimension(
						Math.max(d1.width, d2.width),
						Math.max(d1.height, d2.height));
			}
		}

		/** Changes button state to regular state. */
		void setToRegular() {
			setText(textRegular);
		}

		/** Changes button state to inverted state. */
		void setToInverted() {
			setText(textInverted);
		}

		@Override
		public Dimension getPreferredSize() { return prefSize; }

	}

	/**
	 * Custom button representing an invertible unary or binary operation.
	 * 
	 * @author Luka Mesaric
	 */
	private static class InvertibleOperationButton<E>
			extends InvertibleButton implements Supplier<E> {
		/** Serial version UID. */
		private static final long serialVersionUID = -8561768902523009648L;
		/** Unary or binary operation represented by this button in regular state. */
		private final E operationRegular;
		/** Unary or binary operation represented by this button in inverted state. */
		private final E operationInverted;
		/** Currently active operation. */
		private E activeOperation;

		/**
		 * Default constructor.
		 * 
		 * @param textRegular       button text in regular state
		 * @param textInverted      button text in inverted state
		 * @param operationRegular  unary or binary operation represented by this button
		 *                          in regular state
		 * @param operationInverted unary or binary operation represented by this button
		 *                          in inverted state
		 */
		private InvertibleOperationButton(
				String textRegular, String textInverted,
				E operationRegular, E operationInverted) {
			super(textRegular, textInverted);
			this.operationRegular = operationRegular;
			this.operationInverted = operationInverted;
			this.activeOperation = this.operationRegular;
		}

		@Override
		void setToRegular() {
			super.setToRegular();
			activeOperation = operationRegular;
		}

		@Override
		void setToInverted() {
			super.setToInverted();
			activeOperation = operationInverted;
		}

		@Override
		public E get() {
			return activeOperation;
		}
	}
}
