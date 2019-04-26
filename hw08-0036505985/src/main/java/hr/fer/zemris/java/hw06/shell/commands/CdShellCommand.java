package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ArgumentChecker;

/**
 * Changes shell's current directory.<br>
 * Expects a single argument: directory name. That name can be either a relative
 * or absolute path. In case of a relative path, it will be resolved against the
 * previous current directory.
 * 
 * @author Luka Mesaric
 */
public class CdShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "cd";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Changes shell's current directory.",
			"Expects a single argument: directory name.",
			"That name can be either a relative or absolute path.",
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
