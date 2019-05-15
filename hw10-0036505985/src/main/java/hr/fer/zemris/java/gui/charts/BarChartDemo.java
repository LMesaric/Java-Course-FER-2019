package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program takes a single command-line argument - path to file containing
 * description of a bar chart that will be shown in GUI.
 * 
 * @author Luka Mesaric
 */
public class BarChartDemo extends JFrame {

	/** Serial version UID. */
	private static final long serialVersionUID = 5151434034409506855L;

	/**
	 * Program entry point.
	 * 
	 * @param args path to file containing the description of the bar chart
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected exactly one argument!");
			return;
		}

		Path path;
		try {
			path = Paths.get(args[0]).toAbsolutePath().normalize();
		} catch (InvalidPathException e) {
			System.out.println("Given argument cannot be interpreted as a path.");
			return;
		}

		BarChart model;
		try {
			model = getBarChartFromFile(path);
		} catch (IOException e) {
			System.out.println("Could not read data from given file.");
			return;
		} catch (NumberFormatException e) {
			System.out.println("Could not parse number: " + e.getMessage());
			return;
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}

		SwingUtilities.invokeLater(() -> {
			new BarChartDemo(model, path.toString()).setVisible(true);
		});
	}

	/**
	 * Parses contents of <code>file</code> and creates a <code>BarChart</code>
	 * based on its content. Reads only first <code>6</code> lines of the file and
	 * ignores any others.
	 * 
	 * @param  file                     file from which to read data
	 * @return                          created <code>BarChart</code>
	 * @throws IOException              if <code>file</code> could not be read
	 * @throws NumberFormatException    if any part of file should have been an
	 *                                  integer but cannot not be parsed into one
	 * @throws IllegalArgumentException if there were less than <code>6</code> lines
	 *                                  in file, or if some pair was not correctly
	 *                                  constructed
	 */
	private static BarChart getBarChartFromFile(Path file) throws IOException {
		List<String> lines = Files.readAllLines(file);
		if (lines.size() < 6) {
			throw new IllegalArgumentException("Not enough lines in file.");
		}
		String xDesc = lines.get(0).strip();
		String yDesc = lines.get(1).strip();
		int yMin = Integer.parseInt(lines.get(3).strip());
		int yMax = Integer.parseInt(lines.get(4).strip());
		int yDelta = Integer.parseInt(lines.get(5).strip());

		List<XYValue> values = Arrays.stream(lines.get(2).strip().split("\\s+"))
				.map(BarChartDemo::parseValue)
				.collect(Collectors.toList());
		return new BarChart(values, xDesc, yDesc, yMin, yMax, yDelta);
	}

	/**
	 * Parses an <code>XYValue</code> from string formatted as <code>x,y</code>.
	 * 
	 * @param  s                        string to parse
	 * @return                          parsed <code>XYValue</code>, never
	 *                                  <code>null</code>
	 * @throws NumberFormatException    if <code>x</code> or <code>y</code> do not
	 *                                  represent integers
	 * @throws IllegalArgumentException if <code>s</code> does not contain exactly
	 *                                  two values (i.e. exactly one comma)
	 */
	private static XYValue parseValue(String s) {
		String[] parts = s.split(",");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Bad pair: " + s);
		}
		return new XYValue(
				Integer.parseInt(parts[0]),
				Integer.parseInt(parts[1]));
	}

	/**
	 * Default constructor.
	 * 
	 * @param  model                model of a bar chart shown by this frame
	 * @param  text                 text shown in a label above the bar chart; if
	 *                              <code>null</code> label is not shown
	 * @throws NullPointerException if <code>model</code> is <code>null</code>
	 */
	public BarChartDemo(BarChart model, String text) {
		Objects.requireNonNull(model);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar Chart v1.0");
		initGUI(model, text);
		setSize(800, 600);
		setLocationRelativeTo(null);
	}

	/**
	 * Initializes GUI.
	 * 
	 * @param model model of a bar chart shown by this frame
	 * @param text  text shown in a label above the bar chart; if <code>null</code>
	 *              label is not shown
	 */
	private void initGUI(BarChart model, String text) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		cp.add(new BarChartComponent(model), BorderLayout.CENTER);
		cp.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.PAGE_START);
	}

}
