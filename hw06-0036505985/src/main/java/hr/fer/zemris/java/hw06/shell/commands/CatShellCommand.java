package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ArgumentChecker;

/**
 * Opens given file and writes its content to console.<br>
 * Command takes either one or two arguments. The first argument (mandatory) is
 * path to some file. The second argument (optional) is name of charset that
 * should be used to read from file. If charset name is not provided, a default
 * platform charset is used.
 * 
 * @author Luka Mesaric
 */
public class CatShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "cat";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Opens given file and writes its content to console.",
			"Command takes either one or two arguments.",
			"The first argument (mandatory) is path to some file.",
			"The second argument (optional) is name of charset "
					+ "that should be used to read from file.",
			"If charset name is not provided, "
					+ "a default platform charset is used.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		List<String> strings = ArgumentChecker.safeParseStrings(arguments, env);
		if (strings == null) {
			return ShellStatus.CONTINUE;
		}

		int size = strings.size();
		if (size != 1 && size != 2) {
			env.writeln("Expected one or two arguments. Received: " + size);
			return ShellStatus.CONTINUE;
		}

		Path path;
		try {
			path = Paths.get(strings.get(0));
		} catch (InvalidPathException e) {
			env.writeln("Invalid path: " + e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (!ArgumentChecker.validateIsFile(path, env)) {
			return ShellStatus.CONTINUE;
		}

		Charset charset;
		if (size == 1) {
			charset = Charset.defaultCharset();
		} else {
			try {
				charset = Charset.forName(strings.get(1));
			} catch (IllegalArgumentException e) {
				env.writeln("Unsupported charset: " + e.getMessage());
				return ShellStatus.CONTINUE;
			}
		}

		writeToEnvironment(env, path, charset);

		return ShellStatus.CONTINUE;
	}

	/**
	 * Reads all data from <code>path</code> and writes it to <code>env</code>. If
	 * any exception occurs, an error message is written.
	 * 
	 * @param env     environment used for writing output
	 * @param path    path to read from, must represent a valid regular file
	 * @param charset charset to use for reading
	 */
	private void writeToEnvironment(Environment env, Path path, Charset charset) {

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(
						new BufferedInputStream(Files.newInputStream(path)),
						charset));
				Stream<String> lines = br.lines()) {
			lines.forEach(env::writeln);
		} catch (IOException | UncheckedIOException | SecurityException e) {
			env.writeln("Exception occured while reading from file: "
					+ e.getMessage());
		}
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
