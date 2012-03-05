package Lir;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import IC.BinaryOps;
import IC.LiteralTypes;
import IC.UnaryOps;
import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.Expression;
import IC.AST.ExpressionBlock;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.Literal;
import IC.AST.LocalVariable;
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.Method;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import SymbolTable.Symbol;
import SymbolTable.Symbol.SymbolKind;

public class TranslationVisitor implements Visitor {

	boolean DebugFlag = true;
	void debugPrint(String msg){
		if (DebugFlag) System.out.print(msg);
	}

	Map<String, String> literalsMap = new HashMap<String,String>();					// a list of literal labels
	Map<String, ClassLayout> classLayoutMap = new HashMap<String, ClassLayout>();	// a map from class name to classLayout
	Map<Method, String> methodToClassName = new HashMap<Method, String>();			// keep a map of Method->ClassName
	Map<Method, String> methodTrans = new HashMap<Method, String>();

	List<String> dispatchVectors = new LinkedList<String>();						// a list of class dispatch tables
	List<String> functions = new LinkedList<String>();								// a list of transl. of methods.

	String main = null;																// the translation of main method
	
	// this is for break and continue;
	int loopCounter = 0;
	
	// variable names management
	Map<Symbol, String> varMaps = new HashMap<Symbol, String>();
	int varMapsUidCounter = 0; 
	
	String getVarUniqID(ASTNode node){
		Symbol sym= null;
		
		if (node instanceof LocalVariable){
			LocalVariable v = (LocalVariable) node;
			sym = v.getSymbolTable().lookup(v.getName(), v);		
		}
		// more ifs of node typs
		
		
		if (sym.getKind()==SymbolKind.Variable){
			//check if was already given uniq id
			if(varMaps.containsKey(sym))
				return varMaps.get(sym);
			else {
				// create a uniq id
				varMapsUidCounter++;
				String str = "var"+varMapsUidCounter;
				varMaps.put(sym, str);
				return str;
			}
		}
		
		
		// WE DONT KNOW !!!!!!
		if (sym.getKind()==SymbolKind.Parameter){
			//check if was already given uniq id
			if(varMaps.containsKey(sym))
				return varMaps.get(sym);
			else {
				// create a uniq id
				varMapsUidCounter++;
				String str = "var"+varMapsUidCounter;
				varMaps.put(sym, str);
				return str;
			}
		}
		
		
		
	}
	
	
	
	
	/// manage the register count
	int targetReg = 0;
	
	String getCurReg(){
		return "R"+targetReg;
	}


	@Override
	public Object visit(Program program) {
		//init literalsMap
		initLiteralsMap();


		// run over the symbol table hierarchy and create the class layouts
		// this map is for ClassName -> ICClass
		Map<String, ICClass> classMap = new HashMap<String, ICClass>();
		for (ICClass cl : program.getClasses()){
			if (cl.getName().equals("Library")) continue;
			classMap.put(cl.getName(), cl);
		}


		// generate maps for methods and fields (field->offset, method->offset)
		for (ICClass icClass : program.getClasses()){
			if (icClass.getName().equals("Library")) continue;
			if (icClass.hasSuperClass()){
				ICClass superClass = classMap.get(icClass.getSuperClassName());
				icClass.generateClassLayout(superClass.getClassLayout());
			}
			else {
				icClass.generateClassLayout();
			}

			// keep track of which method belongs to which class
			addMethodsToMap(icClass);

			// build the dispatch vector for the class
			String DV = createDV(icClass);
			dispatchVectors.add(DV);
			//DEBUG
			debugPrint(DV + "\n");			
		}

		// translate Class declarations
		for (ICClass icClass : program.getClasses()){
			icClass.accept(this); // catch the returned string
		}
		
		if (DebugFlag) {
			for (String MethodTranslation: methodTrans.values()){
				debugPrint("\n"+MethodTranslation);
			}
				
		}
		

		return null;
	}

	@Override
	public Object visit(ICClass icClass) {

		for (Method method : icClass.getMethods()){
			method.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(Field field) {
		// done by the offset table
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) {

		// generate a comment for the function header
		StringBuffer comment = new StringBuffer();
		comment.append("# " + method.getName() + "(");
		
		int commaCount = method.getFormals().size();
		for (Formal formal : method.getFormals()){
			comment.append(formal.getType().getName() + " " + formal.getName());
			if (commaCount>1){
				comment.append(",");
				commaCount--;
			}
		}
		comment.append(")");

		String functionHeader = "_" + this.methodToClassName.get(method) + "_" + method.getName() + ":";
		functionHeader += "\t" + comment +"\n"; 
		for (Statement statement : method.getStatements()){
			functionHeader += statement.accept(this);
		}
		
		//.translate body

		methodTrans.put(method, functionHeader);
		return null;
	}

	@Override
	public Object visit(StaticMethod method) {
		// generate a comment for the function header
		StringBuffer comment = new StringBuffer();
		comment.append("# " + method.getName() + "(");
		
		int commaCount = method.getFormals().size();
		for (Formal formal : method.getFormals()){
			comment.append(formal.getType().getName() + " " + formal.getName());
			if (commaCount>1){
				comment.append(",");
				commaCount--;
			}
		}
		comment.append(")");

		String function = "_" + this.methodToClassName.get(method) + "_" + method.getName() + ":";
		function += "\t" + comment + "\n"; 
		for (Statement statement : method.getStatements()){
			function += statement.accept(this);
		}

		methodTrans.put(method, function);
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) {
		return null;
	}

	@Override
	public Object visit(Formal formal) {
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(UserType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Assignment assignment) {
		String comment = "# Assignment";
		
		String loc = (String)assignment.getAssignment().accept(this);
		targetReg++;
		String var = (String)assignment.getVariable().accept(this);
		if (assignment.getAssignment() instanceof ArrayLocation){
			
		}
		else {
			
		}
		
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement) {
		String trans = "";
		if (returnStatement.hasValue()){
			String value = (String) returnStatement.getValue().accept(this);
			trans = value+"Return " + getCurReg()+"\n";
			targetReg--;
		}else{
			trans = "Return 9999\n";
		}
		return trans;
	}

	@Override
	public Object visit(If ifStatement) {
		String totalTrans = null;
		String condtrans = (String) ifStatement.getCondition().accept(this);
		int uid = LiteralUtil.uniqID();
		totalTrans = condtrans +
					"Compare 0,"+ getCurReg() + "\n";
		if (ifStatement.hasElse())
			totalTrans += "JumpTrue _else_label"+uid + "\n";
		else
			totalTrans += "JumpTrue _end_label"+uid + "\n";
		
		
		String thenTrans = (String)  ifStatement.getOperation().accept(this);
	
		totalTrans += thenTrans;

		if (ifStatement.hasElse())
			totalTrans += "Jump _end_label"+uid+"\n";

		
		String elseTrans = "";
		
		if (ifStatement.hasElse()){
			elseTrans = "_else_label"+uid+":\n";
			elseTrans += (String)  ifStatement.getElseOperation().accept(this);
		}
		
		totalTrans += elseTrans +
						"_end_label"+uid+":\n";
		
		

		return totalTrans;
	}

	@Override
	public Object visit(While whileStatement) {
		int uid = loopCounter++;
		String trans = "_while_test_label" + uid + ":\n";
						    
		String condTrans = (String)whileStatement.getCondition().accept(this);
		
		trans += condTrans + 
				 "Compare 0,"+getCurReg()+"\n"+
				 "JumpTrue _while_end_label"+uid+"\n";
		
		String loopTrans = (String)whileStatement.getOperation().accept(this);
		
		trans += loopTrans +
				 "Jump _while_test_label"+ uid +"\n"+
				 "_while_end_label"+ uid +":\n";
		
		loopCounter--;

		return trans;
	}

	@Override
	public Object visit(Break breakStatement) {
		String trans = "Jump _while_end_label"+ loopCounter +"\n";
		return trans;
	}

	@Override
	public Object visit(Continue continueStatement) {
		String trans = "Jump _while_test_label"+ loopCounter +"\n";
		return trans;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) {
		String trans = "";
		for (Statement stmt:statementsBlock.getStatements()){
			trans += (String)stmt.accept(this);
		}
		return trans;
	}

	@Override
	public Object visit(LocalVariable localVariable) {
		String trans = "";
		if (localVariable.hasInitValue()){
			trans += (String) localVariable.getInitValue().accept(this);
			trans += "Move "+getCurReg()+","+localVariable.getName();
		}
		
		return null;
	}

	@Override
	public Object visit(VariableLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ArrayLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticCall call) {
		// Adi & riki
		String trans = null;

		// translate the arguments expressions and store them to registers
		int num = targetReg;
		for (Expression arg : call.getArguments()){
			targetReg++;
			trans += arg.accept(this);
		}

		// translate
		int methodNum = 0; //= classLayoutMap.get(call.getName()).
		trans += "StaticCall R" + targetReg + "." + methodNum + "(";
		targetReg = num;
		for (Expression arg : call.getArguments()){
			targetReg++;
			trans += arg.toString() +"=" + "R"+ targetReg;
		}

		trans += ")\n";


		return trans;
	}

	@Override
	public Object visit(VirtualCall call) {
		String trans = null;
		if (call.getLocation()==null)
			trans = "";
		else
			trans = (String) call.getLocation().accept(this);

		// translate the arguments expressions and store them to registers
		int num = targetReg;
		for (Expression arg : call.getArguments()){
			targetReg++;
			trans += arg.accept(this);
		}

		// translate the virtualCall
		int methodNum = 0; //= classLayoutMap.get(call.getName()).
		trans += "VirtualCall R" + targetReg + "." + methodNum + "(";
		targetReg = num;
		for (Expression arg : call.getArguments()){
			targetReg++;
			trans += arg.toString() +"=" + getCurReg();
		}

		trans += ")\n";


		return trans;
	}

	@Override
	public Object visit(This thisExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewClass newClass) {
		String trans = "# new Class()\n";
		int size = 0;
		//TODO size = classSizeMap.get(newClass.getName());
		trans += "Library __allocateObject(" + size + ") R" + targetReg;
		trans += "MoveField _DV_" + newClass.getName() + ", R" + targetReg + ".0\n";
		return trans;
	}

	@Override
	public Object visit(NewArray newArray) {
		targetReg++;
		String trans = "#new Array()\n"; 
		trans += (String) newArray.getSize().accept(this);  
		trans += "Add R" + targetReg + ", 1\n";
		trans += "Library __allocateArray(R" + targetReg + "), R" + (targetReg-1) + "\n";
		trans += "MoveArray R" + targetReg + " R" + (targetReg-1) + "[0]\n";
		return trans;
	}

	@Override
	public Object visit(Length length) {
		String trans = (String) length.getArray().accept(this);
		trans += "ArrayLength R" + targetReg + " R" + targetReg + "\n";
		return trans;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		String trans = null;

		//translate both expressions
		String e1Translation = (String) binaryOp.getFirstOperand().accept(this);
		targetReg++;
		String e2Translation = (String) binaryOp.getSecondOperand().accept(this);
		targetReg--;

		trans = e1Translation + e2Translation;

		BinaryOps operator = binaryOp.getOperator();	
		if (operator == BinaryOps.PLUS){
			if (true /*check if this is integers addition*/) {
				trans += "Library __strcat R" + targetReg + " R" + (targetReg+1) + "\n";
			}
			else { //this is an integers addition operation
				trans += "Add " + targetReg + " R" + (targetReg+1) + "\n";
			}

		}
		else if (operator == BinaryOps.MINUS){
			trans += "Sub" + targetReg + " R" + (targetReg+1) + "\n";	
		}
		else if (operator == BinaryOps.MULTIPLY){
			trans += "Mul " + targetReg + " R" + (targetReg+1) + "\n";	
		}
		else if (operator == BinaryOps.DIVIDE){
			trans += "Div " + targetReg + " R" + (targetReg+1) + "\n";
		}
		else if (operator == BinaryOps.MOD){
			trans += "Mod R" + targetReg + " R" + (targetReg+1) + "\n";
		}
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {

		UnaryOps operator = unaryOp.getOperator();
		if (operator == UnaryOps.UMINUS){
			String trans = (String) unaryOp.getOperand().accept(this);
			return (trans + "Sub 0 R"+targetReg + "\n");
		}
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		if (unaryOp.getOperator() == UnaryOps.LNEG){
			String trans = (String) unaryOp.accept(this);
			return (trans + "Not R"+targetReg + "\n");
		}
		return null;
	}

	@Override
	public Object visit(Literal literal) {
		//get the label for this literal
		String label = null;
		
		switch (literal.getType()){
			case STRING: {
				if (literalsMap.containsKey((String) literal.getValue()))
					label = literalsMap.get((String) literal.getValue());
				else{
					// new literal - create a label for it
					label = LiteralUtil.createLiteralLabel();
					literalsMap.put( (String) literal.getValue(), label);
				}
				break;
			}
			case FALSE:{
				label = "0";
				break;
			}
			case TRUE:{
				label = "1";
				break;
			}
			case NULL:{
				label = "0";
				break;
			}
			case INTEGER:{
				label = literal.getValue().toString();
				break;
			}
			default:{
				label = "<ERROR- bad literal>";
			}
		}
		String trans = "Move " + label;
		targetReg++;
		trans += ","+getCurReg()+"\n";

		
		return trans;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		//EMPTY
		return null;
	}


	public void initLiteralsMap(){
		this.literalsMap.put("false", "_false");
		this.literalsMap.put("true", "_true");
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// HELPER FUNCTIONS

	public void addMethodsToMap(ICClass icClass){
		String className = icClass.getName();
		for (Method method : icClass.getMethods()){
			methodToClassName.put(method, className);
		}
	}

	public String createDV(ICClass icClass){
		String classDV = "_DV_" + icClass.getName() + ": ";
		classDV = classDV + "[";
		for (Method method : icClass.getClassLayout().methodToOffset.keySet()){
			classDV += "_" + methodToClassName.get(method) + "_" + method.getName() + ",";
		}
		classDV = classDV.substring(0, classDV.length()-1);
		classDV += "]";

		return classDV;
	}
}
