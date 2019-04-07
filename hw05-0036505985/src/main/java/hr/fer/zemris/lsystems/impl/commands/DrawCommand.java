package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Command that moves the turtle by <code>step</code> and draws a line.
 * 
 * @author Luka Mesaric
 */
public class DrawCommand implements Command {

	/**
	 * Step by which to move the turtle.
	 */
	private final double step;

	/**
	 * Default constructor.
	 * 
	 * @param step step
	 * 
	 * @throws IllegalArgumentException if <code>step</code> is not finite.
	 */
	public DrawCommand(double step) {
		if (!Double.isFinite(step)) {
			throw new IllegalArgumentException("'step' must be a finite value.");
		}
		this.step = step;
	}

	/**
	 * Moves the turtle by <code>step</code> and draws a line.
	 * 
	 * @throws NullPointerException if <code>ctx</code> or <code>painter</code> is
	 *                              <code>null</code>.
	 * @throws EmptyStackException  if there is no state to update.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Util.validateNotNull(ctx, "ctx");
		Util.validateNotNull(painter, "painter");

		TurtleState state = ctx.getCurrentState();
		Vector2D start = state.getPosition();
		double startX = start.getX();
		double startY = start.getY();

		new SkipCommand(step).execute(ctx, painter);

		Vector2D end = ctx.getCurrentState().getPosition();
		painter.drawLine(startX, startY, end.getX(), end.getY(), state.getColor(), 1f);
	}

}
