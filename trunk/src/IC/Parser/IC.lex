package IC.Parser;


%%

%class Lexer
%public
%function next_token
%type Token
%line
%scanerror LexicalError
%cup
%state SL_COMMENTS
%state COMMENTS
%state COMMENTSEND
%state STRING

%{
	//our code:
	
	StringBuffer STR_BUFFER = new StringBuffer();
	boolean COMMENTS_OPEN = false;
	boolean STRING_OPEN = false;
	
	public int getLineNumber() { return yyline+1; }
%}
%eofval{
	if (COMMENTS_OPEN) 
		throw new LexicalError("Lexical error: Comment '/*' left unclosed",yyline+1);
	if (STRING_OPEN) 
		throw new LexicalError("Lexical error: Quote string is left unclosed",yyline+1);
  	return new Token(sym.EOF,yyline+1);
%eofval}

ALPHA = [A-Za-z_]
DIGIT = [0-9]
ALPHA_NUMERIC = {ALPHA}|{DIGIT}
NUMBER =({DIGIT})+
ILLEGAL_NUMER=[0]+[1-9]({DIGIT})*
WHITE_SPACE = [ |\r|\n|\r\n|\t]


%%

//ignores white spaces
<YYINITIAL> {WHITE_SPACE} {}

<YYINITIAL> "(" 			{ return new Token(sym.LP,yyline+1); }
<YYINITIAL> ")" 			{ return new Token(sym.RP,yyline+1); }
<YYINITIAL> "=" 			{ return new Token(sym.ASSIGN,yyline+1); }
<YYINITIAL> "boolean" 		{ return new Token(sym.BOOLEAN,yyline+1); }
<YYINITIAL> "break" 		{ return new Token(sym.BREAK,yyline+1); }
<YYINITIAL> "class" 		{ return new Token(sym.CLASS,yyline+1); }
<YYINITIAL> "," 			{ return new Token(sym.COMMA,yyline+1); }
<YYINITIAL> "continue" 		{ return new Token(sym.CONTINUE,yyline+1); }
<YYINITIAL> "/" 			{ return new Token(sym.DIVIDE,yyline+1); }
<YYINITIAL> "." 			{ return new Token(sym.DOT,yyline+1); }
<YYINITIAL> "extends" 		{ return new Token(sym.EXTENDS,yyline+1); }
<YYINITIAL> "else" 			{ return new Token(sym.ELSE,yyline+1); }
<YYINITIAL> "false" 		{ return new Token(sym.FALSE,yyline+1); }
<YYINITIAL> ">" 			{ return new Token(sym.GT,yyline+1); }
<YYINITIAL> ">=" 			{ return new Token(sym.GTE,yyline+1); }
<YYINITIAL> "if" 			{ return new Token(sym.IF,yyline+1); }
<YYINITIAL> "int" 			{ return new Token(sym.INT,yyline+1); }
<YYINITIAL> "integer" 		{ return new Token(sym.INTEGER,yyline+1); }
<YYINITIAL> "&&" 			{ return new Token(sym.LAND,yyline+1); }
<YYINITIAL> "[" 			{ return new Token(sym.LB,yyline+1); }
<YYINITIAL> "{" 			{ return new Token(sym.LCBR,yyline+1); }
<YYINITIAL> "length" 		{ return new Token(sym.LENGTH,yyline+1); }
<YYINITIAL> "new" 			{ return new Token(sym.NEW ,yyline+1); }
<YYINITIAL> "!" 			{ return new Token(sym.LNEG ,yyline+1); }
<YYINITIAL> "||" 			{ return new Token(sym.LOR ,yyline+1); }
<YYINITIAL> "<" 			{ return new Token(sym.LT ,yyline+1); }
<YYINITIAL> "<=" 			{ return new Token(sym.LTE ,yyline+1); }
<YYINITIAL> "-" 			{ return new Token(sym.MINUS ,yyline+1); }
<YYINITIAL> "%" 			{ return new Token(sym.MOD ,yyline+1); }
<YYINITIAL> "*" 			{ return new Token(sym.MULTIPLY ,yyline+1); }
<YYINITIAL> "!=" 			{ return new Token(sym.NEQUAL ,yyline+1); }
<YYINITIAL> "==" 			{ return new Token(sym.EQUAL ,yyline+1); }
<YYINITIAL> "null" 			{ return new Token(sym.NULL ,yyline+1); }
<YYINITIAL> "+" 			{ return new Token(sym.PLUS ,yyline+1); }
<YYINITIAL> "]" 			{ return new Token(sym.RB ,yyline+1); }
<YYINITIAL> "}" 			{ return new Token(sym.RCBR ,yyline+1); }
<YYINITIAL> "return" 		{ return new Token(sym.RETURN ,yyline+1); }
<YYINITIAL> ";" 			{ return new Token(sym.SEMI ,yyline+1); }
<YYINITIAL> "static" 		{ return new Token(sym.STATIC ,yyline+1); }
<YYINITIAL> "string" 		{ return new Token(sym.STRING ,yyline+1); }
<YYINITIAL> "this" 			{ return new Token(sym.THIS ,yyline+1); }
<YYINITIAL> "true"			{ return new Token(sym.TRUE ,yyline+1); }
<YYINITIAL> "void"			{ return new Token(sym.VOID ,yyline+1); }
<YYINITIAL> "while" 		{ return new Token(sym.WHILE ,yyline+1); }

 


//A rule for // comments
<YYINITIAL> "//" { yybegin(SL_COMMENTS); }
<SL_COMMENTS> . {}
<SL_COMMENTS> [\n] { yybegin(YYINITIAL); }

//A rule for /**/ comments
<YYINITIAL> "/*" { COMMENTS_OPEN= true; yybegin(COMMENTS); }
<COMMENTS> [^\*] { }
<COMMENTS> \* { yybegin(COMMENTSEND);} 


<COMMENTSEND> \* {  }
<COMMENTSEND> \/ { COMMENTS_OPEN= false; yybegin(YYINITIAL); }
<COMMENTSEND> [^\*/] {yybegin(COMMENTS); }

//Class identifier
<YYINITIAL> [A-Z]({ALPHA_NUMERIC})* { 
	  return new Token(sym.CLASS_ID, yytext(), yyline+1);
	}
// identifier
<YYINITIAL> [a-z]({ALPHA_NUMERIC})* {
	  return new Token(sym.ID, yytext(), yyline+1);
	}
//Digits
<YYINITIAL> ({ILLEGAL_NUMER}) {
	throw new LexicalError("Illegal number: strats with zero '"+ yytext()+"'",yyline+1);}

<YYINITIAL> ({NUMBER}) { 
	try {
		Integer.parseInt(yytext());
		return new Token (sym.INTEGER,Integer.parseInt(yytext()),yyline+1);
    }
	catch (NumberFormatException e) {
		throw new LexicalError( "Number '"+yytext()+ "' is out of range",yyline+1);
	}
}

//A rule for String
<YYINITIAL> "\"" { STRING_OPEN=true;STR_BUFFER.delete(0,STR_BUFFER.length());
				   yybegin(STRING); }
<STRING> \"
        {STRING_OPEN=false;	
		yybegin(YYINITIAL); 
		return new Token(sym.QUOTE,"\"" + STR_BUFFER.toString() + "\"",yyline+1); 
		}
<STRING> [\\|\t]
		{ throw new LexicalError("Illegal string token: '" + yytext()+"'", yyline+1); }
<STRING> [\r\n|\n] 
          { throw new LexicalError("Lexical error: Quote string is left unclosed",yyline+1);}
<STRING> ([ |!|#-\[|\]-~]|"\\\\"|"\\\""|"\\t"|"\\n")* { STR_BUFFER.append(yytext());}


[-]?[0-9]+({ALPHA})+ { throw new LexicalError("Illegal identifier: '" + yytext()+"'", yyline+1); }

<YYINITIAL> . {throw new LexicalError("Lexical error: illegal token '"+yytext()+"'",yyline+1);}

