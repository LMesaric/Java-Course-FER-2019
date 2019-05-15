package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program with GUI which generates prime numbers every time a button is
 * clicked.<br>
 * Program takes no command-line arguments.
 * 
 * @author Luka Mesaric
 */
public class PrimDemo extends JFrame {

	/** Serial version UID. */
	private static final long serialVersionUID = 2552614965736703542L;

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
	}

	/** Default constructor. */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prime Numbers v1.0");
		initGUI();
		setSize(300, 300);
		setLocationRelativeTo(null);
	}

	/** Initializes GUI. */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		final PrimListModel model = new PrimListModel();

		JButton next = new JButton("Sljedeći");
		next.addActionListener(e -> model.next());
		cp.add(next, BorderLayout.PAGE_END);

		JPanel listsPanel = new JPanel(new GridLayout(1, 0));
		listsPanel.add(new JScrollPane(new JList<>(model)));
		listsPanel.add(new JScrollPane(new JList<>(model)));
		cp.add(listsPanel, BorderLayout.CENTER);
	}
}
