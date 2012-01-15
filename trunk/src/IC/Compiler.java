package IC;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import IC.Parser.LibraryParser;
import IC.Parser.Parser;
import IC.AST.ASTNode;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import java_cup.runtime.*;

import IC.Parser.Lexer;
import Visitors.SemanticChecker;
import Visitors.SymbolTableCreator;


public class Compiler
{
	private static boolean debugMode = false;
	private static boolean printtokens;
	private static boolean printASTFlag;        
	private static boolean libraryFlag = false; 
	private static boolean dumpSymTblFlag = false;  
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
			ASTNode LibNode = null;
			if (libraryFlag)
			{
				LibNode = parseFile(libraryFile,true,null);
			}
				
			// Parse the input file
			ASTNode ProgNode = parseFile(args[0],false,LibNode);
			

	        
	        
	        /// semantic checks
	        SemanticChecker semanticChecker = new SemanticChecker();
	        ProgNode.accept(semanticChecker);
			
	        /// print symbol table if needed
            if (dumpSymTblFlag)
            {
                    System.out.println();
                    System.out.println(ProgNode.getSymbolTable());
                    System.out.println();
                    System.out.println(TypeTable.Table.toString(args[0]));
            }
	        
			
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

			debugMode = true;
		}
		if (argSet.contains("-dump-symtab")){

			dumpSymTblFlag = true;
		}
		else {
			printtokens = false;
		}
	}

	protected static ASTNode parseFile(String filename, boolean libFlag, ASTNode lib)
			throws FileNotFoundException, Exception {
		printToLog("\nParsing file: " + filename);

		FileReader txtFile = new FileReader(filename);
		Lexer scanner = new Lexer(txtFile);
		
		Symbol parseSymbol;
		
		if (libFlag)
		{
			LibraryParser parser = new LibraryParser(scanner);
			parser.printTokens = printtokens;
			parseSymbol = parser.parse();
		}
		else
		{
			Parser parser = new Parser(scanner);
			parser.printTokens = printtokens;
			parseSymbol = parser.parse();
		}
		
		//this is for our internal debug
		printToLog("\nFinished Parsing file: " + filename + "" +
				"=================================================");

		if ((parseSymbol!=null) && (parseSymbol.value!=null)) { // need both checks for "segfault" check
		
			System.out.println("Parsed " + filename + " successfully!");
			ASTNode root = (Program) parseSymbol.value;
			// Pretty-print the program to System.out if needed
			if (printASTFlag ){
				//input file printer
				PrettyPrinter printer = new PrettyPrinter(filename);
				System.out.println(printer.visit(root));
			}
			
			//////// creating the symbol table
			//creating visitor 
	        SymbolTableCreator symbolTableCreator = new SymbolTableCreator(filename, lib);
	        symbolTableCreator.setLibraryFlag(libFlag);
	        //the visitor will build the symbol table based on the AST

	        root.accept(symbolTableCreator);
	        
			return root;
		
		} else {
			System.out.println("Parsing of " + filename + " failed.");
			return null;
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