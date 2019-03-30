package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Example of an implementation for {@link Tester}.
 * 
 * @author Luka Mesaric
 */
class EvenIntegerTester implements Tester<Integer> {

	@Override
	public boolean test(Integer obj) {
		return obj % 2 == 0;
	}

}