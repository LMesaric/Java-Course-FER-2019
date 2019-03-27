package hr.fer.zemris.java.custom.scripting.parser;

import static hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser.createOriginalDocumentBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;

/**
 * @author Luka Mesaric
 */
class SmartScriptParserTest {

	/**
	 * Helper method for reading text from a file whose name is given.
	 * 
	 * @param filename name of file to read text from
	 * @return text from file; <code>null</code> if text could not be read
	 */
	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * Helper method for parsing document body, recreating source code using the
	 * constructed document tree, then parsing that source code and finally
	 * comparing resulting two document trees.
	 * 
	 * @param docBody        document body for parsing
	 * @param directChildren expected number of direct children of DocumentNode,
	 *                       <code>-1</code> to be ignored
	 * 
	 * @throws SmartScriptParserException if document body cannot be parsed
	 */
	private void parseThenReconstructAndParseAgain(int directChildren, String docBody) {
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();

		if (directChildren >= 0) {
			assertEquals(directChildren, document.numberOfChildren());
			assertEquals(directChildren, document2.numberOfChildren());
		}
		assertEquals(document, document2);
	}

	/**
	 * Number of direct children is ignored.
	 * 
	 * @see SmartScriptParserTest#parseThenReconstructAndParseAgain(String, int)
	 */
	private void parseThenReconstructAndParseAgain(String docBody) {
		parseThenReconstructAndParseAgain(-1, docBody);
	}

	/*
	 * 
	 * --- TESTS ---
	 * 
	 */

	@Test
	void testNullInput() {
		assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
	}

	@Test
	void testEmptyInput() {
		parseThenReconstructAndParseAgain(0, "");
	}

	@Test
	void testIllegalOperatorPeriod() {
		// {$= 1.2.3$}
		SmartScriptParserException e = assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain(1, "{$= 1.2.3$}"));
		assertEquals("Invalid operator: .", e.getMessage());
	}

	@Test
	void testIllegalDoubleWithoutDecimalPart() {
		// {$= 1. i$}
		SmartScriptParserException e = assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain(1, "{$= 1. i$}"));
		assertEquals("Double value cannot end with a decimal point.", e.getMessage());
	}

	@Test
	void testIllegalTagName() {
		// {$IF 1 2$}
		assertThrows(SmartScriptParserException.class, () -> parseThenReconstructAndParseAgain(1, "{$IF 1 2$}"));
	}

	@Test
	void testSingleBackslashInsideString() {
		// {$= "\\" $}
		parseThenReconstructAndParseAgain(1, "{$= \"\\\\\" $}");
	}

	@Test
	void testSingleQuoteInsideString() {
		// {$= "\"" $}
		parseThenReconstructAndParseAgain(1, "{$= \"\\\"\" $}");
	}

	@Test
	void testSingleNewlineInsideString() {
		// {$= "\n" $}
		parseThenReconstructAndParseAgain(1, "{$= \"\\n\" $}");
	}

	@Test
	void testSingleTabInsideString() {
		// {$= "\t" $}
		parseThenReconstructAndParseAgain(1, "{$= \"\\t\" $}");
	}

	@Test
	void testIllegalEscapeInsideString() {
		// {$= "\{" $}
		assertThrows(SmartScriptParserException.class, () -> parseThenReconstructAndParseAgain(1, "{$= \"\\{\" $}"));
	}

	@Test
	void testIllegalEscapeOutsideTags() {
		// Text \$ a
		assertThrows(SmartScriptParserException.class, () -> parseThenReconstructAndParseAgain(1, "Text \\$ a"));
	}

	@Test
	void testEscapingQuotesInsideString() {
		// A tag follows {$= "Joe \"Long\" Smith"$}.
		parseThenReconstructAndParseAgain(3, "A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}.");
	}

	@Test
	void testEscapingOutsideTagsFirst() {
		// Example { bla } blu \{$=1$}. Nothing interesting {=here}.
		parseThenReconstructAndParseAgain(1, "Example { bla } blu \\{$=1$}. Nothing interesting {=here}.");
	}

	@Test
	void testEscapingOutsideTagsSecond() {
		// Example \{$=1$}. Now actually write one {$=1$}
		parseThenReconstructAndParseAgain(2, "Example \\{$=1$}. Now actually write one {$=1$}");
	}

	@Test
	void testParsingRegularEqualsTag() {
		// {$= i i * @sin "0.000" @decfmt $}
		parseThenReconstructAndParseAgain(1, "{$= i i * @sin  \"0.000\" @decfmt $}");
	}

	@Test
	void testIllegalVariableName() {
		// {$FOR _ab 1 2$}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$FOR _ab 1 2$} {$END$}"));
	}

	@Test
	void testTagNeverClosed() {
		// {$FOR ab 1 2$}
		// END is missing a curly brace
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$FOR ab 1 2$} {$END$"));
	}

	@Test
	void testParsingRegularForTagOnlyNumbers() {
		// {$ FOR i -1 10 1 $}
		parseThenReconstructAndParseAgain(1, "{$ FOR i -1 10 1 $} {$END$}");
	}

	@Test
	void testParsingRegularForTagOnlyNumbersMissingEndTag() {
		// {$ FOR i -1 10 1 $}
		assertThrows(SmartScriptParserException.class, () -> parseThenReconstructAndParseAgain("{$ FOR i -1 10 1 $}"));
	}

	@Test
	void testParsingRegularForTagOnlyNumbersTooManyEndTags() {
		// {$ FOR i -1 10 1 $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR i -1 10 1 $} {$END$} {$END$}"));
	}

	@Test
	void testParsingRegularForTagNumbersAndStrings() {
		// {$ FOR sco_re "-1"10 "1" $}
		parseThenReconstructAndParseAgain(1, "{$    FOR    sco_re            \"-1\"10 \"1\" $} {$END$}");
	}

	@Test
	void testParsingRegularForTagNumberAndVariable() {
		// {$ FOR year 1 last_year $}
		parseThenReconstructAndParseAgain(1, "{$ FOR year 1 last_year $} {$END$}");
	}

	@Test
	void testParsingRegularForTagElementsConcatenated() {
		// {$ FOR i-1.35bbb"1" $}
		parseThenReconstructAndParseAgain(1, "{$ FOR i-1.35bbb\"1\" $} {$END$}");
	}

	@Test
	void testParsingBadForTagNumberInsteadOfVariable() {
		// {$ FOR 3 1 10 1 $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR 3 1 10 1 $} {$END$}"));
	}

	@Test
	void testParsingBadForTagNumberOperatorOfVariable() {
		// {$ FOR * "1" -10 "1" $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR * \"1\" -10 \"1\" $} {$END$}"));
	}

	@Test
	void testParsingBadForTagFunctionInsteadOfElement() {
		// {$ FOR year @sin 10 $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR year @sin 10 $} {$END$}"));
	}

	@Test
	void testParsingBadForTagTooFewArgumentsFirst() {
		// {$ FOR year $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR year $} {$END$}"));
	}

	@Test
	void testParsingBadForTagTooFewArgumentsSecond() {
		// {$ FOR year 3 $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR year 3 $} {$END$}"));
	}

	@Test
	void testParsingBadForTagTooManyArgumentsFirst() {
		// {$ FOR year 1 10 1 3 $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR year 1 10 1 3 $} {$END$}"));
	}

	@Test
	void testParsingBadForTagTooManyArgumentsSecond() {
		// {$ FOR year 1 10 "1" "10" $}
		assertThrows(SmartScriptParserException.class,
				() -> parseThenReconstructAndParseAgain("{$ FOR year 1 10 \"1\" \"10\" $} {$END$}"));
	}

}
