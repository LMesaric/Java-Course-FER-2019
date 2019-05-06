package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program repeatedly takes complex numbers that the user inputs through
 * console, until 'done' is written. Afterwards, a fractal is shown. Given
 * complex numbers are used as polynomial roots.
 * 
 * @author Luka Mesaric
 */
public class Newton {

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. "
				+ "Enter 'done' when done.");

		List<Complex> userInput = new ArrayList<>();

		try (Scanner sc = new Scanner(System.in)) {
			int num = 1;
			while (true) {
				System.out.print("Root " + num + "> ");
				String line = sc.nextLine().strip();
				if ("done".equals(line.toLowerCase())) {
					break;
				}
				try {
					userInput.add(parseComplex(line));
					num++;
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		if (userInput.size() < 2) {
			System.out.println("At least two roots are needed. Terminating the program.");
			return;
		}

		System.out.println("Image of fractal will appear shortly. Thank you.");
		ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(
				Complex.ONE, userInput.toArray(Complex[]::new));
		FractalViewer.show(new NewtonProducer(polynomial));
	}

	/**
	 * Constructs a new <code>Complex</code> by parsing <code>s</code>.<br>
	 * Pure real numbers and pure imaginary numbers are valid. Letter <code>i</code>
	 * must be placed before the imaginary part. Multiple signs in a row are not
	 * supported. Leading plus sign is allowed.
	 * 
	 * @param  s                        string to parse, must be stripped and not
	 *                                  <code>null</code>
	 * @return                          parsed complex number
	 * @throws NullPointerException     if <code>s</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>s</code> cannot be parsed
	 */
	private static Complex parseComplex(String s) {
		if (s.isEmpty()) {
			throw new IllegalArgumentException("Input cannot be blank.");
		} else if (s.indexOf('i') != s.lastIndexOf('i')) {
			throw new IllegalArgumentException(
					"Must contain at most one imaginary unit.");
		} else if (!s.contains("i")) {
			return new Complex(parseDouble(s), 0.0);
		}
		// At this point, there is precisely one imaginary unit.
		if (s.equals("i")) {
			return Complex.IM;
		}
		String[] parts = s.split("i");
		// parts[1] does not exist iff 'i' is the last char of line
		final double imAbs = parts.length < 2 ? 1.0 : parseDouble(parts[1]);

		String reDirty = parts[0].strip();
		if (reDirty.isEmpty()) {
			return new Complex(0.0, imAbs);
		}
		char sign = reDirty.charAt(reDirty.length() - 1);
		if (sign != '+' && sign != '-') {
			throw new IllegalArgumentException("Expected a sign before 'i': " + sign);
		}
		final double im = sign == '-' ? -imAbs : imAbs;

		String reClean = reDirty.substring(0, reDirty.length() - 1).strip();
		final double re = reClean.isEmpty() ? 0.0 : parseDouble(reClean);

		return new Complex(re, im);
	}

	/**
	 * Parses a double from <code>s</code>. Throws an
	 * <code>IllegalArgumentException</code> with a custom message if parsing fails.
	 * 
	 * @param  s                        the string to be parsed
	 * @return                          the double value represented by the string
	 *                                  argument
	 * @throws IllegalArgumentException if <code>s</code> cannot be parsed
	 */
	private static double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Cannot convert to double: " + s, e);
		}
	}

	/**
	 * Job calculating a part of Newton fractal. This job will be executed in the
	 * thread that calls {@link #call()}.
	 * 
	 * @author Luka Mesaric
	 */
	public static class NewtonJob implements Callable<Void> {

		/** Treshold for convergence. */
		private static final double CONVERGENCE_TRESHOLD = 0.001;
		/** Treshold for root distance. */
		private static final double ROOT_TRESHOLD = 0.001;
		/** Maximum number of iterations for determining convergence. */
		private static final int MAX_ITER = 50;

		/** Polynomial used for evaluation. */
		private final ComplexRootedPolynomial polynomial;
		/** Derivative of <code>polynomial</code>. */
		private final ComplexPolynomial derived;
		/** Smallest number shown on real axis. */
		private final double reMin;
		/** Greatest number shown on real axis. */
		private final double reMax;
		/** Smallest number shown on imaginary axis. */
		private final double imMin;
		/** Greatest number shown on imaginary axis. */
		private final double imMax;
		/** Width of canvas. */
		private final int width;
		/** Height of canvas. */
		private final int height;
		/** Index of starting line, inclusive. */
		private final int yMin;
		/** Index of ending line, inclusive. */
		private final int yMax;
		/** Array to be filled with calculated data. */
		private final short[] data;
		/** Flag for cancelling outdated calculations. */
		private final AtomicBoolean cancel;

		/**
		 * Default constructor.
		 * 
		 * @param  polynomial           polynomial used for evaluation
		 * @param  derived              derivative of <code>polynomial</code>.
		 * @param  reMin                smallest number shown on real axis
		 * @param  reMax                greatest number shown on real axis
		 * @param  imMin                smallest number shown on imaginary axis
		 * @param  imMax                greatest number shown on imaginary axis
		 * @param  width                width of canvas
		 * @param  height               height of canvas
		 * @param  yMin                 index of starting line, inclusive
		 * @param  yMax                 index of ending line, inclusive
		 * @param  data                 array to be filled with calculated data
		 * @param  cancel               flag for cancelling outdated calculations
		 * @throws NullPointerException if any argument is <code>null</code>
		 */
		public NewtonJob(
				ComplexRootedPolynomial polynomial, ComplexPolynomial derived,
				double reMin, double reMax,
				double imMin, double imMax,
				int width, int height,
				int yMin, int yMax,
				short[] data, AtomicBoolean cancel) {
			this.polynomial = Objects.requireNonNull(polynomial);
			this.derived = Objects.requireNonNull(derived);
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
			this.cancel = Objects.requireNonNull(cancel);
		}

		@Override
		public Void call() {
			int offset = yMin * width;
			for (int y = yMin; y <= yMax; y++) {
				if (cancel.get()) break;
				for (int x = 0; x < width; x++) {
					Complex zn = mapToComplexPlain(x, y);
					int iter = 0;
					double module;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = znold.sub(zn).module();
						iter++;
					} while (module > CONVERGENCE_TRESHOLD && iter < MAX_ITER);
					int index = polynomial.indexOfClosestRootFor(zn, ROOT_TRESHOLD);
					data[offset++] = (short) (index + 1);
				}
			}
			return null;
		}

		/**
		 * Maps pixel coordinates to a complex plain.
		 * 
		 * @param  x x-coordinate
		 * @param  y y-coordinate
		 * @return   complex number for given coordinate
		 */
		private Complex mapToComplexPlain(double x, double y) {
			// Mind the implicit conversion to double values!
			return new Complex(
					x / (width - 1.0) * (reMax - reMin) + reMin,
					(height - 1.0 - y) / (height - 1.0) * (imMax - imMin) + imMin);
		}
	}

	/**
	 * Producer for Newton fractals. Performs tasks in parallel.
	 * 
	 * @author Luka Mesaric
	 */
	public static class NewtonProducer implements IFractalProducer {

		/** Thread pool used for execution. */
		private final ExecutorService pool;
		/** Polynomial used for evaluation. */
		private final ComplexRootedPolynomial polynomial;
		/** Derivative of <code>polynomial</code>. */
		private final ComplexPolynomial derived;
		/** Order of <code>polynomial</code>. */
		private final short polynomialOrder;

		/**
		 * Default constructor.
		 * 
		 * @param  polynomial           polynomial used for evaluation
		 * @throws NullPointerException if <code>polynomial</code> is <code>null</code>
		 */
		public NewtonProducer(ComplexRootedPolynomial polynomial) {
			this.polynomial = Objects.requireNonNull(polynomial);
			ComplexPolynomial polynomialTemp = polynomial.toComplexPolynom();
			this.derived = polynomialTemp.derive();
			this.polynomialOrder = polynomialTemp.order();

			this.pool = Executors.newFixedThreadPool(
					Runtime.getRuntime().availableProcessors(),
					new DaemonicThreadFactory());
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer,
				AtomicBoolean cancel) {

			final short[] data = new short[width * height];
			final int numOfBands = 8 * Runtime.getRuntime().availableProcessors();
			final int linesPerBand = height / numOfBands;

			final List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < numOfBands; i++) {
				int yMin = i * linesPerBand;
				int yMax = (i + 1) * linesPerBand - 1;
				if (i == numOfBands - 1) {
					yMax = height - 1;
				}

				NewtonJob posao = new NewtonJob(
						polynomial, derived,
						reMin, reMax,
						imMin, imMax,
						width, height,
						yMin, yMax,
						data, cancel);
				results.add(pool.submit(posao));
			}

			for (Future<Void> posao : results) {
				try {
					posao.get();
				} catch (InterruptedException | ExecutionException e) {}
			}
			observer.acceptResult(data, (short) (polynomialOrder + 1), requestNo);
		}
	}

	/**
	 * Creates new deamonic threads on demand.
	 * 
	 * @author Luka Mesaric
	 */
	public static class DaemonicThreadFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setDaemon(true);
			return thread;
		}
	}

}
