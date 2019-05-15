package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model of a simple bar chart.
 * 
 * @author Luka Mesaric
 */
public class BarChart {

	/**
	 * Unmodifiable list of <code>XYValues</code> to be shown in chart. Contains no
	 * <code>null</code> values.
	 */
	private final List<XYValue> values;
	/** Description of <code>x</code>-axis. */
	private final String xDesc;
	/** Description of <code>y</code>-axis. */
	private final String yDesc;
	/** Minimum <code>y</code> value shown on <code>y</code>-axis. */
	private final int yMin;
	/** Maximum <code>y</code> value shown on <code>y</code>-axis. */
	private final int yMax;
	/** Difference between two <code>y</code> value shown on <code>y</code>-axis. */
	private final int yDelta;

	/**
	 * Default constructor.
	 * 
	 * @param  values                   list of <code>XYValues</code> to be shown in
	 *                                  this bar chart
	 * @param  xDesc                    description of <code>x</code>-axis
	 * @param  yDesc                    description of <code>y</code>-axis
	 * @param  yMin                     minimum <code>y</code> value shown on
	 *                                  <code>y</code>-axis
	 * @param  yMax                     maximum <code>y</code> value shown on
	 *                                  <code>y</code>-axis
	 * @param  yDelta                   difference between two <code>y</code> value
	 *                                  shown on <code>y</code>-axis
	 * @throws NullPointerException     if any argument is <code>null</code>
	 * @throws IllegalArgumentException if <code>yDelta</code> is <code>0</code> or
	 *                                  less, if <code>yMin</code> is negative, if
	 *                                  {@code yMax <= yMin}, if any <code>y</code>
	 *                                  value from <code>values</code> is less than
	 *                                  <code>yMin</code>
	 */
	public BarChart(
			List<XYValue> values, String xDesc, String yDesc,
			final int yMin, final int yMax, final int yDelta) {
		Objects.requireNonNull(values);
		Objects.requireNonNull(xDesc);
		Objects.requireNonNull(yDesc);

		this.xDesc = xDesc;
		this.yDesc = yDesc;

		if (yDelta <= 0) {
			throw new IllegalArgumentException(
					"yDelta must not be positive: " + yDelta);
		}
		this.yDelta = yDelta;

		if (yMin < 0) {
			throw new IllegalArgumentException(
					"yMin must not be negative: " + yMin);
		}
		this.yMin = yMin;

		if (yMax <= yMin) {
			throw new IllegalArgumentException(
					"yMax must be greater than yMin: " + yMax);
		}

		int yDiff = yMax - yMin;
		if ((yDiff % yDelta) != 0) {
			this.yMax = yMin + yDelta * (1 + (yDiff / yDelta));
		} else {
			this.yMax = yMax;
		}

		for (XYValue value : values) {
			// implicit check if value is null
			if (value.y < yMin) {
				throw new IllegalArgumentException(
						"XYValues must not be less than yMin: " + value.y);
			}
		}
		this.values = Collections.unmodifiableList(new ArrayList<>(values));
	}

	/**
	 * Getter for <code>values</code>.
	 *
	 * @return <code>values</code> as an unmodifiable list
	 */
	public List<XYValue> getValues() { return values; }

	/**
	 * Getter for <code>xDesc</code>.
	 *
	 * @return <code>xDesc</code>
	 */
	public String getXDesc() { return xDesc; }

	/**
	 * Getter for <code>yDesc</code>.
	 *
	 * @return <code>yDesc</code>
	 */
	public String getYDesc() { return yDesc; }

	/**
	 * Getter for <code>yMin</code>.
	 *
	 * @return <code>yMin</code>
	 */
	public int getYMin() { return yMin; }

	/**
	 * Getter for <code>yMax</code>.
	 *
	 * @return <code>yMax</code>
	 */
	public int getYMax() { return yMax; }

	/**
	 * Getter for <code>yDelta</code>.
	 *
	 * @return <code>yDelta</code>
	 */
	public int getYDelta() { return yDelta; }

}
