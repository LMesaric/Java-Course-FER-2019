package coloring.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;

/**
 * Concrete strategies for coloring enclosed parts of a picture.
 * 
 * @author Luka Mesaric
 */
public class Coloring implements
		Supplier<Pixel>,
		Consumer<Pixel>,
		Function<Pixel, List<Pixel>>,
		Predicate<Pixel> {

	/**
	 * Position whose color is used as reference, never <code>null</code>.
	 */
	private final Pixel reference;

	/**
	 * Reference to a picture that will be filled, never <code>null</code>.
	 */
	private final Picture picture;

	/**
	 * Fill color.
	 */
	private final int fillColor;

	/**
	 * Color at position <code>reference</code>.
	 */
	private final int refColor;

	/**
	 * Default constructor.
	 * 
	 * @param  reference                position whose color is used as reference
	 * @param  picture                  reference to a picture that will be filled
	 * @param  fillColor                fill color
	 * @throws NullPointerException     if any argument is <code>null</code>
	 * @throws IllegalArgumentException if <code>reference</code> is not a legal
	 *                                  position inside <code>picture</code>
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		this.picture = Objects.requireNonNull(picture);
		this.reference = Objects.requireNonNull(reference);
		if (!isPartOfPicture(reference)) {
			throw new IllegalArgumentException("Reference is not a part of picture.");
		}
		this.fillColor = fillColor;
		this.refColor = picture.getPixelColor(reference.x, reference.y);
	}

	/**
	 * Tests if pixel <code>p</code> is at a legal position inside
	 * <code>picture</code>.
	 * 
	 * @param  p                    position to test
	 * @return                      <code>true</code> if position is legal,
	 *                              <code>false</code> otherwise
	 * @throws NullPointerException if <code>p</code> is <code>null</code>
	 */
	private boolean isPartOfPicture(Pixel p) {
		if (p.x < 0 || p.x >= picture.getWidth())
			return false;
		if (p.y < 0 || p.y >= picture.getHeight())
			return false;
		return true;
	}

	/**
	 * Returns starting state.
	 * 
	 * @return starting state, never <code>null</code>
	 */
	@Override
	public Pixel get() {
		return reference;
	}

	/**
	 * Sets color of <code>picture</code> at position <code>position</code> to
	 * <code>fillColor</code>.
	 * 
	 * @param  position             position
	 * @throws NullPointerException if <code>position</code> is <code>null</code>
	 */
	@Override
	public void accept(Pixel position) {
		Objects.requireNonNull(position);
		picture.setPixelColor(position.x, position.y, fillColor);
	}

	/**
	 * Returns a list of direct successors (neighbours) of <code>position</code>
	 * whose positions are legal.
	 * 
	 * @param  position             position whose neighbours are wanted
	 * @return                      list of all possible successors (neighbours),
	 *                              never <code>null</code>
	 * @throws NullPointerException if <code>position</code> is <code>null</code>
	 */
	@Override
	public List<Pixel> apply(Pixel position) {
		Objects.requireNonNull(position);
		List<Pixel> neighbours = new ArrayList<>(4);

		addIfLegal(new Pixel(position.x - 1, position.y), neighbours);
		addIfLegal(new Pixel(position.x + 1, position.y), neighbours);
		addIfLegal(new Pixel(position.x, position.y - 1), neighbours);
		addIfLegal(new Pixel(position.x, position.y + 1), neighbours);

		return neighbours;
	}

	/**
	 * Adds <code>p</code> to the end of <code>list</code> if <code>p</code>
	 * represents a legal position inside <code>picture</code>.
	 * 
	 * @param  p                    position to test
	 * @param  list                 list to append <code>p</code> to
	 * @throws NullPointerException if <code>p</code> is <code>null</code>
	 */
	private void addIfLegal(Pixel p, List<Pixel> list) {
		if (isPartOfPicture(p)) {
			list.add(p);
		}
	}

	/**
	 * Tests if given <code>position</code> is a part of wanted subspace.
	 * 
	 * @param  position             position to test
	 * @return                      <code>true</code> if <code>position</code> is
	 *                              legal and should be processed,
	 *                              <code>false</code> if <code>position</code> is
	 *                              not a part of wanted subspace.
	 * @throws NullPointerException if <code>position</code> is <code>null</code>
	 */
	@Override
	public boolean test(Pixel position) {
		return isPartOfPicture(Objects.requireNonNull(position))
				&& picture.getPixelColor(position.x, position.y) == refColor;
	}

}
