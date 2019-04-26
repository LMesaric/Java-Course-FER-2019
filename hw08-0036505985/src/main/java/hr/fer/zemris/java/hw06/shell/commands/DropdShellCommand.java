package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Removes one previous current directory from the top of the stack.<br>
 * Command takes no arguments. Current directory is not changed.
 * 
 * @author Luka Mesaric
 */
public class DropdShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "dropd";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Removes one previous current directory from the top of the stack.",
			"Command takes no arguments.",
			"Current directory is not changed.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return CurrentDirectoryUtil.execute(
				env, arguments, getCommandName(),
				Deque::pop);
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
