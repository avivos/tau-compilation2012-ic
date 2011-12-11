package IC;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import IC.Parser.Parser;
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import java_cup.runtime.*;

import IC.Parser.LexicalError;
import IC.Parser.LibraryParser;
import IC.Parser.Token;
import IC.Parser.sym;
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
				FileReader libFile = new FileReader(libraryFile);
				Lexer libscanner = new Lexer(libFile);
				LibraryParser libparser = new LibraryParser(libscanner);
				libparser.printTokens = printtokens;

				Symbol parseLibSymbol = libparser.parse();
				System.out.println("Parsed library " + libraryFile + " successfully!");
				ICClass libRoot = (ICClass) parseLibSymbol.value;
				if (printASTFlag){
					//library printer
					PrettyPrinter libprinter = new PrettyPrinter(libraryFile);
					System.out.println(libprinter.visit(libRoot));

				}
			}

			// Parse the input file
			FileReader txtFile = new FileReader(args[0]);
			Lexer scanner = new Lexer(txtFile);
			Parser parser = new Parser(scanner);
			parser.printTokens = printtokens;

			Symbol parseSymbol = parser.parse();
			System.out.println("Parsed " + args[0] + " successfully!");
			Program root = (Program) parseSymbol.value;

			// Pretty-print the program to System.out if needed
			if (printASTFlag){
				//input file printer
				PrettyPrinter printer = new PrettyPrinter(args[0]);
				System.out.println(printer.visit(root));
			}
		} catch (Exception e) {
			System.out.print(e);
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