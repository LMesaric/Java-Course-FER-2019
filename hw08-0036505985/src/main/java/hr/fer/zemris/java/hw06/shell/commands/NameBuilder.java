package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.ExceptionUtil;

/**
 * Generates a part of names and appends it to given <code>StringBuilder</code>.
 * 
 * @author Luka Mesaric
 */
@FunctionalInterface
public interface NameBuilder {

	/**
	 * Performs modelled operation on given arguments.
	 * 
	 * @param result filter result
	 * @param sb     string builder that will be used for appending results
	 */
	void execute(FilterResult result, StringBuilder sb);

	/**
	 * Returns a composed <code>NameBuilder</code> that executes, in sequence, this
	 * operation followed by the <code>next</code> operation.
	 *
	 * @param  next                 the operation to execute after this operation
	 * @return                      a composed <code>NameBuilder</code>
	 * @throws NullPointerException if <code>next</code> is <code>null</code>
	 */
	default NameBuilder then(NameBuilder next) {
		ExceptionUtil.validateNotNull(next, "next");
		return (result, sb) -> {
			execute(result, sb);
			next.execute(result, sb);
		};
	}

}
