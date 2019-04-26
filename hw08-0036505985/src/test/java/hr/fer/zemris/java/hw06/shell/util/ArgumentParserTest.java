package hr.fer.zemris.java.hw06.shell.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.hw06.shell.util.ArgumentParser;

/**
 * @author Luka Mesaric
 */
class ArgumentParserTest {

	@Test
	void testValid() {

		/* "Documents and Settings" */
		assertEquals(
				Arrays.asList(
						Paths.get("Documents and Settings")),
				ArgumentParser.parseToPaths(
						" \"Documents and Settings\" "));

		/* "C:\Documents and Settings\Users\javko" */
		assertEquals(
				Arrays.asList(
						Paths.get("C:\\Documents and Settings\\Users\\javko")),
				ArgumentParser.parseToPaths(
						" \"C:\\\\Documents and Settings\\\\Users\\\\javko\" "));

		/* /home/john/info.txt /home/john/backupFolder */
		assertEquals(
				Arrays.asList(
						Paths.get("/home/john/info.txt"),
						Paths.get("/home/john/backupFolder")),
				ArgumentParser.parseToPaths(
						" /home/john/info.txt /home/john/backupFolder "));

		/* "C:/Program Files/Program1/info.txt" C:/tmp/informacije.txt */
		assertEquals(
				Arrays.asList(
						Paths.get("C:/Program Files/Program1/info.txt"),
						Paths.get("C:/tmp/informacije.txt")),
				ArgumentParser.parseToPaths(
						" \"C:/Program Files/Program1/info.txt\"   C:/tmp/informacije.txt "));
	}

	@Test
	void testInvalid() {

		/* "C:\fi le".txt */
		// character directly behind closing quote
		assertThrows(IllegalArgumentException.class,
				() -> ArgumentParser.parseToPaths("\"C:\\\\fi le\".txt"));

		/* "C:\Documents and \" Settings" */
		// illegal character inside path
		assertThrows(IllegalArgumentException.class,
				() -> ArgumentParser.parseToPaths("\"C:\\\\Documents and \\\" Settings\""));
	}

}
