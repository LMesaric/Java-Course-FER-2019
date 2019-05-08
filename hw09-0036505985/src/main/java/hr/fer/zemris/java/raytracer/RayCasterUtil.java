package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * Utility class for a few operations needed in ray casting algorithms.
 * 
 * @author Luka Mesaric
 */
public class RayCasterUtil {

	/** Tolerance when comparing double values and intersections. */
	public static final double TOLERANCE = 1E-9;

	/**
	 * Trace given <code>ray</code> in <code>scene</code>, and fill <code>rgb</code>
	 * with computed data (overwrites old data).
	 * 
	 * @param scene scene in which everything is taking place
	 * @param ray   ray to trace
	 * @param eye   location of the observer
	 * @param rgb   array of length <code>3</code>, for red, green and blue
	 *              components
	 */
	public static void tracer(Scene scene, Ray ray, Point3D eye, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;

		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}
		determineColorFor(closest, scene, eye, rgb);
	}

	/**
	 * Find closest intersection of <code>ray</code> with any object in
	 * <code>scene</code> that is <i>in front of the ray</i>.
	 * 
	 * @param  scene scene in which everything is taking place
	 * @param  ray   ray to intersect with some object
	 * @return       closest intersection, or <code>null</code> if no intersection
	 *               exists
	 */
	public static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection closest = null;
		double closestDistance = Double.MAX_VALUE;
		for (GraphicalObject object : scene.getObjects()) {
			RayIntersection intersection = object.findClosestRayIntersection(ray);
			if (intersection == null)
				continue;
			if (closest == null || intersection.getDistance() < closestDistance) {
				closest = intersection;
				closestDistance = intersection.getDistance();
			}
		}
		return closest;
	}

	/**
	 * Determine color for <code>intersection</code>, from <code>scene</code>, for
	 * an observer looking from <code>eye</code>. Calculated data is stored in given
	 * <code>rgb</code> (overwrites old data).
	 * 
	 * @param intersection intersection whose color needs to be determined
	 * @param scene        scene in which everything is taking place
	 * @param eye          location of the observer
	 * @param rgb          array of length <code>3</code>, for red, green and blue
	 *                     components
	 */
	public static void determineColorFor(
			RayIntersection intersection, Scene scene, Point3D eye, short[] rgb) {

		// ambient component
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;

		for (LightSource lightSource : scene.getLights()) {
			Point3D realInter = intersection.getPoint();
			Point3D lightSourcePos = lightSource.getPoint();
			Ray lightRay = Ray.fromPoints(lightSourcePos, realInter);
			RayIntersection lightInter = findClosestIntersection(scene, lightRay);

			if (lightInter == null) {
				// Numerical error. This intersection must exist
				// because of the way this ray was constructed.
				lightInter = intersection;
			} else {
				// Avoid the use of Math#sqrt(double) in norm()
				Point3D toRealInter = realInter.sub(lightSourcePos);
				Point3D toLightInter = lightInter.getPoint().sub(lightSourcePos);
				double toRealInterDistSq = toRealInter.scalarProduct(toRealInter);
				double toLightInterDistSq = toLightInter.scalarProduct(toLightInter);
				if (toRealInterDistSq > toLightInterDistSq + TOLERANCE) {
					continue;
				}
			}

			Point3D normal = lightInter.getNormal();
			Point3D lightRayDir = lightRay.direction;
			double ln = -lightRayDir.scalarProduct(normal);
			Point3D reflected = lightRayDir.sub(
					normal.scalarMultiply(
							2 * lightRayDir.scalarProduct(normal)))
					.modifyNormalize();

			double rv = reflected.scalarProduct(eye.sub(realInter).modifyNormalize());
			double rvn = rv <= 0 ? 0 : Math.pow(rv, intersection.getKrn());

			rgb[0] += lightSource.getR()
					* (lightInter.getKdr() * ln + lightInter.getKrr() * rvn);
			rgb[1] += lightSource.getG()
					* (lightInter.getKdg() * ln + lightInter.getKrg() * rvn);
			rgb[2] += lightSource.getB()
					* (lightInter.getKdb() * ln + lightInter.getKrb() * rvn);
		}
	}
}
