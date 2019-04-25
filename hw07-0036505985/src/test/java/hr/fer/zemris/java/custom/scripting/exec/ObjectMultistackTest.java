package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class ObjectMultistackTest {

	@Test
	void testPushingNulls() {
		ObjectMultistack multistack = new ObjectMultistack();

		assertThrows(NullPointerException.class,
				() -> multistack.push("price", null));

		assertThrows(NullPointerException.class,
				() -> multistack.push(null, new ValueWrapper("2")));

		assertThrows(NullPointerException.class,
				() -> multistack.push(null, null));
	}

	@Test
	void testGeneralPerformance() {

		ObjectMultistack multistack = new ObjectMultistack();
		assertTrue(multistack.isEmpty(null));
		assertTrue(multistack.isEmpty("abc"));
		assertTrue(multistack.isEmpty("year"));
		assertTrue(multistack.isEmpty("price"));

		ValueWrapper year = new ValueWrapper(Integer.valueOf(2000));
		multistack.push("year", year);
		assertFalse(multistack.isEmpty("year"));
		assertTrue(multistack.isEmpty("price"));

		ValueWrapper price = new ValueWrapper(200.51);
		multistack.push("price", price);
		assertFalse(multistack.isEmpty("year"));
		assertFalse(multistack.isEmpty("price"));

		assertEquals(Integer.valueOf(2000), multistack.peek("year").getValue());
		assertEquals(Double.valueOf(200.51), multistack.peek("price").getValue());

		multistack.push("year", new ValueWrapper(Integer.valueOf(1900)));
		assertEquals(Integer.valueOf(1900), multistack.peek("year").getValue());
		assertFalse(multistack.isEmpty("year"));
		assertFalse(multistack.isEmpty("price"));

		Integer old = (Integer) multistack.peek("year").getValue();
		multistack.peek("year").setValue(old.intValue() + 50);
		assertEquals(Integer.valueOf(1950), multistack.peek("year").getValue());

		multistack.pop("year");
		assertEquals(Integer.valueOf(2000), multistack.peek("year").getValue());
		assertFalse(multistack.isEmpty("year"));
		assertFalse(multistack.isEmpty("price"));

		multistack.peek("year").add("5");
		assertEquals(Integer.valueOf(2005), multistack.peek("year").getValue());

		multistack.peek("year").add(5);
		assertEquals(Integer.valueOf(2010), multistack.peek("year").getValue());

		multistack.peek("year").add(5.0);
		assertEquals(Double.valueOf(2015.0), multistack.peek("year").getValue());

		multistack.pop("year");
		assertTrue(multistack.isEmpty("year"));
		assertFalse(multistack.isEmpty("price"));

		multistack.pop("price");
		assertTrue(multistack.isEmpty("year"));
		assertTrue(multistack.isEmpty("price"));

		assertThrows(EmptyStackException.class, () -> multistack.peek("price"));
		assertThrows(EmptyStackException.class, () -> multistack.peek("year"));
	}

	@Test
	void testAbuseGeneral() {
		// On my PC, this took about 1.6 seconds to execute
		final int max = 3_000_000;
		ObjectMultistack multistack = new ObjectMultistack();

		for (int i = 0; i < max; i++) {
			multistack.push("x", new ValueWrapper(null));
		}

		for (int i = 0; i < max; i++) {
			assertFalse(multistack.isEmpty("x"));
			assertNull(multistack.pop("x").getValue());
		}

		assertTrue(multistack.isEmpty("x"));
	}

	@Test
	void testAbuseSumming() {
		// On my PC, this took about 1.8 seconds to execute
		final int max = 3_000_000;
		ValueWrapper sum = new ValueWrapper("0");
		ObjectMultistack multistack = new ObjectMultistack();

		for (int i = 0; i < max; i++) {
			multistack.push("x", new ValueWrapper("1"));
		}

		while (!multistack.isEmpty("x")) {
			sum.add(multistack.pop("x").getValue());
		}
		assertTrue(multistack.isEmpty("x"));
		assertEquals(max, sum.getValue());
	}

}
