package hr.fer.zemris.java.hw06.crypto;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.hw06.ExceptionUtil;

/**
 * Utility class for converting strings to byte arrays, and vice versa.
 */
public class Util {

	/**
	 * Radix for hexadecimal numbers.
	 */
	private static final int HEX_RADIX = 16;

	/**
	 * List of hexadecimal characters.
	 */
	// Using a map would make calculations a bit faster, but getting characters by
	// index would not be possible.
	// Using this list is about as fast as using a switch-case construct.
	private static final List<Character> DIGITS = Arrays.asList(
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f');

	/**
	 * Takes a hex-encoded String a returns corresponding array of bytes. Supports
	 * both upper case and lower case letters.<br>
	 * For zero-length string, a zero-length byte array will be returned.
	 *
	 * @param keyText hex-encoded string
	 * @return array of bytes represented by <code>keyText</code>
	 * 
	 * @throws IllegalArgumentException if number of characters in
	 *                                  <code>keyText</code> is not even, or any
	 *                                  character does not represent a valid
	 *                                  hexadecimal digit
	 * @throws NullPointerException     if <code>keyText</code> is <code>null</code>
	 */
	public static byte[] hextobyte(String keyText) {
		ExceptionUtil.validateNotNull(keyText, "keyText");
		if (keyText.length() % 2 != 0) {
			throw new IllegalArgumentException("Input must have an even number of characters: " + keyText);
		}

		byte[] bytes = new byte[keyText.length() / 2];
		char[] digits = keyText.toLowerCase().toCharArray();

		for (int d = 0, b = 0; d < digits.length; b++) {
			int first = DIGITS.indexOf(digits[d++]);
			int second = DIGITS.indexOf(digits[d++]);

			if (first < 0 || second < 0) {
				throw new IllegalArgumentException("Illegal character: '" + digits[first < 0 ? d - 2 : d - 1] + "'.");
			}

			bytes[b] = (byte) (first * HEX_RADIX + second);
		}

		return bytes;
	}

	/**
	 * Takes a byte array and creates its hex-encoding. Each byte will be
	 * represented by two characters, in big-endian notation. Only lower case
	 * characters will be used.<br>
	 * For zero-length array, an empty string will be returned.
	 *
	 * @param byteArray array of bytes to convert
	 * @return hex-encoding of <code>byteArray</code>
	 * 
	 * @throws NullPointerException if <code>byteArray</code> is <code>null</code>
	 */
	public static String bytetohex(byte[] byteArray) {
		ExceptionUtil.validateNotNull(byteArray, "byteArray");
		StringBuilder sb = new StringBuilder();
		for (byte b : byteArray) {
			sb.append(DIGITS.get((b & 0xF0) >> 4));
			sb.append(DIGITS.get(b & 0x0F));
		}
		return sb.toString();
	}

}
