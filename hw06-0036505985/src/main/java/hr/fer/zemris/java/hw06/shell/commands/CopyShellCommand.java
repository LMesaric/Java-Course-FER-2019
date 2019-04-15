package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ArgumentChecker;

/**
 * Copies given file to given destination.<br>
 * Expects exactly two arguments: source and destination. Source must be path to
 * an existing file (not directory). Destination must be path to a file or
 * directory. If it is a path to a directory, source is copied into that
 * directory using the original file name.<br>
 * If destination already exists, user is asked for confirmation about
 * overwriting the file.
 * 
 * @author Luka Mesaric
 */
public class CopyShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "copy";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Copies given file to given destination.",
			"Expects exactly two arguments: source and destination.",
			"Source must be path to an existing file (not directory).",
			"Destination must be path to a file or directory.",
			"If it is a path to a directory, source is copied",
			"into that directory using the original file name.",
			"If destination already exists, user is asked",
			"for confirmation about overwriting the file.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		List<Path> argsList = ArgumentChecker.safeParsePaths(arguments, env);
		if (argsList == null) {
			return ShellStatus.CONTINUE;
		} else if (argsList.size() != 2) {
			env.writeln("Exactly two arguments expected. Received: " + argsList.size());
			return ShellStatus.CONTINUE;
		}

		Path source = argsList.get(0);
		Path dest = argsList.get(1);
		if (!ArgumentChecker.validateIsFile(source, env)) {
			return ShellStatus.CONTINUE;
		}
		// At this point, source exists and it is a file
		try {
			if (Files.isDirectory(dest)) {
				dest = Paths.get(dest.toString(), source.getFileName().toString());
				// It is possible that there exists a subdirectory
				// with the same name as source
				if (Files.isDirectory(dest)) {
					env.writeln("Subdirectory with the same name already exists.");
					env.writeln("Aborting execution.");
					return ShellStatus.CONTINUE;
				}
			}
			if (Files.isRegularFile(dest)) {
				env.writeln("Destination already exists: " + dest);
				env.writeln("Are you sure you want to overwrite it? Yes/No");
				if (!getConfirmationFromUser(env)) {
					return ShellStatus.CONTINUE;
				}
			}
			copyFile(env, source, dest);
		} catch (SecurityException e) {
			env.writeln("Access denied: " + e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Copies <code>source</code> do <code>dest</code>, overwriting it if it exists.
	 * 
	 * @param  env               environment used for writing error messages
	 * @param  source            file to copy
	 * @param  dest              destination to which <code>source</code> is copied
	 * @throws SecurityException if access to file is denied
	 */
	private void copyFile(Environment env, Path source, Path dest) {
		try (BufferedInputStream bis = new BufferedInputStream(
					Files.newInputStream(source));
				BufferedOutputStream bos = new BufferedOutputStream(
						Files.newOutputStream(dest))) {
			bis.transferTo(bos);
		} catch (IOException e) {
			env.writeln("Exception occured while reading or writing: "
					+ e.getMessage());
		}
	}

	/**
	 * Requests confirmation from user, in form of words <code>yes</code>,
	 * <code>y</code>, <code>no</code> or <code>n</code> (case insensitive).<br>
	 * Writes a message if input was none of the above.
	 * 
	 * @param  env              environment user to read user input
	 * @return                  <code>true</code> if user input was <code>yes</code>
	 *                          or <code>y</code>, <code>false</code> otherwise
	 *                          (case insensitive)
	 * @throws ShellIOException if reading fails
	 */
	private boolean getConfirmationFromUser(Environment env) throws ShellIOException {
		String answerString = env.readLine().strip().toLowerCase();
		switch (answerString) {
		case "yes": // fall through
		case "y":
			return true;
		case "no":  // fall through
		case "n":
			return false;
		default:
			env.writeln("Answer is not recognized. Aborting execution.");
			return false;
		}
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
