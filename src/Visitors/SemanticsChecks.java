package Visitors;

import java.util.List;

import IC.BinaryOps;

import SymbolTable.SymbolTable;
import SymbolTable.Symbol;
import SymbolTable.SymbolMethod;
import SymbolTable.SymbolMethod.MethodKind;
import TypeTable.ArrayType;
//import TypeTable.BooleanType;
import TypeTable.ClassType;
import TypeTable.IntType;
import TypeTable.MethodType;
//import TypeTable.NullType;
//import TypeTable.StringType;
//import TypeTable.Table;
//import TypeTable.VoidType;
import TypeTable.Type;
import IC.SemanticError;
import IC.UnaryOps;
import IC.AST.*;

public class SemanticsChecks implements Visitor {

	int loopsNum = 0;
	boolean is_static = false;

	@Override
	public Object visit(Program program){
		for (ICClass Class : program.getClasses())
			Class.accept(this);

		return null;
	}

	@Override
	public Object visit(ICClass icClass) {
		for (Method method : icClass.getMethods())
			method.accept(this);

		return null;
	}

	@Override
	public Object visit(StaticMethod method) {

		is_static = true;  
		for (Statement stmt: method.getStatements())
			stmt.accept(this);
		is_static = false;

		return null;
	}

	@Override
	public Object visit(VirtualMethod method) {
		List<Statement> stmts = method.getStatements();
		for (int i = 0; i < stmts.size(); i++)
			stmts.get(i).accept(this);

		return null;
	}

	@Override
	public Object visit(LibraryMethod method) {
		for (Statement statement: method.getStatements())
			statement.accept(this);

		return null;
	}
	
	@Override
	public Object visit(Field field) {
		return null;
	}
	
	@Override
	public Object visit(Formal formal) {
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) 
	{
		Type t = TypeTable.Table.getType(type);

		return t;
	}

	@Override
	public Object visit(UserType type) 
	{
		Type t = TypeTable.Table.getType(type);

		return t;
	}

	//type matching in assignments
	public Object visit(Assignment assgin) 
	{
		Type locType = (Type) assgin.getAssignment().accept(this);
		Type varType = (Type) assgin.getVariable().accept(this);
		if (varType == null)
			throw new SemanticError("Variable type is undefined in the assignment", assgin);
		
		if (!varType.SubType(locType))
			throw new SemanticError("Type mismatch in assignment", assgin);
		return null;
	}

	@Override
	public Object visit(CallStatement call) 
	{
		return call.getCall().accept(this);
	}

	public Object visit(Return retStmt) 
	{
		Expression exp = retStmt.getValue();

		Type retVal;
		if (exp != null){
			retVal = (Type)retStmt.getValue().accept(this);
		}
		else {
			retVal = TypeTable.Table.voidType;
		}
		Type formalRetType = retStmt.getSymbolTable().lookup("-return-type", retStmt).getType();

		if (!retVal.SubType(formalRetType)) {
			throw new SemanticError("Return type mismatch " + formalRetType, retStmt);
		}

		return null;
	}

	public Object visit(If ifStmt) {
		Type condType = (Type) ifStmt.getCondition().accept(this);

		if (condType.SubType(TypeTable.Table.booleanType)){
			ifStmt.getOperation().accept(this);

			if (ifStmt.hasElse())
				ifStmt.getElseOperation().accept(this);
		}
		else
			throw new SemanticError("The if condition is not a boolean type", ifStmt);

		return null;
	}

	public Object visit(While whileStmt){
		Type condType = (Type) whileStmt.getCondition().accept(this); 

		if (!condType.SubType(TypeTable.Table.booleanType)) {
			throw new SemanticError("The while condition is not a boolean type", whileStmt);
		}

		loopsNum++;
		whileStmt.getOperation().accept(this);
		loopsNum--;

		return null;
	}

	@Override
	public Object visit(Break breakStatement) {       

		if (loopsNum <= 0)
			throw new SemanticError("Break statement outside of loop", breakStatement);

		return null;
	}

	@Override
	public Object visit(Continue continueStmt) {
		if (loopsNum <= 0)
			throw new SemanticError("Continue statement outside of loop", continueStmt);

		return null;
	}

	@Override
	public Object visit(StatementsBlock stmtBlock) 
	{
		for (Statement stmt : stmtBlock.getStatements())
			stmt.accept(this);

		return null;
	}

	@Override
	public Object visit(LocalVariable localVar) {
		
		//check if this var type was declared (class could show up later - cannot check this in symbolTableBuilder)
		if (localVar.getType() instanceof IC.AST.UserType){
			SymbolTable locTable = localVar.getSymbolTable();
			Symbol locType = locTable.lookupScope(localVar.getName());
			if (locType.getType() == null)
				throw new SemanticError("Variable type is undefined", localVar);
		}
		
		if (localVar.hasInitValue()) {
			Type initVal = (Type)localVar.getInitValue().accept(this);
			Symbol sym = localVar.getSymbolTable().lookup(localVar.getName(), localVar);
			if (!initVal.SubType(sym.getType()))
				throw new SemanticError("Illegal initialization", localVar);
		}
		
		

		return null;
	}

	public Object visit(VariableLocation loc) {
		if (loc.getLocation() != null){   
			//A.x type of stmt
			Type locationType = (Type) loc.getLocation().accept(this);
			
			if (   !(locationType instanceof ClassType)  ) 
				throw new SemanticError( ((VariableLocation) loc.getLocation()).getName() + " is not defined as a Class", loc);
			
			String typeName = (  (ICClass)     (   (ClassType)locationType   ).node).getName();
			loc.setTypeName(typeName);
			Symbol varSym = ((ClassType) locationType).getSymbolTable().lookup(loc.getName(), loc);

			if (varSym == null)
				throw new SemanticError("Field " +loc.getName()+ " does not exist", loc);

			return varSym.getType(); 
		}
		else {
			Symbol varSym = loc.getSymbolTable().lookup(loc.getName(), loc);

			if (varSym == null)
				throw new SemanticError("Variable is not defined", loc);

			return varSym.getType();
		}
	}

	@Override
	public Object visit(ArrayLocation location) 
	{
		Type indexType = (Type)location.getIndex().accept(this);

		if (!(indexType instanceof IntType))
			throw new SemanticError("Illegal array index type", location);

		Type type = (Type)location.getArray().accept(this);
		
		location.TTtype = type; //save the type to read it later by offset table

		if (!(type instanceof ArrayType))
			throw new SemanticError("Can't access "+ type.toString() +" as an array", location);

		return ((ArrayType) type).getObjType();
	}

	@Override
	public Object visit(StaticCall call) 
	{
		SymbolTable symbolTable = null;
		ClassType classType = TypeTable.Table.getClassType(call.getClassName(), call);
		symbolTable = classType.getSymbolTable();
		SymbolMethod symbol = (SymbolMethod)symbolTable.lookup(call.getName(), call);
		
		if (symbol==null)
			throw new SemanticError("Method was not defined in this class", call);

		if (symbol.getMethodKind() != MethodKind.Static && symbol.getMethodKind() != MethodKind.Library)
			throw new SemanticError("Method isn't static", call);

		if (symbol.getMethodKind() == MethodKind.Static)
			call.staticMethod = (StaticMethod) symbol.getNode();

		MethodType methodType = (MethodType)symbol.getType();
		List<Expression> args = call.getArguments();
		List<Type> argTypes = methodType.getArgTypes();

		if (argTypes.size() != args.size())
			throw new SemanticError("Illegal number of arguments", call);

		for (int i = 0; i < args.size(); i++)
		{
			Type argType = (Type)args.get(i).accept(this);

			if (!argType.SubType(argTypes.get(i)))
				throw new SemanticError("Argument number " + (i+1) + " in method call is illegal", call);
		}

		return methodType.getRetType();
	}

	@Override
	public Object visit(VirtualCall call) 
	{
		SymbolTable symbolTable = null;
		Expression locationExpr = call.getLocation();

		if (locationExpr != null)
		{
			Type type = (Type)locationExpr.accept(this);

			if (!(type instanceof ClassType))
				throw new SemanticError("Method call undefined in this expression", call);

			Symbol locSymbol = ((ClassType)type).getSymbolTable().lookup(call.getName(), call);
			
			if (locSymbol==null)
				throw new SemanticError("Method call undefined in this expression", call);
				
			symbolTable = locSymbol.getNode().getSymbolTable();
		}
		else
		{
			Symbol locSymbol = call.getSymbolTable().lookup(call.getName(), call);
			
			if (locSymbol==null)
				throw new SemanticError("Method call undefined in this expression", call);
			symbolTable = locSymbol.getNode().getSymbolTable();
		}

		call.className = symbolTable.getClassSymbolTable().getId();
		call.virtualMethod = (VirtualMethod) symbolTable.lookup(call.getName(), call).getNode();
		SymbolMethod symbol = (SymbolMethod)symbolTable.lookup(call.getName(), call);

		if (symbol.getMethodKind() != MethodKind.Virtual)
			throw new SemanticError("Method isn't virtual", call);

		MethodType methodType = (MethodType)symbol.getType();
		List<Expression> args = call.getArguments();
		List<Type> argTypes = methodType.getArgTypes();

		if (argTypes.size() != args.size())
			throw new SemanticError("Illegal number of arguments", call);

		for (int i = 0; i < args.size(); i++){
			Type argType = (Type)args.get(i).accept(this);
			if (!argType.SubType(argTypes.get(i)))
				throw new SemanticError("Argument number " + (i+1) + " in method call is illegal", call);
		}

		return methodType.getRetType();
	}

	@Override
	public Object visit(This exp) 
	{
		if (is_static)
			throw new SemanticError("Can not use 'this' in static method", exp);

		SymbolTable classSymbolTable = exp.getSymbolTable().getClassSymbolTable();

		if (classSymbolTable == null)
			throw new SemanticError("Can't find containing class scope", exp);

		Type type = TypeTable.Table.getClassType(classSymbolTable.getId(), exp);

		return type;
	}

	@Override
	public Object visit(NewClass newClass) {
		Type type = TypeTable.Table.getClassType(newClass.getName(), newClass);

		return type;
	}

	@Override
	public Object visit(NewArray newArray) 
	{
		Type baseType = TypeTable.Table.getType(newArray.getType());
		Type type = TypeTable.Table.arrayType(baseType);
		
		Type sizeType = (Type) newArray.getSize().accept(this); 
		if (sizeType == null)
			throw new SemanticError("Variable type is undefined", newArray);
		IntType obj = new IntType();
		
		boolean bool = sizeType.toString() == "int";
		if (!bool)
			throw new SemanticError("Type mismatch in new Array size", newArray);
		

		return type;
	}

	@Override
	public Object visit(Length length) 
	{
		Type type = (Type) length.getArray().accept(this);
		if (!(type instanceof ArrayType))
			throw new SemanticError("Length cant be used on non-Arrays", length);

		return TypeTable.Table.intType;
	}

	@Override
	public Object visit(MathBinaryOp op) 
	{
		Type left = (Type) op.getFirstOperand().accept(this);
		Type right = (Type) op.getSecondOperand().accept(this);

		if (op.getOperator() == BinaryOps.PLUS) 
		{
			if ((left instanceof TypeTable.IntType) && (right instanceof TypeTable.IntType)) 
				return TypeTable.Table.intType;

			if ((left instanceof TypeTable.StringType) && (right instanceof TypeTable.StringType)) 
				return TypeTable.Table.stringType;

			throw new SemanticError("Mismatching types for '+' operator", op);
		}
		else 
		{
			if ((left instanceof TypeTable.IntType) && (right instanceof TypeTable.IntType)) 
				return TypeTable.Table.intType;

			throw new SemanticError("Mismatching types for operator", op);
		}
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) 
	{
		Type left = (Type) binaryOp.getFirstOperand().accept(this);
		Type right = (Type) binaryOp.getSecondOperand().accept(this);
		BinaryOps op = binaryOp.getOperator();
		
		if ((op == IC.BinaryOps.LT) ||(op == IC.BinaryOps.LTE) || (op == IC.BinaryOps.GT) || (op == IC.BinaryOps.GTE) ){
			if ((left instanceof TypeTable.IntType) && (right instanceof TypeTable.IntType))
				return TypeTable.Table.booleanType;

			throw new SemanticError("Mismatching types for operator", binaryOp);
		}

		if ((op == IC.BinaryOps.EQUAL) ||(op == IC.BinaryOps.NEQUAL)){
			if (left == right || left.SubType(right) || right.SubType(left))
				return TypeTable.Table.booleanType;

			throw new SemanticError("Mismatching types for operator", binaryOp);
		}

		if ((op == IC.BinaryOps.LOR) ||(op == IC.BinaryOps.LAND)){
			if (left == right && left == TypeTable.Table.booleanType)
				return TypeTable.Table.booleanType;

			throw new SemanticError("Types not applicable for this operator", binaryOp);
		}


		return null;
		
	}

	@Override
	public Object visit(MathUnaryOp op) 
	{
		Type operandType = (Type)op.getOperand().accept(this);

		if ((op.getOperator() == UnaryOps.UMINUS) && operandType.SubType(TypeTable.Table.intType) )
			return TypeTable.Table.intType;
		else
			throw new SemanticError("Illegal operand for unary minus", op);

	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		TypeTable.Type  operandType = (Type) unaryOp.getOperand().accept(this);

		if ((unaryOp.getOperator() == UnaryOps.LNEG) && operandType.SubType(TypeTable.Table.booleanType) )
			return TypeTable.Table.booleanType;
		else
			throw new SemanticError("Illegal operand for '!'", unaryOp);
	}

	@Override
	public Object visit(Literal literal) 
	{
		if ((literal.getType() == IC.LiteralTypes.FALSE) || (literal.getType() == IC.LiteralTypes.TRUE))
			return TypeTable.Table.booleanType;
		if (literal.getType() == IC.LiteralTypes.STRING)
			return TypeTable.Table.stringType;
		if (literal.getType() == IC.LiteralTypes.NULL)
			return TypeTable.Table.nullType;
		if (literal.getType() == IC.LiteralTypes.INTEGER)
			return TypeTable.Table.intType;

		throw new SemanticError("Unrecognized literal type", literal);
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		return null;
	}
}