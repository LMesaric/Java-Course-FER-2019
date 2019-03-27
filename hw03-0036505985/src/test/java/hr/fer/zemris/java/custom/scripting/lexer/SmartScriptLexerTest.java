package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class SmartScriptLexerTest {

	/**
	 * Helper method for checking if lexer generates the same stream of tokens as
	 * the given stream.
	 * 
	 * @param lexer
	 * @param correctData
	 */
	private void checkTokenStream(SmartScriptLexer lexer, SmartScriptToken[] correctData) {
		int counter = 0;
		for (SmartScriptToken expected : correctData) {
			SmartScriptToken actual = lexer.nextToken();
			String msg = "Checking token " + counter + ":";
			assertEquals(expected, actual, msg);
			counter++;
		}
	}

	@Test
	void testSetNullState() {
		assertThrows(NullPointerException.class, () -> new SmartScriptLexer("").setState(null));
	}

	/*
	 * 
	 * STATE: SmartScriptLexerState.TEXT
	 * 
	 */

	@Test
	void testNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		assertNotNull(lexer.nextToken());
	}

	@Test
	void testNullInput() {
		assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
	}

	@Test
	void testEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	void testGetReturnsLastNext() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken());
		assertEquals(token, lexer.getToken());
	}

	@Test
	void testWorkAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		lexer.nextToken(); // EOF
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testPlainTextContent() {
		String s = "   \r\n  \t    ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		assertEquals(new SmartScriptToken(SmartScriptTokenType.PLAIN_TEXT, s), lexer.nextToken());
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	void testVariousInput() {
		String s = " Mahalo Hawaii {$ a";
		SmartScriptLexer lexer = new SmartScriptLexer(s);

		SmartScriptToken correctData[] = { new SmartScriptToken(SmartScriptTokenType.PLAIN_TEXT, " Mahalo Hawaii "),
				new SmartScriptToken(SmartScriptTokenType.OPEN_TAG, "{$") };

		checkTokenStream(lexer, correctData);
	}

	@Test
	void testVariousInputEscaped() {
		String s = "Example { bla } blu \\{$=1$}. Nothing interesting {=here}. ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);

		SmartScriptToken correctData[] = {
				new SmartScriptToken(SmartScriptTokenType.PLAIN_TEXT,
						"Example { bla } blu {$=1$}. Nothing interesting {=here}. "),
				new SmartScriptToken(SmartScriptTokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	void testVariousInputEscapedWithOpenTag() {
		String s = "Example \\{$=1$}. Now actually \\\\ write one {$=1$} ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);

		SmartScriptToken correctData[] = {
				new SmartScriptToken(SmartScriptTokenType.PLAIN_TEXT, "Example {$=1$}. Now actually \\ write one "),
				new SmartScriptToken(SmartScriptTokenType.OPEN_TAG, "{$") };

		checkTokenStream(lexer, correctData);
	}

	@Test
	void testIncorrectEscapeSequence() {
		String s = "Example \\$=1$}.";
		SmartScriptLexer lexer = new SmartScriptLexer(s);

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testNeverEndingEscapeSequence() {
		String s = "Example $=1$}.\\";
		SmartScriptLexer lexer = new SmartScriptLexer(s);

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	/*
	 * 
	 * STATE: SmartScriptLexerState.TAG_NAME
	 * 
	 */

	@Test
	void testTagNameNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_NAME);
		assertNotNull(lexer.nextToken());
	}

	@Test
	void testTagNameEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_NAME);
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	void testTagNameGetReturnsLastNext() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_NAME);

		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken());
		assertEquals(token, lexer.getToken());
	}

	@Test
	void testTagNameWorkAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_NAME);

		lexer.nextToken(); // EOF
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testTagNameNoRealContent() {
		String s = "   \r\n  \t    ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_NAME);
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	void testTagNameEqualsSign() {
		String s = "   \r\n  \t  =  ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_NAME);

		SmartScriptToken correctData[] = { new SmartScriptToken(SmartScriptTokenType.TAG_NAME, "="),
				new SmartScriptToken(SmartScriptTokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	void testTagNameRegularName() {
		String s = "   \r\n  \t  Pero42__3  ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_NAME);

		SmartScriptToken correctData[] = { new SmartScriptToken(SmartScriptTokenType.TAG_NAME, "Pero42__3"),
				new SmartScriptToken(SmartScriptTokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	void testTagNameInvalidNameNumber() {
		String s = "   \r\n  \t  4Pero42__3  ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_NAME);

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testTagNameInvalidNameUnderscore() {
		String s = "   \r\n  \t  _Pero42__3  ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_NAME);

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	/*
	 * 
	 * STATE: SmartScriptLexerState.TAG_BODY
	 * 
	 */

	@Test
	void testTagBodyNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_BODY);
		assertNotNull(lexer.nextToken());
	}

	@Test
	void testTagBodyEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_BODY);
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	void testTagBodyGetReturnsLastNext() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_BODY);

		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken());
		assertEquals(token, lexer.getToken());
	}

	@Test
	void testTagBodyWorkAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG_BODY);

		lexer.nextToken(); // EOF
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testTagBodyNoRealContent() {
		String s = "   \r\n  \t    ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_BODY);
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	void testTagBodyStringWithEscapes() {
		String s = "  \" 4\\\"2 \\n \\\\ \"";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_BODY);
		assertEquals(new SmartScriptToken(SmartScriptTokenType.STRING, " 4\"2 \n \\ "), lexer.nextToken());
	}

	@Test
	void testTagBodyNeverEndingString() {
		String s = "  \"abc ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_BODY);
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testTagBodyIncorrectEscapeSequence() {
		String s = "  \"a\\bc\" ";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_BODY);
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testTagBodyVariousInput() {
		String s = "{$= i i * @sin  \"0.000\" @decfmt $}";
		SmartScriptLexer lexer = new SmartScriptLexer(s);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.OPEN_TAG, "{$"), lexer.nextToken());
		lexer.setState(SmartScriptLexerState.TAG_NAME);
		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_NAME, "="), lexer.nextToken());
		lexer.setState(SmartScriptLexerState.TAG_BODY);

		SmartScriptToken correctData[] = { new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.OPERATOR, "*"),
				new SmartScriptToken(SmartScriptTokenType.FUNCTION, "sin"),
				new SmartScriptToken(SmartScriptTokenType.STRING, "0.000"),
				new SmartScriptToken(SmartScriptTokenType.FUNCTION, "decfmt"),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_TAG, "$}") };

		checkTokenStream(lexer, correctData);
	}

	@Test
	void testTagBodyVariousInputWithoutWhitespace() {
		String s = " i-1.35bbb\"1\"@sin $}";
		SmartScriptLexer lexer = new SmartScriptLexer(s);
		lexer.setState(SmartScriptLexerState.TAG_BODY);

		SmartScriptToken correctData[] = { new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.DOUBLE, -1.35),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "bbb"),
				new SmartScriptToken(SmartScriptTokenType.STRING, "1"),
				new SmartScriptToken(SmartScriptTokenType.FUNCTION, "sin"),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_TAG, "$}"),
				new SmartScriptToken(SmartScriptTokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

}
