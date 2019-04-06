package hr.fer.zemris.java.custom.collections;

import java.util.function.Consumer;

/**
 * Models an object capable of performing any operation on the passed object,
 * which returns no result. Acts as a conceptual contract.
 * 
 * @param <T> the type of the input to processor
 * 
 * @author Luka Mesaric
 * 
 * @see Consumer
 */
@FunctionalInterface
public interface Processor<T> {

	/**
	 * Method can do any operation whatsoever on the passed object.
	 * 
	 * @param value value to process
	 */
	void process(T value);

}
