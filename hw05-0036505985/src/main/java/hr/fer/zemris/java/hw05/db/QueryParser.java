package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.java.hw05.db.lexer.Lexer;
import hr.fer.zemris.java.hw05.db.lexer.LexerException;
import hr.fer.zemris.java.hw05.db.lexer.Token;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;

/**
 * Simple parser for <code>query</code> statement in student database.
 * 
 * @author Luka Mesaric
 */
public class QueryParser {

	/**
	 * Instance of lexer used by this parser.
	 */
	private final Lexer lexer;

	/**
	 * List of conditional expressions parsed from given <code>query</code>, that
	 * are all connected by logical operator <code>AND</code>.
	 */
	private final List<ConditionalExpression> conditionalExpressions = new ArrayList<>();

	/**
	 * Assortment of all legal symbolic operators. Ordered by precedence so that
	 * e.g. {@code >} does not shade {@code >=}.
	 */
	private static final String[] legalSymbolicOperators = {
			"!=", ">=", "<=", "=", ">", "<"
	};

	/**
	 * Constant for keyword "AND".
	 */
	private static final String KEYWORD_AND = "AND";

	/**
	 * Constant for keyword "LIKE".
	 */
	private static final String KEYWORD_LIKE = "LIKE";

	/**
	 * Mapping of all attribute names to corresponding field value getters.
	 */
	private static final Map<String, IFieldValueGetter> attributeNames;

	/**
	 * Mapping of all textual representations of comparison operators to their
	 * corresponding implementations.
	 */
	private static final Map<String, IComparisonOperator> comparisonOperators;

	static {
		attributeNames = new HashMap<>();
		attributeNames.put("firstName", FieldValueGetters.FIRST_NAME);
		attributeNames.put("lastName", FieldValueGetters.LAST_NAME);
		attributeNames.put("jmbag", FieldValueGetters.JMBAG);

		comparisonOperators = new HashMap<>();
		comparisonOperators.put(">", ComparisonOperators.GREATER);
		comparisonOperators.put("<", ComparisonOperators.LESS);
		comparisonOperators.put(">=", ComparisonOperators.GREATER_OR_EQUALS);
		comparisonOperators.put("<=", ComparisonOperators.LESS_OR_EQUALS);
		comparisonOperators.put("=", ComparisonOperators.EQUALS);
		comparisonOperators.put("!=", ComparisonOperators.NOT_EQUALS);
		comparisonOperators.put(KEYWORD_LIKE, ComparisonOperators.LIKE);
	}

	/**
	 * Default constructor. Parses entire query as soon as it is instantiated.
	 * 
	 * @param query query to parse
	 * 
	 * @throws NullPointerException if <code>query</code> is <code>null</code>
	 * @throws ParserException      if <code>query</code> could not be parsed, or it
	 *                              is blank
	 */
	public QueryParser(String query) {
		Util.validateNotNull(query, "query");
		if (query.isBlank()) {
			throw new ParserException("Query cannot be blank.");
		}

		this.lexer = new Lexer(query, legalSymbolicOperators);
		parseQueryClean();
	}

	/**
	 * Parses entire query and fills <code>conditionalExpressions</code> list.
	 * 
	 * @throws ParserException if <code>query</code> could not be parsed
	 */
	private void parseQueryClean() {
		try {
			parseQuery();
		} catch (LexerException e) {
			throw new ParserException(e.getMessage(), e);
		}
	}

	/**
	 * Parses entire query and fills <code>conditionalExpressions</code> list.
	 * 
	 * @throws LexerException  if <code>query</code> could not be parsed
	 * @throws ParserException if <code>query</code> could not be parsed
	 */
	private void parseQuery() {
		conditionalExpressions.add(parseOneExpression());

		while (true) {
			Token tokenAnd = lexer.nextToken();
			if (tokenAnd.getType() == TokenType.EOF) {
				break;
			} else if ((tokenAnd.getType() != TokenType.WORD)
					|| !tokenAnd.getValue().toUpperCase().equals(KEYWORD_AND)) {
				throw new ParserException("Expected AND keyword: " + tokenAnd.getValue());
			}
			conditionalExpressions.add(parseOneExpression());
		}
	}

	/**
	 * Parses and returns one conditional expression.
	 * 
	 * @return parsed conditional expression; never <code>null</code>
	 * 
	 * @throws LexerException  if conditional expression could not be correctly
	 *                         parsed
	 * @throws ParserException if conditional expression could not be correctly
	 *                         parsed
	 */
	private ConditionalExpression parseOneExpression() {

		// extract attribute name
		Token tokenAttribute = lexer.nextToken();
		String tokenAttributeValue = tokenAttribute.getValue();
		if (tokenAttribute.getType() != TokenType.WORD) {
			throw new ParserException("Expected an attribute name but got: " + tokenAttributeValue);
		}
		IFieldValueGetter fieldGetter = attributeNames.get(tokenAttributeValue);
		if (fieldGetter == null) {
			throw new ParserException("Illegal attribute name: " + tokenAttributeValue);
		}

		// extract operator
		Token tokenOperator = lexer.nextToken();
		String tokenOperatorValue = tokenOperator.getValue();
		if (tokenOperator.getType() != TokenType.WORD && tokenOperator.getType() != TokenType.OPERATOR) {
			throw new ParserException("Operator expected after attribute name.");
		}
		IComparisonOperator operator = comparisonOperators.get(tokenOperatorValue);
		if (operator == null) {
			throw new ParserException("Illegal operator: " + tokenOperatorValue);
		}

		// extract string literal
		Token tokenLiteral = lexer.nextToken();
		String tokenLiteralValue = tokenLiteral.getValue();
		if (tokenLiteral.getType() != TokenType.STRING) {
			throw new ParserException("String literal expected after operator: " + tokenLiteralValue);
		}
		if (tokenOperatorValue.equals(KEYWORD_LIKE)
				&& (tokenLiteralValue.indexOf('*') != tokenLiteralValue.lastIndexOf('*'))) {
			throw new ParserException("When LIKE operator is used, string literal"
					+ " can contain at most one wildcard '*': " + tokenLiteralValue);
		}

		return new ConditionalExpression(fieldGetter, tokenLiteralValue, operator);
	}

	/**
	 * Checks whether query was a direct one, i.e. if it was structured as
	 * <code>jmbag="0123456789"</code>.
	 * 
	 * @return <code>true</code> if query was direct, <code>false</code> otherwise
	 */
	public boolean isDirectQuery() {
		if (conditionalExpressions.size() != 1) {
			return false;
		}
		ConditionalExpression expression = conditionalExpressions.get(0);
		if (expression.getFieldGetter() != FieldValueGetters.JMBAG) {
			return false;
		} else if (expression.getComparisonOperator() != ComparisonOperators.EQUALS) {
			return false;
		}
		return true;
	}

	/**
	 * Returns string literal which was given in equality comparison in a direct
	 * query.
	 * 
	 * @return queried JMBAG as string literal
	 * 
	 * @throws IllegalStateException if this method is called when parsed query was
	 *                               not direct
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("This method can only be called when query is direct.");
		}
		return conditionalExpressions.get(0).getStringLiteral();
	}

	/**
	 * Returns a list of conditional expressions from <code>query</code>.<br>
	 * For direct queries returned list will have only one element.
	 * 
	 * @return list of conditional expressions from <code>query</code>
	 */
	public List<ConditionalExpression> getQuery() {
		return conditionalExpressions;
	}

}
