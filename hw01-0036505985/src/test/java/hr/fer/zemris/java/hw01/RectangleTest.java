package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RectangleTest {

	@Test
	void testValidateAndParseInputRegular() {
		double res = Rectangle.validateAndParseInput("23.0");
		assertEquals(23.0, res, 0.0001);
	}
	
	@Test
	void testValidateAndParseInputInteger() {
		double res = Rectangle.validateAndParseInput("11");
		assertEquals(11.0, res, 0.0001);
	}

	@Test
	void testValidateAndParseInputNegative() {
		assertThrows(IllegalArgumentException.class, () -> Rectangle.validateAndParseInput("-23.0"));
	}
	
	@Test
	void testValidateAndParseInputGarbage() {
		assertThrows(IllegalArgumentException.class, () -> Rectangle.validateAndParseInput("2x3"));
	}
	
	@Test
	void testValidateAndParseInputComma() {
		assertThrows(IllegalArgumentException.class, () -> Rectangle.validateAndParseInput("15,0"));
	}

}
