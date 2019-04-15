package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import hr.fer.zemris.java.hw06.ExceptionUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.ArgumentChecker;

/**
 * Writes a directory listing (not recursive).<br>
 * Expects a single argument: directory name.
 * <p>
 * The output consists of 4 columns. First column indicates if current object is
 * directory (<code>d</code>), readable (<code>r</code>), writable
 * (<code>w</code>) and executable (<code>x</code>). Second column contains file
 * size in bytes, right aligned and occupying 10 characters. Third column
 * contains creation date/time. Fourth column contains file name.
 * 
 * @author Luka Mesaric
 */
public class LsShellCommand implements ShellCommand {

	/** Name of this command. */
	private static final String COMMAND_NAME = "ls";

	/** Description of this command. */
	private static final List<String> COMMAND_DESCRIPTION = Arrays.asList(
			"Writes a directory listing (not recursive).",
			"Expects a single argument: directory name.",
			"The output consists of 4 columns.",
			"First column indicates if current object is directory (d),",
			"readable (r), writable (w) and executable (x).",
			"Second column contains file size in bytes,",
			"right aligned and occupying 10 characters.",
			"Third column contains creation date/time.",
			"Fourth column contains file name.");

	/** Format of creation time. */
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** Date formatter for formatting creation time. */
	private static final SimpleDateFormat SDF = new SimpleDateFormat(FORMAT);

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ExceptionUtil.validateNotNull(env, "env");
		ExceptionUtil.validateNotNull(arguments, "arguments");

		Path dir = ArgumentChecker.expectExactlyOneExistingDirectory(arguments, env);
		if (dir == null) {
			return ShellStatus.CONTINUE;
		}

		try (Stream<Path> stream = Files.list(dir)) {
			stream.map(this::formatFileMetadata)
					.forEach(env::writeln);
		} catch (IOException | UncheckedIOException | SecurityException e) {
			env.writeln("Exception occured while accessing file data: "
					+ e.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Formats a single line of output for this command, containing file metadata
	 * (properties).
	 * 
	 * @param  file                 file whose metadata is accessed
	 * @return                      formatted output containing wanted metadata
	 * @throws UncheckedIOException if file could not be read for any reason
	 * @throws SecurityException    if access to file is denied
	 */
	private String formatFileMetadata(Path file)
			throws UncheckedIOException, SecurityException {

		BasicFileAttributes attributes = getFileAttributes(file);

		String permissions = formatFilePermissions(file);
		long fileSize = attributes.size();
		String formattedTime = formatCreationDateTime(attributes);
		String fileName = file.getFileName().toString();

		return String.format("%s %10d %s %s",
				permissions, fileSize, formattedTime, fileName);
	}

	/**
	 * Gets file attributes without throwing checked exceptions.
	 * 
	 * @param  file                 file whose attributes are needed
	 * @return                      file attributes, never <code>null</code>
	 * @throws UncheckedIOException if file could not be read for any reason
	 * @throws SecurityException    if access to file is denied
	 */
	private BasicFileAttributes getFileAttributes(Path file)
			throws UncheckedIOException, SecurityException {

		BasicFileAttributeView faView = Files.getFileAttributeView(
				file, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
		try {
			return faView.readAttributes();
		} catch (IOException e) {
			throw new UncheckedIOException(e.getMessage(), e);
		}
	}

	/**
	 * Tests file's permissions and returns a formatted string.
	 * 
	 * @param  file              file to test
	 * @return                   formatted string
	 * @throws SecurityException if access to file is denied
	 */
	private String formatFilePermissions(Path file) throws SecurityException {
		boolean isD = Files.isDirectory(file);
		boolean isR = Files.isReadable(file);
		boolean isW = Files.isWritable(file);
		boolean isX = Files.isExecutable(file);

		String permissions = String.format("%c%c%c%c",
				isD ? 'd' : '-',
				isR ? 'r' : '-',
				isW ? 'w' : '-',
				isX ? 'x' : '-');

		return permissions;
	}

	/**
	 * Formats file creation date and time.
	 * 
	 * @param  attributes attributes from which time is read
	 * @return            formatted creation date and time
	 * @see               #FORMAT
	 * @see               #SDF
	 */
	private String formatCreationDateTime(BasicFileAttributes attributes) {
		FileTime fileCreationTime = attributes.creationTime();
		return SDF.format(new Date(fileCreationTime.toMillis()));
	}

	@Override
	public String getCommandName() { return COMMAND_NAME; }

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(COMMAND_DESCRIPTION);
	}

}
