package hr.fer.zemris.java.hw06.shell;

/**
 * Enumeration for shell's current status. Primarily used to signal that shell
 * should be terminated.
 * 
 * @author Luka Mesaric
 */
public enum ShellStatus {

	/**
	 * Signal that shell may keep running.
	 */
	CONTINUE,

	/**
	 * Signal that shell should be terminated.
	 */
	TERMINATE

}
