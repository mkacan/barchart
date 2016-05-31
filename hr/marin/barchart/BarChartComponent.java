package hr.marin.barchart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JComponent;

/**
 * The class represents a bar chart that can be drawn.
 * 
 * @author Marin
 *
 */
public class BarChartComponent extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A defined constant for the space between text.
	 */
	private static final int FIXED_SPACE = 10;
	/**
	 * A defined constant for various spaces in the chart.
	 */
	private static final int ARROW_SPACE = 10;

	/**
	 * The {@link BarChart} objects that is backing this component.
	 */
	private BarChart barChart;

	/**
	 * Creates a new {@link BarChartComponent} with the given {@link BarChart}
	 * to back it.
	 * 
	 * @param barChart
	 *            The bar chart that will get drawn.
	 */
	public BarChartComponent(BarChart barChart) {
		this.barChart = barChart;
		setOpaque(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();

		Dimension size = getSize();
		Insets ins = getInsets();

		Rectangle rect = new Rectangle(ins.left, ins.top, size.width - ins.left - ins.right, size.height - ins.top
				- ins.bottom);

		if (isOpaque()) {
			g2d.setColor(getBackground());
			g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
		}

		g2d.setColor(Color.BLACK);
		Rectangle current = drawAxisDescriptions(rect.x, rect.y, rect.width, rect.height, g2d);

		g2d.setColor(Color.BLACK);
		current = drawAxisNumbers(current.x, current.y, current.width, current.height, g2d);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(current.x, current.y, current.width, current.height);

		g2d.setColor(Color.BLACK);
		current = drawGrid(current.x, current.y, current.width, current.height, g2d);

		g2d.setColor(Color.RED);
		drawColumns(current.x, current.y, current.width, current.height, g2d);

		g2d.dispose();
	}

	/**
	 * Method draws the columns of the bar chart.
	 * @param x The x coordinate of the upper left corner of the drawable area.
	 * @param y The y coordinate of the upper left corner of the drawable area.
	 * @param width The width of the drawable area.
	 * @param height The height of the drawable area.
	 * @param g2d The {@link Graphics2D} object used for drawing various shapes.
	 * @return The dimensions of the new drawable area.
	 */
	private Rectangle drawColumns(int x, int y, int width, int height, Graphics2D g2d) {
		int yMax = barChart.getYMax();
		int yMin = barChart.getYMin();
		int yRange = yMax - yMin;

		List<XYValue> values = barChart.getValues();

		int stepWidth = width / values.size();

		int barHeight;
		int counter = 0;
		for (XYValue value : values) {
			barHeight = ((value.getY() - yMin) * height) / yRange;
			g2d.fillRect(x + counter * stepWidth + 1, y + (height - barHeight), stepWidth - 2, barHeight);
			counter++;
		}

		return null;
	}

	/**
	 * Method draws the grid of the bar chart.
	 * @param x The x coordinate of the upper left corner of the drawable area.
	 * @param y The y coordinate of the upper left corner of the drawable area.
	 * @param width The width of the drawable area.
	 * @param height The height of the drawable area.
	 * @param g2d The {@link Graphics2D} object used for drawing various shapes.
	 * @return The dimensions of the new drawable area.
	 */
	private Rectangle drawGrid(int x, int y, int width, int height, Graphics2D g2d) {
		FontMetrics fm = g2d.getFontMetrics();

		int maxY = barChart.getYMax();
		int minY = barChart.getYMin();
		int yDelta = barChart.getYDelta();

		int numHeightSteps = (maxY - minY) / yDelta;
		int stepHeight = (height - fm.getAscent()) / ((barChart.getYMax() - barChart.getYMin()) / barChart.getYDelta());

		int startY = y + height - fm.getAscent() / 2;

		g2d.setColor(Color.DARK_GRAY);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(x, startY, x + width, startY);

		g2d.setColor(Color.LIGHT_GRAY);

		int counter = 1;
		for (int i = minY + yDelta; i <= maxY; i += yDelta) {
			g2d.drawLine(x, startY - counter * stepHeight, x + width - fm.getAscent() / 2, startY - counter
					* stepHeight);
			counter++;
		}

		List<XYValue> values = barChart.getValues();
		int size = values.size();
		int stepWidth = (width - fm.getAscent()) / size;
		int startX = x + fm.getAscent() / 2;

		g2d.setColor(Color.DARK_GRAY);
		g2d.drawLine(startX, y, startX, y + height);
		g2d.setColor(Color.LIGHT_GRAY);
		for (int i = 1; i <= size; i++) {
			g2d.drawLine(startX + i * stepWidth, y + fm.getAscent() / 2, startX + i * stepWidth, y + height);
		}

		g2d.setStroke(new BasicStroke(1));

		g2d.setColor(Color.DARK_GRAY);
		int arrowLength = 10;
		int arrowWidth = 10;
		Point xArrowTop = new Point(startX, y - 1);
		int[] xPoints = { xArrowTop.x, xArrowTop.x - arrowWidth / 2, xArrowTop.x + arrowWidth / 2 };
		int[] yPoints = { xArrowTop.y, xArrowTop.y + arrowLength, xArrowTop.y + arrowLength };
		g2d.fillPolygon(xPoints, yPoints, 3);

		Point yArrowTop = new Point(x + width + 1, startY);
		int[] xPoints2 = { yArrowTop.x, yArrowTop.x - arrowLength, yArrowTop.x - arrowLength };
		int[] yPoints2 = { yArrowTop.y, yArrowTop.y - arrowWidth / 2, yArrowTop.y + arrowWidth / 2 };
		g2d.fillPolygon(xPoints2, yPoints2, 3);

		Rectangle after = new Rectangle(startX, startY - numHeightSteps * stepHeight, size * stepWidth, numHeightSteps
				* stepHeight);

		return after;
	}

	/**
	 * Method draws the axis numbers that represent values of the bar chart.
	 * @param x The x coordinate of the upper left corner of the drawable area.
	 * @param y The y coordinate of the upper left corner of the drawable area.
	 * @param width The width of the drawable area.
	 * @param height The height of the drawable area.
	 * @param g2d The {@link Graphics2D} object used for drawing various shapes.
	 * @return The dimensions of the new drawable area.
	 */
	private Rectangle drawAxisNumbers(int x, int y, int width, int height, Graphics2D g2d) {
		int stepHeight = (height - 2 * ARROW_SPACE - ARROW_SPACE)
				/ ((barChart.getYMax() - barChart.getYMin()) / barChart.getYDelta());
		FontMetrics fm = g2d.getFontMetrics();

		int maxY = barChart.getYMax();
		int yDelta = barChart.getYDelta();
		int yNumWidth = fm.stringWidth(Integer.toString(maxY));

		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));

		String numStr;
		int counter = 0;
		for (int i = barChart.getYMin(); i <= maxY; i += yDelta) {
			numStr = Integer.toString(i);
			g2d.drawString(numStr, x + (yNumWidth - fm.stringWidth(numStr)), y + height - fm.getAscent() / 2
					- ARROW_SPACE - counter * stepHeight);
			counter++;
		}

		List<XYValue> values = barChart.getValues();
		int stepWidth = (width - yNumWidth - ARROW_SPACE - ARROW_SPACE) / values.size();
		
		counter = 0;
		for (XYValue value : values) {
			numStr = Integer.toString(value.getX());
			
			g2d.drawString(numStr, x + yNumWidth + ARROW_SPACE + (stepWidth - fm.stringWidth(numStr)) / 2 + counter
					* stepWidth, y + height);
			counter++;
		}

		g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN));

		Rectangle after = new Rectangle(x + yNumWidth + ARROW_SPACE / 2, y, width - yNumWidth - ARROW_SPACE, height - 2
				* ARROW_SPACE + fm.getAscent() / 2);

		return after;
	}

	/**
	 * Method draws descriptions of the data that each axis represents in the bar chart.
	 * @param x The x coordinate of the upper left corner of the drawable area.
	 * @param y The y coordinate of the upper left corner of the drawable area.
	 * @param width The width of the drawable area.
	 * @param height The height of the drawable area.
	 * @param g2d The {@link Graphics2D} object used for drawing various shapes.
	 * @return The dimensions of the new drawable area.
	 */
	private Rectangle drawAxisDescriptions(int x, int y, int width, int height, Graphics2D g2d) {
		Rectangle after = new Rectangle(x, y, width, height);

		String xDesc = barChart.getXDescription();

		FontMetrics fm = g2d.getFontMetrics();
		int xStrWidth = fm.stringWidth(xDesc);

		g2d.drawString(xDesc, x + (width - xStrWidth) / 2, y + height - fm.getDescent());

		String yDesc = barChart.getYDescription();
		int yStrWidth = fm.stringWidth(yDesc);

		AffineTransform defaultAt = g2d.getTransform();
		AffineTransform at = AffineTransform.getQuadrantRotateInstance(3);
		g2d.setTransform(at);

		g2d.drawString(yDesc, -(y + (height + yStrWidth) / 2), x + fm.getAscent());

		g2d.setTransform(defaultAt);

		after.height = after.height - fm.getDescent() - fm.getAscent() - FIXED_SPACE;
		after.width = after.width - fm.getDescent() - fm.getAscent() - FIXED_SPACE;
		after.x = after.x + (width - after.width);

		return after;
	}
}
