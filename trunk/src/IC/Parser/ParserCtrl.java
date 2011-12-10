package IC.Parser;

import java.util.LinkedList;
import java.util.List;

import IC.AST.Field;
import IC.AST.Method;
import IC.AST.Type;

// a singleton control-class for the parser
public class ParserCtrl {
	// the singleton instance of the class
	private static ParserCtrl singleton = null;
		
	public ParserCtrl(){}	// empty (no effect) constructor
	
	public static ParserCtrl getParserCtrl(){
		if (singleton == null){
			singleton = new ParserCtrl();
		}
		return singleton;
	}
	
	private List<Field> fieldList = new LinkedList<Field>();
	private List<Method> methodList = new LinkedList<Method>();
	
	// these to sets of methods collect fields/methods to lists while parsing
	// a class' code and then release the lists to create the ICClass without a lot of 
	// Constructor-usage (we save new instance calls) 
	
	public void collectFields(List<Field> f){
		fieldList.addAll(f);
	}
	
	public List<Field> releaseFieldList(){
		List<Field> retList = fieldList;
		fieldList = new LinkedList<Field>();
		return retList;
	}
	
	public void collectMethod(Method m){
		methodList.add(m);
	}
	
	public List<Method> releaseMethodList(){
		List<Method> retList = new LinkedList<Method>();
		methodList = new LinkedList<Method>();
		return retList;
	}
	
	public List<Field> convertToFieldList(Type type, List<String> list){
		List<Field> retList = new LinkedList<Field>();
		for (String name : list) {
			fieldList.add(new Field(type, name));
		}
		
		return retList;
	}
	
}
