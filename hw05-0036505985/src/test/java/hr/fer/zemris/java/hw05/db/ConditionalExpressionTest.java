package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class ConditionalExpressionTest {

	@Test
	void testConstructorAndGetters() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);

		assertEquals(FieldValueGetters.LAST_NAME, expr.getFieldGetter());
		assertEquals("Bos*", expr.getStringLiteral());
		assertEquals(ComparisonOperators.LIKE, expr.getComparisonOperator());
	}

	@Test
	void testConstructorNullArguments() {
		assertThrows(NullPointerException.class,
				() -> new ConditionalExpression(
						null, "Bos*", ComparisonOperators.LIKE));

		assertThrows(NullPointerException.class,
				() -> new ConditionalExpression(
						FieldValueGetters.LAST_NAME, null, ComparisonOperators.LIKE));

		assertThrows(NullPointerException.class,
				() -> new ConditionalExpression(
						FieldValueGetters.LAST_NAME, "Bos*", null));
	}

	@Test
	void testSatisfies() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);
		StudentRecord record1 = new StudentRecord("0000000050", "Sikirica", "Alen", 3);
		StudentRecord record2 = new StudentRecord("0000000003", "Bosnić", "Andrea", 4);

		boolean record1Satisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record1),
				expr.getStringLiteral());
		assertFalse(record1Satisfies);
		assertFalse(expr.satisfies(record1));

		boolean record2Satisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record2),
				expr.getStringLiteral());
		assertTrue(record2Satisfies);
		assertTrue(expr.satisfies(record2));

		assertThrows(NullPointerException.class, () -> expr.satisfies(null));
	}

	@Test
	void testEquals() {
		assertEquals(
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER),
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER));
		assertNotEquals(
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER),
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "Am", ComparisonOperators.GREATER));
		assertNotEquals(
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER),
				new ConditionalExpression(
						FieldValueGetters.LAST_NAME, "An", ComparisonOperators.GREATER));
	}

	@Test
	void testHashCode() {
		assertEquals(
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER).hashCode(),
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER).hashCode());
		assertNotEquals(
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER).hashCode(),
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "Am", ComparisonOperators.GREATER).hashCode());
		assertNotEquals(
				new ConditionalExpression(
						FieldValueGetters.FIRST_NAME, "An", ComparisonOperators.GREATER).hashCode(),
				new ConditionalExpression(
						FieldValueGetters.LAST_NAME, "An", ComparisonOperators.GREATER).hashCode());
	}

}
