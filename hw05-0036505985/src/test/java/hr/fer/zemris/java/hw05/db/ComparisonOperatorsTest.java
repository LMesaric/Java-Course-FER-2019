package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class ComparisonOperatorsTest {

	@Test
	void testLess() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertFalse(oper.satisfied("Ana", "Ana"));
	}

	@Test
	void testGreater() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertFalse(oper.satisfied("Ana", "Ana"));
	}

	@Test
	void testEquals() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
	}

	@Test
	void testLessOrEquals() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
	}

	@Test
	void testGreaterOrEquals() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
	}

	@Test
	void testNotEquals() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertFalse(oper.satisfied("Ana", "Ana"));
	}

	@Test
	void testLike() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));

		assertFalse(oper.satisfied("Zagreb", "Aba*"));

		assertFalse(oper.satisfied("AAA", "AA*AA"));
		assertTrue(oper.satisfied("AAAA", "AA*AA"));

		assertTrue(oper.satisfied("AAAA", "*"));

		assertTrue(oper.satisfied("AAAA", "*A"));
		assertTrue(oper.satisfied("AAAA", "*AA"));
		assertTrue(oper.satisfied("AAAA", "*AAA"));
		assertTrue(oper.satisfied("AAAA", "*AAAA"));
		assertFalse(oper.satisfied("AAAA", "*AAAAA"));

		assertTrue(oper.satisfied("AAAA", "A*"));
		assertTrue(oper.satisfied("AAAA", "AA*"));
		assertTrue(oper.satisfied("AAAA", "AAA*"));
		assertTrue(oper.satisfied("AAAA", "AAAA*"));
		assertFalse(oper.satisfied("AAAA", "AAAAA*"));
	}

}
