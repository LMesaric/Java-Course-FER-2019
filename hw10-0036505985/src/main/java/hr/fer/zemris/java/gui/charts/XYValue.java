package hr.fer.zemris.java.gui.charts;

import java.util.Objects;

/**
 * Pair of <code>(x,y)</code> coordinates used for determining bar positions and
 * heights in {@link BarChart}.
 * 
 * @author Luka Mesaric
 */
public class XYValue {

	/** x-coordinate. */
	public final int x;
	/** y-coordinate. */
	public final int y;

	/**
	 * Default constructor.
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof XYValue)) {
			return false;
		}
		XYValue other = (XYValue) obj;
		return x == other.x
				&& y == other.y;
	}
}
