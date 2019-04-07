package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command that removes a single state from the top of the stack.
 * 
 * @author Luka Mesaric
 */
public class PopCommand implements Command {

	/**
	 * Removes one state from the top of the stack.
	 * 
	 * @throws EmptyStackException  if there are no states to remove from
	 *                              <code>context</code>.
	 * @throws NullPointerException if <code>ctx</code> is <code>null</code>.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Util.validateNotNull(ctx, "ctx").popState();
	}

}
