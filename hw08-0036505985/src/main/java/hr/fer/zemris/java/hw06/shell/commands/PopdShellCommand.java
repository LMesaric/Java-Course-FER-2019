package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Removes one previous current directory from the top of the stack and sets is
 * as the active current directory.<br>
 * In case the removed current directory no longer represents an existing
 * directory, or the stack is empty, the the active current directory is not
 * changed.<br>
 * Command takes no arguments.
 * 
 * @author Luka Mesaric
 */
public class PopdShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "popd";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Removes one previous current directory from the top of the stack ",
			"and sets is as the active current directory.",
			"In case the removed current directory no longer represents ",
			"an existing directory, or the stack is empty, ",
			"the the active current directory is not changed.",
			"Command takes no arguments.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Consumer<Deque<Path>> stackOperation = stack -> {
			try {
				env.setCurrentDirectory(stack.pop());
			} catch (IllegalArgumentException e) {
				env.writeln("Previously stored path no longer "
						+ "represents an existing directory.");
			}
		};

		return CurrentDirectoryUtil.execute(
				env, arguments, getCommandName(),
				stackOperation);
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
