package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import hr.fer.zemris.java.hw02.ComplexNumber.ComplexNumberParser;

/**
 * Class for testing {@link ComplexNumber}.
 * <p>
 * See <code>src\test\resources</code> directory for test data, or browse test
 * results after running everything.
 * </p>
 * 
 * @author Luka Mesaric
 */
class ComplexNumberTest {

	/**
	 * Tolerance for comparing double values in these tests.
	 */
	private static final double DOUBLE_TOL = 1E-5;

	@Test
	void testConstructor() {
		ComplexNumber c1 = new ComplexNumber(Double.NaN, 5);
		ComplexNumber c2 = new ComplexNumber(5, Double.NaN);
		ComplexNumber c3 = new ComplexNumber(Double.POSITIVE_INFINITY, 5);
		ComplexNumber c4 = new ComplexNumber(5, Double.NEGATIVE_INFINITY);
		ComplexNumber c5 = new ComplexNumber(4.32, -1.3);

		assertEquals(Double.NaN, c1.getReal());
		assertEquals(5, c1.getImaginary(), DOUBLE_TOL);

		assertEquals(Double.NaN, c2.getImaginary());
		assertEquals(Double.POSITIVE_INFINITY, c3.getReal());
		assertEquals(Double.NEGATIVE_INFINITY, c4.getImaginary());

		assertEquals(4.32, c5.getReal(), DOUBLE_TOL);
		assertEquals(-1.3, c5.getImaginary(), DOUBLE_TOL);
	}

	@Test
	void testStaticConstants() {
		assertEquals(0, ComplexNumber.ZERO.getReal(), DOUBLE_TOL);
		assertEquals(0, ComplexNumber.ZERO.getImaginary(), DOUBLE_TOL);

		assertEquals(1, ComplexNumber.ONE.getReal(), DOUBLE_TOL);
		assertEquals(0, ComplexNumber.ONE.getImaginary(), DOUBLE_TOL);

		assertEquals(0, ComplexNumber.I.getReal(), DOUBLE_TOL);
		assertEquals(1, ComplexNumber.I.getImaginary(), DOUBLE_TOL);

		assertEquals(0, ComplexNumber.MINUS_I.getReal(), DOUBLE_TOL);
		assertEquals(-1, ComplexNumber.MINUS_I.getImaginary(), DOUBLE_TOL);
	}

	@Test
	void testFromReal() {
		assertEquals(new ComplexNumber(Double.NaN, 0), ComplexNumber.fromReal(Double.NaN));
		assertEquals(new ComplexNumber(Double.POSITIVE_INFINITY, 0), ComplexNumber.fromReal(Double.POSITIVE_INFINITY));
		assertEquals(new ComplexNumber(Double.NEGATIVE_INFINITY, 0), ComplexNumber.fromReal(Double.NEGATIVE_INFINITY));

		ComplexNumber c = ComplexNumber.fromReal(4.32);
		assertEquals(4.32, c.getReal(), DOUBLE_TOL);
		assertEquals(0, c.getImaginary(), DOUBLE_TOL);
	}

	@Test
	void testFromImaginary() {
		assertEquals(new ComplexNumber(0, Double.NaN), ComplexNumber.fromImaginary(Double.NaN));
		assertEquals(new ComplexNumber(0, Double.POSITIVE_INFINITY),
				ComplexNumber.fromImaginary(Double.POSITIVE_INFINITY));
		assertEquals(new ComplexNumber(0, Double.NEGATIVE_INFINITY),
				ComplexNumber.fromImaginary(Double.NEGATIVE_INFINITY));

		ComplexNumber c = ComplexNumber.fromImaginary(4.32);
		assertEquals(0, c.getReal(), DOUBLE_TOL);
		assertEquals(4.32, c.getImaginary(), DOUBLE_TOL);
	}

	@Test
	void testFromMagnitudeAndAngle() {
		assertThrows(IllegalArgumentException.class,
				() -> ComplexNumber.fromMagnitudeAndAngle(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.fromMagnitudeAndAngle(-10, 10));
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.fromMagnitudeAndAngle(-0.00001, 10));

		ComplexNumber bothNan = new ComplexNumber(Double.NaN, Double.NaN);
		assertEquals(bothNan, ComplexNumber.fromMagnitudeAndAngle(Double.NaN, -Double.NaN));
		assertEquals(bothNan, ComplexNumber.fromMagnitudeAndAngle(Double.POSITIVE_INFINITY, Double.NaN));
		assertEquals(bothNan, ComplexNumber.fromMagnitudeAndAngle(10, Double.NEGATIVE_INFINITY));
		assertEquals(new ComplexNumber(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
				ComplexNumber.fromMagnitudeAndAngle(Double.POSITIVE_INFINITY, 2));

		ComplexNumber c1 = ComplexNumber.fromMagnitudeAndAngle(3, 1);
		assertEquals(1.620906918, c1.getReal(), DOUBLE_TOL);
		assertEquals(2.524412954, c1.getImaginary(), DOUBLE_TOL);

		ComplexNumber c2 = ComplexNumber.fromMagnitudeAndAngle(5, 0);
		assertEquals(5, c2.getReal(), DOUBLE_TOL);
		assertEquals(0, c2.getImaginary(), DOUBLE_TOL);

		ComplexNumber c3 = ComplexNumber.fromMagnitudeAndAngle(0, 0);
		assertEquals(0, c3.getReal(), DOUBLE_TOL);
		assertEquals(0, c3.getImaginary(), DOUBLE_TOL);

		ComplexNumber c4 = ComplexNumber.fromMagnitudeAndAngle(0, 1.5);
		assertEquals(0, c4.getReal(), DOUBLE_TOL);
		assertEquals(0, c4.getImaginary(), DOUBLE_TOL);

		ComplexNumber c5 = ComplexNumber.fromMagnitudeAndAngle(10, Math.PI);
		assertEquals(-10, c5.getReal(), DOUBLE_TOL);
		assertEquals(0, c5.getImaginary(), DOUBLE_TOL);

		ComplexNumber c6 = ComplexNumber.fromMagnitudeAndAngle(10, 2 * Math.PI);
		assertEquals(10, c6.getReal(), DOUBLE_TOL);
		assertEquals(0, c6.getImaginary(), DOUBLE_TOL);

		ComplexNumber c7 = ComplexNumber.fromMagnitudeAndAngle(10, 20.5 * Math.PI);
		assertEquals(0, c7.getReal(), DOUBLE_TOL);
		assertEquals(10, c7.getImaginary(), DOUBLE_TOL);

		ComplexNumber c8 = ComplexNumber.fromMagnitudeAndAngle(5, -1.5 * Math.PI);
		assertEquals(0, c8.getReal(), DOUBLE_TOL);
		assertEquals(5, c8.getImaginary(), DOUBLE_TOL);
	}

	@Test
	void testGetReal() {
		ComplexNumber c = new ComplexNumber(123456789.123456789, -987654321.987654321);
		assertEquals(123456789.123456789, c.getReal(), DOUBLE_TOL);
	}

	@Test
	void testGetImaginary() {
		ComplexNumber c = new ComplexNumber(123456789.123456789, -987654321.987654321);
		assertEquals(-987654321.987654321, c.getImaginary(), DOUBLE_TOL);
	}

	@Test
	void testGetMagnitude() {
		assertEquals(0, ComplexNumber.ZERO.getMagnitude(), DOUBLE_TOL);
		assertEquals(1, ComplexNumber.ONE.getMagnitude(), DOUBLE_TOL);
		assertEquals(1, ComplexNumber.I.getMagnitude(), DOUBLE_TOL);
		assertEquals(1, ComplexNumber.MINUS_I.getMagnitude(), DOUBLE_TOL);

		assertEquals(Math.sqrt(2), new ComplexNumber(1, 1).getMagnitude(), DOUBLE_TOL);
		assertEquals(Math.sqrt(2), new ComplexNumber(-1, -1).getMagnitude(), DOUBLE_TOL);
		assertEquals(27.899681, new ComplexNumber(13.69, -24.31).getMagnitude(), DOUBLE_TOL);
	}

	@Test
	void testGetAngle() {
		assertEquals(0, ComplexNumber.ZERO.getAngle(), DOUBLE_TOL);
		assertEquals(0, ComplexNumber.ONE.getAngle(), DOUBLE_TOL);
		assertEquals(Math.PI / 2, ComplexNumber.I.getAngle(), DOUBLE_TOL);
		assertEquals(Math.PI * 3 / 2, ComplexNumber.MINUS_I.getAngle(), DOUBLE_TOL);

		assertEquals(Math.PI / 4, new ComplexNumber(1, 1).getAngle(), DOUBLE_TOL);
		assertEquals(Math.PI * 3 / 4, new ComplexNumber(-1, 1).getAngle(), DOUBLE_TOL);
		assertEquals(Math.PI * 5 / 4, new ComplexNumber(-1, -1).getAngle(), DOUBLE_TOL);
		assertEquals(Math.PI * 7 / 4, new ComplexNumber(1, -1).getAngle(), DOUBLE_TOL);

		assertEquals(5.22526656, new ComplexNumber(13.69, -24.31).getAngle(), DOUBLE_TOL);
	}

	@Test
	void testConj() {
		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.conj());
		assertEquals(ComplexNumber.MINUS_I, ComplexNumber.I.conj());

		assertEquals(new ComplexNumber(5, 6), new ComplexNumber(5, -6).conj());
	}

	@Test
	void testAdd() {
		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.add(ComplexNumber.ZERO));
		assertEquals(ComplexNumber.I, ComplexNumber.I.add(ComplexNumber.ZERO));
		assertEquals(ComplexNumber.I, ComplexNumber.ZERO.add(ComplexNumber.I));
		assertEquals(ComplexNumber.ZERO, ComplexNumber.I.add(ComplexNumber.MINUS_I));

		ComplexNumber c1 = new ComplexNumber(3.14, -2.43);
		ComplexNumber c2 = new ComplexNumber(-1.12, 5.89);
		assertEquals(new ComplexNumber(2.02, 3.46), c1.add(c2));
	}

	@ParameterizedTest
	@NullSource
	void testAddNull(ComplexNumber c) {
		assertThrows(NullPointerException.class, () -> ComplexNumber.ONE.add(c));
	}

	@Test
	void testSub() {
		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.sub(ComplexNumber.ZERO));
		assertEquals(ComplexNumber.I, ComplexNumber.I.sub(ComplexNumber.ZERO));
		assertEquals(ComplexNumber.MINUS_I, ComplexNumber.ZERO.sub(ComplexNumber.I));
		assertEquals(ComplexNumber.ZERO, ComplexNumber.I.sub(ComplexNumber.I));

		ComplexNumber c1 = new ComplexNumber(3.14, -2.43);
		ComplexNumber c2 = new ComplexNumber(-1.12, 5.89);
		assertEquals(new ComplexNumber(4.26, -8.32), c1.sub(c2));
	}

	@ParameterizedTest
	@NullSource
	void testSubNull(ComplexNumber c) {
		assertThrows(NullPointerException.class, () -> ComplexNumber.ONE.sub(c));
	}

	@Test
	void testMul() {
		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.mul(ComplexNumber.ZERO));
		assertEquals(ComplexNumber.ZERO, ComplexNumber.I.mul(ComplexNumber.ZERO));
		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.mul(ComplexNumber.ONE));
		assertEquals(ComplexNumber.I, ComplexNumber.I.mul(ComplexNumber.ONE));
		assertEquals(ComplexNumber.ONE, ComplexNumber.MINUS_I.mul(ComplexNumber.I));
		assertEquals(new ComplexNumber(-1, 0), ComplexNumber.I.mul(ComplexNumber.I));
		assertEquals(new ComplexNumber(-1, 0), ComplexNumber.MINUS_I.mul(ComplexNumber.MINUS_I));

		ComplexNumber c1 = new ComplexNumber(3.14, -2.43);
		ComplexNumber c2 = new ComplexNumber(-1.12, 5.89);
		assertEquals(new ComplexNumber(10.7959, 21.2162), c1.mul(c2));

		ComplexNumber c3 = ComplexNumber.fromMagnitudeAndAngle(1.23, 45.6);
		ComplexNumber c4 = ComplexNumber.fromMagnitudeAndAngle(5.43, -17.9);
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(1.23 * 5.43, 45.6 - 17.9), c3.mul(c4));
	}

	@ParameterizedTest
	@NullSource
	void testMulNull(ComplexNumber c) {
		assertThrows(NullPointerException.class, () -> ComplexNumber.ONE.mul(c));
	}

	@Test
	void testDiv() {
		assertEquals(new ComplexNumber(Double.NaN, Double.NaN), ComplexNumber.ONE.div(ComplexNumber.ZERO));

		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.div(ComplexNumber.ONE));
		assertEquals(ComplexNumber.ONE, ComplexNumber.I.div(ComplexNumber.I));
		assertEquals(ComplexNumber.I, ComplexNumber.I.div(ComplexNumber.ONE));
		assertEquals(new ComplexNumber(-1, 0), ComplexNumber.I.div(ComplexNumber.MINUS_I));

		ComplexNumber c1 = new ComplexNumber(3.14, -2.43);
		ComplexNumber c2 = new ComplexNumber(-1.12, 5.89);
		assertEquals(new ComplexNumber(-0.4960010015, -0.438790981), c1.div(c2));

		ComplexNumber c3 = ComplexNumber.fromMagnitudeAndAngle(1.23, 45.6);
		ComplexNumber c4 = ComplexNumber.fromMagnitudeAndAngle(5.43, -17.9);
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(1.23 / 5.43, 45.6 + 17.9), c3.div(c4));
	}

	@ParameterizedTest
	@NullSource
	void testDivNull(ComplexNumber c) {
		assertThrows(NullPointerException.class, () -> ComplexNumber.ONE.div(c));
	}

	@Test
	void testScale() {
		assertEquals(new ComplexNumber(Double.NaN, Double.NaN), ComplexNumber.ONE.scale(Double.NaN));
		assertEquals(new ComplexNumber(Double.NaN, Double.NaN), ComplexNumber.ONE.scale(-Double.NaN));

		assertEquals(new ComplexNumber(Double.POSITIVE_INFINITY, Double.NaN),
				ComplexNumber.ONE.scale(Double.POSITIVE_INFINITY));
		assertEquals(new ComplexNumber(Double.NEGATIVE_INFINITY, Double.NaN),
				ComplexNumber.ONE.scale(Double.NEGATIVE_INFINITY));

		assertEquals(ComplexNumber.MINUS_I, ComplexNumber.I.scale(-1));
		assertEquals(ComplexNumber.ZERO, new ComplexNumber(3.14, -2.43).scale(0));
	}

	@Test
	void testPower() {
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.I.power(-1));

		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.power(149));
		assertEquals(ComplexNumber.ONE, ComplexNumber.ONE.power(149));
		assertEquals(new ComplexNumber(0, -1), ComplexNumber.I.power(99));
		assertEquals(new ComplexNumber(1643.026511416, 2365.57047829), new ComplexNumber(2.13, -1.67).power(8));

		ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(5.43, -17.9);
		assertEquals(ComplexNumber.fromMagnitudeAndAngle(5.43 * 5.43 * 5.43, -17.9 * 3), c.power(3));
	}

	@Test
	void testRoot() {
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.I.root(0));

		ComplexNumber c = new ComplexNumber(1, 1);
		ComplexNumber[] roots = c.root(4);

		assertEquals(4, roots.length);

		assertEquals(new ComplexNumber(1.0695539323, 0.21274750473), roots[0]);
		assertEquals(new ComplexNumber(-0.21274750473, 1.0695539324), roots[1]);
		assertEquals(new ComplexNumber(-1.0695539324, -0.21274750473), roots[2]);
		assertEquals(new ComplexNumber(0.2127475047, -1.0695539324), roots[3]);
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersOneElement.csv", "/ComplexNumbersTwoElementsRealThenImag.csv",
			"/ComplexNumbersTwoElementsRealThenImagWithBlanks.csv" })
	void testToStringWithParserCombination(String s, double re, double im) {
		ComplexNumber correct = new ComplexNumber(re, im);
		assertEquals(correct, ComplexNumber.parse(correct.toString()));
	}

	// All further methods test parsing complex numbers from strings

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersOneElement.csv" })
	void testGetNextComplexNumberOneElement(String text, double re, double im) {
		ComplexNumberParser parser = new ComplexNumberParser(text);
		assertEquals(new ComplexNumber(re, im), parser.getNextComplexNumber());
		assertNull(parser.getNextComplexNumber());
		// all further calls will also return null
		assertNull(parser.getNextComplexNumber());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersTwoElementsRealThenImag.csv" })
	void testGetNextComplexNumberTwoElementsRealThenImag(String text, double re, double im) {
		ComplexNumberParser parser = new ComplexNumberParser(text);
		assertEquals(new ComplexNumber(re, 0), parser.getNextComplexNumber());
		assertEquals(new ComplexNumber(0, im), parser.getNextComplexNumber());
		assertNull(parser.getNextComplexNumber());
		// all further calls will also return null
		assertNull(parser.getNextComplexNumber());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersTwoElementsImagThenReal.csv" })
	void testGetNextComplexNumberTwoElementsImagThenReal(String text, double re, double im) {
		ComplexNumberParser parser = new ComplexNumberParser(text);
		assertEquals(new ComplexNumber(0, im), parser.getNextComplexNumber());
		assertEquals(new ComplexNumber(re, 0), parser.getNextComplexNumber());
		assertNull(parser.getNextComplexNumber());
		// all further calls will also return null
		assertNull(parser.getNextComplexNumber());

	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersTwoElementsRealThenImagWithBlanks.csv" })
	void testGetNextComplexNumberTwoElementsRealThenImagWithBlanks(String text, double re, double im) {
		ComplexNumberParser parser = new ComplexNumberParser(text);
		assertEquals(new ComplexNumber(re, 0), parser.getNextComplexNumber());
		assertEquals(new ComplexNumber(0, im), parser.getNextComplexNumber());
		assertNull(parser.getNextComplexNumber());
		// all further calls will also return null
		assertNull(parser.getNextComplexNumber());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersFailOnFirst.csv" })
	void testGetNextComplexNumberFailOnFirst(String s) {
		ComplexNumberParser parser = new ComplexNumberParser(s);
		assertThrows(IllegalArgumentException.class, () -> parser.getNextComplexNumber());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersFailOnSecond.csv" })
	void testGetNextComplexNumberFailOnSecond(String s) {
		ComplexNumberParser parser = new ComplexNumberParser(s);
		parser.getNextComplexNumber(); // result is ignored for this test
		assertThrows(IllegalArgumentException.class, () -> parser.getNextComplexNumber());
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersOneElement.csv", "/ComplexNumbersTwoElementsRealThenImag.csv",
			"/ComplexNumbersTwoElementsRealThenImagWithBlanks.csv" })
	void testParseValid(String s, double re, double im) {
		assertEquals(new ComplexNumber(re, im), ComplexNumber.parse(s));
	}

	@ParameterizedTest
	@CsvFileSource(resources = { "/ComplexNumbersFailOnFirst.csv", "/ComplexNumbersFailOnSecond.csv",
			"/ComplexNumbersTwoElementsImagThenReal.csv", "/ComplexNumbersBigExpressionsValid.csv",
			"/ComplexNumbersBigExpressionsInvalid.csv" })
	void testParseInvalid(String s) {
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(s));
	}

	@ParameterizedTest
	@NullSource
	void testParseNull(String s) {
		assertThrows(NullPointerException.class, () -> ComplexNumber.parse(s));
	}

	@ParameterizedTest
	@EmptySource
	void testParseBlank(String s) {
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse(s));
	}

}
