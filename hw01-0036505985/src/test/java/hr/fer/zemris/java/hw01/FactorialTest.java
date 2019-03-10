package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FactorialTest {

	@Test
	void testCalculateFactorialRegularSmall() {
		long fact = Factorial.calculateFactorial(4);
		assertEquals(24L, fact);
	}

	@Test
	void testCalculateFactorialRegularLarge() {
		long fact = Factorial.calculateFactorial(20);
		assertEquals(2432902008176640000L, fact);
	}

	@Test
	void testCalculateFactorialRegularEnormous() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.calculateFactorial(21));
	}
	
	@Test
	void testCalculateFactorialZero() {
		long fact = Factorial.calculateFactorial(0);
		assertEquals(1L, fact);
	}
	
	@Test
	void testCalculateFactorialOne() {
		long fact = Factorial.calculateFactorial(1);
		assertEquals(1L, fact);
	}
	
	@Test
	void testCalculateFactorialTwo() {
		long fact = Factorial.calculateFactorial(2);
		assertEquals(2L, fact);
	}
	
	@Test
	void testCalculateFactorialNegative() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.calculateFactorial(-1));
	}

}
