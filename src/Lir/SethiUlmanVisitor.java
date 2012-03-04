package Lir;

import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.BinaryOp;
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
import IC.AST.UnaryOp;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;

public class SethiUlmanVisitor implements Visitor {

	@Override
	public Object visit(Program program) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ICClass icClass) {
		// TODO Auto-generated method stub
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
//		int totalWeight = 0;
//		int maxWeight = 0;
//		int argWeight = 0;
//		for (Expression arg : call.getArguments()){
//			argWeight = (Integer) arg.accept(this);
//		}
	}

	@Override
	public Object visit(This thisExpression) {
		return 0;
	}

	@Override
	public Object visit(NewClass newClass) {
		return 0;
	}

	@Override
	public Object visit(NewArray newArray) {
		int sizeExpWeight = (Integer) newArray.getSize().accept(this);
		return sizeExpWeight;
	}

	@Override
	public Object visit(Length length) {
		length.setWeight(1);
		return 1;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		return handleBinOp(binaryOp);
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		return handleBinOp(binaryOp);
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {
		return handleUnOpe(unaryOp);
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		return handleUnOpe(unaryOp);
	}

	@Override
	public Object visit(Literal literal) {
		literal.setWeight(0);
		return 0;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		return 0;
	}

	
	private Object handleUnOpe(UnaryOp unaryOp){
		int w = (Integer) unaryOp.getOperand().accept(this);
		unaryOp.setWeight(w);
		return w;
	}
	
	
	private Object handleBinOp(BinaryOp binaryOp) {
		int w1 = (Integer) binaryOp.getFirstOperand().accept(this);
		int w2 = (Integer) binaryOp.getSecondOperand().accept(this);
		
		int retW = 0;
		if (w1 == w2){
			retW =  w1 + 1;
		}
		else {
			retW = Math.max(w1, w2);
		}
		
		binaryOp.setWeight(retW);
		return retW;
	}
}





