package Lir;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.wsdl.writer.document.ParamType;

import IC.BinaryOps;
import IC.DataTypes;
import IC.LiteralTypes;
import IC.SemanticError;
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
import TypeTable.ArrayType;
import TypeTable.ClassType;
import TypeTable.MethodType;
import TypeTable.StringType;
import TypeTable.Type;
import Visitors.SemanticsChecks;

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
	int loopid = 0;

	// variable names management
	Map<Symbol, String> varMaps = new HashMap<Symbol, String>();
	int varMapsUidCounter = 0; 

	String getVarUniqID(ASTNode node){
		Symbol sym= null;

		if (node instanceof LocalVariable){
			LocalVariable v = (LocalVariable) node;
			sym = v.getSymbolTable().lookup(v.getName(), v);		
		}
		else if (node instanceof VariableLocation) {
			VariableLocation v = (VariableLocation) node;
			sym = v.getSymbolTable().lookup(v.getName(), v);
		}

		//add more ifs


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



		return "error getting unique id of node in lien " + node.getLine();				// this 
	}




	/// manage the register count
	int targetReg = 0;
	private ICClass libraryNode;

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
			if (cl.getName().equals("Library")){
				libraryNode = cl;
				continue;
			}
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

			// add <Name classLayout> to classLayoutMap
			classLayoutMap.put(icClass.getName(), icClass.getClassLayout());

			// keep track of which method belongs to which class
			addMethodsToMap(icClass);

			// build the dispatch vector for the class
			String DV = createDV(icClass);
			dispatchVectors.add(DV);			
		}

		// translate Class declarations
		for (ICClass icClass : program.getClasses()){
			icClass.accept(this); // catch the returned string
		}

		//		if (DebugFlag) {
		//			for (String MethodTranslation: methodTrans.values()){
		//				debugPrint("\n"+MethodTranslation);
		//			}
		//
		//		}

		// put together the whole translation
		String finalTrans = "";

		//LITERALS
		String literalTrans = "";
		for (String lit : literalsMap.keySet()){
			literalTrans += literalsMap.get(lit) + ": " + lit + "\n";
		}

		//DVs
		String DVTrans = "";
		for (String dv : dispatchVectors){
			DVTrans += dv + "\n";
		}

		finalTrans = literalTrans + "\n" + DVTrans + "\n";

		// add the methods translation
		for (String MethodTranslation: methodTrans.values()){
			finalTrans += MethodTranslation + "\n";
		}

		finalTrans += main;

		return finalTrans;
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
		String function = "";
		if(!method.getName().equals("main"))
			function = "_" + this.methodToClassName.get(method) + "_" + method.getName() + ":";
		else
			function += "_ic_main: \n";

		function += comment + "\n"; 
		for (Statement statement : method.getStatements()){
			function += statement.accept(this);
		}

		if (method.getName().equals("main")){
			function += "Library __exit(0), Rdummy\n";
			main = function;
		}
		else {
			methodTrans.put(method, function);
		}
		return function;
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
		return null;
	}

	@Override
	public Object visit(UserType type) {
		return null;
	}

	@Override
	public Object visit(Assignment assignment) {

		String trans="";
		String valStr = (String)assignment.getAssignment().accept(this);
		int valregister = targetReg;
		targetReg++; // meaning this ^ gets a register;
		trans += valStr;
		//trans += "Move " +getCurReg()+","

		//String varStr = (String)assignment.getVariable().accept(this);
		//trans += varStr;

		if (assignment.getVariable() instanceof VariableLocation){
			// ID | expr.ID
			VariableLocation location = (VariableLocation)assignment.getVariable();

			if (location.isExternal()){
				if(!(location.getLocation() instanceof This)){

					String locationStr = (String)location.getLocation().accept(this);
					trans += locationStr;

					if (location.getLocation() instanceof VariableLocation){
						VariableLocation expr = (VariableLocation) location.getLocation();
						
						Symbol exprSym = expr.getSymbolTable().lookup(expr.getName(), expr);
						if (!(exprSym.getType() instanceof ClassType)){
							throw new SemanticError("Not a class type field",expr);
						}
						ClassType type = (ClassType) exprSym.getType();
						
						
						ClassLayout cl = classLayoutMap.get(type.toString());
						trans += "MoveField R"+valregister+","    +getCurReg()+".";

						Field fieldNode = cl.getFieldNameToNode().get(location.getName());
						int offset = cl.getFieldOffMap().get(fieldNode);

						trans+= offset +"\n";
					}
					if (location.getLocation() instanceof ArrayLocation){
						ArrayLocation expr = (ArrayLocation) location.getLocation();

						ArrayType arrtype = (ArrayType) expr.TTtype;
						ClassType clstype = (ClassType)arrtype.getObjType();
						ICClass cls = (ICClass) clstype.node;

						ClassLayout cl = cls.getClassLayout();

						trans += "MoveField R"+valregister+","+getCurReg()+".";

						Field fieldNode = cl.getFieldNameToNode().get(location.getName());
						int offset = cl.getFieldOffMap().get(fieldNode);

						trans+= offset + "\n";
					}
				}
			}
			if (!location.isExternal()  ||  (location.isExternal() && (location.getLocation() instanceof This) )   ){ 
				//simple form - ID or this.ID
				Symbol sym = location.getSymbolTable().lookup(location.getName(), location);
				if (sym.getKind() == Symbol.SymbolKind.Field){
					String classname = sym.getNode().getSymbolTable().getId();
					ClassLayout cl = classLayoutMap.get(classname);
					Field field = cl.fieldNameToNode.get(location.getName());
					int offset = cl.fieldToOffset.get(field);
					trans += "Move this," +getCurReg()+"\n";
					trans += "MoveField R"+valregister+","+getCurReg()+"."+offset + "\n";
				} else {
					///not a field - just a var
					trans += "Move R"+valregister+","+ getVarUniqID(location) + "\n";
				}

			}
		}


		//or - array
		if (assignment.getVariable() instanceof ArrayLocation){
			ArrayLocation location = (ArrayLocation)assignment.getVariable();

			String indexStr = (String)location.getIndex().accept(this);
			trans += indexStr;
		//	trans += "Add 1,"+getCurReg()+"			#add 1 to index of arr\n";
			targetReg++; // ^this stores i in A[i]

			String arrStr = (String)location.getArray().accept(this);
			trans += arrStr;

			trans += "MoveArray R"+valregister+","+getCurReg();
			targetReg--;
			trans += "["+getCurReg()+"]"+"\n";
		}



		targetReg--;
		return trans;
	}

	@Override
	public Object visit(CallStatement callStatement) {		
		return (String) callStatement.getCall().accept(this);
	}

	@Override
	public Object visit(Return returnStatement) {
		String trans = "";
		if (returnStatement.hasValue()){
			String value = (String) returnStatement.getValue().accept(this);
			trans = value+"Return " + getCurReg()+"\n";
		}else{
			trans = "Return 9999\n";
		}
		return trans;
	}

	@Override
	public Object visit(If ifStatement) {
		String totalTrans = "\n# if statement\n";
		String condtrans = (String) ifStatement.getCondition().accept(this);
		int uid = LiteralUtil.uniqID();
		totalTrans += condtrans +
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
		int uid = ++loopCounter;														//changed loopcounter++ to ++loopcounter 
		if (loopCounter == 1){
			loopid++;
		}
		String trans = "\n# while statement\n";
		trans += "_while_test_label_" + loopid + "_" + uid + ":\n";

		int startReg = targetReg;
		String condTrans = (String)whileStatement.getCondition().accept(this);

		trans += condTrans + 
				"Compare 0,"+getCurReg()+"\n"+
				"JumpTrue _while_end_label_" + loopid + "_" + uid+"\n";

		targetReg++;
		String loopTrans = (String)whileStatement.getOperation().accept(this);

		trans += loopTrans +
				"Jump _while_test_label_" + loopid + "_" + uid +"\n"+
				"_while_end_label_" + loopid + "_" + uid +":\n";

		--loopCounter;
		targetReg = startReg;
		return trans;
	}

	@Override
	public Object visit(Break breakStatement) {
		String trans = "Jump _while_end_label_" + loopid + "_" + loopCounter +"\n";
		return trans;
	}

	@Override
	public Object visit(Continue continueStatement) {
		String trans = "Jump _while_test_label_" + loopid + "_"+ loopCounter +"\n";
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
			trans += "Move " + getCurReg() + "," + getVarUniqID(localVariable) + "\n";
		}
		else {
			getVarUniqID(localVariable);
		}

		return trans;
	}

	@Override
	public Object visit(VariableLocation location) {
		String trans = "";
		if (location.isExternal()){
			if(!(location.getLocation() instanceof This)){

				String locationStr = (String)location.getLocation().accept(this);
				trans += locationStr;
				if (location.getLocation() instanceof VariableLocation){
					VariableLocation expr = (VariableLocation) location.getLocation(); 
					Symbol exprSym = expr.getSymbolTable().lookup(expr.getName(), expr);
					if (!(exprSym.getType() instanceof ClassType)){
						throw new SemanticError("Not a class type field",expr);
					}
					ClassType type = (ClassType) exprSym.getType();
					
					
					ClassLayout cl = classLayoutMap.get(type.toString());
					trans += "MoveField "+getCurReg()+".";

					Field fieldNode = cl.getFieldNameToNode().get(location.getName());
					int offset = cl.getFieldOffMap().get(fieldNode);

					trans+= offset +"," + getCurReg() + "\n";
				}
				if (location.getLocation() instanceof ArrayLocation){
					ArrayLocation expr = (ArrayLocation) location.getLocation();

					//	SemanticsChecks semanticChecker = new SemanticsChecks(); //needed to find type
					//	Type type = (Type)expr.getArray().accept(semanticChecker);
					ArrayType arrtype = (ArrayType) expr.TTtype;
					ClassType clstype = (ClassType)arrtype.getObjType();
					ICClass cls = (ICClass) clstype.node;


					ClassLayout cl = cls.getClassLayout();

					trans += "MoveField "+getCurReg()+".";

					Field fieldNode = cl.getFieldNameToNode().get(location.getName());
					int offset = cl.getFieldOffMap().get(fieldNode);

					trans+= offset +"," + getCurReg() + "\n";
				}
			}				
		} if (!location.isExternal()  ||  (location.isExternal() && (location.getLocation() instanceof This) )   ){
			//simple form - ID or this.ID
			Symbol sym = location.getSymbolTable().lookup(location.getName(), location);
			if (sym.getKind() == Symbol.SymbolKind.Field){
				String classname = sym.getNode().getSymbolTable().getId();
				ClassLayout cl = classLayoutMap.get(classname);
				Field field = cl.fieldNameToNode.get(location.getName());
				int offset = cl.fieldToOffset.get(field);
				trans += "Move this," +getCurReg()+"\n";
				trans += "MoveField "+getCurReg()+"."+offset +"," + getCurReg() + "\n";
			} else {
				///not a field - just a var
				trans += "Move "+ getVarUniqID(location) +"," + getCurReg() + "\n";
			}

		}
		return trans;
	}

	@Override
	//this saves 2 registers !!!
	public Object visit(ArrayLocation location) {
		String trans = "";
		String indexStr = (String)location.getIndex().accept(this);
		trans += indexStr;
	//	trans += "Add 1,"+getCurReg()+"			#add 1 to index of arr\n";
		
		targetReg++; // ^this stores i in A[i]

		String arrStr = (String)location.getArray().accept(this);
		trans += arrStr;
		
		trans += "MoveArray "+getCurReg();
		targetReg--;
		trans += "["+getCurReg()+"],"+getCurReg()+"\n";

		return trans;
	}

	@Override
	public Object visit(StaticCall call) {
		// Adi & riki


		String trans = "# static call to " + call.getClassName() + "." + call.getName() + "()\n";
		// FORMAT: func-name({Memory=param}*
		String paramsTrans = null;
		if (!call.getClassName().equals("Library")){
			ClassLayout cl = classLayoutMap.get(call.getClassName());
			Symbol sym = call.getSymbolTable().lookup(call.getName(), call);
			ASTNode node = sym.getNode();
			if (!(node instanceof Method)){
				return "error finding the call's formals " + call.getName();

			}

			Method method = (Method) node;

			// translate the arguments expressions and store them to registers
			int num = targetReg;

			// run over the formal and actual params. and parse them into the FORMAT {Memory=param}*
			paramsTrans = "";
			Iterator<Expression> actuals = call.getArguments().iterator();
			Iterator<Formal> formals = method.getFormals().iterator();
			Formal formal = null;
			Expression actual = null;
			int counter = call.getArguments().size();
			for (; (formals.hasNext() && actuals.hasNext()) ;){
				actual = actuals.next();
				formal = formals.next();

				trans += (String) actual.accept(this);
				paramsTrans += formal.getName() + "=" + getCurReg();
				if (counter > 1){
					paramsTrans += ",";
				}
				counter--;
				targetReg++;			
			}
			targetReg = num;

			trans += "StaticCall " + "_" + this.methodToClassName.get(method) + "_" + method.getName();

			//this will add a Rdummy if nothing returns
			Symbol typesym = call.getSymbolTable().lookup(call.getName(), call);
			if (typesym.getType() instanceof TypeTable.VoidType)
				trans += "(" + paramsTrans + "),Rdummy\n";	
			else
				trans += "(" + paramsTrans + ")," + getCurReg() + "\n";			// method name translation


		}
		else {
			// translate the arguments expressions and store them to registers
			int num = targetReg;

			// run over the formal and actual params. and parse them into the FORMAT {Memory=param}*
			paramsTrans = "";
			Iterator<Expression> actuals = call.getArguments().iterator();
			Expression actual = null;
			int counter = call.getArguments().size();
			for (; actuals.hasNext() ;){
				actual = actuals.next();

				trans += (String) actual.accept(this);
				paramsTrans += getCurReg();
				if (counter > 1){
					paramsTrans += ",";
				}
				counter--;
				targetReg++;			
			}
			targetReg = num;

			trans += "Library " + "__" + call.getName();

			Symbol sym = libraryNode.getSymbolTable().lookup(call.getName(), libraryNode);
			if (((MethodType)sym.getType()).getRetType() instanceof TypeTable.VoidType)
				trans += "(" + paramsTrans + "),Rdummy\n";	
			else
				trans += "(" + paramsTrans + ")," + getCurReg() + "\n";			// method name translation

		}


		return trans;
	}

	@Override
	public Object visit(VirtualCall call) {
		String trans = "# virtual call to ." + call.getName() + "()\n";

		if (call.isExternal()){
			trans += (String) call.getLocation().accept(this);			
		}
		else {
			trans += "Move this, " + getCurReg() + "\n";
		}

		ClassLayout cl = classLayoutMap.get(call.className);
		Method method = cl.getmethodNameToNode().get(call.getName());
		int offset = cl.getMethodOffMap().get(method);
		//		trans += "MoveField " + getCurReg() + ".0 ," + getCurReg() + "\n";

		int num = targetReg;
		String paramsTrans = "";
		Iterator<Expression> actuals = call.getArguments().iterator();
		Iterator<Formal> formals = method.getFormals().iterator();
		Formal formal = null;
		Expression actual = null;
		for (; (formals.hasNext() && actuals.hasNext()) ;){
			targetReg++;
			actual = actuals.next();
			formal = formals.next();

			trans += (String) actual.accept(this);
			paramsTrans += formal.getName() + "=" + getCurReg() + ",";

		}
		targetReg = num;
		
//		trans += "Move " + getCurReg() + ",this\n";
		
		trans += "VirtualCall " + getCurReg() + "." + offset + "(" ;
		trans += paramsTrans + "),";
		if ((method.getType() instanceof PrimitiveType) && (method.getType().getName().equals("void"))){
			trans += "Rdummy\n";					
		}
		else {
			trans += getCurReg() + "\n";
		}

		return trans;
	}

	@Override
	public Object visit(This thisExpression) {

		return "Move this,"+getCurReg()+"\n";
	}

	@Override
	public Object visit(NewClass newClass) {
		String trans = "# new Class()" + newClass.getName() + "\n";
		ClassLayout cl = classLayoutMap.get(newClass.getName());
		int size = (cl.getFieldOffMap().size() + 1)*4;
		trans += "Library __allocateObject(" + size + ")," + getCurReg() + "\n";
		trans += "MoveField _DV_" + newClass.getName() + ",R" + targetReg + ".0\n";
		return trans;
	}

	@Override
	public Object visit(NewArray newArray) {
		//targetReg++; // save a Reg for the array itself - use the next Reg for size
		String trans = "#new Array()\n"; 
		trans += (String) newArray.getSize().accept(this); 
		targetReg++;
		
		trans += "Move R" + (targetReg-1)+ ","+ getCurReg() + "\n";
		targetReg++;
		trans += "Move R" + (targetReg-1)+ ","+ getCurReg() + "\n";
		
	//	trans += "Add 1,R" + targetReg + "\n";
		trans += "Mul 4,"+ getCurReg() + "\n";
		trans += "Library __allocateArray(R" + targetReg + "),R" + (targetReg-2) + "\n";
		targetReg--; // done with size+1*4		
		trans += "MoveArray R" + targetReg + ",R" + (targetReg-1) + "[0]\n";

		targetReg--; // done with size
		return trans;
	}

	@Override
	public Object visit(Length length) {
		String trans = (String) length.getArray().accept(this);
		trans += "ArrayLength R" + targetReg + ",R" + targetReg + "\n";
		return trans;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		String trans = "";

		//translate both expressions
		String e1Translation = (String) binaryOp.getFirstOperand().accept(this);
		trans+= e1Translation;
		
		targetReg++;
		String e2Translation = (String) binaryOp.getSecondOperand().accept(this);
		trans+= e2Translation;
		

		BinaryOps operator = binaryOp.getOperator();	
		if (operator == BinaryOps.PLUS){

			Type firstOp = (Type) binaryOp.getFirstOperand().accept(new SemanticsChecks());

			if (firstOp instanceof StringType) {
				trans += "Library __stringCat(R"+ (targetReg-1) + ",R" + targetReg + "),R" +(targetReg-1) + "\n";
			}
			else { //this is an integers addition operation
				trans += "Add R"+ targetReg + ",R" + (targetReg-1) + "\n";	
			}

		}
		else if (operator == BinaryOps.MINUS){
			trans += "Sub R"+ targetReg + ",R" + (targetReg-1) + "\n";	
		}
		else if (operator == BinaryOps.MULTIPLY){
			trans += "Mul R"+ targetReg + ",R" + (targetReg-1) + "\n";	
		}
		else if (operator == BinaryOps.DIVIDE){
			trans += "Div R"+ targetReg + ",R" + (targetReg-1) + "\n";	
		}
		else if (operator == BinaryOps.MOD){
			trans += "Mod R"+ targetReg + ",R" + (targetReg-1) + "\n";	
		}
		targetReg--;
		return trans;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		String trans = "";
		

		if ((binaryOp.getOperator() == BinaryOps.LAND) || (binaryOp.getOperator() == BinaryOps.LOR)){
			String firstOpTrans = (String) binaryOp.getFirstOperand().accept(this);

			targetReg++;
			String secondOpTrans = (String) binaryOp.getSecondOperand().accept(this);
			targetReg--;
			int uid = LiteralUtil.uniqID();
			
			switch(binaryOp.getOperator()){
			case LAND:
				trans += "# e1 AND e2 \n";
				trans += firstOpTrans;
				trans += "Compare 0,R" + targetReg + "\n";
				trans += "JumpTrue _end_label" + uid + "\n";
				trans += secondOpTrans;
				trans += "And R" + (targetReg+1) + ",R" + targetReg + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			case LOR:
				trans += "# e1 OR e2 \n";
				trans += firstOpTrans;
				trans += "Compare 1,R" + targetReg + "\n";
				trans += "JumpTrue _end_label" + uid + "\n";
				trans += secondOpTrans;
				trans += "Or R" + (targetReg+1) + ",R" + targetReg + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			}
		}
		else {
			String secondOpTrans = (String) binaryOp.getSecondOperand().accept(this);
			targetReg++;
			String firstOpTrans = (String) binaryOp.getFirstOperand().accept(this);
			
			trans += firstOpTrans + secondOpTrans;
			trans += "Compare R" + targetReg + ",R" + (targetReg-1) + "\n";
			int uid = LiteralUtil.uniqID();
			targetReg--;

			switch (binaryOp.getOperator()){
			case EQUAL:
				trans += "# e1 == e2 \n";
				trans += "JumpTrue _true_label" + uid + "\n";
				trans += "Jump _false_label" + uid + "\n";
				trans += "_true_label" + uid + ":\n";
				trans += "Move 1," + getCurReg() + "\n";
				trans += "Jump _end_label" + uid + "\n";
				trans += "_false_label" + uid + ":\n";
				trans += "Move 0," + getCurReg() + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			case GT:
				trans += "# e1 > e2 \n";
				trans += "JumpL _true_label" + uid + "\n";
				trans += "Jump _false_label" + uid + "\n";
				trans += "_true_label" + uid + ":\n";
				trans += "Move 1," + getCurReg() + "\n";
				trans += "Jump _end_label" + uid + "\n";
				trans += "_false_label" + uid + ":\n";
				trans += "Move 0," + getCurReg() + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			case GTE:
				trans += "# e1 >= e2 \n";
				trans += "JumpLE _true_label" + uid + "\n";
				trans += "Jump _false_label" + uid + "\n";
				trans += "_true_label" + uid + ":\n";
				trans += "Move 1," + getCurReg() + "\n";
				trans += "Jump _end_label" + uid + "\n";
				trans += "_false_label" + uid + ":\n";
				trans += "Move 0," + getCurReg() + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			case LT:
				trans += "# e1 < e2 \n";
				trans += "JumpG _true_label" + uid + "\n";
				trans += "Jump _false_label" + uid + "\n";
				trans += "_true_label" + uid + ":\n";
				trans += "Move 1," + getCurReg() + "\n";
				trans += "Jump _end_label" + uid + "\n";
				trans += "_false_label" + uid + ":\n";
				trans += "Move 0," + getCurReg() + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			case LTE:
				trans += "# e1 <= e2 \n";
				trans += "JumpGE _true_label" + uid + "\n";
				trans += "Jump _false_label" + uid + "\n";
				trans += "_true_label" + uid + ":\n";
				trans += "Move 1," + getCurReg() + "\n";
				trans += "Jump _end_label" + uid + "\n";
				trans += "_false_label" + uid + ":\n";
				trans += "Move 0," + getCurReg() + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			case NEQUAL:
				trans += "# e1 != e2 \n";
				trans += "JumpTrue _true_label" + uid + "\n";
				trans += "Jump _false_label" + uid + "\n";
				trans += "_true_label" + uid + ":\n";
				trans += "Move 0," + getCurReg() + "\n";
				trans += "Jump _end_label" + uid + "\n";
				trans += "_false_label" + uid + ":\n";
				trans += "Move 1," + getCurReg() + "\n";
				trans += "_end_label" + uid + ":\n";
				break;
			}
		}

		return trans;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {

		UnaryOps operator = unaryOp.getOperator();
		if (operator == UnaryOps.UMINUS){
			String trans = (String) unaryOp.getOperand().accept(this);
			return (trans + "Neg "+getCurReg() + "\n");
		}
		return "UNARY OP ERROR\n";
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		if (unaryOp.getOperator() == UnaryOps.LNEG){
			String trans = (String) unaryOp.getOperand().accept(this);
			return (trans + "Not "+getCurReg() + "\n");
		}
		return "UNARY OP ERROR\n";
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
		//		targetReg++;
		trans += ","+getCurReg()+"\n";


		return trans;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		//EMPTY
		return null;
	}


	public void initLiteralsMap(){
		//		this.literalsMap.put("false", "false");
		//		this.literalsMap.put("true", "true");
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
		Map<Method, Integer> mToOff = icClass.getClassLayout().methodToOffset;
		Map<Integer, Method> offTom = icClass.getClassLayout().offsetToMethod;
		int counter = mToOff.keySet().size();
		
		
		for (int i = 0; i < counter; i++){
			Method method = offTom.get(i);
			if (method.getName().equals("main")){
				classDV += "_ic_main";
			}
			else {
				classDV += "_" + methodToClassName.get(method) + "_" + method.getName();
			}
			if (i < counter-1){
				classDV +=",";	
			}			
		}

		classDV += "]";

		return classDV;
	}
}
