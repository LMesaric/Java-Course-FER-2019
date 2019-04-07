package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class StudentDatabaseTest {

	private static List<String> lines;
	private static StudentDatabase db;

	@BeforeAll
	static void setUpBeforeClass() throws IOException {
		lines = Files.readAllLines(
				Paths.get("src/main/resources/database.txt"),
				StandardCharsets.UTF_8);
		db = new StudentDatabase(lines);
	}

	@Test
	void testForJmbag() {
		assertNull(db.forJMBAG("0000000000"));
		assertNull(db.forJMBAG("0000000064"));

		assertEquals(
				new StudentRecord("0000000001", "Akšamović", "Marin", 2),
				db.forJMBAG("0000000001"));
		assertEquals(
				new StudentRecord("0000000027", "Komunjer", "Luka", 4),
				db.forJMBAG("0000000027"));
		assertEquals(
				new StudentRecord("0000000063", "Žabčić", "Željko", 4),
				db.forJMBAG("0000000063"));
	}

	@Test
	void testFilterLeaveAll() {
		assertEquals(
				lines.size(),
				db.filter(x -> true).size());
	}

	@Test
	void testFilterLeaveNone() {
		assertEquals(
				0,
				db.filter(x -> false).size());
	}

}
