package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Writes all previous current directories to console, starting from the most
 * recent.<br>
 * If the stack is empty, appropriate message is displayed.<br>
 * Command takes no arguments.
 * 
 * @author Luka Mesaric
 */
public class ListdShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "listd";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Writes all previous current directories to console, "
					+ "starting from the most recent.",
			"If the stack is empty, appropriate message is displayed.",
			"Command takes no arguments.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return CurrentDirectoryUtil.execute(
				env, arguments, getCommandName(),
				stack -> stack.forEach(
						path -> env.writeln(path.toString())));
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
