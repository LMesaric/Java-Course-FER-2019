package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Represents a turtle command.
 * 
 * @author Luka Mesaric
 */
@FunctionalInterface
public interface Command {

	/**
	 * Executes the modelled command.
	 * 
	 * @param ctx     context
	 * @param painter painter
	 */
	void execute(Context ctx, Painter painter);

}
