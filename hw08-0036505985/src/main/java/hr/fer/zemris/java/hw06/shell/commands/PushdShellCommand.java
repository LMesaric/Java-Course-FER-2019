package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ArgumentChecker;

/**
 * Changes shell's current directory and pushes the previous one onto stack.<br>
 * Expects a single argument: directory name.<br>
 * If the argument does not represent an existing directory, nothing is changed.
 * <p>
 * Argument can be either a relative or absolute path. In case of a relative
 * path, it will be resolved against the previous current directory.
 * 
 * @author Luka Mesaric
 */
public class PushdShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "pushd";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Changes shell's current directory and pushes the previous one onto stack.",
			"Expects a single argument: directory name.",
			"If the argument does not represent "
					+ "an existing directory, nothing is changed.",
			"Argument can be either a relative or absolute path.",
			"In case of a relative path, it will be resolved against "
					+ "the previous current directory.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		Path dir = ArgumentChecker.expectExactlyOneExistingDirectory(arguments, env);
		if (dir == null) {
			return ShellStatus.CONTINUE;
		}

		@SuppressWarnings("unchecked")
		Deque<Path> stack = (Deque<Path>) env.getSharedData(
				CurrentDirectoryUtil.CDSTACK_KEY);
		if (stack == null) {
			stack = new ArrayDeque<>();
			env.setSharedData(CurrentDirectoryUtil.CDSTACK_KEY, stack);
		}

		stack.push(env.getCurrentDirectory());
		env.setCurrentDirectory(dir);
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
