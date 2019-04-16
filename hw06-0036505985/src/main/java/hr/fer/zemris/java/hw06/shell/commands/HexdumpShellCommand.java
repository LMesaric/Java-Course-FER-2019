package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * Produces hex-output of a file.<br>
 * When printing characters, only a standard subset of characters is shown. All
 * other characters are replaced by <code>'.'</code>.<br>
 * Expects a single argument: file name.<br>
 * 
 * @author Luka Mesaric
 */
public class HexdumpShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "hexdump";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Produces hex-output of a file.",
			"When printing characters, only a standard subset of characters is shown.",
			"All other characters are replaced by '.'.",
			"Expects a single argument: file name.");

	/** Number of bytes dumped per line. Must be divisible by 2. */
	private static final int BYTES_PER_LINE = 16;

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		Path file = ArgumentChecker.expectExactlyOneExistingFile(arguments, env);
		if (file == null) {
			return ShellStatus.CONTINUE;
		}

		writeHexDump(env, file);

		return ShellStatus.CONTINUE;
	}

	/**
	 * Reads all data from <code>path</code> and writes it to <code>env</code> as
	 * hex bytes. If any exception occurs, an error message is written.
	 * 
	 * @param env  environment used for writing output
	 * @param file path to read from, must represent a valid regular file
	 */
	private void writeHexDump(Environment env, Path file) {
		try (InputStream is = new BufferedInputStream(
				Files.newInputStream(file))) {

			byte[] bytesLine = new byte[BYTES_PER_LINE];
			StringBuilder sbHex = new StringBuilder(BYTES_PER_LINE << 2); // x4
			StringBuilder sbChar = new StringBuilder(BYTES_PER_LINE);
			int lineIndex = 0;
			String line;

			while ((line = formatLine(is, lineIndex, bytesLine, sbHex, sbChar)) != null) {
				env.writeln(line);
				lineIndex += BYTES_PER_LINE;
			}

		} catch (IOException | SecurityException e) {
			env.writeln("Exception occured while reading from file: "
					+ e.getMessage());
		}
	}

	/**
	 * Reads and formats one line of data used for hex dumping.<br>
	 * Beware that <code>buffer</code>, <code>sbHex</code> and <code>sbChar</code>
	 * will lose their previous content after calling this method.
	 * 
	 * @param  is          input stream from which data is read
	 * @param  lineIndex   ordinal number of the first byte that will be formatted
	 *                     in this line
	 * @param  buffer      byte array of size {@link #BYTES_PER_LINE}
	 * @param  sbHex       string builder, preferably of capacity
	 *                     <code>4*BYTES_PER_LINE</code>
	 * @param  sbChar      string builder, preferably of capacity
	 *                     <code>BYTES_PER_LINE</code>
	 * @return             formatted line; <code>null</code> if all data was read
	 * @throws IOException if data cannot be read from <code>is</code> for any
	 *                     reason
	 */
	private String formatLine(InputStream is, int lineIndex,
			byte[] buffer, StringBuilder sbHex, StringBuilder sbChar)
			throws IOException {
		// byte array and string builders are sent to this method so that one instance
		// can be reused for many calls

		int read = is.readNBytes(buffer, 0, buffer.length);
		if (read <= 0) {
			return null;
		}
		// clearing string builders takes minimal effort
		sbHex.setLength(0);
		sbChar.setLength(0);
		sbHex.append(String.format("%08X:", lineIndex));

		final int max = BYTES_PER_LINE;	// cache for speed
		final int mid = max >> 1;		// divide by 2
		for (int i = 0; i < max; i++) {
			sbHex.append(i == mid ? '|' : ' ');
			byte b = buffer[i];
			boolean r = i >= read;
			sbHex.append(r ? "  " : String.format("%02X", b));
			sbChar.append(r ? ' ' : ((b < 32 || b >= 127) ? '.' : (char) b));
			// value 127 is checked because it is mapped
			// to a 'DEL' char that cannot be rendered
		}
		return sbHex.append(" | ").append(sbChar).toString();
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
