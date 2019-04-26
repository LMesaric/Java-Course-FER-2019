package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Models a shell environment.
 * 
 * @author Luka Mesaric
 */
public interface Environment {

	/**
	 * Reads <i>one line</i> of user input. A single line of input may span over
	 * multiple physical lines if each line that is not the last one is terminated
	 * by <code>MORELINES</code> symbol.<br>
	 * Returned input may be empty or blank, but it will never be <code>null</code>.
	 * 
	 * @return                  user input as a single string, never
	 *                          <code>null</code>
	 * @throws ShellIOException if reading fails
	 * @see                     #getMorelinesSymbol()
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes <code>text</code> to user (e.g. in console).
	 * 
	 * @param  text                 text to write
	 * @throws ShellIOException     if writing fails
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 * @see                         #writeln(String)
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes <code>text</code> to user (e.g. in console), and then terminates the
	 * line.
	 * 
	 * @param  text
	 * @throws ShellIOException     if writing fails
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 * @see                         #write(String)
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Returns an unmodifiable map of command names mapped to corresponding
	 * instances of {@link ShellCommand}.
	 * 
	 * @return map of all supported shell commands
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Getter for <code>multilineSymbol</code>.
	 *
	 * @return <code>multilineSymbol</code>, never <code>null</code>
	 */
	Character getMultilineSymbol();

	/**
	 * Setter for <code>multilineSymbol</code>.
	 *
	 * @param  symbol               the <code>multilineSymbol</code> to set
	 * @throws NullPointerException if <code>symbol</code> is <code>null</code>
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Getter for <code>promptSymbol</code>.
	 *
	 * @return <code>promptSymbol</code>, never <code>null</code>
	 */
	Character getPromptSymbol();

	/**
	 * Setter for <code>promptSymbol</code>.
	 *
	 * @param  symbol               the <code>promptSymbol</code> to set
	 * @throws NullPointerException if <code>symbol</code> is <code>null</code>
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Getter for <code>morelinesSymbol</code>.
	 *
	 * @return <code>morelinesSymbol</code>, never <code>null</code>
	 */
	Character getMorelinesSymbol();

	/**
	 * Setter for <code>morelinesSymbol</code>.
	 *
	 * @param  symbol               the <code>morelinesSymbol</code> to set
	 * @throws NullPointerException if <code>symbol</code> is <code>null</code>
	 */
	void setMorelinesSymbol(Character symbol);

	/**
	 * Getter for <code>currentDirectory</code>. Returned path is absolute.
	 *
	 * @return <code>currentDirectory</code>, never <code>null</code>
	 */
	Path getCurrentDirectory();

	/**
	 * Setter for <code>currentDirectory</code>.
	 *
	 * @param  path                     the <code>currentDirectory</code> to set
	 * @throws NullPointerException     if <code>path</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>path</code> does not represent an
	 *                                  existing directory
	 */
	void setCurrentDirectory(Path path);

	/**
	 * Returns shared data stored under the given <code>key</code>.
	 * 
	 * @param  key name of stored data
	 * @return     shared data stored under the given <code>key</code>, or
	 *             <code>null</code> if such data does not exist
	 */
	Object getSharedData(String key);

	/**
	 * Setter for shared data for given <code>key</code>.
	 * 
	 * @param  key                  name of stored data
	 * @param  value                the <code>value</code> to set
	 * @throws NullPointerException if <code>key</code> is <code>null</code>
	 */
	void setSharedData(String key, Object value);

}
