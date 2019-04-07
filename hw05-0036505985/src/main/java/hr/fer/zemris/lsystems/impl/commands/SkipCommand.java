package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Command that moves the turtle by <code>step</code> without drawing a line.
 * 
 * @author Luka Mesaric
 */
public class SkipCommand implements Command {

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
	public SkipCommand(double step) {
		if (!Double.isFinite(step)) {
			throw new IllegalArgumentException("'step' must be a finite value.");
		}
		this.step = step;
	}

	/**
	 * Moves the turtle by <code>step</code> without drawing a line.
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
		double totalLength = step * state.getEffectiveLength();
		Vector2D trans = state.getDirection().scaled(totalLength);
		state.getPosition().translate(trans);
	}

}
