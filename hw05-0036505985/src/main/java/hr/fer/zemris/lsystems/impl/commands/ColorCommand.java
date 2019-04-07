package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.Util;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command that changes turtle's drawing color.
 * 
 * @author Luka Mesaric
 */
public class ColorCommand implements Command {

	/**
	 * Turtle's new drawing color.
	 */
	private final Color color;

	/**
	 * Default constructor.
	 * 
	 * @param color color
	 * 
	 * @throws NullPointerException if <code>color</code> is <code>null</code>.
	 */
	public ColorCommand(Color color) {
		this.color = Util.validateNotNull(color, "color");
	}

	/**
	 * Changes turtle's drawing color.
	 * 
	 * @throws NullPointerException if <code>ctx</code> is <code>null</code>.
	 * @throws EmptyStackException  if there is no state to update.
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Util.validateNotNull(ctx, "ctx");
		TurtleState state = ctx.getCurrentState();
		state.setColor(color);
	}

}
