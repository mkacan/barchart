package hr.marin.barchart;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The class represents a 2D bar chart model. It contains a list of values as
 * well as data description and the range of y values that are drawn.
 * 
 * @author Marin
 *
 */
public class BarChart {
	/**
	 * All the drawn values in the bar chart.
	 */
	private List<XYValue> values;
	/**
	 * A description of the data on the x axis.
	 */
	private String xDescription;
	/**
	 * A description of the data on the y axis.
	 */
	private String yDescription;
	/**
	 * The minimum y value that is drawn.
	 */
	private int yMin;
	/**
	 * The maximum y value that is drawn.
	 */
	private int yMax;
	/**
	 * The interval between each two adjacent y values that are written on the y
	 * axis.
	 */
	private int yDelta;

	/**
	 * Create a new bar chart model with the given values.
	 * 
	 * @param values
	 *            All the values in the chart.
	 * @param xDescription
	 *            A description of the data on the x axis.
	 * @param yDescription
	 *            A description of the data on the y axis.
	 * @param yMin
	 *            The minimum y value that is drawn.
	 * @param yMax
	 *            The maximum y value that is drawn.
	 * @param yDelta
	 *            The interval between each two adjacent y values that are
	 *            written on the y axis.
	 */
	public BarChart(List<XYValue> values, String xDescription, String yDescription, int yMin, int yMax, int yDelta) {
		super();

		if (yMin >= yMax) {
			throw new IllegalArgumentException("yMax should be larger than yMin.");
		}

		if (yDelta <= 0) {
			throw new IllegalArgumentException("yDelta should be positive.");
		}

		this.values = values;
		Collections.sort(this.values, new Comparator<XYValue>() {
			public int compare(XYValue o1, XYValue o2) {
				return Integer.valueOf(o1.getX()).compareTo(Integer.valueOf(o2.getX()));
			}
		});
		
		this.xDescription = xDescription;
		this.yDescription = yDescription;
		this.yMin = yMin;
		this.yDelta = yDelta;

		this.yMax = determineYMax(yMin, yMax, yDelta);
	}

	/**
	 * A helper method that calculates the maximum y value that is going to be
	 * drawn. If the range yStart to yEnd is divisible by delta, yMax is yEnd.
	 * Otherwise, yMax is the smallest integer value larger than yEnd for which
	 * the range yStart to yMax is divisible by delta.
	 * 
	 * @param yStart
	 *            The minimum y value that is going to be drawn.
	 * @param yEnd
	 *            The lower bound of the maximum y value that is going to be
	 *            drawn.
	 * @param delta
	 *            The interval that has to divide the range yStart to yMax
	 *            without a remainder.
	 * @return A correct value for yMax.
	 */
	private int determineYMax(int yStart, int yEnd, int delta) {
		int range = yEnd - yStart;

		if (range % delta == 0) {
			return yEnd;
		}

		return ((range / delta + 1) * delta);
	}

	/**
	 * Gets all the XYValues contained in the chart.
	 * 
	 * @return A list of XYValues.
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Gets the description of the data on the x axis.
	 * 
	 * @return A textual description of the x axis data.
	 */
	public String getXDescription() {
		return xDescription;
	}

	/**
	 * Gets the description of the data on the y axis.
	 * 
	 * @return A textual description of the y axis data.
	 */
	public String getYDescription() {
		return yDescription;
	}

	/**
	 * The minimum y value that is drawn.
	 * 
	 * @return The minimum y value.
	 */
	public int getYMin() {
		return yMin;
	}

	/**
	 * The maximum y value that is drawn.
	 * 
	 * @return The maximum y value.
	 */
	public int getYMax() {
		return yMax;
	}

	/**
	 * The interval between each two adjacent y values that are written on the y
	 * axis.
	 * 
	 * @return The y axis value interval.
	 */
	public int getYDelta() {
		return yDelta;
	}
}
