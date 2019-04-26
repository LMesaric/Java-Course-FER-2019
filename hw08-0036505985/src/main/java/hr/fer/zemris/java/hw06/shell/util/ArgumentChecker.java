package hr.fer.zemris.java.hw06.shell.util;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellIOException;

/**
 * Utility class for checking if given paths have wanted properties, and writing
 * appropriate messages to the environment.
 * 
 * @author Luka Mesaric
 */
public final class ArgumentChecker {

	/**
	 * Parses given argument into a list of <code>Paths</code>. Escaping quotation
	 * marks and backslashes inside strings is allowed.<br>
	 * Automatically resolves the parsed <code>paths</code> against the current
	 * directory from <code>env</code>.
	 * <p>
	 * After the ending quotation mark, either no more characters must be present or
	 * at least one space character must be present.<br>
	 * If <code>paths</code> cannot be parsed for any reason (e.g. string is never
	 * terminated, or any parsed path string cannot be converted to a
	 * <code>Path</code>), an appropriate message is written to <code>env</code>.
	 * 
	 * @param  paths                paths to be parsed
	 * @param  env                  environment used to write a message
	 * @return                      list of parsed <code>Paths</code>, empty list if
	 *                              input is blank, <code>null</code> if
	 *                              <code>paths</code> could not be parsed
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         ArgumentParser#parseToPaths(String)
	 */
	public static List<Path> safeParsePaths(String paths, Environment env) {
		ExceptionUtil.validateNotNull(paths, "paths");
		ExceptionUtil.validateNotNull(env, "env");

		try {
			return ArgumentParser.parseToPaths(paths)
					.stream()
					.map(path -> resolveAgainstCurrentDir(path, env))
					.collect(Collectors.toList());
		} catch (IllegalArgumentException e) {
			env.writeln("Invalid path: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Parses given argument into a list of <code>Strings</code>. Escaping quotation
	 * marks and backslashes inside strings is allowed.
	 * <p>
	 * After the ending quotation mark, either no more characters must be present or
	 * at least one space character must be present.<br>
	 * If <code>paths</code> cannot be parsed for any reason (e.g. string is never
	 * terminated), an appropriate message is written to <code>env</code>.
	 * 
	 * @param  paths                paths to be parsed
	 * @param  env                  environment used to write a message
	 * @return                      list of parsed <code>Strings</code>, empty list
	 *                              if input is blank, <code>null</code> if
	 *                              <code>paths</code> could not be parsed
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         ArgumentParser#parseToStrings(String)
	 */
	public static List<String> safeParseStrings(String paths, Environment env) {
		ExceptionUtil.validateNotNull(paths, "paths");
		ExceptionUtil.validateNotNull(env, "env");

		try {
			return ArgumentParser.parseToStrings(paths);
		} catch (IllegalArgumentException e) {
			env.writeln("Invalid input: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Resolves the given <code>path</code> against the current directory from
	 * <code>env</code>. Given <code>path</code> can be either relative or absolute.
	 * 
	 * @param  path                 path to resolve
	 * @param  env                  environment whose current directory is used for
	 *                              resolving paths
	 * @return                      resolved absolute path, never <code>null</code>
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public static Path resolveAgainstCurrentDir(Path path, Environment env) {
		ExceptionUtil.validateNotNull(path, "path");
		ExceptionUtil.validateNotNull(env, "env");

		Path currDir = env.getCurrentDirectory();
		return currDir.resolve(path);
	}

	/**
	 * Parses given argument into a single <code>Path</code> that represents an
	 * existing directory. Escaping quotation marks and backslashes inside strings
	 * is allowed.<br>
	 * Automatically resolves the parsed <code>paths</code> against the current
	 * directory from <code>env</code>.
	 * <p>
	 * After the ending quotation mark, either no more characters must be present or
	 * at least one space character must be present.<br>
	 * If <code>paths</code> cannot be parsed for any reason (e.g. string is never
	 * terminated, or any parsed path string cannot be converted to a
	 * <code>Path</code>), or parsed path does not represent an existing directory,
	 * an appropriate message is written to <code>env</code>.
	 * 
	 * @param  paths                path to directory to be parsed
	 * @param  env                  environment used to write messages
	 * @return                      parsed <code>Path</code> representing an
	 *                              existing directory, resolved against the current
	 *                              directory; <code>null</code> if
	 *                              <code>paths</code> could not be parsed, or
	 *                              parsed path does not represent an existing
	 *                              directory
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         ArgumentChecker#expectExactlyOnePath(String,
	 *                              Environment, BiPredicate)
	 */
	public static Path expectExactlyOneExistingDirectory(String paths, Environment env) {
		return expectExactlyOnePath(
				paths,
				env,
				ArgumentChecker::validateIsDirectory);
	}

	/**
	 * Parses given argument into a single <code>Path</code> that represents an
	 * existing regular file. Escaping quotation marks and backslashes inside
	 * strings is allowed.<br>
	 * Automatically resolves the parsed <code>paths</code> against the current
	 * directory from <code>env</code>.
	 * <p>
	 * After the ending quotation mark, either no more characters must be present or
	 * at least one space character must be present.<br>
	 * If <code>paths</code> cannot be parsed for any reason (e.g. string is never
	 * terminated, or any parsed path string cannot be converted to a
	 * <code>Path</code>), or parsed path does not represent an existing regular
	 * file, an appropriate message is written to <code>env</code>.
	 * 
	 * @param  paths                path to regular file to be parsed
	 * @param  env                  environment used to write messages
	 * @return                      parsed <code>Path</code> representing an
	 *                              existing regular file, resolved against the
	 *                              current directory; <code>null</code> if
	 *                              <code>paths</code> could not be parsed, or
	 *                              parsed path does not represent an existing
	 *                              regular file
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         ArgumentChecker#expectExactlyOnePath(String,
	 *                              Environment, BiPredicate)
	 */
	public static Path expectExactlyOneExistingFile(String paths, Environment env) {
		return expectExactlyOnePath(
				paths,
				env,
				ArgumentChecker::validateIsFile);
	}

	/**
	 * Parses given argument into a single <code>Path</code> and tests if it
	 * satisfies the given predicate. Escaping quotation marks and backslashes
	 * inside strings is allowed.<br>
	 * Automatically resolves the parsed <code>paths</code> against the current
	 * directory from <code>env</code>.
	 * <p>
	 * After the ending quotation mark, either no more characters must be present or
	 * at least one space character must be present.<br>
	 * If <code>paths</code> cannot be parsed for any reason (e.g. string is never
	 * terminated, or any parsed path string cannot be converted to a
	 * <code>Path</code>), or parsed path does not satisfy the given predicate, an
	 * appropriate message is written to <code>env</code>.
	 * 
	 * @param  paths                path to be parsed and tested
	 * @param  env                  environment used to write messages
	 * @param  predicate            predicate that needs to be satisfied
	 * @return                      parsed <code>Path</code> that satisfies the
	 *                              given predicate, resolved against the current
	 *                              directory; <code>null</code> if
	 *                              <code>paths</code> could not be parsed, or
	 *                              parsed path does not satisfy the given
	 *                              predicate, or does not exist
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         ArgumentChecker#safeParsePaths(String,
	 *                              Environment)
	 */
	public static Path expectExactlyOnePath(
			String paths, Environment env,
			BiPredicate<? super Path, ? super Environment> predicate) {

		ExceptionUtil.validateNotNull(paths, "paths");
		ExceptionUtil.validateNotNull(env, "env");

		List<Path> pathList = ArgumentChecker.safeParsePaths(paths, env);
		if (pathList == null) {
			return null;
		}

		int listSize = pathList.size();
		if (listSize != 1) {
			env.writeln("Exactly one argument expected. Received: " + listSize);
			return null;
		}

		Path dirPath = pathList.get(0);
		if (!predicate.test(dirPath, env)) {
			return null;
		}

		return dirPath;
	}

	/**
	 * Tests whether a file or directory exists. Writes a message to the environment
	 * if it does not exist, or it could not be read due to a
	 * <code>SecurityException</code>.
	 * 
	 * @param  path                 path to the file to test for existence
	 * @param  env                  environment used to write a message
	 * @return                      <code>true</code> if the file exists,
	 *                              <code>false</code> if the file does not exist or
	 *                              its existence cannot be determined
	 * @throws ShellIOException     if writing to <code>env</code> fails
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         Files#exists(Path, LinkOption...)
	 */
	public static boolean validateExists(Path path, Environment env) {
		return evaluatePredicate(
				path,
				env,
				"Specified file or directory does not exist.",
				Files::exists);
	}

	/**
	 * Tests whether given path represents a real directory. Writes a message to the
	 * environment if it does not exist or does not represent a directory, or it
	 * could not be accessed due to a <code>SecurityException</code>.<br>
	 * Automatically resolves the given <code>path</code> against the current
	 * directory from <code>env</code>.
	 * 
	 * @param  path                 path to the file to test
	 * @param  env                  environment used to write a message
	 * @return                      <code>true</code> if the file is a directory,
	 *                              <code>false</code> if the file does not exist,
	 *                              is not a directory, or it cannot be determined
	 *                              if the file is a directory or not
	 * @throws ShellIOException     if writing to <code>env</code> fails
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         Files#isDirectory(Path, LinkOption...)
	 */
	public static boolean validateIsDirectory(Path path, Environment env) {
		Path pathResolved = resolveAgainstCurrentDir(path, env);
		return validateExists(pathResolved, env)
				&& evaluatePredicate(
						pathResolved,
						env,
						"Specified argument is not a directory.",
						Files::isDirectory);
	}

	/**
	 * Tests whether given path represents a regular file. Writes a message to the
	 * environment if it does not exist or does not represent a regular file, or it
	 * could not be read due to a <code>SecurityException</code>.<br>
	 * Automatically resolves the given <code>path</code> against the current
	 * directory from <code>env</code>.
	 * 
	 * @param  path                 path to the file to test
	 * @param  env                  environment used to write a message
	 * @return                      <code>true</code> if the file is a regular file,
	 *                              <code>false</code> if the file does not exist,
	 *                              is not a regular file, or it cannot be
	 *                              determined if the file is a regular file or not
	 * @throws ShellIOException     if writing to <code>env</code> fails
	 * @throws NullPointerException if any argument is <code>null</code>
	 * @see                         Files#isRegularFile(Path, LinkOption...)
	 */
	public static boolean validateIsFile(Path path, Environment env) {
		Path pathResolved = resolveAgainstCurrentDir(path, env);
		return validateExists(pathResolved, env)
				&& evaluatePredicate(
						pathResolved,
						env,
						"Specified argument is not a regular file.",
						Files::isRegularFile);
	}

	/**
	 * Tests whether given <code>path</code> satisfies the given
	 * <code>predicate</code>. If it does not, <code>message</code> is written to
	 * <code>env</code>. If <code>message</code> is <code>null</code>, nothing is
	 * written.<br>
	 * If <code>SecurityException</code> is thrown while evaluating
	 * <code>predicate</code>, an error message is written and <code>false</code> is
	 * returned.
	 * 
	 * @param  path                 path to the file to test
	 * @param  env                  environment used to write a message
	 * @param  message              message to write to environment, if not
	 *                              <code>null</code>
	 * @param  predicate            predicate to evaluate on <code>path</code>
	 * @return                      <code>true</code> if file satisfies the
	 *                              predicate, <code>false</code> otherwise
	 * @throws NullPointerException if <code>path</code>, <code>env</code> or
	 *                              <code>predicate</code> is <code>null</code>
	 */
	public static boolean evaluatePredicate(
			Path path, Environment env, String message,
			Predicate<Path> predicate) {

		ExceptionUtil.validateNotNull(path, "path");
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(predicate, "predicate");

		try {
			boolean satisfied = predicate.test(path);
			if (!satisfied && message != null) {
				env.writeln(message);
			}
			return satisfied;
		} catch (SecurityException e) {
			env.writeln("Security exception while accessing file: " + path);
			return false;
		}
	}

	/** Disable creating instances. */
	private ArgumentChecker() {}

}
