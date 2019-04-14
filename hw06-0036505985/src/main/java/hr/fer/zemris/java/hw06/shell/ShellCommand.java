package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Models a simple shell command. Supports command execution and command help.
 * 
 * @author Luka Mesaric
 */
public interface ShellCommand {

	/**
	 * Execute modelled command.
	 * 
	 * @param env       environment used for execution
	 * @param arguments a single string containing all arguments
	 * @return new shell status
	 * 
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Returns the name of this command.
	 * 
	 * @return name of this command, never <code>null</code>
	 */
	String getCommandName();

	/**
	 * Returns command description (usage instructions). Each line is a separate
	 * element of returned list.
	 * 
	 * @return command description
	 */
	List<String> getCommandDescription();

}
