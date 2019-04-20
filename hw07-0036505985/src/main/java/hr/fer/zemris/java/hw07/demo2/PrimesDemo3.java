package hr.fer.zemris.java.hw07.demo2;

/**
 * Second demo program for {@link PrimesCollection}.
 * 
 * @author Luka Mesaric
 */
public class PrimesDemo3 {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(3);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				for (Integer prime3 : primesCollection) {
					for (Integer prime4 : primesCollection) {
						System.out.format(
								"Got primes: (%d, %d, %d, %d)%n",
								prime, prime2, prime3, prime4);
					}
				}
			}
		}
	}

}
