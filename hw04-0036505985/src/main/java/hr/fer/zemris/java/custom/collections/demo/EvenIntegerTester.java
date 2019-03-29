package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Example of an implementation for {@link Tester}.
 * 
 * @author Luka Mesaric
 */
class EvenIntegerTester implements Tester {

	@Override
	public boolean test(Object obj) {
		if (!(obj instanceof Integer))
			return false;
		Integer i = (Integer) obj;
		return i % 2 == 0;
	}

}