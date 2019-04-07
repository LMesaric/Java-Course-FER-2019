package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;

/**
 * @author Luka Mesaric
 */
class LSystemBuilderImplTest {

	private static LSystem lSystemFromCode;
	private static LSystem lSystemFromText;

	@BeforeAll
	static void setUpBeforeClass() {
		lSystemFromCode = new LSystemBuilderImpl()
				.setAxiom("F")
				.registerProduction('F', "F+F--F+F")
				.build();

		lSystemFromText = new LSystemBuilderImpl()
				.configureFromText(new String[] {
						"axiom F",
						"production F F+F--F+F"
				}).build();
	}

	@Test
	void testGenerateZeroCode() {
		assertEquals("F", lSystemFromCode.generate(0));
	}

	@Test
	void testGenerateZeroText() {
		assertEquals("F", lSystemFromText.generate(0));
	}

	@Test
	void testGenerateOneCode() {
		assertEquals("F+F--F+F", lSystemFromCode.generate(1));
	}

	@Test
	void testGenerateOneText() {
		assertEquals("F+F--F+F", lSystemFromText.generate(1));
	}

	@Test
	void testGenerateTwoCode() {
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", lSystemFromCode.generate(2));
	}

	@Test
	void testGenerateTwoText() {
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", lSystemFromText.generate(2));
	}

}
