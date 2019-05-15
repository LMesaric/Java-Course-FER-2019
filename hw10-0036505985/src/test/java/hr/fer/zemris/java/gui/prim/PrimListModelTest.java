package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class PrimListModelTest {

	private PrimListModel model;

	@BeforeEach
	void prepareModel() {
		model = new PrimListModel();
	}

	@Test
	void testConstructor() {
		assertEquals(1, model.getSize());
		assertEquals(1, model.getElementAt(0));
	}

	@Test
	void test47Primes() {
		int[] primes = {
				1, 2, 3, 5, 7, 11, 13, 17, 19,
				23, 29, 31, 37, 41, 43, 47, 53,
				59, 61, 67, 71, 73, 79, 83, 89,
				97, 101, 103, 107, 109, 113,
				127, 131, 137, 139, 149, 151,
				157, 163, 167, 173, 179, 181,
				191, 193, 197, 199 };

		for (int i = 0; i < primes.length; i++) {
			assertEquals(i + 1, model.getSize());
			for (int j = 0; j <= i; j++) {
				assertEquals(primes[j], model.getElementAt(j));
			}
			model.next();
		}
	}

	@Test
	void testAddNullListener() {
		assertThrows(NullPointerException.class,
				() -> model.addListDataListener(null));
	}

	@Test
	void testRemoveNullListener() {
		assertThrows(NullPointerException.class,
				() -> model.removeListDataListener(null));
	}
}
