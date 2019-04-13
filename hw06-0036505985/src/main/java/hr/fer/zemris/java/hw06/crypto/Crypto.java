package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import hr.fer.zemris.java.hw06.ExceptionUtil;

/**
 * Command-line program that allows the user to encrypt/decrypt given file using
 * the AES crypto-algorithm and the 128-bit encryption key or calculate and
 * check the SHA-256 file digest.
 * 
 * @author Luka Mesaric
 */
public class Crypto {

	/**
	 * Program entry point.
	 * 
	 * @param args program arguments (commands and file paths)
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Program must receive at least two arguments. "
					+ "Received: " + args.length);
			return;
		}
		try (Scanner sc = new Scanner(System.in)) {
			switch (args[0]) {
			case "checksha":
				chechSha256Helper(Paths.get(args[1]), sc);
				break;
			case "encrypt": // fall through
			case "decrypt":
				encryptionCommonHelper(args, sc);
				break;
			default:
				System.out.println("Unknown command: " + args[0]);
				return;
			}
		} catch (IOException | NoSuchAlgorithmException | InvalidKeyException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Helper method for communicating with the user through standard output and by
	 * using provided scanner. Encrypts or decrypts wanted file using key and vector
	 * supplied by user.
	 * 
	 * @param args array of arguments - keyword (encrypt/decrypt), path to source
	 *             file, and path to destination file
	 * @param sc   scanner
	 * 
	 * @throws NullPointerException               if any argument is
	 *                                            <code>null</code>
	 * 
	 * @throws IOException                        if arguments represent files that
	 *                                            do not exist on disk, or it was
	 *                                            not possible to read or write to
	 *                                            files for any reason
	 * @throws BadPaddingException                if there is an issue with
	 *                                            encryption or decryption
	 * @throws IllegalBlockSizeException          if there is an issue with
	 *                                            encryption or decryption
	 * @throws InvalidAlgorithmParameterException if there is an issue with
	 *                                            encryption or decryption
	 * @throws NoSuchPaddingException             if there is an issue with
	 *                                            encryption or decryption
	 * @throws NoSuchAlgorithmException           if there is an issue with
	 *                                            encryption or decryption
	 * @throws InvalidKeyException                if there is an issue with
	 *                                            encryption or decryption
	 */
	private static void encryptionCommonHelper(String[] args, Scanner sc)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		ExceptionUtil.validateNotNull(args, "args");
		ExceptionUtil.validateNotNull(sc, "sc");

		if (args.length != 3) {
			System.out.println(
					"For encryption and decrypt exactly 2 arguments are needed.");
			return;
		}

		boolean encrypt = true;
		if (args[0].equals("decrypt")) {
			encrypt = false;
		}

		System.out.format("Please provide password as hex-encoded text "
				+ "(16 bytes, i.e. 32 hex-digits):%n>");
		String password = sc.nextLine();

		System.out.format("Please provide initialization vector as "
				+ "hex-encoded text (32 hex-digits):%n>");
		String initializationVector = sc.nextLine();

		Path source = Paths.get(args[1]);
		Path destination = Paths.get(args[2]);

		encryption(source, destination, password, initializationVector, encrypt);

		System.out.format("%s completed. Generated file %s based on file %s.%n",
				encrypt ? "Encryption" : "Decryption",
				destination, source);
	}

	/**
	 * Helper method for communicating with the user through standard output and by
	 * using provided scanner. Calculates SHA-256 file digest.
	 * 
	 * @param path path to file whose digest is to be calculated
	 * @param sc   scanner
	 * 
	 * @throws NullPointerException     if any argument is <code>null</code>
	 * @throws IOException              if <code>path</code> does not exist, or it
	 *                                  was not possible to read <code>path</code>
	 *                                  for any reason
	 * @throws NoSuchAlgorithmException if SHA-256 algorithm does not exist on the
	 *                                  running computer
	 */
	private static void chechSha256Helper(Path path, Scanner sc)
			throws NoSuchAlgorithmException, IOException {
		ExceptionUtil.validateNotNull(path, "path");
		ExceptionUtil.validateNotNull(sc, "sc");

		System.out.format("Please provide expected sha-256 digest for %s:%n> ", path);
		String expectedDigest = sc.nextLine();
		String calculatedDigest = calculateSha256(path);

		if (expectedDigest.equals(calculatedDigest)) {
			System.out.format(
					"Digesting completed. Digest of %s matches expected digest.%n",
					path.toString());
		} else {
			System.out.format(
					"Digesting completed. Digest of %s does not match the expected digest."
							+ " Digest was: %s%n",
					path.toString(), calculatedDigest);
		}
	}

	/**
	 * Calculates the SHA-256 file digest.
	 * 
	 * @param file path to file whose digest is to be calculated
	 * @return hex-encoding of the SHA-256 file digest, never <code>null</code>
	 * 
	 * @throws NullPointerException     if <code>file</code> is <code>null</code>
	 * @throws IOException              if <code>file</code> does not exist, or it
	 *                                  was not possible to read <code>file</code>
	 *                                  for any reason
	 * @throws NoSuchAlgorithmException if SHA-256 algorithm does not exist on the
	 *                                  running computer
	 */
	private static String calculateSha256(Path file)
			throws NoSuchAlgorithmException, IOException {
		ExceptionUtil.validateNotNull(file, "file");
		MessageDigest sha = MessageDigest.getInstance("SHA-256");

		try (InputStream is = new BufferedInputStream(Files.newInputStream(file))) {
			byte[] buff = new byte[8192]; // 8 KB
			while (true) {
				int r = is.read(buff);
				if (r < 1) {
					break;
				}
				sha.update(buff, 0, r);
			}
		}
		return Util.bytetohex(sha.digest());
	}

	/**
	 * Encrypts or decrypts file with path <code>source</code> by using symmetric
	 * crypto-algorithm <code>AES</code> and 128-bit encryption key.
	 * 
	 * @param source      file to be encrypted/decrypted
	 * @param destination where the result will be written
	 * @param keyText     password for encryption/decryption
	 * @param ivText      initialization vector
	 * @param encrypt     <code>true</code> if file should be encrypted,
	 *                    <code>false</code> if it should be decrypted
	 * 
	 * @throws NullPointerException               if any argument is
	 *                                            <code>null</code>
	 * @throws IOException                        if <code>source</code> does not
	 *                                            exist, or <code>destination</code>
	 *                                            does not represent a valid path on
	 *                                            disk, or it was not possible to
	 *                                            read or write to files for any
	 *                                            reason
	 * @throws NoSuchPaddingException             if there is an issue with
	 *                                            encryption or decryption
	 * @throws NoSuchAlgorithmException           if there is an issue with
	 *                                            encryption or decryption
	 * @throws InvalidAlgorithmParameterException if there is an issue with
	 *                                            encryption or decryption
	 * @throws InvalidKeyException                if there is an issue with
	 *                                            encryption or decryption
	 * @throws BadPaddingException                if there is an issue with
	 *                                            encryption or decryption
	 * @throws IllegalBlockSizeException          if there is an issue with
	 *                                            encryption or decryption
	 */
	private static void encryption(Path source, Path destination,
			String keyText, String ivText, boolean encrypt)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		ExceptionUtil.validateNotNull(source, "source");
		ExceptionUtil.validateNotNull(destination, "destination");
		ExceptionUtil.validateNotNull(keyText, "keyText");
		ExceptionUtil.validateNotNull(ivText, "ivText");

		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
				keySpec, paramSpec);

		try (InputStream is = new BufferedInputStream(Files.newInputStream(source));
				OutputStream os = new BufferedOutputStream(
						Files.newOutputStream(destination))) {
			byte[] buff = new byte[8192]; // 8 KB
			while (true) {
				int r = is.read(buff);
				if (r < 1) {
					os.write(cipher.doFinal());
					break;
				}
				os.write(cipher.update(buff, 0, r));
			}
		}
	}

}
