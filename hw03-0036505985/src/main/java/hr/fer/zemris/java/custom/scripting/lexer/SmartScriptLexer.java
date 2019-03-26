package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Lexer (tokenizer) for a custom language.
 * 
 * @author Luka Mesaric
 * 
 * @see SmartScriptToken
 * @see SmartScriptTokenType
 * @see SmartScriptLexerState
 * @see SmartScriptLexerException
 */
public class SmartScriptLexer {

	/**
	 * Input data used for tokenization.
	 */
	private final char[] data;

	/**
	 * Index of first unused character in <code>data</code>.
	 */
	private int currentIndex = 0;

	/**
	 * Current token (last generated).
	 */
	private SmartScriptToken token;

	/**
	 * Current state.
	 */
	private SmartScriptLexerState state = SmartScriptLexerState.TEXT;

	/**
	 * Characters used to open a tag.
	 */
	public static final char[] TAG_OPENING_SEQUENCE = { '{', '$' };

	/**
	 * Characters used to close a tag.
	 */
	public static final char[] TAG_CLOSING_SEQUENCE = { '$', '}' };

	/**
	 * Constant for a backslash character.
	 */
	private static final char BACKSLASH = '\\';

	/**
	 * Constant for a minus character.
	 */
	private static final char MINUS = '-';

	/**
	 * Default constructor. Lexer starts tokenizing from the first character. By
	 * default, state is set to {@link SmartScriptLexerState#TEXT}.
	 * 
	 * @param text text that will be tokenized
	 * 
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 */
	public SmartScriptLexer(String text) {
		data = Util.validateNotNull(text, "text").toCharArray();
		token = null;
	}

	/**
	 * Changes lexer state. That reflects the way input is grouped into tokens.
	 * 
	 * @param state new state
	 * 
	 * @throws NullPointerException if <code>state</code> is <code>null</code>
	 */
	public void setState(SmartScriptLexerState state) {
		this.state = Util.validateNotNull(state, "state");
	}

	/**
	 * Returns last generated token. It can be called as many times as wanted since
	 * it does not trigger generation of next token.
	 *
	 * @return last generated token, never <code>null</code>
	 * 
	 * @throws SmartScriptLexerException if this method is called before calling
	 *                                   {@link #nextToken()}
	 */
	public SmartScriptToken getToken() {
		if (token == null) {
			throw new SmartScriptLexerException("Cannot get token without first extracting it using 'nextToken()'.");
		}
		return token;
	}

	/**
	 * Extracts and returns next token. After the first thrown
	 * <code>SmartScriptLexerException</code>, behaviour is undefined.
	 * 
	 * @return extracted token; never <code>null</code>
	 * 
	 * @throws SmartScriptLexerException if next token cannot be extracted from
	 *                                   <code>text</code>
	 */
	public SmartScriptToken nextToken() {
		if (!isCurrentPositionLegal()) {
			token = eofOrException();
			return token;
		}

		switch (state) {
		case TEXT:
			extractTokenAsText();
			break;
		case TAG_NAME:
			extractTokenAsTagName();
			break;
		case TAG_BODY:
			extractTokenAsTagBody();
			break;
		default:
			throw new SmartScriptLexerException("Lexer is in invalid state: " + state);
		}

		return getToken();
	}

	/**
	 * Returns EOF token. If current token is already EOF, an exception is thrown.
	 * 
	 * @return EOF token
	 * 
	 * @throws SmartScriptLexerException if <code>token</code> is EOF
	 */
	private SmartScriptToken eofOrException() {
		if (token == null || token.getType() != SmartScriptTokenType.EOF) {
			return new SmartScriptToken(SmartScriptTokenType.EOF, null);
		} else {
			throw new SmartScriptLexerException("Cannot get next token after EOF.");
		}
	}

	/**
	 * Extracts next token by rules that apply in state
	 * {@link SmartScriptLexerState#TEXT}.
	 * 
	 * @throws SmartScriptLexerException if next token cannot be extracted from
	 *                                   <code>text</code>
	 */
	private void extractTokenAsText() {

		if (!isCurrentPositionLegal()) {
			token = eofOrException();
			return;
		}

		if (isAtCurrentIndexOpenTag()) {
			String s = new String(TAG_OPENING_SEQUENCE);
			currentIndex += TAG_OPENING_SEQUENCE.length;
			token = new SmartScriptToken(SmartScriptTokenType.OPEN_TAG, s);
			return;
		}

		StringBuilder sb = new StringBuilder();

		while (isCurrentPositionLegal()) {
			if (data[currentIndex] == BACKSLASH) {
				if (isAtCurrentIndex(BACKSLASH, BACKSLASH)) {
					sb.append(BACKSLASH);
				} else if (isAtCurrentIndex(BACKSLASH, '{')) {
					sb.append('{');
				} else if (currentIndex + 1 >= data.length) {
					throw new SmartScriptLexerException("Started escape sequence did not end.");
				} else {
					throw new SmartScriptLexerException("Cannot escape character '" + data[currentIndex + 1] + "'.");
				}
				currentIndex += 2;
			} else if (!isAtCurrentIndexOpenTag()) {
				sb.append(data[currentIndex++]);
			} else {
				break;
			}
		}

		if (sb.length() == 0) {
			throw new SmartScriptLexerException("Could not extract token from position '" + currentIndex + "'.");
		}
		token = new SmartScriptToken(SmartScriptTokenType.PLAIN_TEXT, sb.toString());
	}

	/**
	 * Extracts next token by rules that apply in state
	 * {@link SmartScriptLexerState#TAG_NAME}.
	 * 
	 * @throws SmartScriptLexerException if next token cannot be extracted from
	 *                                   <code>text</code>
	 */
	private void extractTokenAsTagName() {
		skipWhitespace();

		if (!isCurrentPositionLegal()) {
			token = eofOrException();
			return;
		}

		if (isAtCurrentIndex('=')) {
			currentIndex++;
			token = new SmartScriptToken(SmartScriptTokenType.TAG_NAME, "=");
			return;
		}

		String name = extractName();
		token = new SmartScriptToken(SmartScriptTokenType.TAG_NAME, name);
		return;
	}

	/**
	 * Extracts next token by rules that apply in state
	 * {@link SmartScriptLexerState#TAG_BODY}.
	 * 
	 * @throws SmartScriptLexerException if next token cannot be extracted from
	 *                                   <code>text</code>
	 */
	private void extractTokenAsTagBody() {
		skipWhitespace();

		if (!isCurrentPositionLegal()) {
			token = eofOrException();
			return;
		}

		if (isAtCurrentIndexCloseTag()) {
			String s = new String(TAG_CLOSING_SEQUENCE);
			currentIndex += TAG_CLOSING_SEQUENCE.length;
			token = new SmartScriptToken(SmartScriptTokenType.CLOSE_TAG, s);
		} else if (isAtCurrentIndex('@')) {
			currentIndex++;
			String fun = extractName();
			token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, fun);
		} else if (isAtCurrentIndex('"')) {
			String s = extractString();
			token = new SmartScriptToken(SmartScriptTokenType.STRING, s);
		} else if (isAtCurrentIndex(MINUS)) {
			token = extractMinusOrNumberToken();
		} else if (Character.isDigit(data[currentIndex])) {
			token = extractNumberToken();
		} else if (Character.isLetter(data[currentIndex])) {
			String variable = extractName();
			token = new SmartScriptToken(SmartScriptTokenType.VARIABLE, variable);
		} else {
			String op = String.valueOf(data[currentIndex++]);
			token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, op);
		}
	}

	/**
	 * Extracts and returns a single word representing a valid name. Increases
	 * <code>currentIndex</code> until it no longer points to a character that can
	 * be a part of a word, or all data is used up.
	 * <p>
	 * Valid names start with a letter that is followed by zero or more letters,
	 * numbers and underscores.
	 * </p>
	 * 
	 * @return extracted name, never <code>null</code> nor empty String
	 * 
	 * @throws SmartScriptLexerException if name cannot be extracted
	 */
	private String extractName() {
		if (!isCurrentPositionLegal() || !Character.isLetter(data[currentIndex])) {
			throw new SmartScriptLexerException("First letter of name is not valid: " + data[currentIndex]);
		}

		int start = currentIndex++;
		while (isCurrentPositionLegal()) {
			if (Character.isLetterOrDigit(data[currentIndex]) || data[currentIndex] == '_') {
				currentIndex++;
			} else {
				break;
			}
		}
		return new String(data, start, currentIndex - start);
	}

	/**
	 * Extracts and returns a string. Increases <code>currentIndex</code> until it
	 * no longer points to a part of the string. Must be called only while in state
	 * {@link SmartScriptLexerState#TAG_BODY} and while pointing to a
	 * <code>'"'</code> character.
	 * 
	 * @return extracted string, never <code>null</code>
	 * 
	 * @throws SmartScriptLexerException if string cannot be extracted (e.g. string
	 *                                   is never terminated)
	 */
	private String extractString() {
		if (!isCurrentPositionLegal()) {
			throw new SmartScriptLexerException("Not pointing to any data.");
		} else if (data[currentIndex] != '"') {
			throw new SmartScriptLexerException(
					"String must start with '\"' but it started with '" + data[currentIndex] + "'.");
		}

		currentIndex++; // skip opening quotation mark
		StringBuilder sb = new StringBuilder();
		while (isCurrentPositionLegal()) {
			if (data[currentIndex] == BACKSLASH) {
				if (isAtCurrentIndex(BACKSLASH, BACKSLASH)) {
					sb.append(BACKSLASH);
				} else if (isAtCurrentIndex(BACKSLASH, '"')) {
					sb.append('"');
				} else if (isAtCurrentIndex(BACKSLASH, 'n')) {
					sb.append('\n');
				} else if (isAtCurrentIndex(BACKSLASH, 'r')) {
					sb.append('\r');
				} else if (isAtCurrentIndex(BACKSLASH, 't')) {
					sb.append('\t');
				} else if (currentIndex + 1 >= data.length) {
					throw new SmartScriptLexerException("Started escape sequence did not end.");
				} else {
					throw new SmartScriptLexerException("Cannot escape character '" + data[currentIndex + 1] + "'.");
				}
				currentIndex += 2;
			} else if (data[currentIndex] == '"') {
				currentIndex++;
				return sb.toString();
			} else {
				sb.append(data[currentIndex++]);
			}
		}
		throw new SmartScriptLexerException("String was never terminated.");
	}

	/**
	 * Extracts and returns a token for a number value, or for minus operator.
	 * 
	 * @return extracted token
	 * 
	 * @throws SmartScriptLexerException if token cannot be extracted
	 */
	private SmartScriptToken extractMinusOrNumberToken() {
		if (isAtCurrentIndex(MINUS)) {
			if (currentIndex + 1 >= data.length || !Character.isDigit(data[currentIndex + 1])) {
				currentIndex++;
				return new SmartScriptToken(SmartScriptTokenType.OPERATOR, String.valueOf(MINUS));
			} else {
				return extractNumberToken();
			}
		} else {
			return extractNumberToken();
		}
	}

	/**
	 * Extracts and returns a token for a number value.
	 * 
	 * @return extracted token
	 * 
	 * @throws SmartScriptLexerException if number (double or integer) cannot be
	 *                                   extracted
	 */
	private SmartScriptToken extractNumberToken() {
		int start = currentIndex;
		if (data[currentIndex] == MINUS) {
			currentIndex++;
		}
		if (skipDigits() <= 0) {
			throw new SmartScriptLexerException("Could not extract a number from position " + start + " .");
		}
		if (isAtCurrentIndex('.')) {
			currentIndex++;
			if (skipDigits() <= 0) {
				throw new SmartScriptLexerException("Double value cannot end with a decimal point.");
			}
			String d = new String(data, start, currentIndex - start);
			try {
				double num = Double.parseDouble(d);
				return new SmartScriptToken(SmartScriptTokenType.DOUBLE, num);
			} catch (NumberFormatException e) {
				throw new SmartScriptLexerException("Could not extract 'double' from position " + start + " .", e);
			}
		} else {
			String d = new String(data, start, currentIndex - start);
			try {
				int num = Integer.parseInt(d);
				return new SmartScriptToken(SmartScriptTokenType.INTEGER, num);
			} catch (NumberFormatException e) {
				throw new SmartScriptLexerException("Could not extract 'int' from position " + start + " .", e);
			}
		}
	}

	/**
	 * Skips all digits by increasing <code>currentIndex</code>.
	 * 
	 * @return number of digits that were skipped; <code>0</code> if not a single
	 *         digit was skipped
	 */
	private int skipDigits() {
		int before = currentIndex;
		while (isCurrentPositionLegal() && Character.isDigit(data[currentIndex])) {
			currentIndex++;
		}
		return currentIndex - before;
	}

	/**
	 * Skips all whitespace characters by increasing <code>currentIndex</code>.
	 */
	private void skipWhitespace() {
		while (isCurrentPositionLegal()) {
			char ch = data[currentIndex];
			if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
				currentIndex++;
			} else {
				return;
			}
		}
	}

	/**
	 * Tests if <code>currentIndex</code> is less than <code>data.length</code>.
	 * 
	 * @return <code>true</code> if <code>currentIndex</code> is less than
	 *         <code>data.length</code>, <code>false</code> otherwise
	 */
	private boolean isCurrentPositionLegal() {
		return currentIndex < data.length;
	}

	/**
	 * Tests if <code>currentIndex</code> points to tag opening. Does not change
	 * <code>currentIndex</code>.
	 * 
	 * @return <code>true</code> if <code>currentIndex</code> points to tag opening,
	 *         <code>false</code> otherwise
	 */
	private boolean isAtCurrentIndexOpenTag() {
		return isAtCurrentIndex(TAG_OPENING_SEQUENCE);
	}

	/**
	 * Tests if <code>currentIndex</code> points to tag closing. Does not change
	 * <code>currentIndex</code>.
	 * 
	 * @return <code>true</code> if <code>currentIndex</code> points to tag closing,
	 *         <code>false</code> otherwise
	 */
	private boolean isAtCurrentIndexCloseTag() {
		return isAtCurrentIndex(TAG_CLOSING_SEQUENCE);
	}

	/**
	 * Tests if <code>data</code> has exactly the same content as
	 * <code>chars</code>, starting from <code>currentIndex</code>. Does not change
	 * <code>currentIndex</code>.
	 * 
	 * @param chars chars to compare
	 * @return <code>true</code> if they are equal, <code>false</code> otherwise
	 * 
	 * @throws NullPointerException if <code>chars</code> is <code>null</code>
	 */
	private boolean isAtCurrentIndex(char... chars) {
		Util.validateNotNull(chars, "chars");
		int charsIndex = 0;
		for (int dataIndex = currentIndex; dataIndex < data.length
				&& charsIndex < chars.length; charsIndex++, dataIndex++) {
			if (data[dataIndex] != chars[charsIndex]) {
				return false;
			}
		}
		if (charsIndex < chars.length) {
			// not all input was tested - ran out of 'data' to compare with
			return false;
		}
		return true;
	}

}
