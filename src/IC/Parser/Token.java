package IC.Parser;

import java_cup.runtime.Symbol;



public class Token extends Symbol {
	public int id = 0;
	public int line = 0;
	public Object value;
	
	
    public Token(int id, int line) {
        super(id, null);
        this.line = line;
        this.id = id;
    }
    
    public Token(int id,Object val, int line) {
        super(id, val);
        this.id = id;
        this.line = line;
        //this.value = new Return_Object(line,val);
        this.value = val; //should be replaced with elaborate object ?
    }

	public int getLine() {	// WE CHANGED THIS METHOD TO RETURN int
		return line;
	}
    
    /*
    public class Return_Object {
    	public int line = 0;
    	public Object value;
    	
    	public Return_Object (int l, Object obj){
    		this.line = l;
    		this.value = obj;
    	}
    	
    }
    */
}

