package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ArgumentChecker;

/**
 * Prints a tree-like structure of all files and directories, from given root.
 * Each directory level shifts output two characters to the right.<br>
 * Expects a single argument: directory name.
 * 
 * @author Luka Mesaric
 */
public class TreeShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "tree";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Prints a tree-like structure of all files "
					+ "and directories, from given root.",
			"Each directory level shifts output "
					+ "two characters to the right.",
			"Expects a single argument: directory name.");

	/** Constant for indenting each level (two spaces). */
	private static final String INDENTATION_PER_LEVEL = "  ";

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		Path root = ArgumentChecker.expectExactlyOneExistingDirectory(arguments, env);
		if (root == null) {
			return ShellStatus.CONTINUE;
		}
		try {
			Files.walkFileTree(root, new TreeFileVisitor(env));
		} catch (IOException | SecurityException e) {
			env.writeln("Exception occured while traversing directories: "
					+ e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

	/**
	 * Implementation of {@link FileVisitor} used for writing a tree like output.
	 */
	private static class TreeFileVisitor extends SimpleFileVisitor<Path> {

		/** Current depth (level). */
		private int depth = 0;

		/** Environment used for writing output. */
		private final Environment env;

		/**
		 * Default constructor.
		 * 
		 * @param env environment used for writing output
		 */
		private TreeFileVisitor(Environment env) {
			this.env = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			env.writeln(INDENTATION_PER_LEVEL.repeat(depth) + dir.getFileName());
			depth++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			env.writeln(INDENTATION_PER_LEVEL.repeat(depth) + file.getFileName());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
			depth--;
			return FileVisitResult.CONTINUE;
		}

	}

}
