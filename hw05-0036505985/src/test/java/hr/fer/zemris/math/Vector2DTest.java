package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class Vector2DTest {

	// Getters and equals cannot be tested without constructors.
	@Test
	void testConstructorAndGettersAndEquals() {
		Vector2D v1 = new Vector2D(Double.NaN, 5);
		Vector2D v2 = new Vector2D(5, Double.NaN);
		Vector2D v3 = new Vector2D(Double.POSITIVE_INFINITY, 5);
		Vector2D v4 = new Vector2D(5, Double.NEGATIVE_INFINITY);
		Vector2D v5 = new Vector2D(4.32, -1.3);

		assertEquals(Double.NaN, v1.getX());
		assertEquals(5.00000000001, v1.getY(), Vector2D.TOLERANCE);
		assertEquals(v1, new Vector2D(Double.NaN, 5.00000000001));

		assertEquals(5.00000000001, v2.getX(), Vector2D.TOLERANCE);
		assertEquals(Double.NaN, v2.getY());
		assertEquals(v2, new Vector2D(5.00000000001, Double.NaN));

		assertEquals(Double.POSITIVE_INFINITY, v3.getX());
		assertEquals(5.00000000001, v3.getY(), Vector2D.TOLERANCE);
		assertEquals(v3, new Vector2D(Double.POSITIVE_INFINITY, 5.00000000001));

		assertEquals(5.00000000001, v4.getX(), Vector2D.TOLERANCE);
		assertEquals(Double.NEGATIVE_INFINITY, v4.getY());
		assertEquals(v4, new Vector2D(5.00000000001, Double.NEGATIVE_INFINITY));

		assertEquals(4.32000001, v5.getX(), Vector2D.TOLERANCE);
		assertEquals(-1.29999998, v5.getY(), Vector2D.TOLERANCE);
		assertEquals(v5, new Vector2D(4.32000001, -1.29999998));
	}

	@Test
	void testCopy() {
		Vector2D one = new Vector2D(2.7, 6.3);
		Vector2D two = new Vector2D(4.2, -1.3);
		Vector2D three = new Vector2D(Double.POSITIVE_INFINITY, Double.NaN);

		assertEquals(one, one.copy());
		assertEquals(two, two.copy());
		assertEquals(three, three.copy());
	}

	@Test
	void testTranslateNull() {
		Vector2D v = new Vector2D(1, 2);
		assertThrows(NullPointerException.class, () -> v.translate(null));
	}

	@Test
	void testTranslatedNull() {
		Vector2D v = new Vector2D(1, 2);
		assertThrows(NullPointerException.class, () -> v.translated(null));
	}

	@Test
	void testTranslate() {
		Vector2D v1 = new Vector2D(1.0, 2.0);
		Vector2D v2 = new Vector2D(-1.0, -2.0);
		v1.translate(v2);
		assertEquals(new Vector2D(0, 0), v1);
	}

	@Test
	void testTranslated() {
		Vector2D v1 = new Vector2D(1.0, 2.0);
		Vector2D v2 = new Vector2D(-1.0, -2.0);
		assertEquals(new Vector2D(0, 0), v1.translated(v2));
	}

	@Test
	void testScale() {
		Vector2D one = new Vector2D(2.7, 6.3);
		Vector2D two = new Vector2D(4.2, -1.3);
		Vector2D three = new Vector2D(Double.POSITIVE_INFINITY, Double.NaN);

		one.scale(3.1);
		two.scale(Double.NEGATIVE_INFINITY);
		three.scale(0);

		assertEquals(new Vector2D(8.37, 19.53), one);
		assertEquals(new Vector2D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), two);
		assertEquals(new Vector2D(Double.NaN, Double.NaN), three);
	}

	@Test
	void testScaled() {
		Vector2D one = new Vector2D(1, 0);

		assertEquals(new Vector2D(Double.NaN, Double.NaN), one.scaled(Double.NaN));
		assertEquals(new Vector2D(Double.NaN, Double.NaN), one.scaled(-Double.NaN));

		assertEquals(new Vector2D(Double.POSITIVE_INFINITY, Double.NaN), one.scaled(Double.POSITIVE_INFINITY));
		assertEquals(new Vector2D(Double.NEGATIVE_INFINITY, Double.NaN), one.scaled(Double.NEGATIVE_INFINITY));

		assertEquals(new Vector2D(0, -1), new Vector2D(0, 1).scaled(-1));
		assertEquals(new Vector2D(0, 0), new Vector2D(3.14, -2.43).scaled(0));
	}

	@Test
	void testRotate() {
		Vector2D v1 = new Vector2D(1.0, 2.0);
		Vector2D v2 = new Vector2D(-1.0, -2.0);
		Vector2D v3 = new Vector2D(1.0, 0);
		Vector2D v4 = new Vector2D(1.0, 0);

		v1.rotate(Math.PI);
		v2.rotate(0);
		v3.rotate(Math.PI / 2);
		v4.rotate(Double.POSITIVE_INFINITY);

		assertEquals(new Vector2D(-1.0, -2.0), v1);
		assertEquals(new Vector2D(-1.0, -2.0), v2);
		assertEquals(new Vector2D(0, 1.0), v3);
		assertEquals(new Vector2D(Double.NaN, Double.NaN), v4);
	}

	@Test
	void testRotated() {
		Vector2D v1 = new Vector2D(1.0, 2.0);
		Vector2D v2 = new Vector2D(-1.0, -2.0);
		Vector2D v3 = new Vector2D(1.0, 0);
		Vector2D v4 = new Vector2D(1.0, 0);

		assertEquals(new Vector2D(-1.0, -2.0), v1.rotated(Math.PI));
		assertEquals(new Vector2D(-1.0, -2.0), v2.rotated(0));
		assertEquals(new Vector2D(0, 1.0), v3.rotated(Math.PI / 2));
		assertEquals(new Vector2D(Double.NaN, Double.NaN), v4.rotated(Double.POSITIVE_INFINITY));
	}

}
