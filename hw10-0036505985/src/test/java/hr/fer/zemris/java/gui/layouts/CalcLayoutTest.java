package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class CalcLayoutTest {

	@Test
	void testConstraintOutsideGrid() {
		JPanel p = new JPanel(new CalcLayout());
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(0, 1)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(0, 4)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(-1, 1)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(6, 1)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(6, 4)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(3, 0)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(3, 8)));
	}

	@Test
	void testConstraintOnCoveredByStretch() {
		JPanel p = new JPanel(new CalcLayout());
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(1, 2)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(1, 3)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(1, 4)));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(1, 5)));
	}

	@Test
	void testConstraintAlreadyUsed() {
		JPanel p = new JPanel(new CalcLayout());
		p.add(new JLabel("x"), new RCPosition(1, 1));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), new RCPosition(1, 1)));
		p.add(new JLabel("z"), new RCPosition(3, 2));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("z"), new RCPosition(3, 2)));

		JLabel label = new JLabel("y");
		p.add(label, new RCPosition(2, 2));
		// allow "adding" the same component
		p.add(label, new RCPosition(2, 2));
	}

	@Test
	void testConstraintParsingFromString() {
		JPanel p = new JPanel(new CalcLayout());
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "0,1"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "0,4"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "-1,1"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "6,1"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "6,4"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "3,0"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "3,8"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "1,2"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "1,3"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "1,4"));
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "1,5"));

		p.add(new JLabel("x"), "1,1");
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("x"), "1,1"));
		p.add(new JLabel("z"), "3,2");
		assertThrows(CalcLayoutException.class,
				() -> p.add(new JLabel("z"), "3,2"));

		JLabel label = new JLabel("y");
		p.add(label, "2,2");
		// allow "adding" the same component
		p.add(label, "2,2");
	}

	@Test
	void testPreferredSize1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(10, 30));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(20, 15));
		p.add(l1, new RCPosition(2, 2));
		p.add(l2, new RCPosition(3, 3));

		Dimension dim = p.getPreferredSize();
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}

	@Test
	void testPreferredSize2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(108, 15));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(16, 30));
		p.add(l1, new RCPosition(1, 1));
		p.add(l2, new RCPosition(3, 3));

		Dimension dim = p.getPreferredSize();
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}

}
