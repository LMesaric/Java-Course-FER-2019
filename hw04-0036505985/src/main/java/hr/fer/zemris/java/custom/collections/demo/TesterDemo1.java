package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Demo program for {@link Tester}.
 * 
 * @author Luka Mesaric
 */
public class TesterDemo1 {

	public static void main(String[] args) {
		Tester<Integer> t = new EvenIntegerTester();
//		System.out.println(t.test("Ivo"));
		System.out.println(t.test(22));
		System.out.println(t.test(3));
	}
}
