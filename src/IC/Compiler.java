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
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import java_cup.runtime.*;

import IC.Parser.Lexer;
import Lir.TranslationVisitor;
import Visitors.SemanticsChecks;
import Visitors.SymbolTableBuilder;


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
	private static boolean printLirFlag = false;


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


			if (ProgNode!=null){

				/// semantic checks
				SemanticsChecks semanticChecker = new SemanticsChecks();
				ProgNode.accept(semanticChecker);


				/// print symbol table if needed
				if (dumpSymTblFlag)
				{
					System.out.println();
					System.out.println(ProgNode.getSymbolTable());
					System.out.println();
					System.out.println(TypeTable.Table.toString(args[0]));
				}


				// Starting IC->LIR Translation
				Program program = (Program)ProgNode;

				// add the library node to the program class list
				if (libraryFlag){
					ICClass l = ((Program)LibNode).getClasses().get(0);
					program.getClasses().add(l);
				}

				TranslationVisitor translator = new TranslationVisitor();
				String trans = (String) program.accept(translator);

				if (printLirFlag){
					String lirFilename = args[0].substring(0, args[0].length()-3) + ".lir";
					FileWriter lirFile = new FileWriter(lirFilename);
					PrintWriter lirPrinter = new PrintWriter(lirFile);
					lirPrinter.println(trans);
					lirPrinter.close();
					lirFile.close();
				}
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

		//auto parse the lib file:
		libraryFlag = true;
		libraryFile = "src/IC/Parser/libic.sig";
		////////
		for (String arg : args) {
			if (arg.startsWith("-L")){
				printToLog("a library file was given");
				libraryFlag = true;
				libraryFile = arg.substring(2);
				continue;
			}
			if (arg.startsWith("-print-lir")){
				printLirFlag = true;
				continue;
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
			SymbolTableBuilder symbolTableCreator = new SymbolTableBuilder(filename, lib);
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