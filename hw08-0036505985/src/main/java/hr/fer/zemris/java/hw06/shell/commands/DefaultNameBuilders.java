package hr.fer.zemris.java.hw06.shell.commands;

/**
 * A collection of methods for creating common <code>NameBuilders</code>.
 * 
 * @author Luka Mesaric
 */
public class DefaultNameBuilders {

	/**
	 * Returns a <code>NameBuilder</code> that appends <code>t</code>.
	 * 
	 * @param  t string to append
	 * @return   new instance of <code>NameBuilder</code>
	 */
	public static NameBuilder text(String t) {
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
	public static NameBuilder group(int index) {
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
	public static NameBuilder group(int index, char padding, int minWidth) {
		return (result, sb) -> {
			final String group = result.group(index);
			final int extra = minWidth - group.length();
			for (int i = 0; i < extra; i++) {
				sb.append(padding);
			}
			sb.append(group);
		};
	}

	/** Disable creating instances. */
	private DefaultNameBuilders() {}

}
