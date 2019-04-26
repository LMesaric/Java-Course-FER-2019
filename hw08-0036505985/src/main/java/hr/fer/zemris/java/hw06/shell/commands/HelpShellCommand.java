package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * When given no arguments, command lists names of all supported commands.<br>
 * When given one argument, command prints name and description of selected
 * command, or an error message if that command does not exist.
 * 
 * @author Luka Mesaric
 */
public class HelpShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "help";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"When given no arguments, command lists names "
					+ "of all supported commands.",
			"When given one argument, command prints name "
					+ "and description of selected command.",
			"If selected command does not exist, "
					+ "an error message is printed.");

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
		if (parts.length > 1) {
			env.writeln(getCommandName()
					+ " command must have exactly 0 or 1 argument.");
		} else if (parts[0].isEmpty()) {
			printSupportedCommands(env);
		} else {
			printHelpForCommand(env, parts[0]);
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints names of all supported commands.
	 * 
	 * @param env environment used for printing
	 */
	private void printSupportedCommands(Environment env) {
		env.writeln("Supported commands are: ");
		for (String commandName : env.commands().keySet()) {
			env.writeln(commandName);
		}
	}

	/**
	 * Prints help for command named <code>commandName</code>, or an error message
	 * if such command does not exist.
	 * 
	 * @param env         environment used for printing
	 * @param commandName command name
	 */
	private void printHelpForCommand(Environment env, String commandName) {
		ShellCommand command = env.commands().get(commandName);
		if (command == null) {
			env.writeln("Selected command does not exist: " + commandName);
		} else {
			env.writeln(String.format(
					"Description of command '%s':", command.getCommandName()));
			for (String line : command.getCommandDescription()) {
				env.writeln(line);
			}
		}
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
