package hr.fer.zemris.java.raytracer.model;

import java.util.Objects;

/**
 * Represents a sphere that can exist in {@linkplain Scene} and interact with
 * {@linkplain Ray rays}.<br>
 * Sphere is determined by its {@linkplain #center} and {@linkplain #radius}.
 * 
 * @author Luka Mesaric
 */
public class Sphere extends GraphicalObject {

	/** Center of sphere. */
	private final Point3D center;
	/** Radius of sphere. */
	private final double radius;
	/** Radius of sphere, squared. */
	private final double radiusSquared;
	/** Coefficient of diffusion for red light. */
	private final double kdr;
	/** Coefficient of diffusion for green light. */
	private final double kdg;
	/** Coefficient of diffusion for blue light. */
	private final double kdb;
	/** Coefficient of reflection for red light. */
	private final double krr;
	/** Coefficient of reflection for green light. */
	private final double krg;
	/** Coefficient of reflection for blue light. */
	private final double krb;
	/** Coefficient of specular reflectivity (shininess factor). */
	private final double krn;

	/**
	 * Default constructor.
	 * 
	 * @param  center               center of sphere
	 * @param  radius               radius of sphere
	 * @param  kdr                  coefficient of diffusion for red light
	 * @param  kdg                  coefficient of diffusion for green light
	 * @param  kdb                  coefficient of diffusion for blue light
	 * @param  krr                  coefficient of reflection for red light
	 * @param  krg                  coefficient of reflection for green light
	 * @param  krb                  coefficient of reflection for blue light
	 * @param  krn                  coefficient of specular reflectivity (shininess
	 *                              factor)
	 * @throws NullPointerException if <code>center</code> is <code>null</code>
	 */
	public Sphere(Point3D center, double radius,
			double kdr, double kdg, double kdb,
			double krr, double krg, double krb,
			double krn) {
		this.center = Objects.requireNonNull(center);
		this.radius = radius;
		this.radiusSquared = radius * radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		// Due to some performance issues because of too many objects being allocated,
		// some parts of code were slightly complicated to reduce the number of objects.

		final Point3D startToCenter = center.sub(ray.start);
		final Point3D projectedCenter = vectorProjection(startToCenter, ray);

		if (startToCenter.scalarProduct(ray.direction) < 0) {
			// Center of the sphere is behind ray's starting point.
			// There is a possibility of an inner intersection.
			if (isLongerThanRadius(startToCenter)) {
				// Starting point is outside of the sphere.
				return null;
			}
			// Starting point is inside the sphere and we have an inner intersection.
			double projToInterDist = distFromProjectionToIntersection(projectedCenter);
			double finalDist = projToInterDist
					- projectedCenter.modifySub(ray.start).norm();
			Point3D intersection = pointOnRay(ray, finalDist);
			return new SphereIntersection(intersection, finalDist, false);

		} else {
			// Center of the sphere is in front of ray's starting point.
			// There is a possibility of both an inner and outer intersection.
			Point3D centerToProjection = projectedCenter.sub(center);
			if (isLongerThanRadius(centerToProjection)) {
				// Ray's closest point to the center is outside of the sphere,
				// therefore no intersection exists.
				return null;
			}
			// There must be at least one intersection with the sphere.
			// If there are two, the closer one is calculated.
			double projToInterDist = distFromProjectionToIntersection(centerToProjection);
			double finalDistPart = projectedCenter.modifySub(ray.start).norm();
			boolean outer = isLongerThanRadius(startToCenter);
			double finalDist;
			if (outer) {
				// Starting point is outside of the sphere.
				finalDist = finalDistPart - projToInterDist;
			} else {
				// Starting point is inside the sphere.
				finalDist = finalDistPart + projToInterDist;
			}
			Point3D intersection = pointOnRay(ray, finalDist);
			return new SphereIntersection(intersection, finalDist, outer);
		}
	}

	/**
	 * Project vector <code>toProject</code> onto line defined by ray
	 * <code>line</code>. Note that the vector is projected to a <i>line</i>, not a
	 * <i>ray</i>.
	 * 
	 * @param  toProject vector to project
	 * @param  line      line to which vector is projected
	 * @return           projection of <code>toProject</code> on <code>line</code>
	 */
	private static Point3D vectorProjection(Point3D toProject, Ray line) {
		double scalarProjection = toProject.scalarProduct(line.direction);
		return line.direction.scalarMultiply(scalarProjection).modifyAdd(line.start);
	}

	/**
	 * Calculate the distance from projection of {@linkplain #center} onto
	 * <code>ray</code>, to the intersection of <code>ray</code> with this sphere.
	 * 
	 * @param  centerToProjection vector from center to projection of
	 *                            <code>center</code> on <code>ray</code>
	 * @return                    distance from projection of <code>center</code> to
	 *                            intersection
	 */
	private double distFromProjectionToIntersection(Point3D centerToProjection) {
		// using the Pythagoras' theorem
		double projectionDistanceSquared = normSquared(centerToProjection);
		return Math.sqrt(radiusSquared - projectionDistanceSquared);
	}

	/**
	 * Evaluate function <code>p(lambda) = start + lambda * direction</code>.
	 * 
	 * @param  ray    ray whose <code>start</code> and <code>direction</code> are
	 *                used
	 * @param  lambda parameter from equation
	 * @return        point on <code>ray</code> for given parameter
	 *                <code>lambda</code>
	 */
	private static Point3D pointOnRay(Ray ray, double lambda) {
		return ray.direction.scalarMultiply(lambda).modifyAdd(ray.start);
	}

	/**
	 * Test if length of given <code>vector</code> is greater than
	 * <code>radius</code>. Test is performed without calculating any square roots.
	 * 
	 * @param  vector vector whose length is compared to <code>radius</code>, not
	 *                <code>null</code>
	 * @return        <code>true</code> if <code>vector</code> is longer than
	 *                <code>radius</code>, <code>false</code> otherwise
	 */
	private boolean isLongerThanRadius(Point3D vector) {
		return normSquared(vector) > radiusSquared;
	}

	/**
	 * Return square of <code>vector</code>'s norm.
	 * 
	 * @param  vector vector
	 * @return        square of norm
	 */
	private static double normSquared(Point3D vector) {
		return vector.scalarProduct(vector);
	}

	@Override
	public String toString() {
		return String.format("Sphere @[%f,%f,%f] radius: %f",
				center.x, center.y, center.z, radius);
	}

	/**
	 * Represents intersections of ray and sphere.
	 * 
	 * @author Luka Mesaric
	 */
	private class SphereIntersection extends RayIntersection {

		/**
		 * Default constructor.
		 * 
		 * @param point    point of intersection between ray and sphere
		 * @param distance distance between start of ray and intersection
		 * @param outer    <code>true</code> if intersection is outer,
		 *                 <code>false</code> if it is inner
		 */
		protected SphereIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		/**
		 * Returns normal to sphere's surface at this intersection point. Unit vector.
		 */
		@Override
		public Point3D getNormal() {
			return (isOuter()
					? getPoint().sub(center)
					: center.sub(getPoint()))
							.modifyNormalize();
		}

		@Override
		public double getKdr() { return kdr; }

		@Override
		public double getKdg() { return kdg; }

		@Override
		public double getKdb() { return kdb; }

		@Override
		public double getKrr() { return krr; }

		@Override
		public double getKrg() { return krg; }

		@Override
		public double getKrb() { return krb; }

		@Override
		public double getKrn() { return krn; }

		@Override
		public String toString() {
			Point3D intersect = getPoint();
			return String.format("SphereIntersection @[%f,%f,%f] distance: %f, outer: %b",
					intersect.x, intersect.y, intersect.z, getDistance(), isOuter());
		}
	}
}
