package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class QueryParserTest {
	
	@Test
	void testNullArgument() {
		assertThrows(NullPointerException.class, () -> new QueryParser(null));
	}

	@Test
	void testDirectQuery() {
		QueryParser qp = new QueryParser(" jmbag       =\"0123456789\"    ");
		assertTrue(qp.isDirectQuery());
		assertEquals("0123456789", qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(1, query.size());

		assertEquals("0123456789", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.EQUALS, query.get(0).getComparisonOperator());
	}

	@Test
	void testSimpleNonDirectQuery() {
		QueryParser qp = new QueryParser("  lastName  =    \"Blažić\"    ");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(1, query.size());

		assertEquals("Blažić", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.LAST_NAME, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.EQUALS, query.get(0).getComparisonOperator());
	}

	@Test
	void testSimpleCompositeQuery() {
		QueryParser qp = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(2, query.size());

		assertEquals("0123456789", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.EQUALS, query.get(0).getComparisonOperator());

		assertEquals("J", query.get(1).getStringLiteral());
		assertEquals(FieldValueGetters.LAST_NAME, query.get(1).getFieldGetter());
		assertEquals(ComparisonOperators.GREATER, query.get(1).getComparisonOperator());
	}

	@Test
	void testCompositeQueryWithLike() {
		QueryParser qp = new QueryParser("firstName>=\"A\" and lastName LIKE \"B*ć\"");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(2, query.size());

		assertEquals("A", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.FIRST_NAME, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.GREATER_OR_EQUALS, query.get(0).getComparisonOperator());

		assertEquals("B*ć", query.get(1).getStringLiteral());
		assertEquals(FieldValueGetters.LAST_NAME, query.get(1).getFieldGetter());
		assertEquals(ComparisonOperators.LIKE, query.get(1).getComparisonOperator());
	}

	@Test
	void testCompositeQueryWithLikeComplex() {
		QueryParser qp = new QueryParser(
				"firstName<=\"3\"   and  firstName!= \"C\" AND lastName LIKE\"B*ć\"AnD jmbag>\"0000000002\"	");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(4, query.size());

		assertEquals("3", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.FIRST_NAME, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.LESS_OR_EQUALS, query.get(0).getComparisonOperator());

		assertEquals("C", query.get(1).getStringLiteral());
		assertEquals(FieldValueGetters.FIRST_NAME, query.get(1).getFieldGetter());
		assertEquals(ComparisonOperators.NOT_EQUALS, query.get(1).getComparisonOperator());

		assertEquals("B*ć", query.get(2).getStringLiteral());
		assertEquals(FieldValueGetters.LAST_NAME, query.get(2).getFieldGetter());
		assertEquals(ComparisonOperators.LIKE, query.get(2).getComparisonOperator());

		assertEquals("0000000002", query.get(3).getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, query.get(3).getFieldGetter());
		assertEquals(ComparisonOperators.GREATER, query.get(3).getComparisonOperator());
	}

	@Test
	void testSimpleQueryBadlyFormatted() {
		assertThrows(ParserException.class, () -> new QueryParser("firstName=lastName"));
		assertThrows(ParserException.class, () -> new QueryParser("\"Ante\"=firstName"));
	}

	@Test
	void testLikeWildcardMiddle() {
		QueryParser qp = new QueryParser(" jmbag LIKE \"aa*bb\" ");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(1, query.size());

		assertEquals("aa*bb", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.LIKE, query.get(0).getComparisonOperator());
	}

	@Test
	void testLikeWildcardLeft() {
		QueryParser qp = new QueryParser(" jmbag LIKE \"*bb\" ");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(1, query.size());

		assertEquals("*bb", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.LIKE, query.get(0).getComparisonOperator());
	}

	@Test
	void testLikeWildcardRight() {
		QueryParser qp = new QueryParser(" jmbag LIKE \"aa*\" ");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(1, query.size());

		assertEquals("aa*", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.LIKE, query.get(0).getComparisonOperator());
	}

	@Test
	void testLikeWildcardMultiple() {
		assertThrows(ParserException.class, () -> new QueryParser(" jmbag LIKE \"aa*bb*cc\" "));
	}
	
	@Test
	void testWildcardInNonLike() {
		QueryParser qp = new QueryParser("  lastName  >=    \"Bla*i*\"    ");
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());

		List<ConditionalExpression> query = qp.getQuery();
		assertEquals(1, query.size());

		assertEquals("Bla*i*", query.get(0).getStringLiteral());
		assertEquals(FieldValueGetters.LAST_NAME, query.get(0).getFieldGetter());
		assertEquals(ComparisonOperators.GREATER_OR_EQUALS, query.get(0).getComparisonOperator());
	}
	
	@Test
	void testNeverEndingString() {
		assertThrows(ParserException.class, () -> new QueryParser(" jmbag = \"0123456789 "));
	}
	
	@Test
	void testMissingStringLiteral() {
		assertThrows(ParserException.class, () -> new QueryParser(" lastName >=  "));
	}
	
	@Test
	void testMissingOperator() {
		assertThrows(ParserException.class, () -> new QueryParser("jmbag  \"0123456789\" "));
	}
	
	@Test
	void testTrailingAnd() {
		assertThrows(ParserException.class, () -> new QueryParser("jmbag=\"0123456789\" and "));
	}

}
