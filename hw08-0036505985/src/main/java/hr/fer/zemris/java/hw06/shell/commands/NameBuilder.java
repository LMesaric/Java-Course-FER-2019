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

	/**
	 * Returns a <code>NameBuilder</code> that appends <code>t</code>.
	 * 
	 * @param  t string to append
	 * @return   new instance of <code>NameBuilder</code>
	 */
	static NameBuilder text(String t) {
		return (result, sb) -> {
			sb.append(t);
		};
	}

	/**
	 * Returns a <code>NameBuilder</code> that appends group at <code>index</code>.
	 * 
	 * @param  index index of group to append
	 * @return       new instance of <code>NameBuilder</code>
	 */
	static NameBuilder group(int index) {
		return (result, sb) -> {
			sb.append(result.group(index));
		};
	}

	/**
	 * Returns a <code>NameBuilder</code> that appends group at <code>index</code>,
	 * padded with <code>padding</code> to be at least as long as
	 * <code>minWidth</code>.
	 * 
	 * @param  index    index of group to append
	 * @param  padding  character used for padding
	 * @param  minWidth minimum width of appended string
	 * @return          new instance of <code>NameBuilder</code>
	 */
	static NameBuilder group(int index, char padding, int minWidth) {
		return (result, sb) -> {
			final String group = result.group(index);
			final int extra = minWidth - group.length();
			for (int i = 0; i < extra; i++) {
				sb.append(padding);
			}
			sb.append(group);
		};
	}

}
