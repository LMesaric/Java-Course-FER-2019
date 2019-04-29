package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ArgumentChecker;

/**
 * Command renames or moves multiple files at once.<br>
 * Signature is as follows:
 * <code>massrename DIR1 DIR2 CMD MASK [other]</code><br>
 * <code>DIR1</code> is the source directory. <code>DIR2</code> is the
 * destination directory (may be the same as <code>DIR1</code>).
 * <code>CMD</code> is the name of the subcommand (<code>filter</code>,
 * <code>groups</code>, <code>show</code> or <code>execute</code>).
 * <code>MASK</code> is a regex used for filtering files in <code>DIR1</code>.
 * <code>other</code> is used for <code>show</code> or <code>execute</code> and
 * represents an expression used for generating new file names.
 * 
 * @author Luka Mesaric
 */
public class MassrenameShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "massrename";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Command renames or moves multiple files at once.",
			"Signature is as follows: massrename DIR1 DIR2 CMD MASK [other]",
			"DIR1 is the source directory.",
			"DIR2 is the destination directory (may be the same as DIR1).",
			"CMD is the name of the subcommand (filter, groups, show or execute).",
			"MASK is a regex used for filtering files in DIR1.",
			"other is used for show or execute and represents "
					+ "an expression used for generating new file names.");

	/** Flags used for regex matching. */
	private static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	/**
	 * Helper functional interface for command execution. Allows throwing
	 * {@link IOException}.
	 * 
	 * @author Luka Mesaric
	 */
	@FunctionalInterface
	private static interface SubcommandExecutor {

		/**
		 * Executes modelled command by passing it <code>env</code> and
		 * <code>args</code>.
		 * 
		 * @param  env         environment used for execution
		 * @param  args        list of command arguments
		 * @throws IOException if files could not be read
		 */
		void execute(Environment env, List<String> args) throws IOException;
	}

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

		final int size = strings.size();
		if (size < 4) {
			env.writeln("Expected at least 4 arguments. Received: " + size);
			return ShellStatus.CONTINUE;
		}

		// Using logical "or" so that at most one error message is displayed.
		Path dirSourceAbs, dirDestAbs;
		if ((dirSourceAbs = validateDirectoryArgument(strings.get(0), env)) == null ||
				(dirDestAbs = validateDirectoryArgument(strings.get(1), env)) == null) {
			return ShellStatus.CONTINUE;
		}

		final List<String> extraArgs = strings.subList(3, size);
		switch (strings.get(2)) {
		case "filter":
			cleanSubcommandExecution(env, "filter", 1, extraArgs,
					(e, l) -> subcommandFilter(dirSourceAbs, l, e));
			break;
		case "groups":
			cleanSubcommandExecution(env, "groups", 1, extraArgs,
					(e, l) -> subcommandGroups(dirSourceAbs, l, e));
			break;
		case "show":
			cleanSubcommandExecution(env, "show", 2, extraArgs,
					(e, l) -> subcommandShow(dirSourceAbs, l, e));
			break;
		case "execute":
			cleanSubcommandExecution(env, "show", 2, extraArgs,
					(e, l) -> subcommandExecute(dirSourceAbs, dirDestAbs, l, e));
			break;
		default:
			env.writeln("Unknown subcommand: " + strings.get(2));
			break;
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Converts given <code>string</code> to <code>Path</code> and <b>resolves it
	 * against the current working directory</b> from <code>env</code>. Validates
	 * that the parsed path is an existing directory.<br>
	 * Any error messages are written to <code>env</code>. In such cases,
	 * <code>null</code> is returned.
	 * 
	 * @param  string path to parse and validate
	 * @param  env    environment for execution
	 * @return        parsed path representing an existing directory, or
	 *                <code>null</code> if cannot be parsed or is invalid
	 */
	private static Path validateDirectoryArgument(String string, Environment env) {
		Path path;
		try {
			path = Paths.get(string);
		} catch (InvalidPathException e) {
			env.writeln("Invalid path: " + e.getMessage());
			return null;
		}
		path = ArgumentChecker.resolveAgainstCurrentDir(path, env);
		if (!ArgumentChecker.validateIsDirectory(path, env)) {
			return null;
		}
		return path;
	}

	/**
	 * Helper method for cleanly executing <code>subcommand</code>. Writes custom
	 * exception messages to <code>env</code>.<br>
	 * Handled exceptions are:
	 * <ul>
	 * <li>{@link PatternSyntaxException}
	 * <li>{@link InvalidPathException}
	 * <li>{@link IndexOutOfBoundsException}
	 * <li>{@link IllegalArgumentException}
	 * <li>{@link FileAlreadyExistsException}
	 * <li>{@link IOException}
	 * </ul>
	 * 
	 * @param env                  environment used for writing exception messages
	 * @param name                 name of subcommand
	 * @param expectedNumberOfArgs expected length of <code>args</code>
	 * @param args                 list of string arguments
	 * @param subcommand           subcommand to cleanly execute
	 */
	private static void cleanSubcommandExecution(
			Environment env, String name,
			int expectedNumberOfArgs, List<String> args,
			SubcommandExecutor subcommand) {

		if (args.size() != expectedNumberOfArgs) {
			env.write(String.format(
					"'%s' takes exactly %d extra arguments. Received: %d%n",
					name, expectedNumberOfArgs, args.size()));
			return;
		}

		try {
			subcommand.execute(env, args);
		} catch (PatternSyntaxException e) {
			env.writeln("Invalid mask: " + e.getMessage());
		} catch (InvalidPathException e) {
			env.writeln("Generated name is invalid: " + e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			env.writeln("Invalid group reference: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			env.writeln("Could not parse: " + e.getMessage());
		} catch (FileAlreadyExistsException e) {
			env.writeln("File already exists: " + e.getMessage());
		} catch (IOException e) {
			env.writeln("Could not read: " + e.getMessage());
		}
	}

	/**
	 * Filters all direct children of directory <code>dir</code> that are regular
	 * files and whose names match the given <code>pattern</code>. Matching is case
	 * insensitive.
	 * 
	 * @param  dir                    absolute path to directory whose children are
	 *                                filtered
	 * @param  pattern                regex to match
	 * @return                        list of filtered results
	 * @throws IOException            if directory cannot be read for any reason
	 * @throws PatternSyntaxException if <code>pattern</code>'s syntax is invalid
	 * @throws NullPointerException   if any argument is <code>null</code>
	 */
	private static List<FilterResult> filter(Path dir, String pattern)
			throws IOException {

		ExceptionUtil.validateNotNull(dir, "dir");
		ExceptionUtil.validateNotNull(pattern, "pattern");

		final Pattern p = Pattern.compile(pattern, FLAGS);
		final Function<Path, Matcher> pathMatcher = path -> {
			String fileName = path.getFileName().toString();
			return p.matcher(fileName);
		};

		try (Stream<Path> stream = Files.list(dir)) {
			return stream.filter(Files::isRegularFile)
					.map(pathMatcher)
					.filter(Matcher::matches)
					.map(FilterResult::new)
					.collect(Collectors.toList());
		} catch (UncheckedIOException | SecurityException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	/**
	 * Writes names of all direct children of directory <code>dirSource</code> that
	 * are regular files and whose names match the given regex <code>pattern</code>.
	 * Matching is case insensitive.
	 * 
	 * @param  dirSource            path to an existing directory
	 * @param  pattern              list containing exactly one element: a regex
	 * @param  env                  environment used for writing output
	 * @throws IOException          if directory cannot be read for any reason
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	private static void subcommandFilter(
			Path dirSource, List<String> pattern, Environment env)
			throws IOException {
		filter(dirSource, pattern.get(0)).forEach(res -> env.writeln(res.toString()));
	}

	/**
	 * Writes names of all direct children of directory <code>dirSource</code> that
	 * are regular files and whose names match the given regex <code>pattern</code>.
	 * Additionally, writes all groups that were matched using <code>pattern</code>.
	 * Matching is case insensitive.
	 * 
	 * @param  dirSource   path to an existing directory
	 * @param  pattern     list containing exactly one element: a regex
	 * @param  env         environment used for writing output
	 * @throws IOException if directory cannot be read for any reason
	 */
	private static void subcommandGroups(
			Path dirSource, List<String> pattern, Environment env)
			throws IOException {
		for (FilterResult result : filter(dirSource, pattern.get(0))) {
			StringBuilder sb = new StringBuilder(result.toString());
			for (int i = 0, max = result.numberOfGroups(); i <= max; i++) {
				sb.append(" ").append(i).append(": ")
						.append(result.group(i));
			}
			env.writeln(sb.toString());
		}
	}

	/**
	 * Filters files from <code>dirSource</code> that satisfy the filter from
	 * <code>pattern</code> and writes new names to <code>env</code> , generated
	 * according to the second element of <code>pattern</code>.
	 * 
	 * @param  dirSource                 absolute path to directory whose children
	 *                                   are filtered
	 * @param  pattern                   list containing exactly two elements: a
	 *                                   regex and a pattern for generating the new
	 *                                   file name
	 * @param  env                       environment used for writing output
	 * @throws IOException               if directory cannot be read for any reason
	 * @throws PatternSyntaxException    if syntax of <code>patternFind</code> is
	 *                                   invalid
	 * @throws IndexOutOfBoundsException if there is no group in
	 *                                   <code>patternFind</code> that is referenced
	 *                                   in <code>patternCreate</code>
	 * @throws IllegalArgumentException  if <code>patternCreate</code> cannot be
	 *                                   parsed for any reason
	 */
	private static void subcommandShow(
			Path dirSource, List<String> pattern, Environment env)
			throws IOException {
		String patternFind = pattern.get(0);
		String patternCreate = pattern.get(1);
		for (RenamePair pair : renamingHelper(dirSource, patternFind, patternCreate)) {
			String line = pair.oldName + " => " + pair.newName;
			env.writeln(line);
		}
	}

	/**
	 * Moves (or renames) files from <code>dirSource</code> that satisfy the filter
	 * from <code>pattern</code> to <code>dirDest</code> under a new name, generated
	 * according to the second element of <code>pattern</code>.
	 * 
	 * @param  dirSource                  absolute path to directory whose children
	 *                                    are filtered
	 * @param  dirDest                    absolute path to an existing directory to
	 *                                    which files will be moved, may be the same
	 *                                    as <code>dirSource</code>
	 * @param  pattern                    list containing exactly two elements: a
	 *                                    regex and a pattern for generating the new
	 *                                    file name
	 * @param  env                        environment used for writing output
	 * @throws FileAlreadyExistsException if any target file already exists
	 * @throws IOException                if directory cannot be read for any reason
	 * @throws PatternSyntaxException     if syntax of <code>patternFind</code> is
	 *                                    invalid
	 * @throws IndexOutOfBoundsException  if there is no group in
	 *                                    <code>patternFind</code> that is
	 *                                    referenced in <code>patternCreate</code>
	 * @throws InvalidPathException       if name generated using patterns from
	 *                                    <code>pattern</code> does not represent a
	 *                                    valid path, or valid file name
	 * @throws IllegalArgumentException   if <code>patternCreate</code> cannot be
	 *                                    parsed for any reason
	 */
	private static void subcommandExecute(
			Path dirSource, Path dirDest, List<String> pattern, Environment env)
			throws IOException {
		String patternFind = pattern.get(0);
		String patternCreate = pattern.get(1);
		for (RenamePair pair : renamingHelper(dirSource, patternFind, patternCreate)) {
			Path oldFile = dirSource.resolve(pair.oldName);
			Path newFile = dirDest.resolve(pair.newName);
			Files.move(oldFile, newFile);
			env.writeln(oldFile + " => " + newFile);
		}
	}

	/**
	 * Helper method for generating new file names based on old names and given
	 * patterns.
	 * 
	 * @param  dir                       absolute path to directory whose children
	 *                                   are filtered according to
	 *                                   <code>patternFind</code>
	 * @param  patternFind               regex for filtering files
	 * @param  patternCreate             expression used for generating new file
	 *                                   names through referencing groups in
	 *                                   <code>patternFind</code>
	 * @return                           list of pairs of old and new file names
	 * @throws IOException               if directory cannot be read for any reason
	 * @throws PatternSyntaxException    if syntax of <code>patternFind</code> is
	 *                                   invalid
	 * @throws IndexOutOfBoundsException if there is no group in
	 *                                   <code>patternFind</code> that is referenced
	 *                                   in <code>patternCreate</code>
	 * @throws IllegalArgumentException  if <code>patternCreate</code> cannot be
	 *                                   parsed for any reason
	 */
	private static List<RenamePair> renamingHelper(
			Path dir, String patternFind, String patternCreate)
			throws IOException {
		List<RenamePair> pairs = new ArrayList<>();
		NameBuilderParser parser = new NameBuilderParser(patternCreate);
		NameBuilder builder = parser.getNameBuilder();
		for (FilterResult file : filter(dir, patternFind)) {
			StringBuilder sb = new StringBuilder();
			builder.execute(file, sb);
			pairs.add(new RenamePair(file.toString(), sb.toString()));
		}
		return pairs;
	}

	/** Helper class for storing pairs of old file names and new files names. */
	private static class RenamePair {
		/** Old file name. */
		private final String oldName;
		/** New file name. */
		private final String newName;

		/**
		 * Default constructor.
		 * 
		 * @param oldName old file name
		 * @param newName new file name
		 */
		private RenamePair(String oldName, String newName) {
			this.oldName = oldName;
			this.newName = newName;
		}
	}

}
