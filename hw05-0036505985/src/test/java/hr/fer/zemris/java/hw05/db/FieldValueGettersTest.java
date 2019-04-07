package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class FieldValueGettersTest {

	private StudentRecord record = new StudentRecord("0000000050", "Sikirica", "Alen", 3);

	@Test
	void testFirstName() {
		IFieldValueGetter getter = FieldValueGetters.FIRST_NAME;
		assertEquals("Alen", getter.get(record));
	}

	@Test
	void testLastName() {
		IFieldValueGetter getter = FieldValueGetters.LAST_NAME;
		assertEquals("Sikirica", getter.get(record));
	}

	@Test
	void testJmbag() {
		IFieldValueGetter getter = FieldValueGetters.JMBAG;
		assertEquals("0000000050", getter.get(record));
	}

}
