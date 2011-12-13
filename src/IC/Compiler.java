package IC;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import IC.Parser.Parser;
import IC.AST.ASTNode;
import IC.AST.PrettyPrinter;
import java_cup.runtime.*;

import IC.Parser.Lexer;


public class Compiler
{
	private static final boolean debugMode = false;
	private static boolean printtokens;
	private static boolean printASTFlag;
	private static boolean libraryFlag = false;
	private static String libraryFile;

	public static void main(String[] args) {
		try {
			// handle arguments
			Set<String> argSet = parseArgs(args);
			if (argSet.isEmpty()) {
				System.out.println("Error: Missing input file argument!");
				printUsage();
				System.exit(-1);
			}
			if (argSet.contains("-print-ast")) {
				printASTFlag = true;
			}
			for (String arg : args) {
				if (arg.startsWith("-L")){
					libraryFlag = true;
					libraryFile = arg.substring(2);
					break;
				}
			}

			if (argSet.contains("-debug") || debugMode){
				printASTFlag = printtokens = true;
			}
			else {
				printtokens = false;
			}

			// parse the library file if needed
			if (libraryFlag){
				parseFile(libraryFile);
			}

			// Parse the input file
			parseFile(args[0]);
		} catch (Exception e) {
			System.out.print(e);
		}


	}
	
	
	//////////
	// these are some helper funcs.

	protected static void parseFile(String filename)
			throws FileNotFoundException, Exception {
		FileReader txtFile = new FileReader(filename);
		Lexer scanner = new Lexer(txtFile);
		Parser parser = new Parser(scanner);
		parser.printTokens = printtokens;

		Symbol parseSymbol = parser.parse();
		System.out.println("Parsed " + filename + " successfully!");
		ASTNode root = (ASTNode) parseSymbol.value;

		// Pretty-print the program to System.out if needed
		if (printASTFlag){
			//input file printer
			PrettyPrinter printer = new PrettyPrinter(filename);
			System.out.println(printer.visit(root));
		}
	}

	public static void printUsage(){
		System.out.println("java IC.Compiler <file.ic> [ -L</path/to/libic.sig> ] [-print-ast]");
	}

	public static Set<String> parseArgs(String[] args){
		Set<String> argSet = new HashSet<String>();
		for (String arg : args){
			argSet.add(arg);
		}

		return argSet;
	}
}