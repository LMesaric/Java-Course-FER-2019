package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.ExceptionUtil;

/**
 * Parser for strings that allow referencing to groups and padding.
 * 
 * @author Luka Mesaric
 */
public class NameBuilderParser {

	/** Constant representing character <code>0</code>. */
	private static final char ZERO = '0';

	/** Constant representing a single space character. */
	private static final char SPACE = ' ';

	/** Constructed composite name builder. */
	private NameBuilder nameBuilder = null;

	/** Data to parse. */
	private final char[] data;

	/** Current index. */
	private int index = 0;

	/**
	 * Default constructor.
	 * 
	 * @param  expression               expression to parse
	 * @throws IllegalArgumentException if <code>expression</code> cannot be parsed
	 *                                  for any reason
	 * @throws NullPointerException     if <code>expression</code> is
	 *                                  <code>null</code>
	 */
	public NameBuilderParser(String expression) {
		ExceptionUtil.validateNotNull(expression, "expression");
		data = expression.toCharArray();

		while (index < data.length) {
			if (isCurrentOpenTag()) {
				parseTag();
			} else {
				parsePlainText();
			}
		}
	}

	/**
	 * Getter for <code>nameBuilder</code> constructed by parsing input.
	 *
	 * @return <code>nameBuilder</code>
	 */
	public NameBuilder getNameBuilder() { return nameBuilder; }

	/**
	 * Changes stored <code>nameBuilder</code> to a composite of current
	 * <code>nameBuilder</code> and <code>another</code>. If
	 * <code>nameBuilder</code> is <code>null</code>, it is directly replaced with
	 * <code>another</code>.
	 * 
	 * @param  another              the operation to execute after executing
	 *                              <code>nameBuilder</code>
	 * @throws NullPointerException if <code>another</code> is <code>null</code>
	 */
	private void appendBuiler(NameBuilder another) {
		ExceptionUtil.validateNotNull(another, "another");
		if (nameBuilder == null) {
			nameBuilder = another;
		} else {
			nameBuilder = nameBuilder.then(another);
		}
	}

	/**
	 * Parses plain text. Increases <code>index</code> until it no longer points any
	 * data, or it points to a dollar sign followed by an open curly brace
	 * (<code>${</code>). Does not skip <code>${</code>.
	 */
	private void parsePlainText() {
		int startIndex = index;
		while (index < data.length) {
			if (isCurrentOpenTag()) {
				break;
			}
			index++;
		}
		String extracted = new String(data, startIndex, index - startIndex);
		appendBuiler(NameBuilder.text(extracted));
	}

	/**
	 * Parses a tag (substitution command). Increases <code>index</code> until it no
	 * longer points to a part of the tag. Must be called only while pointing to a
	 * <code>${</code>.
	 * 
	 * @throws IllegalArgumentException if tag cannot be parsed for any reason (e.g.
	 *                                  it is never closed, or numbers cannot be
	 *                                  parsed)
	 */
	private void parseTag() {
		index += 2;  // skip "${"

		int startIndex = index;
		while (index < data.length) {
			if (data[index] == '}') {
				break;
			}
			index++;
		}

		if (index >= data.length) {
			throw new IllegalArgumentException("Tag is never closed.");
		}
		index++;	// skip "}"

		String tagBody = new String(data, startIndex, index - startIndex - 1);
		tagBody = tagBody.strip();
		if (tagBody.isEmpty()) {
			throw new IllegalArgumentException("Tag body cannot be blank.");
		}
		if (tagBody.charAt(tagBody.length() - 1) == ',') {
			// special case when splitting will give bad results
			throw new IllegalArgumentException("Tag body cannot end with a comma.");
		}

		String[] parts = tagBody.split(",");
		if (parts.length > 2) {
			throw new IllegalArgumentException("Tag body can have at most one comma.");
		}

		int groupNumber = parseNonNegativeInteger(parts[0].strip());

		if (parts.length == 1) {
			appendBuiler(NameBuilder.group(groupNumber));
			return;
		}

		String widthModifier = parts[1].strip();
		char padding = SPACE;
		if (widthModifier.length() > 1 && widthModifier.charAt(0) == ZERO) {
			padding = ZERO;
			widthModifier = widthModifier.substring(1);
		}

		int minWidth = parseNonNegativeInteger(widthModifier);
		appendBuiler(NameBuilder.group(groupNumber, padding, minWidth));
	}

	/**
	 * Parses an integer. Throws customized exceptions.
	 * 
	 * @param  number                   number to parse
	 * @return                          parsed number
	 * @throws IllegalArgumentException if <code>number</code> cannot be parsed to
	 *                                  an integer, or parsed integer is negative
	 */
	private int parseNonNegativeInteger(String number) {
		if (!number.matches("[0-9]+")) {
			throw new IllegalArgumentException("Expected only digits: " + number);
		}
		int parsed;
		try {
			parsed = Integer.parseInt(number);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Expected a number: " + number, e);
		}
		// Parsed number will always be 0 or greater because the regex matched.
		return parsed;
	}

	/**
	 * Returns <code>true</code> if <code>index</code> points to <code>${</code>
	 * sequence.
	 * 
	 * @return true if <code>index</code> points to an open tag
	 */
	private boolean isCurrentOpenTag() {
		return index + 1 < data.length
				&& data[index] == '$'
				&& data[index + 1] == '{';
	}

}
