package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Deque;
import java.util.function.Consumer;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Utility class for executing some commands that deal with shell's current
 * directory. Stores the name of the stack saved in shared data.
 * 
 * @author Luka Mesaric
 */
class CurrentDirectoryUtil {

	/**
	 * Key for stack stored in shared data.
	 */
	static final String CDSTACK_KEY = "cdstack";

	/**
	 * Helper method for executing some commands that deal with shell's current
	 * directory.<br>
	 * Validates that given arguments are blank, and then executes
	 * <code>stackOperation</code> on stack saved in shared data under the name
	 * {@link #CDSTACK_KEY}, but only when said stack is not empty.
	 * 
	 * @param  env                  environment used for execution
	 * @param  arguments            a single string containing all command arguments
	 * @param  commandName          name of command, used for printing an error
	 *                              message in case of incorrect arguments
	 * @param  stackOperation       operation to perform
	 * @return                      new shell status, always
	 *                              {@link ShellStatus#CONTINUE}
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	static ShellStatus execute(Environment env, String arguments,
			String commandName, Consumer<Deque<Path>> stackOperation) {

		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");
		ExceptionUtil.validateNotNull(commandName, "commandName");
		ExceptionUtil.validateNotNull(stackOperation, "stackOperation");

		if (!arguments.isBlank()) {
			env.writeln(commandName + " command cannot have any arguments.");
			return ShellStatus.CONTINUE;
		}
		@SuppressWarnings("unchecked")
		Deque<Path> stack = (Deque<Path>) env.getSharedData(CDSTACK_KEY);
		if (stack == null || stack.isEmpty()) {
			env.writeln("Nema pohranjenih direktorija.");
			return ShellStatus.CONTINUE;
		}
		stackOperation.accept(stack);
		return ShellStatus.CONTINUE;
	}

	/** Disable creating instances. */
	private CurrentDirectoryUtil() {}

}
