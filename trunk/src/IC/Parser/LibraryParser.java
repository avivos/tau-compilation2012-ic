
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Tue Dec 13 16:41:45 IST 2011
//----------------------------------------------------

package IC.Parser;

import java_cup.runtime.*;
import IC.AST.*;
import IC.DataTypes;
import java.util.List;
import java.util.ArrayList;
import IC.UnaryOps;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Tue Dec 13 16:41:45 IST 2011
  */
public @SuppressWarnings(value={"all"}) class LibraryParser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public LibraryParser() {super();}

  /** Constructor which sets the default scanner. */
  public LibraryParser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public LibraryParser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\015\000\002\002\007\000\002\002\004\000\002\003" +
    "\004\000\002\003\002\000\002\007\011\000\002\004\003" +
    "\000\002\004\003\000\002\004\003\000\002\004\003\000" +
    "\002\004\003\000\002\004\005\000\002\005\002\000\002" +
    "\005\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\030\000\004\011\005\001\002\000\004\002\032\001" +
    "\002\000\004\012\006\001\002\000\004\031\007\001\002" +
    "\000\006\046\ufffe\052\ufffe\001\002\000\006\046\012\052" +
    "\011\001\002\000\014\007\017\012\015\053\014\057\020" +
    "\062\021\001\002\000\004\002\001\001\002\000\006\046" +
    "\uffff\052\uffff\001\002\000\004\004\ufffa\001\002\000\004" +
    "\004\ufff8\001\002\000\004\004\024\001\002\000\004\004" +
    "\ufffb\001\002\000\004\004\ufff9\001\002\000\006\004\ufffc" +
    "\027\022\001\002\000\004\061\023\001\002\000\004\004" +
    "\ufff7\001\002\000\004\030\025\001\002\000\004\050\ufff6" +
    "\001\002\000\004\050\ufff5\001\002\000\004\050\030\001" +
    "\002\000\004\051\031\001\002\000\006\046\ufffd\052\ufffd" +
    "\001\002\000\004\002\000\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\030\000\004\002\003\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\004\003\007\001\001" +
    "\000\004\007\012\001\001\000\004\004\015\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\006\005\026\006\025\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$LibraryParser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$LibraryParser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$LibraryParser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {

	Token t = lexer.next_token();
	t.left = t.getLine();
if (printTokens)
	System.out.println(t.getLine() + "::" + t);
return t; 

    }


	/** Causes the parser to print every token it reads.
	 * This is useful for debugging.
	 */
	public boolean printTokens;

private Lexer lexer;

public LibraryParser(Lexer lexer) {
	super(lexer);
	this.lexer = lexer;
}

public int getLine() {
	return lexer.getLineNumber();
}

public void syntax_error(Symbol s) {
	Token tok = (Token) s;
	System.out.println("Line " + tok.getLine()+": Syntax error; unexpected " + tok);
}

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$LibraryParser$actions {
  private final LibraryParser parser;

  /** Constructor */
  CUP$LibraryParser$actions(LibraryParser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$LibraryParser$do_action(
    int                        CUP$LibraryParser$act_num,
    java_cup.runtime.lr_parser CUP$LibraryParser$parser,
    java.util.Stack            CUP$LibraryParser$stack,
    int                        CUP$LibraryParser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$LibraryParser$result;

      /* select the action based on the action number */
      switch (CUP$LibraryParser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // formals ::= formals_list 
            {
              List<Formal> RESULT =null;
		int listleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int listright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		List<Formal> list = (List<Formal>)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		 
	RESULT = list; 

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("formals",3, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // formals ::= 
            {
              List<Formal> RESULT =null;
		
	RESULT = new ArrayList<Formal>();

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("formals",3, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // type ::= INT LB RB 
            {
              Type RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).right;
		Object name = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).value;
		
	RESULT = new PrimitiveType(nameleft, DataTypes.INT);
	RESULT.incrementDimension();

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // type ::= CLASS_ID 
            {
              Type RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		String name = (String)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		
	RESULT = new UserType(nameleft, name);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // type ::= VOID 
            {
              Type RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object name = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		
	RESULT = new PrimitiveType(nameleft, DataTypes.VOID);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // type ::= STRING 
            {
              Type RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object name = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		
	RESULT = new PrimitiveType(nameleft, DataTypes.STRING);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // type ::= BOOLEAN 
            {
              Type RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object name = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		
	RESULT = new PrimitiveType(nameleft, DataTypes.BOOLEAN);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // type ::= INT 
            {
              Type RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		Object name = (Object)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		
	RESULT = new PrimitiveType(nameleft, DataTypes.INT);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("type",2, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // method ::= STATIC type ID LP formals RP SEMI 
            {
              LibraryMethod RESULT =null;
		int type_nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).left;
		int type_nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).right;
		Type type_name = (Type)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-5)).value;
		int m_nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-4)).left;
		int m_nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-4)).right;
		String m_name = (String)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-4)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).right;
		List<Formal> f = (List<Formal>)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-2)).value;
		 
	RESULT = new LibraryMethod(type_name, m_name, f); 

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("method",5, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-6)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // methodDeclList ::= 
            {
              List<Method> RESULT =null;
		 
	RESULT = new ArrayList<Method>();

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("methodDeclList",1, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // methodDeclList ::= methodDeclList method 
            {
              List<Method> RESULT =null;
		int dclListleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).left;
		int dclListright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).right;
		List<Method> dclList = (List<Method>)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).value;
		int mleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).left;
		int mright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()).right;
		LibraryMethod m = (LibraryMethod)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.peek()).value;
		
	RESULT = dclList;
	RESULT.add(m);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("methodDeclList",1, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= classDecl EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).right;
		ICClass start_val = (ICClass)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).value;
		RESULT = start_val;
              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$LibraryParser$parser.done_parsing();
          return CUP$LibraryParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // classDecl ::= CLASS CLASS_ID LCBR methodDeclList RCBR 
            {
              ICClass RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).right;
		String name = (String)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-3)).value;
		int dclListleft = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).left;
		int dclListright = ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).right;
		List<Method> dclList = (List<Method>)((java_cup.runtime.Symbol) CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-1)).value;
		
	RESULT = new ICClass(nameleft, (String)name, new ArrayList<Field>(), dclList);

              CUP$LibraryParser$result = parser.getSymbolFactory().newSymbol("classDecl",0, ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.elementAt(CUP$LibraryParser$top-4)), ((java_cup.runtime.Symbol)CUP$LibraryParser$stack.peek()), RESULT);
            }
          return CUP$LibraryParser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

