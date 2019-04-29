package hr.fer.zemris.java.hw06.shell.commands;

import java.util.regex.Matcher;

import hr.fer.zemris.java.hw06.ExceptionUtil;

/**
 * Represents a single file that matched the given regex. Contains information
 * about matched groups.
 * 
 * @author Luka Mesaric
 */
public class FilterResult {

	/**
	 * Matcher containing needed information about groups, never <code>null</code>.
	 */
	private final Matcher matcher;

	/**
	 * Default constructor.
	 * 
	 * @param  matcher              matcher containing needed information about
	 *                              groups and original file name
	 * @throws NullPointerException if any argument is <code>null</code>
	 */
	public FilterResult(Matcher matcher) {
		this.matcher = ExceptionUtil.validateNotNull(matcher, "matcher");
	}

	/**
	 * Returns the name of the file, without the path.
	 * 
	 * @return name of the file, never <code>null</code>
	 */
	@Override
	public String toString() {
		return matcher.group();
	}

	/**
	 * Returns the number of groups that were found.
	 * 
	 * @return number of groups
	 */
	public int numberOfGroups() {
		return matcher.groupCount();
	}

	/**
	 * Returns the input subsequence captured by the group.<br>
	 * <code>index</code> must be from range <code>[0, numberOfGroups()]</code>.
	 * 
	 * @param  index                     group index
	 * @return                           subsequence captured by the group, possibly
	 *                                   empty but never <code>null</code>
	 * @throws IndexOutOfBoundsException if there is no group in the pattern with
	 *                                   the given <code>index</code>
	 */
	public String group(int index) {
		String group = matcher.group(index);
		return group == null ? "" : group;
	}

}
