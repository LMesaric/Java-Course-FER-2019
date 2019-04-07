package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command that rotates the turtle by <code>angle</code> degrees.
 * 
 * @author Luka Mesaric
 */
public class RotateCommand implements Command {

	/**
	 * Angle by which to rotate, in degrees.
	 */
	private final double angle;

	/**
	 * Default constructor.
	 * 
	 * @param angle angle in degrees
	 * 
	 * @throws IllegalArgumentException if <code>angle</code> is not finite.
	 */
	public RotateCommand(double angle) {
		if (!Double.isFinite(angle)) {
			throw new IllegalArgumentException("'angle' must be a finite value.");
		}
		this.angle = angle;
	}

	/**
	 * Rotates the turtle by <code>angle</code> degrees.
	 * 
	 * @throws NullPointerException if <code>ctx</code> is <code>null</code>.
	 * @throws EmptyStackException  if there is no state to update.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = Util.validateNotNull(ctx, "ctx").getCurrentState();
		state.getDirection().rotate(Math.toRadians(angle));
	}

}
