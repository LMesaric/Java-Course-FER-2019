package hr.fer.zemris.java.hw06.shell.util;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.ExceptionUtil;

/**
 * Utility class for parsing command arguments, namely file and directory paths.
 * 
 * @author Luka Mesaric
 */
public final class ArgumentParser {

	/** Current index. */
	private int index = 0;

	/** Data which is parsed. */
	private final char[] data;

	/** List of all parsed paths. */
	private final List<String> parsedStrings = new ArrayList<>();

	/**
	 * Parses given argument into a list of <code>Paths</code>. Escaping quotation
	 * marks and backslashes inside strings is allowed.<br>
	 * After the ending quotation mark, either no more characters must be present or
	 * at least one space character must be present.
	 * 
	 * @param  paths                    paths to be parsed
	 * @return                          list of parsed <code>Paths</code>, empty
	 *                                  list if input is blank
	 * @throws NullPointerException     if <code>paths</code> is <code>null</code>
	 * @throws IllegalArgumentException if paths cannot be parsed for any reason
	 *                                  (e.g. string is never terminated, or any
	 *                                  parsed path cannot be converted to a
	 *                                  <code>Path</code> object)
	 */
	public static List<Path> parseToPaths(String paths) {
		ExceptionUtil.validateNotNull(paths, "argument");
		try {
			return parseToStrings(paths).stream()
					.map(Paths::get)
					.collect(Collectors.toList());
		} catch (InvalidPathException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Parses given argument into a list of <code>Strings</code>. Escaping quotation
	 * marks and backslashes inside strings is allowed.<br>
	 * After the ending quotation mark, either no more characters must be present or
	 * at least one space character must be present.
	 * 
	 * @param  paths                    paths to be parsed
	 * @return                          list of parsed <code>Strings</code>, empty
	 *                                  list if input is blank
	 * @throws NullPointerException     if <code>paths</code> is <code>null</code>
	 * @throws IllegalArgumentException if paths cannot be parsed for any reason
	 *                                  (e.g. string is never terminated)
	 */
	public static List<String> parseToStrings(String paths) {
		ExceptionUtil.validateNotNull(paths, "argument");
		String strippedArguments = paths.strip();
		if (strippedArguments.isEmpty()) {
			return Collections.emptyList();
		}
		char[] chars = strippedArguments.toCharArray();
		return new ArgumentParser(chars).parse();
	}

	/**
	 * Default (private) constructor.
	 * 
	 * @param  data                 data for parsing, cleared of any leading or
	 *                              trailing spaces
	 * @throws NullPointerException if <code>data</code> is <code>null</code>
	 */
	private ArgumentParser(char[] data) {
		ExceptionUtil.validateNotNull(data, "data");
		this.data = data;
	}

	/**
	 * Parses <code>data</code> to a list of <code>Strings</code>.
	 * 
	 * @return                          list of parsed <code>Strings</code>
	 * @throws IllegalArgumentException if data cannot be parsed for any reason
	 *                                  (e.g. string is never terminated)
	 */
	private List<String> parse() {
		while (index < data.length) {
			String pathString;
			if (data[index] == '"') {
				pathString = parseString();
				if (index < data.length && !Character.isWhitespace(data[index])) {
					throw new IllegalArgumentException("After the ending quotation mark,"
							+ " either no more characters must be present, or at least"
							+ " one space character must be present.");
				}
			} else {
				pathString = parseRegular();
			}
			parsedStrings.add(pathString);
			skipWhitespace();
		}
		return parsedStrings;
	}

	/**
	 * Extracts and returns a string. Increases <code>index</code> until it no
	 * longer points to a part of the string. Must be called only while pointing to
	 * a <code>'"'</code> character.<br>
	 * Escaping quotation marks and backslashes is allowed.
	 * 
	 * @return                          extracted string, never <code>null</code>
	 * @throws IllegalArgumentException if string cannot be extracted (e.g. string
	 *                                  is never terminated)
	 */
	private String parseString() {
		index++;  // skip opening quotation mark
		StringBuilder sb = new StringBuilder();
		while (index < data.length) {
			if (data[index] == '\\') {
				if (index + 1 >= data.length) {
					// string is never terminated, throw exception
					break;
				} else if (data[index + 1] == '"' || data[index + 1] == '\\') {
					sb.append(data[++index]);
					++index;
				} else {
					sb.append(data[index++]);
					sb.append(data[index++]);
				}
			} else if (data[index] == '"') {
				index++; // skip closing quotation mark
				return sb.toString();
			} else {
				sb.append(data[index++]);
			}
		}
		throw new IllegalArgumentException("String was never terminated.");
	}

	/**
	 * Extracts and returns a path. Increases <code>index</code> until it no longer
	 * points any data, or it points to a whitespace. Must be called only while
	 * pointing to a non-whitespace character.
	 * 
	 * @return extracted path, never <code>null</code>
	 */
	private String parseRegular() {
		int startIndex = index;
		while (index < data.length) {
			char c = data[index];
			if (c == '"' || Character.isWhitespace(c)) {
				break;
			}
			index++;
		}
		return new String(data, startIndex, index - startIndex);
	}

	/**
	 * Skips all whitespace characters by increasing <code>currentIndex</code>.
	 */
	private void skipWhitespace() {
		while (index < data.length) {
			if (!Character.isWhitespace(data[index])) {
				break;
			}
			index++;
		}
	}

}
