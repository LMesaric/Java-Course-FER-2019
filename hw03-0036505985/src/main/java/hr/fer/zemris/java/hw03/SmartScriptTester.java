package hr.fer.zemris.java.hw03;

import static hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser.createOriginalDocumentBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Demonstrative program for {@link SmartScriptParser}. Accepts a single
 * command-line argument: path to document.
 * 
 * @author Luka Mesaric
 */
public class SmartScriptTester {

	/**
	 * Program entry point.
	 * 
	 * @param args path to document.
	 * @throws IOException if document cannot be opened or read
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.out.println("Exactly one argument expected (path to document).");
			System.exit(-1);
		}

		String docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.out.println("Reason: " + e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println("ORIGINAL:\n");
		System.out.println(docBody);
		System.out.println("\n----------------------");
		System.out.println("RECREATED:\n");
		System.out.println(originalDocumentBody); // should write something like original content of docBody

		boolean documentsMatch = new SmartScriptParser(originalDocumentBody).getDocumentNode().equals(document);
		System.out.println("\n----------------------");
		System.out.println("Documents match: " + documentsMatch);
	}

}
