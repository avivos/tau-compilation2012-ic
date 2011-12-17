package IC;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import IC.Parser.Parser;
import IC.AST.ASTNode;
import IC.AST.PrettyPrinter;
import java_cup.runtime.*;

import IC.Parser.Lexer;


public class Compiler
{
	private static boolean debugMode = false;
	private static boolean printtokens;
	private static boolean printASTFlag;
	private static boolean libraryFlag = false;
	private static String libraryFile;
	private static PrintWriter log = null;
	private static PrintWriter mainState = null;

	public static void main(String[] args) {
		try {
			// handle arguments
			parseMainArgs(args);
			
			if (debugMode){
				FileWriter outFile = new FileWriter("compiler.log");
				log = new PrintWriter(outFile);
				log.println("Starting Log writing");
				
				FileWriter stateFile = new FileWriter("main.state");
				mainState = new PrintWriter(stateFile);
			}

			// parse the library file if needed
			if (libraryFlag){
				parseFile(libraryFile);
			}

			// Parse the input file
			parseFile(args[0]);
		} catch (Exception e) {
			System.out.print(e);
			printState("ERROR");
			closeFiles();
			return;
		}

		printState("OK");
		closeFiles();
		return;
	}

	//////////
	// these are some helper funcs.
	
	private static void closeFiles(){
		if (debugMode){
			//close PrintWriters
			mainState.close();
			log.close();			
			
		}
	}
	
	private static void print(String msg, PrintWriter writer){
		if (debugMode){
			writer.println(msg);
		}
	}
	
	private static void printState(String msg){
		print(msg, mainState);
	}
	
	private static void printToLog(String msg){
		print(msg, log);		
	}

	private static void parseMainArgs(String[] args) {
		Set<String> argSet = parseArgs(args);
		if (argSet.isEmpty()) {
			System.out.println("Error: Missing input file argument!");
			printToLog("Error: Missing input file argument!");
			printUsage();
			System.exit(-1);
		}
		if (argSet.contains("-print-ast")) {
			printASTFlag = true;
		}
		for (String arg : args) {
			if (arg.startsWith("-L")){
				printToLog("a library file was given");
				libraryFlag = true;
				libraryFile = arg.substring(2);
				break;
			}
		}

		if (argSet.contains("-debug")){
			//printASTFlag = printtokens = true;
			debugMode = true;
		}
		else {
			printtokens = false;
		}
	}
	
	protected static void parseFile(String filename)
			throws FileNotFoundException, Exception {
		printToLog("\nParsing file: " + filename);
		
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
		printToLog("\nFinished Parsing file: " + filename + "" +
				"=================================================");
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