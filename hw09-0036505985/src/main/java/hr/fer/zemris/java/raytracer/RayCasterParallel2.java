package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Renders snapshots of a {@linkplain RayTracerViewer#createPredefinedScene2()
 * predefined scene} by using ray casting algorithm and a thread pool. Snapshots
 * are shown in GUI as an animation.
 * 
 * @author Luka Mesaric
 */
public class RayCasterParallel2 {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(
				getIRayTracerProducer(),
				getIRayTracerAnimator(),
				30, 30);
	}

	/**
	 * Create an instance of <code>IRayTracerAnimator</code> which is used for
	 * animating the movement of an observer in a scene. Animation is not actually
	 * dependent on a concrete scene.
	 * 
	 * @return an instance of <code>IRayTracerAnimator</code>
	 */
	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {

			/** Total elapsed time. */
			private long time;

			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}

			@Override
			public Point3D getViewUp() {
				// fixed in time
				return new Point3D(0, 0, 10);
			}

			@Override
			public Point3D getView() {
				// fixed in time
				return new Point3D(-2, 0, -0.5);
			}

			@Override
			public long getTargetTimeFrameDuration() {
				// redraw scene every 100 milliseconds
				return 100;
			}

			@Override
			public Point3D getEye() {
				// changes in time
				double t = (double) time / 10000 * 2 * Math.PI;
				double t2 = (double) time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}
		};
	}

	/**
	 * Create an instance of <code>IRayTracerProducer</code> which can create scene
	 * snapshots of a {@linkplain RayTracerViewer#createPredefinedScene2()
	 * predefined scene}.
	 * 
	 * @return an instance of <code>IRayTracerProducer</code>
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new RayTracerProducerImpl(RayTracerViewer.createPredefinedScene2());
	}

}
