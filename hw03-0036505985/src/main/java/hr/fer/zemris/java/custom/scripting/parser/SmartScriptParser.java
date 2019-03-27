package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.List;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Parser for a custom language. Builds document tree out of various nodes and
 * elements.
 * 
 * @author Luka Mesaric
 * 
 * @see SmartScriptLexer
 * @see SmartScriptParserException
 */
public class SmartScriptParser {

	/**
	 * Lexer used by this parser.
	 */
	private final SmartScriptLexer lexer;

	/**
	 * Document node of parsed input.
	 */
	private DocumentNode documentNode = null;

	/**
	 * Stack used for building document tree. Contains only objects of type 'Node'.
	 */
	private ObjectStack stack;

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
		lexer = new SmartScriptLexer(documentBody);

		// keep document node set to null until everything is completed without
		// exception
		documentNode = parseDocumentClean();
	}

	/**
	 * Getter for <code>documentNode</code>.
	 *
	 * @return <code>documentNode</code>; <code>null</code> only when parsing failed
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	/**
	 * Recreates source code from which <code>document</code> was original parsed.
	 * 
	 * @param document root node of document tree
	 * @return source code corresponding to given <code>document</code>
	 * 
	 * @throws NullPointerException if <code>document</code> is <code>null</code>
	 */
	public static String createOriginalDocumentBody(DocumentNode document) {
		Util.validateNotNull(document, "document");
		return document.toString();
	}

	/**
	 * Parses entire document and creates a document tree. Guaranteed to throw only
	 * <code>SmartScriptLexerException</code> if document cannot be parsed.
	 * 
	 * @return document node of parsed document tree
	 * 
	 * @throws SmartScriptParserException if input cannot be correctly parsed for
	 *                                    any reason
	 */
	private DocumentNode parseDocumentClean() {
		try {
			return parseDocument();
		} catch (SmartScriptLexerException lexerEx) {
			// this is an expected exception, it happens when a string is not terminated or
			// escape sequence is not valid etc.
			throw new SmartScriptParserException(lexerEx.getMessage(), lexerEx);
		}
	}

	/**
	 * Parses entire document and creates a document tree.
	 * 
	 * @return document node of parsed document tree
	 * 
	 * @throws SmartScriptParserException if input cannot be correctly parsed for
	 *                                    any reason
	 * @throws SmartScriptLexerException  if any needed token could not be extracted
	 */
	private DocumentNode parseDocument() {
		stack = new ObjectStack();
		stack.push(new DocumentNode());

		parseText();

		if (stack.size() != 1) {
			throw new SmartScriptParserException("Missing {$END$} tags.");
		}
		return (DocumentNode) stack.pop();
	}

	/**
	 * Parses text outside of tags. Continues parsing tags when they are
	 * encountered.
	 * 
	 * @throws SmartScriptParserException if input cannot be correctly parsed for
	 *                                    any reason
	 * @throws SmartScriptLexerException  if any needed token could not be extracted
	 */
	private void parseText() {
		SmartScriptToken token = lexer.nextToken();
		switch (token.getType()) {
		case EOF:
			return;
		case PLAIN_TEXT:
			TextNode textNode = new TextNode((String) token.getValue());
			addNodeToChildrenOfLast(textNode);
			parseText();
			return;
		case OPEN_TAG:
			lexer.setState(SmartScriptLexerState.TAG_NAME);
			parseEntireTag();
			return;
		default:
			// If this executes, lexer needs to be fixed
			throw new SmartScriptParserException("Unexpected token type: " + token.getType());
		}
	}

	/**
	 * Parses entire tag. Adds created node to children of last node on stack.
	 * 
	 * @throws SmartScriptParserException if tag name is incorrect, or tag body is
	 *                                    not correctly formatted (e.g. arguments
	 *                                    are incorrect, tag is never closed,
	 *                                    operator is invalid), or if
	 *                                    <code>END</code> tag does not have a
	 *                                    matching non-empty tag
	 * @throws SmartScriptLexerException  if any needed token could not be extracted
	 */
	private void parseEntireTag() {
		SmartScriptToken token = lexer.nextToken();

		if (token.getType() == SmartScriptTokenType.EOF) {
			throw new SmartScriptParserException("Tag name not given.");
		} else if (token.getType() != SmartScriptTokenType.TAG_NAME) {
			// If this executes, lexer needs to be fixed
			throw new SmartScriptParserException("Unexpected token type: " + token.getType());
		}

		lexer.setState(SmartScriptLexerState.TAG_BODY);

		String tagName = (String) token.getValue();
		switch (tagName.toUpperCase()) {
		case "=":
			EchoNode echoNode = completeEqualsTagBody();
			addNodeToChildrenOfLast(echoNode);
			break;
		case "FOR":
			ForLoopNode forLoopNode = completeForTagBody();
			addNodeToChildrenOfLast(forLoopNode);
			stack.push(forLoopNode);
			break;
		case "END":
			SmartScriptToken closeTag = lexer.nextToken();
			if (closeTag.getType() != SmartScriptTokenType.CLOSE_TAG) {
				throw new SmartScriptParserException("END tag cannot have any content, must be closed instantly.");
			}
			stack.pop();
			if (stack.size() == 0) {
				throw new SmartScriptParserException("There are more {$END$} tags than opened non-empty tags.");
			}
			break;
		default:
			throw new SmartScriptParserException("Invalid tag name: '" + tagName + "'.");
		}

		lexer.setState(SmartScriptLexerState.TEXT);
		parseText();
	}

	/**
	 * Helper method for adding a node to children of the node that is currently on
	 * top of <code>stack</code>. Does <b>not</b> remove anything from
	 * <code>stack</code>.
	 * 
	 * @param node node to add to children
	 * 
	 * @throws NullPointerException if <code>node</code> is <code>null</code>
	 */
	private void addNodeToChildrenOfLast(Node node) {
		Util.validateNotNull(node, "node");
		Node nodeOnStack = (Node) stack.peek();
		nodeOnStack.addChildNode(node);
	}

	/**
	 * Checks if operator is valid.<br>
	 * Valid operators are <code>+</code> (plus), <code>-</code> (minus),
	 * <code>*</code> (multiplication), <code>/</code> (division), <code>^</code>
	 * (power).
	 * 
	 * @param operatorToken token of type {@link SmartScriptTokenType#OPERATOR}
	 * @return constructed <code>ElementOperator</code>
	 * 
	 * @throws NullPointerException       if <code>operatorToken</code> is
	 *                                    <code>null</code>
	 * @throws IllegalArgumentException   if <code>operatorToken</code> type is not
	 *                                    {@link SmartScriptTokenType#OPERATOR}
	 * @throws SmartScriptParserException if operator is not valid
	 */
	private ElementOperator validateOperator(SmartScriptToken operatorToken) {
		Util.validateNotNull(operatorToken, "operatorToken");
		if (operatorToken.getType() != SmartScriptTokenType.OPERATOR) {
			throw new IllegalArgumentException("Token type must be 'OPERATOR'.");
		}
		String operator = (String) operatorToken.getValue();

		if (operator.length() == 1 && "+-*/^".contains(operator)) {
			return new ElementOperator(operator);
		} else {
			throw new SmartScriptParserException("Invalid operator: " + operator);
		}
	}

	/**
	 * Completes the extraction of a =-tag and constructs a <code>EchoNode</code>.
	 * Upon completion, tag body and closing brackets are all skipped.
	 * 
	 * @return correctly constructed <code>EchoNode</code>
	 * 
	 * @throws SmartScriptParserException if tag body is not correctly formatted
	 *                                    (e.g. arguments are incorrect, tag is
	 *                                    never closed, operator is invalid)
	 * @throws SmartScriptLexerException  if any needed token could not be extracted
	 */
	private EchoNode completeEqualsTagBody() {

		// List contains only objects of type 'Element'
		List elementsList = new LinkedListIndexedCollection();

		loop: while (true) {
			SmartScriptToken token = lexer.nextToken();
			switch (token.getType()) {
			case EOF:
				throw new SmartScriptParserException("=-tag was never closed.");
			case VARIABLE:
				elementsList.add(new ElementVariable((String) token.getValue()));
				break;
			case FUNCTION:
				elementsList.add(new ElementFunction((String) token.getValue()));
				break;
			case STRING:
				elementsList.add(new ElementString((String) token.getValue()));
				break;
			case DOUBLE:
				elementsList.add(new ElementConstantDouble((double) token.getValue()));
				break;
			case INTEGER:
				elementsList.add(new ElementConstantInteger((int) token.getValue()));
				break;
			case OPERATOR:
				elementsList.add(validateOperator(token));
				break;
			case CLOSE_TAG:
				break loop;
			default:
				throw new SmartScriptParserException("Illegal token type: " + token.getType());
			}
		}

		int size = elementsList.size();
		if (size == 0) {
			throw new SmartScriptParserException("=-tag cannot have empty body.");
		}

		// Cannot cast directly to Element[]
		Element[] elements = Arrays.copyOf(elementsList.toArray(), size, Element[].class);
		return new EchoNode(elements);
	}

	/**
	 * Completes the extraction of a FOR-tag and constructs a
	 * <code>ForLoopNode</code>. First argument in a FOR-tag must be a variable,
	 * which is then followed by two or three variables, strings, or numbers.<br>
	 * Upon completion, tag body and closing brackets are all skipped.
	 * 
	 * @return correctly constructed <code>ForLoopNode</code>
	 * 
	 * @throws SmartScriptParserException if tag body is not correctly formatted
	 *                                    (e.g. arguments are incorrect or tag is
	 *                                    never closed)
	 * @throws SmartScriptLexerException  if any needed token could not be extracted
	 */
	private ForLoopNode completeForTagBody() {
		ElementVariable firstElement = constructForTagBodyVariable(lexer.nextToken());
		Element secondElement = constructForTagBodyExpression(lexer.nextToken(), "Second");
		Element thirdElement = constructForTagBodyExpression(lexer.nextToken(), "Third");

		SmartScriptToken fourthToken = lexer.nextToken();
		if (fourthToken.getType() == SmartScriptTokenType.CLOSE_TAG) {
			return new ForLoopNode(firstElement, secondElement, thirdElement);
		}

		Element fourthElement = constructForTagBodyExpression(fourthToken, "Fourth");

		SmartScriptToken closeTag = lexer.nextToken();
		if (closeTag.getType() != SmartScriptTokenType.CLOSE_TAG) {
			throw new SmartScriptParserException("FOR-tag must have at most 4 arguments, and then must be closed.");
		}
		return new ForLoopNode(firstElement, secondElement, thirdElement, fourthElement);
	}

	/**
	 * Constructs an <code>ElementVariable</code> from given <code>token</code> that
	 * represents a variable in FOR-tag body.
	 * 
	 * @param token token from which return value is created
	 * @return created <code>ElementVariable</code>
	 * 
	 * @throws NullPointerException       if <code>token</code> is <code>null</code>
	 * @throws SmartScriptParserException if token was not of type
	 *                                    {@link SmartScriptTokenType#VARIABLE}
	 */
	private ElementVariable constructForTagBodyVariable(SmartScriptToken token) {
		Util.validateNotNull(token, "token");

		switch (token.getType()) {
		case EOF:
			throw new SmartScriptParserException("FOR-tag was never closed.");
		case VARIABLE:
			return new ElementVariable((String) token.getValue());
		default:
			throw new SmartScriptParserException("First argument of FOR-tag must be a variable.");
		}
	}

	/**
	 * Constructs an <code>Element</code> from given <code>token</code> that
	 * represents an expression in FOR-tag body.
	 * 
	 * @param token   token from which return value is created
	 * @param ordinal ordinal number of FOR-tag argument, used in custom exception
	 *                message
	 * @return created element, type <code>ElementVariable</code>,
	 *         <code>ElementString</code>, <code>ElementConstantInteger</code> or
	 *         <code>ElementConstantDouble</code>
	 * 
	 * @throws NullPointerException       if any argument is <code>null</code>
	 * @throws SmartScriptParserException if token was not of type
	 *                                    {@link SmartScriptTokenType#VARIABLE},
	 *                                    {@link SmartScriptTokenType#STRING},
	 *                                    {@link SmartScriptTokenType#INTEGER} or
	 *                                    {@link SmartScriptTokenType#DOUBLE}
	 */
	private Element constructForTagBodyExpression(SmartScriptToken token, String ordinal) {
		Util.validateNotNull(token, "token");
		Util.validateNotNull(ordinal, "ordinal");

		switch (token.getType()) {
		case EOF:
			throw new SmartScriptParserException("FOR-tag was never closed.");
		case VARIABLE:
			return new ElementVariable((String) token.getValue());
		case STRING:
			return new ElementString((String) token.getValue());
		case INTEGER:
			return new ElementConstantInteger((int) token.getValue());
		case DOUBLE:
			return new ElementConstantDouble((double) token.getValue());
		default:
			throw new SmartScriptParserException(
					ordinal + " argument of FOR-tag must be a variable, string, integer or double.");
		}
	}

}
