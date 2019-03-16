package hr.fer.zemris.java.custom.collections;

/**
 * Models an object capable of performing any operation on the passed object,
 * which returns no result. Acts as a conceptual contract.
 * 
 * @author Luka Mesaric
 */
public class Processor {

	/**
	 * Default constructor.
	 */
	public Processor() {
		super();
	}

	/**
	 * Method can do any operation whatsoever on the passed object.
	 * 
	 * @param value value to process
	 */
	public void process(Object value) {
		return;
	}

}
