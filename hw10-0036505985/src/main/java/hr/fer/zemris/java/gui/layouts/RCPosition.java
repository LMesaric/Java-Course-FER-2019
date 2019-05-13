package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Pair of row-column coordinates used as constraints in {@link CalcLayout}.
 * 
 * @author Luka Mesaric
 */
public class RCPosition {

	/** Row index. */
	public final int row;
	/** Column index. */
	public final int column;

	/**
	 * Default constructor.
	 * 
	 * @param row    row index
	 * @param column column index
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	@Override
	public String toString() {
		return row + "," + column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RCPosition)) {
			return false;
		}
		RCPosition other = (RCPosition) obj;
		return column == other.column
				&& row == other.row;
	}

}
