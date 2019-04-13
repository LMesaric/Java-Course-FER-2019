package hr.fer.zemris.java.hw06.crypto;

import static hr.fer.zemris.java.hw06.crypto.Util.bytetohex;
import static hr.fer.zemris.java.hw06.crypto.Util.hextobyte;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * @author Luka Mesaric
 */
class UtilTest {

	@Test
	void testHexToByte() {
		assertArrayEquals(new byte[] { 1, -82, 34 }, hextobyte("01aE22"));
		assertArrayEquals(new byte[] { -1 }, hextobyte("FF"));
		assertArrayEquals(new byte[] { -128 }, hextobyte("80"));
		assertArrayEquals(new byte[] { -1, -1 }, hextobyte("fFFf"));
		assertArrayEquals(new byte[] { -128, -128 }, hextobyte("8080"));
		assertArrayEquals(new byte[] { 0, 0, 0 }, hextobyte("000000"));
	}

	@Test
	void testHexToByteEmpty() {
		assertArrayEquals(new byte[0], hextobyte(""));
	}

	@Test
	void testHexToByteNull() {
		assertThrows(NullPointerException.class, () -> hextobyte(null));
	}

	@Test
	void testHexToByteNotEven() {
		assertThrows(IllegalArgumentException.class, () -> hextobyte("1"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("123"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("12345"));
	}

	@Test
	void testHexToByteInvalidDigit() {
		assertThrows(IllegalArgumentException.class, () -> hextobyte("123g"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("-123"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("0x01"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("0 01"));
	}

	@Test
	void testByteToHex() {
		assertEquals("01ae22", bytetohex(new byte[] { 1, -82, 34 }));
		assertEquals("ff", bytetohex(new byte[] { -1 }));
		assertEquals("80", bytetohex(new byte[] { -128 }));
		assertEquals("ffff", bytetohex(new byte[] { -1, -1 }));
		assertEquals("8080", bytetohex(new byte[] { -128, -128 }));
		assertEquals("000000", bytetohex(new byte[] { 0, 0, 0 }));
	}

	@Test
	void testByteToHexEmpty() {
		assertEquals("", bytetohex(new byte[0]));
	}

	@Test
	void testByteToHexNull() {
		assertThrows(NullPointerException.class, () -> bytetohex(null));
	}

	@Test
	void testCompositionOne() {
		assertArrayEquals(new byte[] { 1, -82, 34 }, hextobyte(bytetohex(new byte[] { 1, -82, 34 })));
		assertArrayEquals(new byte[] { -1 }, hextobyte(bytetohex(new byte[] { -1 })));
		assertArrayEquals(new byte[] { -128 }, hextobyte(bytetohex(new byte[] { -128 })));
		assertArrayEquals(new byte[] { -1, -1 }, hextobyte(bytetohex(new byte[] { -1, -1 })));
		assertArrayEquals(new byte[] { -128, -128 }, hextobyte(bytetohex(new byte[] { -128, -128 })));
		assertArrayEquals(new byte[] { 0, 0, 0 }, hextobyte(bytetohex(new byte[] { 0, 0, 0 })));
	}

	@Test
	void testCompositionTwo() {
		assertEquals("01ae22", bytetohex(hextobyte("01Ae22")));
		assertEquals("ff", bytetohex(hextobyte("Ff")));
		assertEquals("80", bytetohex(hextobyte("80")));
		assertEquals("ffff", bytetohex(hextobyte("fFfF")));
		assertEquals("8080", bytetohex(hextobyte("8080")));
		assertEquals("000000", bytetohex(hextobyte("000000")));
	}

}
