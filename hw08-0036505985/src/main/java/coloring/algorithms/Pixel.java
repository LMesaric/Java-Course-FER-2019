package coloring.algorithms;

import java.util.Objects;

/**
 * Models a pixel with <code>x</code> and <code>y</code> coordinates.
 * 
 * @author Luka Mesaric
 */
public class Pixel {

	/** Pixel's x-coordinate. */
	public final int x;

	/** Pixel's y-coordinate. */
	public final int y;

	/**
	 * Default constructor.
	 * 
	 * @param x pixel's x-coordinate
	 * @param y pixel's y-coordinate
	 */
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("(%d,%d)", x, y);
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
		if (!(obj instanceof Pixel)) {
			return false;
		}
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}

}
