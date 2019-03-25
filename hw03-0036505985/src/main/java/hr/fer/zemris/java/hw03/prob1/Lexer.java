package hr.fer.zemris.java.hw03.prob1;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Lexer (tokenizer) for any input consisting of words, numbers, and symbols.
 * Blanks are skipped.
 * <p>
 * Numbers must be of type <code>long</code>. Escaping is allowed only for
 * digits (<code>\1</code>) and backslashes (<code>\\</code>) while in
 * {@link LexerState#BASIC} state.
 * </p>
 * 
 * @author Luka Mesaric
 * 
 * @see Token
 * @see TokenType
 * @see LexerState
 * @see LexerException
 */
public class Lexer {

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
	private Token token;

	/**
	 * Current state.
	 */
	private LexerState state = LexerState.BASIC;

	/**
	 * Constant representing the character used as delimiter between different lexer
	 * states.
	 */
	public static final char STATE_CHANGE_DELIMITER = '#';

	/**
	 * Default constructor. Lexer starts tokenizing from the first character. By
	 * default, state is set to {@link LexerState#BASIC}.
	 * 
	 * @param text text that will be tokenized
	 * 
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 */
	public Lexer(String text) {
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
	public void setState(LexerState state) {
		this.state = Util.validateNotNull(state, "state");
	}

	/**
	 * Returns last generated token. It can be called as many times as wanted since
	 * it does not trigger generation of next token.
	 *
	 * @return last generated token, never <code>null</code>
	 * 
	 * @throws LexerException if this method is called before calling
	 *                        {@link #nextToken()}
	 */
	public Token getToken() {
		if (token == null) {
			throw new LexerException("Cannot get token without first extracting it using 'nextToken()'.");
		}
		return token;
	}

	/**
	 * Extracts and returns next token. After the first thrown
	 * <code>LexerException</code>, behaviour is undefined.
	 * 
	 * @return extracted token; never <code>null</code>
	 * 
	 * @throws LexerException if next token cannot be extracted from
	 *                        <code>text</code>
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Cannot get next token after EOF.");
		}

		skipWhitespace();

		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return token;
		}

		switch (state) {
		case BASIC:
			return extractTokenAsBasic();
		case EXTENDED:
			return extractTokenAsExtended();
		default:
			throw new LexerException("Lexer is in invalid state: " + state);
		}
	}

	/**
	 * Extracts and returns next token by rules that apply in state
	 * {@link LexerState#BASIC}.
	 * 
	 * @return extracted token
	 * 
	 * @throws LexerException if next token cannot be extracted from
	 *                        <code>text</code>
	 */
	private Token extractTokenAsBasic() {
		if (Character.isDigit(data[currentIndex])) {
			Long number = extractLong();
			token = new Token(TokenType.NUMBER, number);
		} else if (data[currentIndex] == '\\' || Character.isLetter(data[currentIndex])) {
			String word = extractWord();
			token = new Token(TokenType.WORD, word);
		} else {
			token = new Token(TokenType.SYMBOL, data[currentIndex++]);
		}

		return token;
	}

	/**
	 * Extracts and returns next token by rules that apply in state
	 * {@link LexerState#EXTENDED}.
	 * 
	 * @return extracted token
	 * 
	 * @throws LexerException if next token cannot be extracted from
	 *                        <code>text</code>
	 */
	private Token extractTokenAsExtended() {
		if (data[currentIndex] == Lexer.STATE_CHANGE_DELIMITER) {
			token = new Token(TokenType.SYMBOL, data[currentIndex++]);
			return token;
		}

		int start = currentIndex;
		while ((currentIndex < data.length) && data[currentIndex] != Lexer.STATE_CHANGE_DELIMITER
				&& !Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
		if (currentIndex == start) {
			throw new LexerException("Could not extract next token from position " + start + " .");
		}
		String word = new String(data, start, currentIndex - start);
		token = new Token(TokenType.WORD, word);
		return token;
	}

	/**
	 * Skips all whitespace characters by increasing <code>currentIndex</code>.
	 */
	private void skipWhitespace() {
		while (currentIndex < data.length) {
			char ch = data[currentIndex];
			if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
				currentIndex++;
			} else {
				return;
			}
		}
	}

	/**
	 * Extracts and returns a single word. Only backslashes and numbers can be
	 * escaped in words. Increases <code>currentIndex</code> until it no longer
	 * points to a character, or all data is used up.
	 * 
	 * @return extracted word
	 * 
	 * @throws LexerException if nothing could be extracted, or escape sequence was
	 *                        invalid
	 */
	private String extractWord() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length) {
			char ch = data[currentIndex];
			if (ch == '\\') {
				currentIndex++;
				if (currentIndex >= data.length) {
					throw new LexerException("Started escape sequence did not end.");
				}
				char escapedChar = data[currentIndex];
				if (escapedChar == '\\' || Character.isDigit(escapedChar)) {
					sb.append(escapedChar);
					currentIndex++;
				} else {
					throw new LexerException("Cannot escape character '" + escapedChar + "'.");
				}
			} else if (Character.isLetter(ch)) {
				sb.append(ch);
				currentIndex++;
			} else {
				break;
			}
		}

		if (sb.length() == 0) {
			throw new LexerException("Could not extract a word.");
		}
		return sb.toString();
	}

	/**
	 * Extracts and returns number of type <code>long</code>. Increases
	 * <code>currentIndex</code> until it no longer points to a digit, or all data
	 * is used up.
	 * 
	 * @return extracted <code>long</code>
	 * 
	 * @throws LexerException if <code>long</code> could not be extracted
	 */
	private Long extractLong() {
		int start = currentIndex;
		while ((currentIndex < data.length) && Character.isDigit(data[currentIndex])) {
			currentIndex++;
		}
		String s = new String(data, start, currentIndex - start);
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			// Number could be too large to fit into 'long', or 's' was empty, etc.
			throw new LexerException("Could not extract 'long' from position " + start + " .", e);
		}
	}

}
