package hr.fer.zemris.java.raytracer;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Renders a single snapshot of a
 * {@linkplain RayTracerViewer#createPredefinedScene() predefined scene} by
 * using ray casting algorithm and a single thread. Snapshot is shown in GUI.
 * 
 * @author Luka Mesaric
 */
public class RayCaster {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(
				getIRayTracerProducer(),
				new Point3D(10, 0, 0),
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10),
				20, 20);
	}

	/**
	 * Create an instance of <code>IRayTracerProducer</code> which can create scene
	 * snapshots of a {@linkplain RayTracerViewer#createPredefinedScene() predefined
	 * scene}.
	 * 
	 * @return an instance of <code>IRayTracerProducer</code>
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(
					Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical,
					int width, int height,
					long requestNo,
					IRayTracerResultObserver observer,
					AtomicBoolean cancel) {

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

				Scene scene = RayTracerViewer.createPredefinedScene();

				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					if (cancel.get()) break;
					for (int x = 0; x < width; x++) {
						double xFactor = x / (width - 1.0) * horizontal;
						double yFactor = y / (height - 1.0) * vertical;
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply(xFactor))
								.modifyAdd(yAxis.scalarMultiply(yFactor));
						Ray ray = Ray.fromPoints(eye, screenPoint);

						RayCasterUtil.tracer(scene, ray, eye, rgb);

						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

						offset++;
					}
				}

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
}
