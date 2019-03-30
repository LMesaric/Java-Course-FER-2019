package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class DictionaryTest {

	private static Dictionary<String, Integer> empty;
	private static Dictionary<String, Integer> one;
	private static Dictionary<String, Integer> five;

	@BeforeEach
	void setUp() {
		empty = new Dictionary<>();

		one = new Dictionary<>();
		one.put("1", 1);

		five = new Dictionary<>();
		five.put("1", 1);
		five.put("2", 2);
		five.put("3", 3);
		five.put("4", 4);
		five.put("5", 5);
	}

	@Test
	void testIsEmpty() {
		assertTrue(empty.isEmpty());
		assertFalse(one.isEmpty());
		assertFalse(five.isEmpty());
	}

	@Test
	void testSize() {
		assertEquals(0, empty.size());
		assertEquals(1, one.size());
		assertEquals(5, five.size());
	}

	@Test
	void testClear() {
		empty.clear();
		assertTrue(empty.isEmpty());
		assertEquals(0, empty.size());

		one.clear();
		assertTrue(one.isEmpty());
		assertEquals(0, one.size());

		five.clear();
		assertTrue(five.isEmpty());
		assertEquals(0, five.size());
	}

	@Test
	void testGetNull() {
		assertNull(empty.get(null));
		assertNull(one.get(null));
		assertNull(five.get(null));
	}

	@Test
	void testGet() {
		assertNull(empty.get(1));
		assertNull(empty.get("1"));

		assertNull(one.get(1));
		assertEquals(1, one.get("1"));

		assertNull(five.get(1));
		assertEquals(1, five.get("1"));
		assertEquals(3, five.get("3"));
		assertEquals(5, five.get("5"));
		assertNull(five.get("6"));
	}

	@Test
	void testPutNull() {
		assertThrows(NullPointerException.class, () -> empty.put(null, 0));
		assertThrows(NullPointerException.class, () -> one.put(null, 0));
		assertThrows(NullPointerException.class, () -> five.put(null, 0));
	}

	@Test
	void testPut() {
		for (int i = 2; i <= 1000; i++) {
			one.put("1", one.get("1") + 1);
			assertEquals(1, one.size());
			assertEquals(i, one.get("1"));
		}

		assertNull(five.get("10"));
		five.put("10", 10);
		assertEquals(10, five.get("10"));
	}

}
