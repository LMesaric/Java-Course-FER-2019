package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class ArrayIndexedCollectionTest {

	private static ArrayIndexedCollection<String> empty;
	private static ArrayIndexedCollection<String> one;
	private static ArrayIndexedCollection<String> ten;

	@BeforeEach
	void setUp() {
		empty = new ArrayIndexedCollection<>();

		one = new ArrayIndexedCollection<>();
		one.add("hey");

		ten = new ArrayIndexedCollection<>();
		ten.add("hey");
		ten.add("12");
		ten.add("-5");
		ten.add("4.25");
		ten.add("hey");
		ten.add("true");
		ten.add("false");
		ten.add("");
		ten.add("24L");
		ten.add("text");
	}

	@Test
	void testConstructors() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(0));
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null));

		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null, 10));
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(one, 0));

		ArrayIndexedCollection<String> emptyFirst = new ArrayIndexedCollection<>(10);
		assertEquals(0, emptyFirst.size());

		ArrayIndexedCollection<String> emptySecond = new ArrayIndexedCollection<>(empty);
		assertEquals(0, emptySecond.size());

		ArrayIndexedCollection<String> emptyThird = new ArrayIndexedCollection<>(empty, 100);
		assertEquals(0, emptyThird.size());

		ArrayIndexedCollection<String> tenFirst = new ArrayIndexedCollection<>(ten, 100);
		assertEquals(10, tenFirst.size());

		ArrayIndexedCollection<String> tenSecond = new ArrayIndexedCollection<>(ten, 1);
		assertEquals(10, tenSecond.size());
	}

	@Test
	void testSize() {
		assertEquals(0, empty.size());
		assertEquals(1, one.size());
		assertEquals(10, ten.size());
	}

	@Test
	void testIsEmpty() {
		assertTrue(empty.isEmpty());
		assertFalse(one.isEmpty());
		assertFalse(ten.isEmpty());
	}

	@Test
	void testAdd() {
		assertThrows(NullPointerException.class, () -> empty.add(null));

		for (int i = 1; i <= 1000; i++) {
			empty.add("1");
			assertEquals(i, empty.size());
		}

		assertThrows(NullPointerException.class, () -> ten.add(null));
	}

	@Test
	void testAddAll() {
		empty.addAll(ten);
		assertEquals(10, empty.size());

		one.addAll(ten);
		assertEquals(11, one.size());
	}

	@Test
	void testAddAllWithEmpty() {
		ten.addAll(empty);
		assertEquals(10, ten.size());

		empty.addAll(empty);
		assertEquals(0, empty.size());
	}

	@Test
	void testInsert() {
		assertThrows(NullPointerException.class, () -> empty.insert(null, 0));
		assertThrows(NullPointerException.class, () -> ten.insert(null, 2));

		for (int i = 0; i < 1000; i++) {
			empty.insert(String.valueOf(i), 0);
			assertEquals(i + 1, empty.size());
			assertEquals(String.valueOf(i), empty.get(0));
			assertEquals("0", empty.get(i));
		}

		assertThrows(IndexOutOfBoundsException.class, () -> ten.insert("1", -1));
		assertThrows(IndexOutOfBoundsException.class, () -> ten.insert("1", 11));

		assertEquals(1, one.size());
		one.insert("x", 1);
		assertEquals(2, one.size());
		assertEquals("x", one.get(1));

		ten.insert("abc", 5);
		assertEquals("abc", ten.get(5));
	}

	@Test
	void testGet() {
		assertThrows(IndexOutOfBoundsException.class, () -> empty.get(0));
		assertThrows(IndexOutOfBoundsException.class, () -> one.get(1));
		assertThrows(IndexOutOfBoundsException.class, () -> one.get(-1));

		assertEquals("hey", one.get(0));
		assertEquals("24L", ten.get(8));
	}

	@Test
	void testIndexOf() {
		assertEquals(-1, empty.indexOf(null));
		assertEquals(-1, empty.indexOf("text"));

		assertEquals(2, ten.indexOf("-5"));
		assertEquals(9, ten.indexOf("text"));
		assertEquals(0, ten.indexOf("hey"));
	}

	@Test
	void testContains() {
		assertFalse(empty.contains(null));
		assertFalse(ten.contains(null));

		assertFalse(ten.contains("heyyyy")); // She wants you :)

		assertTrue(ten.contains("hey")); // Just friends :(
	}

	@Test
	void testRemoveIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> empty.remove(0));

		assertThrows(IndexOutOfBoundsException.class, () -> ten.remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> ten.remove(10));

		ten.remove(9);
		ten.remove(0);
		assertEquals(8, ten.size());
	}

	@Test
	void testRemoveObject() {
		assertFalse(empty.remove(null));
		assertFalse(ten.remove(null));

		assertTrue(ten.remove("hey"));
		assertTrue(ten.remove("hey"));
		assertFalse(ten.remove("hey"));
	}

	@Test
	void testToArray() {
		assertNotNull(empty.toArray());
		assertArrayEquals(new Object[0], empty.toArray());
		assertArrayEquals(new Object[] { "hey" }, one.toArray());
	}

	@Test
	void testClear() {
		empty.clear();
		assertTrue(empty.isEmpty());

		one.clear();
		assertTrue(one.isEmpty());

		ten.clear();
		assertTrue(ten.isEmpty());
	}

	@Test
	void testForEach() {
		var p = new Processor<>() {
			private StringBuilder sb = new StringBuilder();

			@Override
			public void process(Object value) {
				sb.append(value);
			}
		};

		ArrayIndexedCollection<String> three = new ArrayIndexedCollection<>();
		three.add("1");
		three.add("2");
		three.add("3");

		three.forEach(p);

		assertEquals("123", p.sb.toString());
	}

	@Test
	void testEquals() {
		assertEquals(ten, new ArrayIndexedCollection<>(ten));
	}

	@Test
	void testHashCode() {
		assertEquals(ten.hashCode(), new ArrayIndexedCollection<>(ten).hashCode());
	}

}
