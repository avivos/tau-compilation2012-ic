package Lir;

public class LiteralUtil {
	
	static int id = 0;
	static int uniqid = 0;
	

	public static String createLiteralLabel() {
		String retString = "str" + id;
		id++;
		return retString;
	}
	
	public static int uniqID(){
		uniqid++;
		return uniqid;
	}
	

}
