package Lir;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import IC.BinaryOps;
import IC.LiteralTypes;
import IC.UnaryOps;
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
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
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

public class TranslationVisitor implements Visitor {

	Map<String, String> literalsMap = new HashMap<String,String>();				// a list of literal labels
	Map<String, ClassLayout> classLayoutMap = new HashMap<String, ClassLayout>();	// a map from class name to classLayout
	List<String> dispatchvectors = new LinkedList<String>();			// a list of class dispatch tables
	List<String> functions = new LinkedList<String>();				// a list of transl. of methods.
	String main = null;												// the translation of main method
	int targetReg = 0;

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
			dispatchvectors.add("_DV_" + icClass.getName());
		}

		// translate Class declarations
//		for (ICClass icClass : program.getClasses()){
//			icClass.accept(this); // catch the returned string
//		}

		return null;
	}

	@Override
	public Object visit(ICClass icClass) {

		return null;
	}

	@Override
	public Object visit(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Formal formal) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(If ifStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualCall call) {
		String trans = null;
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
			trans += arg.toString() +"=" + "R"+ targetReg;
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
		if (literal.getType() == LiteralTypes.STRING){
			label = literalsMap.get((String) literal.getValue());

			// new literal - create a label for it
			if (label == null){
				label = LiteralUtil.createLiteralLabel();
				literalsMap.put( (String) literal.getValue(), label);
			}
			return label;
		}

		//return null on any other kind of literal
		return null;
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

}
