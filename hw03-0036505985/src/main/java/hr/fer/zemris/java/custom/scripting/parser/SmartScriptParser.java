package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;

/**
 * FIXME
 * 
 * @author Luka Mesaric
 */
public class SmartScriptParser {

	/**
	 * Lexer used by this parser.
	 */
	private final SmartScriptLexer lexer;

	/**
	 * Document node of parsed input.
	 */
	private DocumentNode documentNode;

	/**
	 * Default constructor. Parses entire input as soon as it is instantiated.
	 * 
	 * @throws NullPointerException       if <code>documentBody</code> is
	 *                                    <code>null</code>
	 * @throws SmartScriptParserException if <code>documentBody</code> cannot be
	 *                                    correctly parsed for any reason
	 */
	public SmartScriptParser(String documentBody) {
		Util.validateNotNull(documentBody, "documentBody");
		this.lexer = new SmartScriptLexer(documentBody);
	}

	/**
	 * Getter for <code>documentNode</code>.
	 *
	 * @return <code>documentNode</code>; never <code>null</code> except when
	 *         parsing failed
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}

}
