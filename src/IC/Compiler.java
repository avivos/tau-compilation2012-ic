package IC;

import java.io.FileReader;

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
	private static boolean printtokens;

	public static String translteInt(int id){
		switch (id){
		case 1: {return "LP"; }
		case 2: {return "RP"; }
		case 3: {return "ASSIGN";   }
		case 4: {return "BOOLEAN";  }
		case 5: {return "BREAK";  }
		case 6: {return "CLASS";  }
		case 7: {return "CLASS_ID";   }
		case 8: {return "COMMA";   }
		case 9: {return "CONTINUE";   }
		case 10: {return "DIVIDE";   }
		case 11: {return "DOT";   }
		case 12: {return "EQUAL";   }
		case 13: {return "EXTENDS";   }
		case 14: {return "ELSE";   }
		case 15: {return "FALSE";   }
		case 16: {return "GT";   }
		case 17: {return "GTE";   }
		case 18: {return "ID";   }
		case 19: {return "IF";   }
		case 20: {return "INT";   }
		case 21: {return "INTEGER";   }
		case 22: {return "LAND";   }
		case 23: {return "LB";   }
		case 24: {return "LCBR";   }
		case 25: {return "LENGTH";   }
		case 26: {return "NEW";   }
		case 27: {return "LNEG";   }
		case 28: {return "LOR";   }
		case 29: {return "LT";   }
		case 30: {return "LTE";   }
		case 31: {return "MINUS";   }
		case 32: {return "MOD";   }
		case 33: {return "MULTIPLY";   }
		case 34: {return "NEQUAL";   }
		case 35: {return "NULL";   }
		case 36: {return "PLUS";   }
		case 37: {return "RB";   }
		case 38: {return "RCBR";   }
		case 39: {return "RETURN";   }
		case 40: {return "SEMI";   }
		case 41: {return "STATIC";   }
		case 42: {return "STRING";   }
		case 43: {return "QUOTE";   }
		case 44: {return "THIS";   }
		case 45: {return "TRUE";   }
		case 46: {return "VOID";   }
		case 47: {return "WHILE";   }
		case 48: {return "EOF"; }
		default: return "error";
		}

	}

	public static void main(String[] args) {
		/*Token currToken;
        try {
            FileReader txtFile = new FileReader(args[0]);
            Lexer scanner = new Lexer(txtFile);
            do {
                currToken = scanner.next_token();
                // do something with currToken
                System.out.print((currToken.line)+":"+ translteInt(currToken.id));
                if (currToken.value != null){
                	System.out.print("(" + currToken.value+")");
                }
                System.out.println();
            } while (currToken.sym != sym.EOF);

        }catch (LexicalError err){
        	System.out.println(err.toString());

        }catch (Exception e) {
            throw new RuntimeException("IO Error (brutal exit)"  + e.toString());
        }
      }*/

		try {
			if (args.length == 0) {
				System.out.println("Error: Missing input file argument!");
				System.exit(-1);
			}
			if (args.length == 2) {
				if (args[1].equals("-printtokens")) {
					printtokens = true;
				}
				else {
					System.exit(-1);
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

			// Pretty-print the program to System.out
			PrettyPrinter printer = new PrettyPrinter(args[0]);
			System.out.print(printer.visit(root));
			/*
			// Interpret the program
			SLPEvaluator evaluator = new SLPEvaluator(root);
			evaluator.evaluate();*/
		} catch (Exception e) {
			System.out.print(e);
		}


	}
}