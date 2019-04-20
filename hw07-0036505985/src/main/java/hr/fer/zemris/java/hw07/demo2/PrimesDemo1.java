package hr.fer.zemris.java.hw07.demo2;

/**
 * First demo program for {@link PrimesCollection}.
 * 
 * @author Luka Mesaric
 */
public class PrimesDemo1 {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5);
		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
	}

}
