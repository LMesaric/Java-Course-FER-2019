package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * Implementation of <code>IRayTracerProducer</code> which can create scene
 * snapshots of a given <code>scene</code> by using ray casting algorithm and a
 * thread pool.
 * 
 * @author Luka Mesaric
 */
public class RayTracerProducerImpl implements IRayTracerProducer {

	/** Thread pool used for execution. */
	private final ForkJoinPool pool;
	/** Scene in which everything is taking place. */
	private final Scene scene;

	/**
	 * Default constructor.
	 * 
	 * @param scene scene in which everything is taking place
	 */
	public RayTracerProducerImpl(Scene scene) {
		this.scene = scene;
		this.pool = new ForkJoinPool();
	}

	@Override
	public void produce(
			Point3D eye, Point3D view, Point3D viewUp,
			double horizontal, double vertical,
			int width, int height,
			long requestNo,
			IRayTracerResultObserver observer,
			AtomicBoolean cancel) {

		// Uncomment if you want to spice things up :)
//		 scene.add(new Sphere(
//		 new Point3D(100, 0, 0), 400, .5, .5, .5, .5, .5, .5, 10));

		System.out.println("Započinjem izračune...");
		short[] red = new short[width * height];
		short[] green = new short[width * height];
		short[] blue = new short[width * height];

		Point3D viewUpNomalized = viewUp.normalize();
		Point3D zAxis = eye.sub(view).modifyNormalize();
		Point3D yAxis = zAxis
				.scalarMultiply(
						zAxis.scalarProduct(viewUpNomalized))
				.modifySub(viewUpNomalized)
				.modifyNormalize();
		Point3D xAxis = zAxis.vectorProduct(yAxis);

		Point3D screenCorner = view
				.sub(xAxis.scalarMultiply(horizontal / 2))
				.modifySub(yAxis.scalarMultiply(vertical / 2));

		final double xScaler = horizontal / (width - 1);
		final double yScaler = vertical / (height - 1);

		pool.invoke(new RayCasterJob(red, green, blue, width, 0, height - 1,
				xScaler, yScaler, eye, xAxis, yAxis,
				screenCorner, scene, cancel));

		System.out.println("Izračuni gotovi...");
		observer.acceptResult(red, green, blue, requestNo);
		System.out.println("Dojava gotova...");
	}

}
