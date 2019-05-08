package hr.fer.zemris.java.raytracer;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * Job rendering a part of scene snapshot by using ray casting.
 * 
 * @author Luka Mesaric
 */
public class RayCasterJob extends RecursiveAction {

	/** Serial version UID. */
	private static final long serialVersionUID = -2348029102270843966L;

	/** Treshold for stopping recursive forking. */
	private static final int THRESHOLD = 16;

	/** Array to be filled with calculated data for red component. */
	private final short[] red;
	/** Array to be filled with calculated data for green component. */
	private final short[] green;
	/** Array to be filled with calculated data for blue component. */
	private final short[] blue;
	/** Width of canvas. */
	private final int width;
	/** Index of starting line, inclusive. */
	private final int yMin;
	/** Index of ending line, inclusive. */
	private final int yMax;
	/** Scaling factor for remapping pixel to 3D position, for x-axis. */
	private final double xScaler;
	/** Scaling factor for remapping pixel to 3D position, for y-axis. */
	private final double yScaler;
	/** Observer's position. */
	private final Point3D eye;
	/** Direction of <code>x</code>-axis, unit vector. */
	private final Point3D xAxis;
	/** Direction of <code>y</code>-axis, unit vector. */
	private final Point3D yAxis;
	/** Position of top-left corner of the screen. */
	private final Point3D screenCorner;
	/** Scene in which everything is taking place. */
	private final Scene scene;
	/** Flag for cancelling outdated calculations. */
	private final AtomicBoolean cancel;

	/**
	 * Default constructor.
	 * 
	 * @param red          array to be filled with calculated data for red component
	 * @param green        array to be filled with calculated data for green
	 *                     component
	 * @param blue         array to be filled with calculated data for blue
	 *                     component
	 * @param width        width of canvas
	 * @param yMin         index of starting line, inclusive
	 * @param yMax         index of ending line, inclusive
	 * @param xScaler      scaling factor for remapping pixel to 3D position, for
	 *                     x-axis
	 * @param yScaler      scaling factor for remapping pixel to 3D position, for
	 *                     y-axis
	 * @param eye          observer's position
	 * @param xAxis        direction of <code>x</code>-axis, unit vector
	 * @param yAxis        direction of <code>y</code>-axis, unit vector
	 * @param screenCorner position of top-left corner of the screen
	 * @param scene        scene in which everything is taking place
	 * @param cancel       flag for cancelling outdated calculations
	 */
	public RayCasterJob(short[] red, short[] green, short[] blue,
			int width, int yMin, int yMax,
			double xScaler, double yScaler,
			Point3D eye, Point3D xAxis, Point3D yAxis, Point3D screenCorner,
			Scene scene, AtomicBoolean cancel) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.width = width;
		this.yMin = yMin;
		this.yMax = yMax;
		this.xScaler = xScaler;
		this.yScaler = yScaler;
		this.eye = eye;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.screenCorner = screenCorner;
		this.scene = scene;
		this.cancel = cancel;
	}

	@Override
	protected void compute() {
		if (yMax - yMin + 1 <= THRESHOLD) {
			computeDirect();
			return;
		}

		final int yMid = yMin + (yMax - yMin) / 2;
		invokeAll(
				new RayCasterJob(
						red, green, blue,
						width, yMin, yMid,
						xScaler, yScaler,
						eye, xAxis, yAxis, screenCorner,
						scene, cancel),
				new RayCasterJob(
						red, green, blue,
						width, yMid + 1, yMax,
						xScaler, yScaler,
						eye, xAxis, yAxis, screenCorner,
						scene, cancel));
	}

	/**
	 * Directly compute red, green and blue components for rows in range
	 * <code>[yMin, yMax]</code>.
	 */
	private void computeDirect() {
		short[] rgb = new short[3];
		int offset = width * yMin;
		for (int y = yMin; y <= yMax; y++) {
			if (cancel.get()) break;
			for (int x = 0; x < width; x++, offset++) {
				Point3D screenPoint = screenCorner
						.add(xAxis.scalarMultiply(x * xScaler))
						.modifyAdd(yAxis.scalarMultiply(y * yScaler));
				Ray ray = Ray.fromPoints(eye, screenPoint);

				RayCasterUtil.tracer(scene, ray, eye, rgb);

				red[offset] = rgb[0] > 255 ? 255 : rgb[0];
				green[offset] = rgb[1] > 255 ? 255 : rgb[1];
				blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
			}
		}
	}
}
