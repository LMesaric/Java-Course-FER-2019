package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Lists names of supported charsets for your Java platform. A single charset
 * name is written per line.<br>
 * Command takes no arguments.
 * 
 * @author Luka Mesaric
 */
public class CharsetsShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "charsets";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Lists names of supported charsets for your Java platform.",
			"A single charset name is written per line.",
			"Command takes no arguments.");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		if (!arguments.isBlank()) {
			env.writeln(getCommandName() + " command cannot have any arguments.");
			return ShellStatus.CONTINUE;
		}

		for (String charsetName : Charset.availableCharsets().keySet()) {
			env.writeln(charsetName);
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
