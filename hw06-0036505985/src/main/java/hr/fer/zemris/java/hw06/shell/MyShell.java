package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * Implementation of a shell that uses standard output to write messages and
 * {@link System#in} to read user input.
 * 
 * @author Luka Mesaric
 */
public class MyShell implements Environment {

	/**
	 * Program entry point
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			new MyShell(sc).run();
		}
	}

	/**
	 * Prompt symbol for first line of input.
	 */
	private char promptSymbol = '>';

	/**
	 * Prompt symbol for any line of input that is not the first line.
	 */
	private char multilineSymbol = '|';

	/**
	 * Symbol used to break input into multiple lines.
	 */
	private char morelinesSymbol = '\\';

	/**
	 * Scanner used to read user input.
	 */
	private final Scanner scanner;

	/**
	 * Map of all supported shell commands.
	 */
	private final SortedMap<String, ShellCommand> commands = new TreeMap<>();

	/**
	 * Default constructor.
	 * 
	 * @param  sc                   scanner used to read user input
	 * @throws NullPointerException if <code>sc</code> is <code>null</code>
	 */
	public MyShell(Scanner sc) {
		ExceptionUtil.validateNotNull(sc, "sc");
		this.scanner = sc;
	}

	/**
	 * Starts the shell. Reads user input and executes given commands.
	 */
	public void run() {

		registerShellCommands(
				new CatShellCommand(),
				new CharsetsShellCommand(),
				new CopyShellCommand(),
				new ExitShellCommand(),
				new HelpShellCommand(),
				new HexdumpShellCommand(),
				new LsShellCommand(),
				new MkdirShellCommand(),
				new SymbolShellCommand(),
				new TreeShellCommand());

		try {
			writeln("Welcome to MyShell v 1.0");
			ShellStatus shellStatus = ShellStatus.CONTINUE;

			do {
				String userInput = readLine();
				String[] split = userInput.strip().split("\\s+", 2);
				// even an empty string will be split into an array of length 1
				String commandName = split[0];
				String arguments = split.length < 2 ? "" : split[1];
				ShellCommand command = commands.get(commandName);
				if (command == null) {
					if (!commandName.isEmpty()) {
						writeln("Unknown command: " + commandName);
					}
					continue;
				}
				shellStatus = command.executeCommand(this, arguments);
			} while (shellStatus != ShellStatus.TERMINATE);

		} catch (ShellIOException e) {
			// using 'writeln' might throw another ShellIOException!
			System.out.format("%nException occured: %s%nTerminating the shell!%n",
					e.getMessage());
		}

	}

	/**
	 * Helper method for adding multiple shell commands to the map of all commands.
	 * 
	 * @param  shellCommands        shell commands to register
	 * @throws NullPointerException if <code>shellCommands</code> is
	 *                              <code>null</code>, or any of its elements is
	 *                              <code>null</code>
	 */
	private void registerShellCommands(ShellCommand... shellCommands) {
		for (ShellCommand command : shellCommands) {
			commands.put(command.getCommandName(), command);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ShellIOException {@inheritDoc}
	 */
	@Override
	public String readLine() throws ShellIOException {
		try {
			StringBuilder sb = new StringBuilder();
			write(getPromptSymbol().toString());
			write(" ");
			do {
				String line = scanner.nextLine().strip();
				if (!line.endsWith(getMorelinesSymbol().toString())) {
					return sb.append(line).toString();
				}
				sb.append(line, 0, line.length() - 1);
				write(getMultilineSymbol().toString());
				write(" ");
			} while (true);
		} catch (NoSuchElementException | IllegalStateException e) {
			throw new ShellIOException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ShellIOException     {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void write(String text) throws ShellIOException {
		// NPE is deliberately thrown (unmasked) to detect possible bugs in the code.
		// The code that was written should never actually cause an NPE.
		ExceptionUtil.validateNotNull(text, "text");
		System.out.print(text);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ShellIOException     {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void writeln(String text) throws ShellIOException {
		// NPE is deliberately thrown (unmasked) to detect possible bugs in the code.
		// The code that was written should never actually cause an NPE.
		ExceptionUtil.validateNotNull(text, "text");
		// %n outputs a platform-specific line separator.
		write(String.format("%s%n", text));
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() { return multilineSymbol; }

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void setMultilineSymbol(Character symbol) {
		ExceptionUtil.validateNotNull(symbol, "symbol");
		this.multilineSymbol = symbol;
	}

	@Override
	public Character getPromptSymbol() { return promptSymbol; }

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void setPromptSymbol(Character symbol) {
		ExceptionUtil.validateNotNull(symbol, "symbol");
		this.promptSymbol = symbol;
	}

	@Override
	public Character getMorelinesSymbol() { return morelinesSymbol; }

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public void setMorelinesSymbol(Character symbol) {
		ExceptionUtil.validateNotNull(symbol, "symbol");
		this.morelinesSymbol = symbol;
	}

}
