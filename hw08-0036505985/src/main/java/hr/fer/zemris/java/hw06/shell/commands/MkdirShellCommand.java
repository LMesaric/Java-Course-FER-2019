package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
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
 * Creates new directories. All parent directories are created as well.<br>
 * Expects a single argument: directory name.
 * 
 * @author Luka Mesaric
 */
public class MkdirShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "mkdir";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Creates new directories.",
			"All parent directories are created as well.",
			"Expects a single argument: directory name.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		List<Path> pathArgs = ArgumentChecker.safeParsePaths(arguments, env);
		if (pathArgs == null) {
			return ShellStatus.CONTINUE;
		} else if (pathArgs.size() != 1) {
			env.writeln("Exactly one argument expected. Received: " + pathArgs.size());
			return ShellStatus.CONTINUE;
		}

		try {
			Files.createDirectories(pathArgs.get(0));
		} catch (IOException | SecurityException e) {
			env.writeln("Exception occured while creating directories: "
					+ e.getMessage());
			env.writeln("Please note that some of the parent directories "
					+ "may have been created.");
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
