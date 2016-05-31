package hr.marin.barchart;

/**
 * The class represents a pair of values, x and y. It can also be thought of a
 * point in a 2D cartesian coordinate system.
 * 
 * @author Marin
 *
 */
public class XYValue {
	/**
	 * x value
	 */
	private int x;
	/**
	 * y value
	 */
	private int y;

	/**
	 * Creates a new pair of values.
	 * 
	 * @param x
	 *            First value (the x value).
	 * @param y
	 *            Second value (the y value).
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x value.
	 * 
	 * @return The x value.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y value.
	 * 
	 * @return The y value.
	 */
	public int getY() {
		return y;
	}
}
