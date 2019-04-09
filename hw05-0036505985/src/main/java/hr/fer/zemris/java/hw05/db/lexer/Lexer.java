package hr.fer.zemris.java.hw05.db.lexer;

import hr.fer.zemris.java.custom.collections.Util;

/**
 * Simple lexer for student database queries.
 * 
 * @author Luka Mesaric
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
	 * Current token (last generated). <code>null</code> if no token was ever
	 * generated.
	 */
	private Token token = null;

	/**
	 * Contains all legal symbolic operators for this lexer. Ordered by precedence.
	 */
	private final String[] legalSymbolicOperators;

	/**
	 * Constant for a quotation mark character.
	 */
	private static final char QUOTATION_MARK = '"';

	/**
	 * Default constructor. Lexer starts tokenizing from the first character.
	 * 
	 * @param text                   text that will be tokenized
	 * @param legalSymbolicOperators all legal symbolic operators this lexer can
	 *                               extract; must be ordered by precedence
	 * 
	 * @throws NullPointerException if <code>text</code> or
	 *                              <code>legalSymbolicOperators</code> is
	 *                              <code>null</code>
	 */
	public Lexer(String text, String[] legalSymbolicOperators) {
		this.data = Util.validateNotNull(text, "text").toCharArray();
		this.legalSymbolicOperators = Util.validateNotNull(
				legalSymbolicOperators, "legalSymbolicOperators");
	}

	/**
	 * Returns last generated token. It can be called as many times as wanted since
	 * it does not trigger generation of next token.
	 *
	 * @return last generated token, never <code>null</code>
	 * 
	 * @throws IllegalStateException if this method is called before calling
	 *                               {@link #nextToken()}
	 */
	public Token getToken() {
		if (token == null) {
			throw new IllegalStateException("Cannot get token without" +
					" first extracting it by using 'nextToken()'.");
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
		skipWhitespace();

		if (!isCurrentPositionLegal()) {
			return token = eofOrException();
		}

		if (Character.isLetter(data[currentIndex])) {
			return token = new Token(TokenType.WORD, extractWord());
		} else if (data[currentIndex] == QUOTATION_MARK) {
			return token = new Token(TokenType.STRING, extractString());
		} else {
			return token = new Token(TokenType.OPERATOR, extractSymbolicOperator());
		}
	}

	/**
	 * Returns {@link TokenType#EOF} token. If current token is already
	 * {@link TokenType#EOF}, an exception is thrown.
	 * 
	 * @return {@link TokenType#EOF} token
	 * 
	 * @throws LexerException if <code>token</code> is already {@link TokenType#EOF}
	 */
	private Token eofOrException() {
		if (token == null || token.getType() != TokenType.EOF) {
			return new Token(TokenType.EOF, null);
		} else {
			throw new LexerException("Cannot get next token after EOF.");
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
	 * Skips all whitespace characters by increasing <code>currentIndex</code>.
	 */
	private void skipWhitespace() {
		while (isCurrentPositionLegal()) {
			if (Character.isWhitespace(data[currentIndex])) {
				currentIndex++;
			} else {
				return;
			}
		}
	}

	/**
	 * Extracts and returns a single word consisting of only letters. Increases
	 * <code>currentIndex</code> until it no longer points to a character that can
	 * be a part of a word, or all data is used up.
	 * 
	 * @return extracted word, never <code>null</code> nor empty String
	 * 
	 * @throws LexerException if word cannot be extracted for any reason
	 */
	private String extractWord() {
		if (!isCurrentPositionLegal()) {
			throw new LexerException("Not pointing to any data.");
		} else if (!Character.isLetter(data[currentIndex])) {
			throw new LexerException("Word must start with a letter but it started with '"
					+ data[currentIndex] + "'.");
		}

		int startIndex = currentIndex++;
		while (isCurrentPositionLegal() && Character.isLetter(data[currentIndex])) {
			currentIndex++;
		}
		return new String(data, startIndex, currentIndex - startIndex);
	}

	/**
	 * Extracts and returns a string. Increases <code>currentIndex</code> until it
	 * no longer points to a part of the string (skips closing quotation mark).<br>
	 * Must be called only when pointing to a <code>'"'</code> character.
	 * 
	 * @return extracted string, never <code>null</code>
	 * 
	 * @throws LexerException if string cannot be extracted (e.g. string is never
	 *                        terminated)
	 */
	private String extractString() {
		if (!isCurrentPositionLegal()) {
			throw new LexerException("Not pointing to any data.");
		} else if (data[currentIndex] != QUOTATION_MARK) {
			throw new LexerException("String must start with '\"' but it started with '"
					+ data[currentIndex] + "'.");
		}

		int startIndex = ++currentIndex; // skip opening quotation mark
		while (isCurrentPositionLegal() && data[currentIndex] != QUOTATION_MARK) {
			currentIndex++;
		}
		if (!isCurrentPositionLegal()) {
			throw new LexerException("String was never terminated.");
		}
		currentIndex++; // skip closing quotation mark
		return new String(data, startIndex, currentIndex - startIndex - 1);
	}

	/**
	 * Extracts and returns an operator from <code>legalSymbolicOperators</code>.
	 * Increases <code>currentIndex</code> until it no longer points to that
	 * operator.
	 * 
	 * @return extracted operator as string, never <code>null</code>
	 * 
	 * @throws LexerException if operator cannot be extracted
	 */
	private String extractSymbolicOperator() {
		if (!isCurrentPositionLegal()) {
			throw new LexerException("Not pointing to any data.");
		}
		for (String operator : legalSymbolicOperators) {
			if (isAtCurrentIndex(operator.toCharArray())) {
				currentIndex += operator.length();
				return operator;
			}
		}
		throw new LexerException("Expected an operator, but none was recognized: "
				+ data[currentIndex]);
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
	private boolean isAtCurrentIndex(char[] chars) {
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
