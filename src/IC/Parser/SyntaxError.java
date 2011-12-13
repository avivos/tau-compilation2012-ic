package IC.Parser;

public class SyntaxError extends Exception
{
	int errorLine = 0;
	int token_type = -1;
	boolean have_content = false; //this is a flag to notify we have string, number or something
	Object token_value;
	String msg;

    public SyntaxError(String message, int id, int line) {
    	this.token_type = id;
    	this.have_content = false;
    	this.msg = message;
    	this.errorLine = line;
    }
    
    public SyntaxError(String message, int id, Object val, int line) {
    	this.have_content = true;
    	this.token_type = id;
    	this.msg = message;
    	this.token_value = val;
    	this.errorLine = line;
    }
    
    public String toString(){
    	ParserCtrl PC = ParserCtrl.getParserCtrl();
    	if (!have_content)
    		return (this.errorLine+":"+this.msg+" "+PC.getTokenName(token_type));
    	else
    		return (this.errorLine+":"+this.msg+" "+PC.getTokenName(token_type)+" ("+token_value.toString()+")");
    }
}

