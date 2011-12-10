package IC.Parser;

public class LexicalError extends Exception
{
	int errorLine = 0;
	String msg;
    public LexicalError(String message) {
    	this.msg = message;
    
    }
    public LexicalError(String message,int line) {
    	this.msg = message;
    	this.errorLine = line;
    
    }
    
    public String toString(){
    	return (this.errorLine+":"+this.msg);
    }
}

