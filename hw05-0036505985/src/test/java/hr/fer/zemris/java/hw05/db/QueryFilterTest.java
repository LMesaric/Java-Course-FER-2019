package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class QueryFilterTest {

	@Test
	void testNullArgument() {
		assertThrows(NullPointerException.class, () -> new QueryFilter(null));
	}

	@Test
	void testAcceptsOne() {
		List<ConditionalExpression> expressions = new LinkedList<>();
		expressions.add(new ConditionalExpression(
				FieldValueGetters.LAST_NAME, "C", ComparisonOperators.GREATER));

		QueryFilter qf = new QueryFilter(expressions);
		assertTrue(qf.accepts(new StudentRecord("123", "Car", "Ana", 3)));
		assertTrue(qf.accepts(new StudentRecord("123", "Zan", "Ana", 3)));
		assertFalse(qf.accepts(new StudentRecord("123", "Ban", "Ana", 3)));
		assertFalse(qf.accepts(new StudentRecord("123", "Anic", "Ana", 3)));
	}

	@Test
	void testAcceptsTwo() {
		List<ConditionalExpression> expressions = new LinkedList<>();
		expressions.add(new ConditionalExpression(
				FieldValueGetters.LAST_NAME, "C", ComparisonOperators.GREATER));
		expressions.add(new ConditionalExpression(
				FieldValueGetters.FIRST_NAME, "Jan", ComparisonOperators.LESS_OR_EQUALS));

		QueryFilter qf = new QueryFilter(expressions);

		assertTrue(qf.accepts(new StudentRecord("123", "Car", "Ana", 3)));
		assertTrue(qf.accepts(new StudentRecord("123", "Zen", "Ana", 3)));
		assertFalse(qf.accepts(new StudentRecord("123", "Ban", "Ana", 3)));
		assertFalse(qf.accepts(new StudentRecord("123", "Anic", "Ana", 3)));

		assertFalse(qf.accepts(new StudentRecord("123", "Zen", "Jana", 3)));
		assertFalse(qf.accepts(new StudentRecord("123", "Car", "Zana", 3)));
		assertTrue(qf.accepts(new StudentRecord("123", "Zen", "Hana", 3)));
	}

}
