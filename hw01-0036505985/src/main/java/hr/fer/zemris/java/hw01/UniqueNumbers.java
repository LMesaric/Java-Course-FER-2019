package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * User repeatedly inputs integers. If an integer was already used, it is
 * ignored. To end input, write <code>"kraj"</code>. All entered numbers will be
 * printed in ascending and in descending order.
 * <p>
 * Command line arguments are ignored.
 * </p>
 * 
 * @author Luka Mesaric
 */
public class UniqueNumbers {

	/**
	 * Simulates a node of a binary tree with integer values.
	 */
	public static class TreeNode {
		/**
		 * Left child.
		 */
		public TreeNode left;

		/**
		 * Right child.
		 */
		public TreeNode right;

		/**
		 * Node value.
		 */
		public int value;
	}

	/**
	 * Adds another node with value set to <code>value</code>. If such node exists,
	 * do nothing.
	 * 
	 * @param node  root node of binary tree
	 * @param value value to add to the binary tree inside a new node
	 * @return new root node, changed only if previous root was <code>null</code>
	 */
	public static TreeNode addNode(TreeNode node, int value) {

		if (node == null) {
			TreeNode newNode = new TreeNode();
			newNode.value = value;
			return newNode;
		}

		if (value < node.value) {
			node.left = addNode(node.left, value);
		} else if (value > node.value) {
			node.right = addNode(node.right, value);
		}

		return node;
	}

	/**
	 * Counts number of nodes in binary tree.
	 * 
	 * @param node root node
	 * @return number of nodes
	 */
	public static int treeSize(TreeNode node) {

		if (node == null) {
			return 0;
		}

		return 1 + treeSize(node.left) + treeSize(node.right);
	}

	/**
	 * Checks if tree already contains a node with given <code>value</code>.
	 * 
	 * @param node  root node
	 * @param value value for which to search
	 * @return <code>true</code> if such node exists, <code>false</code> otherwise
	 */
	public static boolean containsValue(TreeNode node, int value) {

		if (node == null) {
			return false;
		} else if (node.value == value) {
			return true;
		} else if (value < node.value) {
			return containsValue(node.left, value);
		} else {
			return containsValue(node.right, value);
		}
	}

	/**
	 * Traverses binary tree starting from <code>node</code> and returns a String of
	 * all values in sorted order.
	 * 
	 * @param node        root node
	 * @param isAscending <code>true</code> if order is ascending (in-order
	 *                    traversal), <code>false</code> if order is descending
	 *                    (out-order traversal).
	 * @return all values, sorted and separated by one blank
	 */
	public static String sortedValue(TreeNode node, boolean isAscending) {

		StringBuilder sb = new StringBuilder();

		sortedValueInternal(node, isAscending, sb);

		sb.setLength(Math.max(sb.length() - 1, 0));
		return sb.toString();
	}

	/**
	 * Traverses binary tree starting from <code>node</code> to visit all nodes in a
	 * sorted order. Saves visited nodes in <code>sb</code>.
	 * 
	 * @param node        current node
	 * @param isAscending <code>true</code> if order is ascending (in-order
	 *                    traversal), <code>false</code> if order is descending
	 *                    (out-order traversal)
	 * @param sb          StringBuilder used to collect all values
	 */
	private static void sortedValueInternal(TreeNode node, boolean isAscending, StringBuilder sb) {

		if (node == null) {
			return;
		}

		TreeNode visitFirst;
		TreeNode visitSecond;

		if (isAscending) {
			visitFirst = node.left;
			visitSecond = node.right;
		} else {
			visitFirst = node.right;
			visitSecond = node.left;
		}

		sortedValueInternal(visitFirst, isAscending, sb);
		sb.append(node.value).append(" ");
		sortedValueInternal(visitSecond, isAscending, sb);
	}

	/**
	 * Program entry point.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {

		try (Scanner sc = new Scanner(System.in)) {
			TreeNode node = null;

			while (true) {
				System.out.print("Unesite broj > ");

				if (sc.hasNextInt()) {
					int value = sc.nextInt();

					if (containsValue(node, value)) {
						System.out.println("Broj već postoji. Preskačem.");
					} else {
						node = addNode(node, value);
						System.out.println("Dodano.");
					}

				} else {
					String input = sc.next();

					if (!input.trim().equalsIgnoreCase("kraj")) {
						System.out.printf("'%s' nije cijeli broj.%n", input);
						continue;
					}

					System.out.printf("Ispis od najmanjeg: %s%n", sortedValue(node, true));
					System.out.printf("Ispis od najvećeg: %s%n", sortedValue(node, false));
					break;
				}
			}
		}
	}

}
