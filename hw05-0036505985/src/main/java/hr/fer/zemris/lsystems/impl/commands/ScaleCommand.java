package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command that scales turtle's effective length by <code>factor</code>.
 * 
 * @author Luka Mesaric
 */
public class ScaleCommand implements Command {

	/**
	 * Factor by which to scale turtle's effective length.
	 */
	private final double factor;

	/**
	 * Default constructor.
	 * 
	 * @param factor factor
	 * 
	 * @throws IllegalArgumentException if <code>factor</code> is not finite.
	 */
	public ScaleCommand(double factor) {
		if (!Double.isFinite(factor)) {
			throw new IllegalArgumentException("'factor' must be a finite value.");
		}
		this.factor = factor;
	}

	/**
	 * Scales turtle's effective length by <code>factor</code>.
	 * 
	 * @throws NullPointerException if <code>ctx</code> is <code>null</code>.
	 * @throws EmptyStackException  if there is no state to update.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = Util.validateNotNull(ctx, "ctx").getCurrentState();
		double newEffectiveLength = factor * state.getEffectiveLength();
		state.setEffectiveLength(newEffectiveLength);
	}

}
