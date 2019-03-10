package hr.fer.zemris.java.hw01;

import static hr.fer.zemris.java.hw01.UniqueNumbers.addNode;
import static hr.fer.zemris.java.hw01.UniqueNumbers.containsValue;
import static hr.fer.zemris.java.hw01.UniqueNumbers.sortedValue;
import static hr.fer.zemris.java.hw01.UniqueNumbers.treeSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

class UniqueNumbersTest {

	private static TreeNode builtTree = null;
	private static TreeNode emptyTree = null;

	@BeforeAll
	static void setUpBeforeClass() {
		builtTree = addNode(builtTree, 42);
		builtTree = addNode(builtTree, 76);
		builtTree = addNode(builtTree, 21);
		builtTree = addNode(builtTree, 76);
		builtTree = addNode(builtTree, 35);
	}

	@Test
	void testAddNode() {
		assertEquals(42, builtTree.value);
		assertEquals(21, builtTree.left.value);
		assertEquals(35, builtTree.left.right.value);
		assertEquals(76, builtTree.right.value);
	}

	@Test
	void testTreeSize() {
		assertEquals(0, treeSize(emptyTree));
		assertEquals(4, treeSize(builtTree));
	}

	@Test
	void testContainsValue() {
		assertTrue(containsValue(builtTree, 76));
		assertFalse(containsValue(builtTree, 25));
		assertFalse(containsValue(emptyTree, 25));
	}

	@Test
	void testSortedValue() {
		assertEquals("21 35 42 76", sortedValue(builtTree, true));
		assertEquals("76 42 35 21", sortedValue(builtTree, false));
		assertEquals("", sortedValue(emptyTree, true));
		assertEquals("", sortedValue(emptyTree, false));
	}

}
