package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Renders a single snapshot of a
 * {@linkplain RayTracerViewer#createPredefinedScene() predefined scene} by
 * using ray casting algorithm and a thread pool. Snapshot is shown in GUI.
 * 
 * @author Luka Mesaric
 */
public class RayCasterParallel {

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
		return new RayTracerProducerImpl(RayTracerViewer.createPredefinedScene());
	}

}
