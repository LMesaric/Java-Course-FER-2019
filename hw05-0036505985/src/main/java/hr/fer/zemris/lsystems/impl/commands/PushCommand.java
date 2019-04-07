package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command that copies the state from the top of the stack and puts in on top of
 * that same stack.
 * 
 * @author Luka Mesaric
 */
public class PushCommand implements Command {

	/**
	 * Copies the state from the top of the stack and puts in on top of that same
	 * stack.
	 * 
	 * @throws NullPointerException if <code>ctx</code> is <code>null</code>.
	 * @throws EmptyStackException  if there is no state to copy from
	 *                              <code>context</code>.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Util.validateNotNull(ctx, "ctx");
		TurtleState state = ctx.getCurrentState();
		ctx.pushState(state.copy());
	}

}
