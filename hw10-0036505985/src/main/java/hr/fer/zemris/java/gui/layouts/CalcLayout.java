package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * Layout consisting of a fixed <code>5x7</code> grid. Positions from
 * <code>(1,1)</code> to <code>(1,5)</code> are occupied by a single stretched
 * element, located at <code>(1,1)</code>. Enumeration starts at <code>1</code>.
 * 
 * @author Luka Mesaric
 */
public class CalcLayout implements LayoutManager2 {

	/** Number of rows. Must not be changed. */
	private static final int ROW_NUM = 5;
	/** Number of columns. Must not be changed. */
	private static final int COL_NUM = 7;

	/** Horizontal and vertical gap between elements. Non-negative value. */
	private final int gap;
	/** Matrix of children managed by this layout. */
	private final Component[][] children = new Component[ROW_NUM][COL_NUM];

	/**
	 * Default constructor. Sets gap to <code>0</code>.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Default constructor.
	 * 
	 * @param  gap                 horizontal and vertical gap between elements
	 * @throws CalcLayoutException if <code>gap</code> is negative
	 */
	public CalcLayout(int gap) {
		if (gap < 0) {
			throw new CalcLayoutException(
					"Gap must be 0 or greater: " + gap);
		}
		this.gap = gap;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if (comp == null) return;
		for (int row = 0; row < children.length; row++) {
			for (int col = 0; col < children[0].length; col++) {
				if (children[row][col] == comp) {
					children[row][col] = null;
					return;
				}
			}
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return layoutSize(parent, Component::getPreferredSize);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return layoutSize(parent, Component::getMinimumSize);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return layoutSize(target, Component::getMaximumSize);
	}

	/**
	 * Calculates the size of layout based on some property of components it
	 * contains. Getter for said property is given as
	 * <code>propertyExtractor</code>.
	 * 
	 * @param  parent            the component whose size needs to be determined
	 * @param  propertyExtractor getter for some dimension property of components
	 * @return                   layout size
	 */
	private Dimension layoutSize(Container parent,
			Function<Component, Dimension> propertyExtractor) {

		int maxWidth = 0;
		int maxHeight = 0;
		Component first = children[0][0];
		Dimension dimFirst = first != null ? propertyExtractor.apply(first) : null;
		if (dimFirst != null) {
			maxHeight = dimFirst.height;
			maxWidth = Math.max(0, dimFirst.width - 4 * gap) / 5;
		}
		for (int row = 0; row < children.length; row++) {
			for (int col = 0; col < children[0].length; col++) {
				if (row == 0 && col == 0) continue;
				Component comp = children[row][col];
				if (comp == null) continue;
				Dimension dim = propertyExtractor.apply(comp);
				if (dim == null) continue;
				if (dim.width > maxWidth) {
					maxWidth = dim.width;
				}
				if (dim.height > maxHeight) {
					maxHeight = dim.height;
				}
			}
		}

		Insets insets = parent.getInsets();
		return new Dimension(
				maxWidth * COL_NUM
						+ (COL_NUM - 1) * gap
						+ insets.left + insets.right,
				maxHeight * ROW_NUM
						+ (ROW_NUM - 1) * gap
						+ insets.top + insets.bottom);
	}

	/**
	 * Constructs an array of widths for every column in table.
	 * 
	 * @param  rectWidth width of area that the table can use
	 * @return           constructed array
	 */
	private int[] constructWidthsArray(int rectWidth) {
		int effectiveTotalWidth = rectWidth - (COL_NUM - 1) * gap;
		int minWidth = effectiveTotalWidth / COL_NUM;
		int[] widths = new int[COL_NUM];
		Arrays.fill(widths, minWidth);

		switch (effectiveTotalWidth - minWidth * COL_NUM) {
		case 6:
			widths[1]++;
			widths[5]++;
		case 4:
			widths[0]++;
			widths[6]++;
		case 2:
			widths[2]++;
			widths[4]++;
			break;

		case 5:
			widths[0]++;
			widths[6]++;
		case 3:
			widths[1]++;
			widths[5]++;
		case 1:
			widths[3]++;
			break;
		}
		return widths;
	}

	/**
	 * Constructs an array of heights for every row in table.
	 * 
	 * @param  rectHeight height of area that the table can use
	 * @return            constructed array
	 */
	private int[] constructHeightsArray(int rectHeight) {
		int effectiveTotalHeight = rectHeight - (ROW_NUM - 1) * gap;
		int minHeight = effectiveTotalHeight / ROW_NUM;
		int[] heights = new int[ROW_NUM];
		Arrays.fill(heights, minHeight);

		switch (effectiveTotalHeight - minHeight * ROW_NUM) {
		case 4:
			heights[0]++;
			heights[4]++;
		case 2:
			heights[1]++;
			heights[3]++;
			break;

		case 3:
			heights[0]++;
			heights[4]++;
		case 1:
			heights[2]++;
			break;
		}
		return heights;
	}

	/**
	 * @throws NullPointerException if <code>parent</code> is <code>null</code>
	 */
	@Override
	public void layoutContainer(Container parent) {
		Objects.requireNonNull(parent);

		Insets ins = parent.getInsets();
		Dimension dim = parent.getSize();
		Rectangle rect = new Rectangle(
				ins.left,
				ins.top,
				dim.width - ins.left - ins.right,
				dim.height - ins.top - ins.bottom);

		int[] widths = constructWidthsArray(rect.width);
		int[] heights = constructHeightsArray(rect.height);

		final int rows = children.length;
		final int cols = children[0].length;
		int y = rect.y;
		for (int r = 0; r < rows; r++, y += r >= ROW_NUM ? 0 : heights[r] + gap) {
			int x = rect.x;
			for (int c = 0; c < cols; c++, x += c >= COL_NUM ? 0 : widths[c] + gap) {
				Component comp = children[r][c];
				if (comp == null) continue;
				if (r == 0 && c == 0) {
					int sum = 0;
					for (int i = 0; i < 5; i++) {
						sum += widths[i];
					}
					comp.setBounds(rect.x, rect.y, sum + 4 * gap, heights[r]);
					continue;
				}
				comp.setBounds(x, y, widths[c], heights[r]);
			}
		}
	}

	/**
	 * @throws NullPointerException          if any argument is <code>null</code>
	 * @throws CalcLayoutException           if given <code>constraints</code>
	 *                                       cannot be parsed, or it represents and
	 *                                       illegal or occupied position
	 * @throws UnsupportedOperationException if given <code>constraints</code> is
	 *                                       neither <code>RCPosition</code> nor
	 *                                       <code>String</code>
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		Objects.requireNonNull(comp);
		Objects.requireNonNull(constraints);

		RCPosition position;
		if (constraints instanceof String) {
			position = parseConstraints((String) constraints);
		} else if (constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
		} else {
			throw new UnsupportedOperationException("Illegal type of constraint: "
					+ constraints.getClass().getCanonicalName());
		}

		if (!isPositionLegal(position)) {
			throw new CalcLayoutException("Illegal position: " + position);
		}
		int r = position.row - 1;
		int c = position.column - 1;
		if (children[r][c] != null && children[r][c] != comp) {
			throw new CalcLayoutException("Position already occupied: " + position);
		}

		children[r][c] = comp;
	}

	/**
	 * Parses input constraint. Does not test if parsed position represents a legal
	 * value.
	 * 
	 * @param  constraints         constraints to parse, e.g. <code>3,5</code>
	 * @return                     parsed <code>RCPosition</code>, never
	 *                             <code>null</code>
	 * @throws CalcLayoutException if <code>constraints</code> cannot be parsed
	 */
	private static RCPosition parseConstraints(String constraints) {
		constraints = constraints.strip();
		if (!constraints.contains(",")) {
			throw new CalcLayoutException(
					"Input must be separated by a comma: " + constraints);
		}
		String[] parts = constraints.split(",");
		if (parts.length != 2) {
			throw new CalcLayoutException(
					"Expected exactly two numbers: " + constraints);
		}
		try {
			int first = Integer.parseInt(parts[0]);
			int second = Integer.parseInt(parts[1]);
			return new RCPosition(first, second);
		} catch (NumberFormatException e) {
			throw new CalcLayoutException("Expected integers.", e);
		}
	}

	/**
	 * Tests if given <code>position</code> represents a legal value. Does not test
	 * if position is occupied.
	 * 
	 * @param  position position to test
	 * @return          <code>true</code> if position is legal, <code>false</code>
	 *                  otherwise
	 */
	private static boolean isPositionLegal(RCPosition position) {
		final int r = position.row;
		final int c = position.column;
		if (r < 1 || c < 1) {
			return false;
		} else if (r > ROW_NUM || c > COL_NUM) {
			return false;
		} else if (r == 1 && c > 1 && c < 6) {
			return false;
		}
		return true;
	}

	@Override
	public void invalidateLayout(Container target) {
		// no calculations are cached so there is nothing to do
		return;
	}

	/**
	 * @return always <code>0</code>
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	/**
	 * @return always <code>0</code>
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	/**
	 * Does nothing, throws an exception.
	 * 
	 * @throws UnsupportedOperationException on every call
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

}
