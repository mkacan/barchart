package hr.marin.barchart;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The program draws a bar chart from the data given in a text file. The path to
 * the text file must be given as the first command-line argument.
 * 
 * @author Marin
 *
 */
public class BarChartDemo extends JFrame {
	/**
	 * Creates the frame in which the bar chart is drawn.
	 * @param barChart The bar chart that is drawn.
	 * @param path The path to the file containing the bar chart data.
	 */
	public BarChartDemo(BarChart barChart, Path path) {
		setLocation(20, 50);
		setSize(800, 600);
		setTitle("Bar chart");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI(barChart, path);

	}

	/**
	 * Initializes all the elements of the GUI.
	 * @param barChart The bar chart that is drawn.
	 * @param path The path to the file containing the bar chart data.
	 */
	private void initGUI(BarChart barChart, Path path) {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabel(path.toAbsolutePath().toString(), SwingConstants.CENTER), BorderLayout.NORTH);
		getContentPane().add(new BarChartComponent(barChart), BorderLayout.CENTER);
	}

	/**
	 * This method is called once the program is run.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Must provide the path to the file as the argument.");
			System.exit(1);
		}
		Path path = null;
		try {
			path = Paths.get(args[0]);
		} catch (InvalidPathException e) {
			System.out.println("The given path is invalid.");
			System.exit(1);
		}

		List<String> lines = null;

		try {
			lines = Files.readAllLines(path);
		} catch (Exception e) {
			System.out.println("Error while reading the file.");
			System.exit(1);
		}

		List<XYValue> xyValues = null;
		BarChart bc = null;
		try {
			xyValues = generateXYList(lines.get(2));
			bc = new BarChart(xyValues, lines.get(0), lines.get(1), Integer.parseInt(lines.get(3)),
					Integer.parseInt(lines.get(4)), Integer.parseInt(lines.get(5)));
		} catch (RuntimeException e) {
			System.out.println("Invalid data given.");
			System.exit(1);
		}

		final BarChart barChart = bc;
		final Path filePath = path;

		SwingUtilities.invokeLater(() -> {
			BarChartDemo bcDemo = new BarChartDemo(barChart, filePath);
			bcDemo.setVisible(true);
		});
	}

	/**
	 * Method takes a string and generates a list of x-y value pairs.
	 * @param string String containing x-y value pairs.
	 * @return The list of all the value pairs.
	 */
	private static List<XYValue> generateXYList(String string) {
		String[] split = string.trim().split(" +");
		List<XYValue> list = new ArrayList<>();

		int x, y;
		String[] xySplit;
		for (String xyStr : split) {
			xySplit = xyStr.split(",");
			x = Integer.parseInt(xySplit[0]);
			y = Integer.parseInt(xySplit[1]);
			list.add(new XYValue(x, y));
		}

		return list;
	}
}
