package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class ValueWrapperTest {

	@Test
	void testAddingNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());

		assertEquals(Integer.valueOf(0), v1.getValue());
		assertNull(v2.getValue());
	}

	@Test
	void testSubtractingNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.subtract(v2.getValue());

		assertEquals(Integer.valueOf(0), v1.getValue());
		assertNull(v2.getValue());
	}

	@Test
	void testMultiplyingNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.multiply(v2.getValue());

		assertEquals(Integer.valueOf(0), v1.getValue());
		assertNull(v2.getValue());
	}

	@Test
	void testDividingNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		assertThrows(ArithmeticException.class, () -> v1.divide(v2.getValue()));

		assertNull(v1.getValue());
		assertNull(v2.getValue());
	}

	@Test
	void testComparingNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		int res = v1.numCompare(v2.getValue());

		assertEquals(0, res);
		assertNull(v1.getValue());
		assertNull(v2.getValue());
	}

	@Test
	void test() {
		// TODO
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue());

		assertEquals(Double.valueOf(13), v3.getValue());
		assertEquals(Integer.valueOf(1), v4.getValue());

		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.add(v6.getValue());

		assertEquals(Integer.valueOf(13), v5.getValue());
		assertEquals(Integer.valueOf(1), v6.getValue());
	}

	@Test
	void testIntegerAndInteger() {
		ValueWrapper v0 = new ValueWrapper(Integer.valueOf(-2));

		ValueWrapper v1 = new ValueWrapper(Integer.valueOf(3));
		v1.add(v0.getValue());
		assertEquals(Integer.valueOf(1), v1.getValue());

		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));
		v2.subtract(v0.getValue());
		assertEquals(Integer.valueOf(5), v2.getValue());

		ValueWrapper v3 = new ValueWrapper(Integer.valueOf(3));
		v3.multiply(v0.getValue());
		assertEquals(Integer.valueOf(-6), v3.getValue());

		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(3));
		v4.divide(v0.getValue());  // integer division !!
		assertEquals(Integer.valueOf(-1), v4.getValue());

		assertEquals(Integer.valueOf(-2), v0.getValue());

		ValueWrapper v5 = new ValueWrapper(Integer.valueOf(3));
		assertTrue(v5.numCompare(v0.getValue()) > 0);
		assertTrue(v0.numCompare(v5.getValue()) < 0);
		assertTrue(v0.numCompare(v0.getValue()) == 0);
	}

	@Test
	void testDoubleAndDouble() {
		ValueWrapper v0 = new ValueWrapper(Double.valueOf(-2));

		ValueWrapper v1 = new ValueWrapper(Double.valueOf(3.5));
		v1.add(v0.getValue());
		assertEquals(Double.valueOf(1.5), v1.getValue());

		ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.5));
		v2.subtract(v0.getValue());
		assertEquals(Double.valueOf(5.5), v2.getValue());

		ValueWrapper v3 = new ValueWrapper(Double.valueOf(3.5));
		v3.multiply(v0.getValue());
		assertEquals(Double.valueOf(-7), v3.getValue());

		ValueWrapper v4 = new ValueWrapper(Double.valueOf(3.5));
		v4.divide(v0.getValue());
		assertEquals(Double.valueOf(-1.75), v4.getValue());

		assertEquals(Double.valueOf(-2), v0.getValue());

		ValueWrapper v5 = new ValueWrapper(Double.valueOf(3));
		assertTrue(v5.numCompare(v0.getValue()) > 0);
		assertTrue(v0.numCompare(v5.getValue()) < 0);
		assertTrue(v0.numCompare(v0.getValue()) == 0);
	}

	@Test
	void testIntegerAndDouble() {
		ValueWrapper v0 = new ValueWrapper(Integer.valueOf(-2));

		ValueWrapper v1 = new ValueWrapper(Double.valueOf(3.5));
		v1.add(v0.getValue());
		assertEquals(Double.valueOf(1.5), v1.getValue());

		ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.5));
		v2.subtract(v0.getValue());
		assertEquals(Double.valueOf(5.5), v2.getValue());

		ValueWrapper v3 = new ValueWrapper(Double.valueOf(3.5));
		v3.multiply(v0.getValue());
		assertEquals(Double.valueOf(-7), v3.getValue());

		ValueWrapper v4 = new ValueWrapper(Double.valueOf(3.5));
		v4.divide(v0.getValue());
		assertEquals(Double.valueOf(-1.75), v4.getValue());

		assertEquals(Integer.valueOf(-2), v0.getValue());

		ValueWrapper v5 = new ValueWrapper(Double.valueOf(3));
		assertTrue(v5.numCompare(v0.getValue()) > 0);
		assertTrue(v0.numCompare(v5.getValue()) < 0);
		assertTrue(v0.numCompare(v0.getValue()) == 0);
	}

	@Test
	void testIntegerAndStringInteger() {
		ValueWrapper v0 = new ValueWrapper("-2");

		ValueWrapper v1 = new ValueWrapper(Integer.valueOf(3));
		v1.add(v0.getValue());
		assertEquals(Integer.valueOf(1), v1.getValue());

		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));
		v2.subtract(v0.getValue());
		assertEquals(Integer.valueOf(5), v2.getValue());

		ValueWrapper v3 = new ValueWrapper(Integer.valueOf(3));
		v3.multiply(v0.getValue());
		assertEquals(Integer.valueOf(-6), v3.getValue());

		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(3));
		v4.divide(v0.getValue());  // integer division !!
		assertEquals(Integer.valueOf(-1), v4.getValue());

		assertEquals("-2", v0.getValue());

		ValueWrapper v5 = new ValueWrapper(Integer.valueOf(3));
		assertTrue(v5.numCompare(v0.getValue()) > 0);
		assertTrue(v0.numCompare(v5.getValue()) < 0);
		assertTrue(v0.numCompare(v0.getValue()) == 0);
	}

	@Test
	void testDoubleAndStringInteger() {
		ValueWrapper v0 = new ValueWrapper("-2");

		ValueWrapper v1 = new ValueWrapper(Double.valueOf(3.5));
		v1.add(v0.getValue());
		assertEquals(Double.valueOf(1.5), v1.getValue());

		ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.5));
		v2.subtract(v0.getValue());
		assertEquals(Double.valueOf(5.5), v2.getValue());

		ValueWrapper v3 = new ValueWrapper(Double.valueOf(3.5));
		v3.multiply(v0.getValue());
		assertEquals(Double.valueOf(-7), v3.getValue());

		ValueWrapper v4 = new ValueWrapper(Double.valueOf(3.5));
		v4.divide(v0.getValue());
		assertEquals(Double.valueOf(-1.75), v4.getValue());

		assertEquals("-2", v0.getValue());

		ValueWrapper v5 = new ValueWrapper(Double.valueOf(3));
		assertTrue(v5.numCompare(v0.getValue()) > 0);
		assertTrue(v0.numCompare(v5.getValue()) < 0);
		assertTrue(v0.numCompare(v0.getValue()) == 0);
	}

	@Test
	void testDoubleAndStringDouble() {
		ValueWrapper v0 = new ValueWrapper("-2.0");

		ValueWrapper v1 = new ValueWrapper(Double.valueOf(3.5));
		v1.add(v0.getValue());
		assertEquals(Double.valueOf(1.5), v1.getValue());

		ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.5));
		v2.subtract(v0.getValue());
		assertEquals(Double.valueOf(5.5), v2.getValue());

		ValueWrapper v3 = new ValueWrapper(Double.valueOf(3.5));
		v3.multiply(v0.getValue());
		assertEquals(Double.valueOf(-7), v3.getValue());

		ValueWrapper v4 = new ValueWrapper(Double.valueOf(3.5));
		v4.divide(v0.getValue());
		assertEquals(Double.valueOf(-1.75), v4.getValue());

		assertEquals("-2.0", v0.getValue());

		ValueWrapper v5 = new ValueWrapper(Double.valueOf(3));
		assertTrue(v5.numCompare(v0.getValue()) > 0);
		assertTrue(v0.numCompare(v5.getValue()) < 0);
		assertTrue(v0.numCompare(v0.getValue()) == 0);
	}

	@Test
	void testStrings() {
		ValueWrapper v1 = new ValueWrapper("-2.0");
		ValueWrapper v2 = new ValueWrapper("3.5");
		v2.add(v1.getValue());
		assertEquals(Double.valueOf(1.5), v2.getValue());

		ValueWrapper v3 = new ValueWrapper("-2");
		ValueWrapper v4 = new ValueWrapper("3.5");
		v3.multiply(v4.getValue());
		assertEquals(Double.valueOf(-7), v3.getValue());

		ValueWrapper v5 = new ValueWrapper("-2.0");
		ValueWrapper v6 = new ValueWrapper("4");
		v5.divide(v6.getValue());
		assertEquals(Double.valueOf(-0.5), v5.getValue());

		ValueWrapper v7 = new ValueWrapper("-2");
		ValueWrapper v8 = new ValueWrapper("4");
		v7.subtract(v8.getValue());
		assertEquals(Integer.valueOf(-6), v7.getValue());

		ValueWrapper v9 = new ValueWrapper("2E2");
		ValueWrapper v10 = new ValueWrapper("4e+1");
		v9.subtract(v10.getValue());
		assertEquals(Double.valueOf(160), v9.getValue());

		ValueWrapper v11 = new ValueWrapper("-2");
		ValueWrapper v12 = new ValueWrapper("4.");
		v11.add(v12.getValue());
		assertEquals(Double.valueOf(2), v11.getValue());

		ValueWrapper v13 = new ValueWrapper("0");
		ValueWrapper v14 = new ValueWrapper(null);
		assertEquals(0, v13.numCompare(v14.getValue()));
		assertEquals(0, v14.numCompare(v13.getValue()));

		ValueWrapper v15 = new ValueWrapper("3");
		ValueWrapper v16 = new ValueWrapper("3.001");
		assertTrue(v15.numCompare(v16.getValue()) < 0);
		assertTrue(v16.numCompare(v15.getValue()) > 0);
	}

	@Test
	void testInvalidValueType() {
		ValueWrapper v = new ValueWrapper(Boolean.valueOf(true));
		assertThrows(RuntimeException.class, () -> v.add(Integer.valueOf(5)));

		ValueWrapper v1 = new ValueWrapper("Ankica");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		assertThrows(RuntimeException.class, () -> v1.add(v2.getValue()));
	}

	@Test
	void testInvalidArgumentType() {
		ValueWrapper v = new ValueWrapper(Integer.valueOf(5));
		assertThrows(RuntimeException.class, () -> v.add(Boolean.valueOf(true)));
	}

}
