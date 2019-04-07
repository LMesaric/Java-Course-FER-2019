package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * Implementation of <code>LSystemBuilder</code> used for configuring all
 * parameters before building an <code>LSystem</code>.
 * 
 * @author Luka Mesaric
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	/**
	 * Map of all registered productions.
	 */
	private final Dictionary<Character, String> productions = new Dictionary<>();

	/**
	 * Map of all registered commands.
	 */
	private final Dictionary<Character, Command> commands = new Dictionary<>();

	/**
	 * Starting point for productions.
	 */
	private String axiom = "";

	/**
	 * Unit length for turtle.
	 */
	private double unitLength = 0.1;

	/**
	 * Scaler for <code>unitLength</code>.
	 */
	private double unitLengthDegreeScaler = 1;

	/**
	 * Turtle's starting point.
	 */
	private Vector2D origin = new Vector2D(0, 0);

	/**
	 * Turtle's angle in degrees, towards positive <code>x</code>-axis.
	 */
	private double angle = 0;

	/**
	 * Turtle's default drawing color.
	 */
	public static final Color DEFAULT_COLOR = Color.BLACK;

	/**
	 * Setter for <code>unitLength</code>.
	 *
	 * @param unitLength the <code>unitLength</code> to set
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if <code>effectiveLength</code> is
	 *                                  <code>0</code> or negative, or is not
	 *                                  finite.
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		if (unitLength <= 0 || !Double.isFinite(unitLength)) {
			throw new IllegalArgumentException("'unitLength' must be a finite positive value.");
		}
		this.unitLength = unitLength;
		return this;
	}

	/**
	 * Setter for <code>origin</code>.
	 *
	 * @param x the <code>x</code> to set
	 * @param y the <code>y</code> to set
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if <code>x</code> or <code>y</code> is not
	 *                                  finite.
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		if (!Double.isFinite(x) || !Double.isFinite(y)) {
			throw new IllegalArgumentException("'x' and 'y' must be finite values.");
		}
		this.origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Setter for <code>angle</code>.
	 *
	 * @param angle the <code>angle</code> to set
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if <code>x</code> is not finite.
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		if (!Double.isFinite(angle)) {
			throw new IllegalArgumentException("'angle' must be a finite value.");
		}
		this.angle = angle;
		return this;
	}

	/**
	 * Setter for <code>axiom</code>.
	 *
	 * @param axiom the <code>axiom</code> to set
	 * @return a reference to this object
	 * 
	 * @throws NullPointerException if <code>axiom</code> is <code>null</code>.
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		Util.validateNotNull(axiom, "axiom");
		this.axiom = axiom;
		return this;
	}

	/**
	 * Setter for <code>unitLengthDegreeScaler</code>.
	 * 
	 * @param unitLengthDegreeScaler the <code>unitLengthDegreeScaler</code> to set
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if <code>unitLengthDegreeScaler</code> is
	 *                                  <code>0</code> or negative, or is not
	 *                                  finite.
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		if (unitLengthDegreeScaler <= 0 || !Double.isFinite(unitLengthDegreeScaler)) {
			throw new IllegalArgumentException("'unitLengthDegreeScaler' must be a finite positive value.");
		}
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}

	/**
	 * Registers a production used for generating sequences by replacing
	 * <code>symbol</code> with <code>production</code>. If <code>symbol</code> is
	 * already registered, it is overwritten.
	 * 
	 * @param symbol     symbol
	 * @param production production
	 * @return a reference to this object
	 * 
	 * @throws NullPointerException if <code>production</code> is <code>null</code>.
	 */
	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		Util.validateNotNull(production, "production");
		productions.put(symbol, production);
		return this;
	}

	/**
	 * Registers a turtle command. If <code>symbol</code> is already registered, it
	 * is overwritten.
	 * 
	 * @param symbol symbol
	 * @param action action
	 * @return a reference to this object
	 * 
	 * @throws NullPointerException     if <code>action</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>action</code> cannot be parsed.
	 */
	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		Util.validateNotNull(action, "action");
		commands.put(symbol, parseCommand(action));
		return this;
	}

	/**
	 * Configures this builder from textual commands.
	 * 
	 * @param lines strings with commands for configuration
	 * @return a reference to this object
	 * 
	 * @throws NullPointerException     if <code>lines</code> is <code>null</code>.
	 * @throws IllegalArgumentException if any line from <code>lines</code> cannot
	 *                                  be parsed.
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		Util.validateNotNull(lines, "lines");
		for (String line : lines) {
			line = line.strip();
			if (line.isEmpty()) {
				continue;
			}
			parseDirectiveFromLine(line);
		}
		return this;
	}

	/**
	 * Returns an instance of an <code>LSystem</code>.
	 * 
	 * @return implementation of <code>LSystem</code>
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Parses line for {@link #configureFromText(String[])} and executes parsed
	 * directive.
	 * 
	 * @param line line to parse into a directive
	 * 
	 * @throws IllegalArgumentException if <code>line</code> cannot be parsed.
	 */
	private void parseDirectiveFromLine(String line) {
		String[] parts = line.split("\\s+", 2);
		if (parts.length != 2) {
			throw new IllegalArgumentException("Directive must have some arguments: " + line);
		}
		String arguments = parts[1];

		switch (parts[0].toLowerCase()) {
		case "origin":
			String[] originCoords = splitExactNumberOfArgs(arguments, 2);
			double originX = parseDoubleValue(originCoords[0]);
			double originY = parseDoubleValue(originCoords[1]);
			setOrigin(originX, originY);
			break;

		case "angle":
			String[] angleValue = splitExactNumberOfArgs(arguments, 1);
			setAngle(parseDoubleValue(angleValue[0]));
			break;

		case "unitlength":
			String[] unitValue = splitExactNumberOfArgs(arguments, 1);
			setUnitLength(parseDoubleValue(unitValue[0]));
			break;

		case "unitlengthdegreescaler":
			setUnitLengthDegreeScaler(parseUnitLengthDegreeScalerArgument(arguments));
			break;

		case "axiom":
			String[] axiomValue = splitExactNumberOfArgs(arguments, 1);
			setAxiom(axiomValue[0]);
			break;

		case "command":
			// second part usually contains has some blanks that must not be split
			String[] subpartsCommand = arguments.split("\\s+", 2);
			if (subpartsCommand.length != 2 || subpartsCommand[0].length() != 1) {
				throw new IllegalArgumentException("Command must have a symbol and command body: " + arguments);
			}
			registerCommand(subpartsCommand[0].charAt(0), subpartsCommand[1]);
			break;

		case "production":
			String[] subpartsProduction = splitExactNumberOfArgs(arguments, 2);
			if (subpartsProduction[0].length() != 1) {
				throw new IllegalArgumentException(
						"Production must have a symbol as first argument: " + subpartsProduction[0]);
			}
			registerProduction(subpartsProduction[0].charAt(0), subpartsProduction[1]);
			break;

		default:
			throw new IllegalArgumentException(parts[0] + " is not a valid directive name.");
		}
	}

	/**
	 * Parses an action and returns a corresponding <code>Command</code> instance.
	 * 
	 * @param action action to parse
	 * @return corresponding <code>command</code> instance
	 * 
	 * @throws IllegalArgumentException if <code>action</code> cannot be parsed.
	 */
	private Command parseCommand(String action) {
		String[] parts = action.strip().split("\\s+");

		switch (parts.length) {
		case 0:
			throw new IllegalArgumentException("There was nothing to split.");
		case 1:
			return nameToCommand(parts[0]);
		case 2:
			return parseCommandWithTwoArguments(parts[0], parts[1]);
		default:
			throw new IllegalArgumentException("Too many arguments were given.");
		}
	}

	/**
	 * Returns a <code>Command</code> instance for given command name.
	 * 
	 * @param commandName command name
	 * @return corresponding <code>command</code> instance
	 * 
	 * @throws IllegalArgumentException if <code>commandName</code> if not valid.
	 */
	private Command nameToCommand(String commandName) {
		switch (commandName.toLowerCase()) {
		case "push":
			return new PushCommand();
		case "pop":
			return new PopCommand();
		default:
			throw new IllegalArgumentException(commandName + " is not a valid command name for use without arguments.");
		}
	}

	/**
	 * Returns a <code>Command</code> instance for given command arguments.
	 * 
	 * @param commandName command name
	 * @param argument    command argument
	 * @return corresponding <code>command</code> instance
	 * 
	 * @throws IllegalArgumentException if command cannot be constructed.
	 */
	private Command parseCommandWithTwoArguments(String commandName, String argument) {
		switch (commandName.toLowerCase()) {
		case "draw":
			return new DrawCommand(parseDoubleValue(argument));
		case "skip":
			return new SkipCommand(parseDoubleValue(argument));
		case "scale":
			return new ScaleCommand(parseDoubleValue(argument));
		case "rotate":
			return new RotateCommand(parseDoubleValue(argument));
		case "color":
			return new ColorCommand(new Color(parseHexIntegerValue(argument)));
		default:
			throw new IllegalArgumentException(commandName + " is not a valid command name for use with one argument.");
		}

	}

	/**
	 * Helper method for parsing the argument of a
	 * <code>parseUnitLengthDegreeScalerArgument</code> directive.
	 * 
	 * @param arg string to parse
	 * @return double value
	 * 
	 * @throws IllegalArgumentException if <code>arg</code> cannot be parsed.
	 */
	private double parseUnitLengthDegreeScalerArgument(String arg) {
		String[] subpartsScaler = arg.split("/", 2);
		if (subpartsScaler.length == 1) {
			return parseDoubleValue(subpartsScaler[0]);
		} else if (subpartsScaler.length == 2) {
			return parseDoubleValue(subpartsScaler[0]) / parseDoubleValue(subpartsScaler[1]);
		} else {
			throw new IllegalArgumentException("Invalid argument for unitLengthDegreeScaler: " + arg);
		}
	}

	/**
	 * Helper method for parsing a double or throwing a custom exception.
	 * 
	 * @param number string to parse
	 * @return double value
	 * 
	 * @throws IllegalArgumentException if <code>number</code> cannot be parsed.
	 */
	private double parseDoubleValue(String number) {
		try {
			return Double.parseDouble(number.strip());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Expected a number, but received: " + number, e);
		}
	}

	/**
	 * Helper method for parsing an integer from hex representation, or throwing a
	 * custom exception.
	 * 
	 * @param number string to parse
	 * @return parsed integer
	 * 
	 * @throws IllegalArgumentException if <code>number</code> cannot be parsed.
	 */
	private int parseHexIntegerValue(String number) {
		try {
			return Integer.parseInt(number.strip(), 16);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Expected a hex integer, but received: " + number, e);
		}
	}

	/**
	 * Helper method for validating <code>args</code> consists of exactly
	 * <code>number</code> arguments separated by whitespace.
	 * 
	 * @param args   all arguments
	 * @param number number of expected arguments
	 * @return array of split arguments, always length <code>number</code>
	 * 
	 * @throws IllegalArgumentException if <code>args</code> does not consist of
	 *                                  exactly <code>number</code> arguments.
	 */
	private String[] splitExactNumberOfArgs(String args, int number) {
		String[] argsArray = args.strip().split("\\s+");
		if (argsArray.length != number) {
			throw new IllegalArgumentException("Expected exactly " + number + " arguments, but received: " + args);
		}
		return argsArray;
	}

	/**
	 * Implementation of <code>LSystem</code> interface, used for drawing
	 * Lindenmayer systems.
	 * 
	 * @author Luka Mesaric
	 */
	private class LSystemImpl implements LSystem {

		/**
		 * Generates a sequence that represents turtle commands used to draw an LSystem.
		 * 
		 * @param level level of fractal
		 * @return generated sequence
		 * 
		 * @throws IllegalArgumentException if <code>level</code> is negative.
		 */
		@Override
		public String generate(int level) {
			if (level < 0) {
				throw new IllegalArgumentException("Level must be 0 or greater.");
			}
			String result = axiom;
			for (int i = 0; i < level; i++) {
				result = applyOneLevel(result);
			}
			return result;
		}

		/**
		 * Applies one level of sequence generation.
		 * 
		 * @param current starting sequence
		 * @return generated sequence
		 */
		private String applyOneLevel(String current) {
			StringBuilder sb = new StringBuilder();
			for (char c : current.toCharArray()) {
				String production = productions.get(c);
				if (production != null) {
					sb.append(production);
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		}

		/**
		 * Draws generated LSystem on <code>painter</code>, which can later be shown in
		 * GUI.
		 * 
		 * @param level   level of fractal
		 * @param painter painter for drawing
		 * 
		 * @throws NullPointerException     if <code>painter</code> is
		 *                                  <code>null</code>.
		 * @throws IllegalArgumentException if <code>level</code> is negative.
		 * 
		 */
		@Override
		public void draw(int level, Painter painter) {
			Util.validateNotNull(painter, "painter");
			String sequence = generate(level);
			Context context = new Context();

			Vector2D direction = new Vector2D(1, 0).rotated(Math.toRadians(angle));
			double effectiveLength = unitLength * Math.pow(unitLengthDegreeScaler, level);
			TurtleState state = new TurtleState(origin.copy(), direction, DEFAULT_COLOR, effectiveLength);
			context.pushState(state);

			for (char c : sequence.toCharArray()) {
				Command command = commands.get(c);
				if (command == null) {
					continue;
				}
				command.execute(context, painter);
			}
		}

	}

}
