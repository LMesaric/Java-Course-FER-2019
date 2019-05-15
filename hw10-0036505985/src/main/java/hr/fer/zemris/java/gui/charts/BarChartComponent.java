package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.IntStream;

import javax.swing.JComponent;

/**
 * Custom component which draws a bar chart modelled by {@link BarChart}.
 * 
 * @author Luka Mesaric
 */
public class BarChartComponent extends JComponent {

	/** Serial version UID. */
	private static final long serialVersionUID = -2290688286165650352L;

	/** Length of little dashes (markings) on axes. */
	private static final int DASH_LEN = 6;
	/** Distance from numbers to ends of dashes. */
	private static final int NUM_TO_DASH_DIST = 5;
	/** Distance from text describing axes to numbers on axes. */
	private static final int NUM_TO_DESC_DIST = 10;
	/** Distance from last dash to top of arrow. */
	private static final int ARROW_DIST = 12;

	/** Fill color for bars. */
	private static final Color BAR_COLOR = new Color(0xF47747);
	/** Color of background grid. */
	private static final Color GRID_COLOR = Color.ORANGE;
	/** Color of axes and dashes. */
	private static final Color AXIS_COLOR = Color.BLACK;

	/** Model of a bar chart shown by this component, not <code>null</code>. */
	private final BarChart model;

	/**
	 * Default constructor.
	 * 
	 * @param  model                model of a bar chart shown by this component
	 * @throws NullPointerException if <code>model</code> is <code>null</code>
	 */
	public BarChartComponent(BarChart model) {
		this.model = Objects.requireNonNull(model);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		final Graphics2D g2 = (Graphics2D) g;
		final Rectangle r = getInnerRectangle();
		if (isOpaque()) {
			g2.setColor(getBackground());
			g2.fillRect(r.x, r.y, r.width, r.height);
		}

		final FontMetrics fm = g2.getFontMetrics();
		final Font regularFont = g2.getFont();
		final Font boldFont = regularFont.deriveFont(Font.BOLD);

		final int rRight = r.x + r.width;
		final int rDown = r.y + r.height;
		final int bufferSpace = NUM_TO_DESC_DIST + NUM_TO_DASH_DIST + DASH_LEN;

		final int xAxisPos = rDown - 2 * (fm.getHeight() - fm.getDescent()) - bufferSpace;
		final int yAxisPos = r.x + fm.getHeight() + longestYNumber(fm) + bufferSpace;

		final double modelYSpan = model.getYMax() - model.getYMin();
		final double heightOfOne = (xAxisPos - r.y - ARROW_DIST) / modelYSpan;

		// Input did not have to be sorted.
		TreeMap<Integer, Integer> values = new TreeMap<>();
		model.getValues().forEach(v -> values.put(v.x, v.y));
		final int xMin = values.firstKey();
		final int xMax = values.lastKey();
		final int numOfBars = xMax - xMin + 1;
		final double widthOfOne = (double) (rRight - ARROW_DIST - yAxisPos) / numOfBars;

		// draw axes and arrows
		g2.setColor(AXIS_COLOR);
		g2.drawLine(yAxisPos, xAxisPos, rRight, xAxisPos);
		g2.drawLine(yAxisPos, xAxisPos, yAxisPos, r.y);
		drawArrows(g2, r, xAxisPos, yAxisPos);

		// draw labels for axes
		g2.drawString(
				model.getXDesc(),
				(yAxisPos + rRight - fm.stringWidth(model.getXDesc())) / 2,
				rDown - fm.getDescent());
		drawYaxisLabel(g2, r, fm, xAxisPos);

		// finish drawing y-axis and horizontal grid
		g2.setFont(boldFont);
		decorateYaxis(g2, fm, rRight, xAxisPos, yAxisPos, heightOfOne);
		g2.setFont(regularFont);

		// finish drawing x-axis and vertical grid; draw bars
		g2.setFont(boldFont);
		decorateXaxisAndDrawBars(g2, r, fm, xAxisPos, yAxisPos,
				heightOfOne, widthOfOne, values, xMin, numOfBars);
		g2.setFont(regularFont);
	}

	/**
	 * Constructs a rectangle that represents free space in which this component can
	 * draw its contents.
	 * 
	 * @return rectangle representing free space, never <code>null</code>
	 */
	private Rectangle getInnerRectangle() {
		final Insets ins = getInsets();
		final Dimension dim = getSize();
		return new Rectangle(
				ins.left,
				ins.top,
				dim.width - ins.left - ins.right,
				dim.height - ins.top - ins.bottom);
	}

	/**
	 * Draws label for <code>y</code> axis without changing the <code>g2</code> when
	 * done.
	 * 
	 * @param g2       graphics object used for drawing
	 * @param r        area in which component can be drawn
	 * @param fm       font metrics
	 * @param xAxisPos position of <code>x</code> axis
	 */
	private void drawYaxisLabel(Graphics2D g2, Rectangle r,
			FontMetrics fm, int xAxisPos) {
		final AffineTransform saveAT = g2.getTransform();
		AffineTransform at = (AffineTransform) saveAT.clone();
		at.rotate(-Math.PI / 2);
		g2.setTransform(at);
		g2.drawString(
				model.getYDesc(),
				-((r.y + xAxisPos + fm.stringWidth(model.getYDesc())) / 2),
				r.x + fm.getAscent());
		g2.setTransform(saveAT);
	}

	/**
	 * Decorates <code>y</code>-axis by adding dashes and appropriate numerical
	 * values. Draws horizontal lines of background grid.<br>
	 * Correct font must be set in <code>g2</code>. Modifies <code>g2</code>'s
	 * color.
	 * 
	 * @param g2          graphics object used for drawing
	 * @param fm          font metrics
	 * @param rRight      right end of free space
	 * @param xAxisPos    position of <code>x</code> axis
	 * @param yAxisPos    position of <code>y</code> axis
	 * @param heightOfOne height for value <code>1</code>
	 */
	private void decorateYaxis(Graphics2D g2, FontMetrics fm,
			int rRight, int xAxisPos, int yAxisPos, double heightOfOne) {
		streamOfYValues().forEach(y -> {
			final int h = mapToHeight(y, xAxisPos, heightOfOne);
			g2.setColor(AXIS_COLOR);
			g2.drawLine(yAxisPos - DASH_LEN, h, yAxisPos, h);
			String yStr = Integer.toString(y);
			g2.drawString(
					yStr,
					yAxisPos - DASH_LEN - NUM_TO_DASH_DIST - fm.stringWidth(yStr),
					h - fm.getHeight() / 2 + fm.getAscent() - 1);
			if (y != model.getYMin()) {
				g2.setColor(GRID_COLOR);
				g2.drawLine(yAxisPos + 1, h, rRight - ARROW_DIST / 2, h);
			}
		});
	}

	/**
	 * Decorates <code>x</code>-axis by adding dashes and appropriate numerical
	 * values. Draws vertical lines of background grid, as well as bars.<br>
	 * Correct font must be set in <code>g2</code>. Modifies <code>g2</code>'s
	 * color.
	 * 
	 * @param g2          graphics object used for drawing
	 * @param rarea       in which component can be drawn
	 * @param fm          font metrics
	 * @param xAxisPos    position of <code>x</code> axis
	 * @param yAxisPos    position of <code>y</code> axis
	 * @param heightOfOne height for value <code>1</code>
	 * @param widthOfOne  width of one bar
	 * @param values      map of values from <code>model</code>
	 * @param xMin        smallest value on <code>x</code>-axis
	 * @param numOfBars   number of bars that will be drawn; greater or equal to
	 *                    size of <code>values</code> (some bars can be skipped)
	 */
	private void decorateXaxisAndDrawBars(Graphics2D g2, Rectangle r, FontMetrics fm,
			int xAxisPos, int yAxisPos, double heightOfOne, double widthOfOne,
			Map<Integer, Integer> values, int xMin, int numOfBars) {

		g2.setColor(AXIS_COLOR);
		g2.drawLine(yAxisPos, xAxisPos, yAxisPos, xAxisPos + DASH_LEN);
		for (int x = 0; x < numOfBars; x++) {
			int wL = mapToWidth(x, yAxisPos, widthOfOne);
			int wR = mapToWidth(x + 1, yAxisPos, widthOfOne);

			g2.setColor(AXIS_COLOR);
			g2.drawLine(wR, xAxisPos, wR, xAxisPos + DASH_LEN);
			String num = Integer.toString(x + xMin);
			g2.drawString(
					num,
					(wR + wL - fm.stringWidth(num)) / 2,
					xAxisPos + DASH_LEN + NUM_TO_DASH_DIST + fm.getAscent());

			g2.setColor(GRID_COLOR);
			g2.drawLine(wR, xAxisPos - 1, wR, r.y + ARROW_DIST / 2);

			Integer mappedH = values.get(x + xMin);
			if (mappedH == null) continue;
			int h = mapToHeight(mappedH, xAxisPos, heightOfOne);
			if (h < r.y) {
				h = r.y + 1;
			}
			g2.setColor(BAR_COLOR);
			g2.fillRect(wL + 1, h, wR - wL - 1, xAxisPos - h - 1);
		}
	}

	/**
	 * Draws arrows on <code>x</code> and <code>y</code> axes. Modifies
	 * <code>g2</code>'s color.
	 * 
	 * @param g2       graphics object used for drawing
	 * @param r        area in which component can be drawn
	 * @param xAxisPos position of <code>x</code> axis
	 * @param yAxisPos position of <code>y</code> axis
	 */
	private void drawArrows(Graphics2D g2, Rectangle r, int xAxisPos, int yAxisPos) {
		int x1 = r.x + r.width - (2 * ARROW_DIST) / 3;
		int y2 = r.y + (2 * ARROW_DIST) / 3;
		int arr3 = ARROW_DIST / 3;
		g2.setColor(AXIS_COLOR);
		g2.fillPolygon(
				new int[] { x1, x1, r.x + r.width },
				new int[] { xAxisPos - arr3, xAxisPos + arr3, xAxisPos },
				3);
		g2.fillPolygon(
				new int[] { yAxisPos - arr3, yAxisPos + arr3, yAxisPos },
				new int[] { y2, y2, r.y },
				3);
	}

	/**
	 * Maps given <code>y</code> value to exact height from top of the component.
	 * 
	 * @param  y           value to map
	 * @param  xAxisPos    position of <code>x</code>-axis
	 * @param  heightOfOne height for value <code>1</code>
	 * @return             height of <code>y</code> value
	 */
	private int mapToHeight(int y, int xAxisPos, double heightOfOne) {
		return (int) Math.round(xAxisPos - heightOfOne * (y - model.getYMin()));
	}

	/**
	 * Maps given <code>x</code> value to exact width from left edge of the
	 * component.
	 * 
	 * @param  x          value to map
	 * @param  yAxisPos   position of <code>y</code>-axis
	 * @param  widthOfOne width of one bar
	 * @return            position for given <code>x</code>
	 */
	private int mapToWidth(int x, int yAxisPos, double widthOfOne) {
		return (int) Math.round(x * widthOfOne + yAxisPos);
	}

	/**
	 * Finds the longest (widest) value shown on <code>y</code>-axis.
	 * 
	 * @param  fm font metrics used for measuring width of strings
	 * @return    width of the widest value shown on <code>y</code>-axis
	 */
	private int longestYNumber(FontMetrics fm) {
		// There will always be at least one element.
		return streamOfYValues()
				.map(x -> fm.stringWidth(Integer.toString(x)))
				.max()
				.orElseThrow();
	}

	/**
	 * Using data from <code>model</code>, creates a stream of <code>y</code> values
	 * to be shown as numbers on the <code>y</code>-axis.
	 * 
	 * @return stream of <code>y</code> values
	 */
	private IntStream streamOfYValues() {
		int max = model.getYMax();
		int min = model.getYMin();
		int delta = model.getYDelta();
		return IntStream
				.rangeClosed(0, (max - min) / delta)
				.map(x -> x * delta + min);
	}

}
