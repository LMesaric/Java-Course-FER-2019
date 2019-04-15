package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * When given one argument, command prints currently used symbol for that symbol
 * name.<br>
 * When given two arguments, command updates currently used symbol to the symbol
 * that was given as the second argument.<br>
 * Names of symbols are <code>PROMPT</code>, <code>MORELINES</code> and
 * <code>MULTILINE</code>.<br>
 * First argument must always be a symbol name.
 * 
 * @author Luka Mesaric
 */
public class SymbolShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "symbol";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"When given one argument, command prints currently "
					+ "used symbol for that symbol name.",
			"When given two arguments, command updates currently used symbol",
			"to the symbol that was given as the second argument.",
			"Names of symbols are PROMPT, MORELINES and MULTILINE.",
			"First argument must always be a symbol name.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		String[] parts = arguments.strip().split("\\s+");
		if (parts[0].isEmpty() || parts.length > 2) {
			env.writeln(getCommandName()
					+ " command must have exactly 1 or 2 arguments.");
			return ShellStatus.CONTINUE;
		}

		SymbolUpdater symbolUpdater = chooseSymbolUpdater(env, parts[0]);
		if (symbolUpdater == null) {
			env.writeln("Unknown symbol name: " + parts[0]);
		} else if (parts.length == 1) {
			env.writeln(String.format("Symbol for %s is '%s'",
					parts[0], symbolUpdater.supplier.get()));
		} else if (parts[1].length() != 1) {
			env.writeln("New symbol must be a single character.");
		} else {
			char oldSymbol = symbolUpdater.supplier.get();
			symbolUpdater.consumer.accept(parts[1].charAt(0));
			env.writeln(String.format("Symbol for %s changed from '%s' to '%s'",
					parts[0], oldSymbol, parts[1]));
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns data structure containing symbol getter and symbol setter for symbol
	 * with name <code>symbolName</code>.
	 * 
	 * @param  env        environment from which to get getters and setters
	 * @param  symbolName name of wanted symbol
	 * @return            data structure, or <code>null</code> if name is unknown
	 */
	private SymbolUpdater chooseSymbolUpdater(Environment env, String symbolName) {
		switch (symbolName) {
		case "PROMPT":
			return new SymbolUpdater(env::getPromptSymbol, env::setPromptSymbol);
		case "MORELINES":
			return new SymbolUpdater(env::getMorelinesSymbol, env::setMorelinesSymbol);
		case "MULTILINE":
			return new SymbolUpdater(env::getMultilineSymbol, env::setMultilineSymbol);
		default:
			return null;
		}
	}

	/** Data structure for storing references to symbol getters and setters. */
	private static class SymbolUpdater {
		/** Character supplier (getter). */
		private final Supplier<Character> supplier;
		/** Character consumer (setter). */
		private final Consumer<Character> consumer;

		/**
		 * Default constructor.
		 * 
		 * @param supplier character supplier (getter)
		 * @param consumer character consumer (setter)
		 */
		public SymbolUpdater(Supplier<Character> supplier, Consumer<Character> consumer) {
			this.supplier = supplier;
			this.consumer = consumer;
		}
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
