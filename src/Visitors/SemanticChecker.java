package Visitors;



import java.util.List;

import IC.BinaryOps;
import IC.SemanticError;
import IC.UnaryOps;
import IC.AST.*;
import SymbolTable.Symbol;
import SymbolTable.SymbolMethod;
import SymbolTable.SymbolTable;
import SymbolTable.SymbolMethod.MethodKind;
import TypeTable.*;
import TypeTable.Type;


public class SemanticChecker implements Visitor {
        
        boolean staticStatus = false;
        int loopCounter = 0;
        
        @Override
        public Object visit(Program program) 
        {
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
        public Object visit(Field field) {
                return null;
        }

        @Override
        public Object visit(VirtualMethod method) {
                List<Statement> statements = method.getStatements();
                for (int i = 0; i < statements.size(); i++)
                        statements.get(i).accept(this);
                
                return null;
        }

        @Override
        public Object visit(StaticMethod method) {
                staticStatus = true;
                
                for (Statement statement: method.getStatements())
                        statement.accept(this);
                staticStatus = false;
                return null;
        }

        @Override
        public Object visit(LibraryMethod method) {
                for (Statement statement: method.getStatements())
                        statement.accept(this);
                
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

        //checks if the assignment is legal
        // return true if it is else return null
        public Object visit(Assignment assignment) 
        {
                Type locationType = (Type) assignment.getAssignment().accept(this);
                Type varibaleType = (Type) assignment.getVariable().accept(this);
                
                if (!varibaleType.SubType(locationType))
                {
                        throw new SemanticError("Illegal assignment", assignment);
                }
                
                return null;
        }
        
        @Override
        public Object visit(CallStatement callStatement) 
        {
                return callStatement.getCall().accept(this);
        }

        /*Return true if the statement is legal
         * else return null
         * @see IC.AST.Visitor#visit(IC.AST.Return)
         */
        public Object visit(Return returnStatement) 
        {
                Expression returnExpr = returnStatement.getValue();
                Type returnVal = returnExpr != null ? (Type)returnStatement.getValue().accept(this)
                                                                                    : TypeTable.Table.voidType;
                
                Type formalReturnType = (Type) returnStatement.getSymbolTable().lookup("-method", returnStatement).getType();
                
                if (!returnVal.SubType(formalReturnType))
                {
                        throw new SemanticError("The return type is not subType of " + formalReturnType, returnStatement);
                }
                
                return null;
        }

        /*Return true if the statement is legal
         * else return null
         * @see IC.AST.Visitor#visit(IC.AST.Return)
         */
        public Object visit(If ifStatement) {
                TypeTable.Type conditionType = (TypeTable.Type) ifStatement.getCondition().accept(this);
                
                if (conditionType.SubType(TypeTable.Table.booleanType))
                {
                        ifStatement.getOperation().accept(this);
                        
                        if (ifStatement.hasElse())
                        {
                                ifStatement.getElseOperation().accept(this);
                        }
                }
                else 
                {
                        throw new SemanticError("The if expression is not subtype of boolean", ifStatement);
                }
                
                return null;
        }

        /*Return true if the statement is legal
         * else return null
         * @see IC.AST.Visitor#visit(IC.AST.Return)
         */
        public Object visit(While whileStatement) 
        {
                Type conditionType = (Type) whileStatement.getCondition().accept(this); 

                if (!conditionType.SubType(TypeTable.Table.booleanType))
                {
                        throw new SemanticError("Expected boolean argument for while", whileStatement);
                }
                
                /* update the loop counter to indicate we are in loop*/
                loopCounter ++;
                whileStatement.getOperation().accept(this);
                loopCounter --;
                
                return null;
        }

        @Override
        public Object visit(Break breakStatement) 
        {       
                //checks if we are in loop
                if (loopCounter > 0)
                {
                        loopCounter--;
                }
                else
                {
                        throw new SemanticError("Unexpected break outside of loop", breakStatement);
                }
                
                return null;
        }

        @Override
        public Object visit(Continue continueStatement) 
        {
                //checks if we are in loop
                if (loopCounter <= 0)
                {
                        throw new SemanticError("Unexpected continue outside of loop", continueStatement);
                }
                
                return null;
        }
        

        @Override
        public Object visit(StatementsBlock statementsBlock) 
        {
                for (Statement statement : statementsBlock.getStatements()) 
                {
                        statement.accept(this);
                }
                
                return null;
        }

        @Override
        public Object visit(LocalVariable localVariable) 
        {
                if (localVariable.hasInitValue())//recursive check for the init value
                {
                        Type initValType = (Type)localVariable.getInitValue().accept(this);
                        Symbol symbol = localVariable.getSymbolTable().lookup(localVariable.getName(), localVariable);

                        if (!initValType.SubType(symbol.getType()))
                        {
                                throw new SemanticError("Illegal initialization", localVariable);
                        }
                }
                
                return null;
        }

        //return location type
        public Object visit(VariableLocation location) 
        {
                //get the location type
                if (location.getLocation() != null) //x.y.z case
                {   //checks if it is a class
                        Type locationType = (Type) location.getLocation().accept(this);
                        String typeName = ((ICClass)((ClassType)locationType).node).getName();
                        
                        location.setTypeName(typeName);
                        
                        if ((!(locationType instanceof ClassType))) 
                        {
                                throw new SemanticError("Not a class type", location);
                        }
                        
                        Symbol varSym = ((ClassType) locationType).getSymbolTable().lookup(location.getName(), location);
                        
                        if (varSym == null)
                        {
                                throw new SemanticError("Field does not exist", location);
                        }
                        
                        return varSym.getType(); 
                }
                else 
                {
                        Symbol varSym = location.getSymbolTable().lookup(location.getName(), location);
                        
                        if (varSym == null)
                        {
                                throw new SemanticError("Variable does not exist", location);
                        }
                        
                        return varSym.getType();
                }
        }

        @Override
        public Object visit(ArrayLocation location) 
        {
                Type indexType = (Type)location.getIndex().accept(this);
                
                if (!(indexType instanceof IntType))
                {
                        throw new SemanticError("Illegal array index", location);
                }
                
                Type type = (Type)location.getArray().accept(this);
                
                if (!(type instanceof ArrayType))
                {
                        throw new SemanticError("Can't access index of non-array type", location);
                }
                
                return ((ArrayType) type).getObjType();
        }

        @Override
        public Object visit(StaticCall call) 
        {
                SymbolTable symbolTable = null;

                ClassType classType = TypeTable.Table.getClassType(call.getClassName(), call);
                symbolTable = classType.getSymbolTable();
                
                SymbolMethod symbol = (SymbolMethod)symbolTable.lookup(call.getName(), call);
                
                if (symbol.getMethodKind() != MethodKind.Static &&
                        symbol.getMethodKind() != MethodKind.Library)
                {
                        throw new SemanticError("Method isn't static", call);
                }
                
                if (symbol.getMethodKind() == MethodKind.Static)
                {
                        call.staticMethod = (StaticMethod) symbol.getNode();
                }
                
                MethodType methodType = (MethodType)symbol.getType();
                List<Expression> args = call.getArguments();
                List<Type> argTypes = methodType.getArgTypes();
                
                if (argTypes.size() != args.size())
                {
                        throw new SemanticError("Illegal number of arguments", call);
                }
                
                for (int i = 0; i < args.size(); i++)
                {
                        Type argType = (Type)args.get(i).accept(this);
                        
                        if (!argType.SubType(argTypes.get(i)))
                        {
                                throw new SemanticError("Illegal argument: #" + i, call);
                        }
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
                        {
                                throw new SemanticError("Can't invoke method on this expression", call);
                        }
                        
                        symbolTable = ((ClassType)type).getSymbolTable().lookup(call.getName(), call).getNode().getSymbolTable();
                }
                else
                {
                        symbolTable = call.getSymbolTable().lookup(call.getName(), call).getNode().getSymbolTable();
                }
                
                call.className = symbolTable.getClassSymbolTable().getId();
                call.virtualMethod = (VirtualMethod) symbolTable.lookup(call.getName(), call).getNode();
                SymbolMethod symbol = (SymbolMethod)symbolTable.lookup(call.getName(), call);
                
                if (symbol.getMethodKind() != MethodKind.Virtual)
                {
                        throw new SemanticError("Method isn't virtual", call);
                }
                
                MethodType methodType = (MethodType)symbol.getType();
                List<Expression> args = call.getArguments();
                List<Type> argTypes = methodType.getArgTypes();
                
                if (argTypes.size() != args.size())
                {
                        throw new SemanticError("Illegal number of arguments", call);
                }
                
                for (int i = 0; i < args.size(); i++)
                {
                        Type argType = (Type)args.get(i).accept(this);
                        
                        if (!argType.SubType(argTypes.get(i)))
                        {
                                throw new SemanticError("Illegal argument: #" + i, call);
                        }
                }
                
                return methodType.getRetType();
        }

        @Override
        public Object visit(This thisExpression) 
        {
                if (staticStatus)
                        throw new SemanticError("Illegal 'this' in static method", thisExpression);
                
                SymbolTable classSymbolTable = thisExpression.getSymbolTable().getClassSymbolTable();

                if (classSymbolTable == null)
                        throw new SemanticError("Can't find containing class scope", thisExpression);
                
                Type type = TypeTable.Table.getClassType(classSymbolTable.getId(), thisExpression);
                
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
                 
                return type;
        }

        @Override
        public Object visit(Length length) 
        {
                length.getArray().accept(this);
                
                return TypeTable.Table.intType;
        }

        @Override
        public Object visit(MathBinaryOp binaryOp) 
        {
                Type left = (Type) binaryOp.getFirstOperand().accept(this);
                Type right = (Type) binaryOp.getSecondOperand().accept(this);
                
                if (binaryOp.getOperator() == BinaryOps.PLUS) 
                {
                        if ((left instanceof TypeTable.IntType) && 
                                (right instanceof TypeTable.IntType)) 
                        {
                                return TypeTable.Table.intType;
                        }
                        
                        if ((left instanceof TypeTable.StringType) && 
                                (right instanceof TypeTable.StringType)) 
                        {
                                return TypeTable.Table.stringType;
                        }

                        throw new SemanticError("Types not applicable for plus operator", binaryOp);
                }
                else 
                {
                        if ((left instanceof TypeTable.IntType) && 
                                (right instanceof TypeTable.IntType)) 
                        {
                                        return TypeTable.Table.intType;
                        }
                        
                        throw new SemanticError("Types not applicable for this operator", binaryOp);
                }
        }

        @Override
        public Object visit(LogicalBinaryOp binaryOp) 
        {
                Type left = (Type) binaryOp.getFirstOperand().accept(this);
                Type right = (Type) binaryOp.getSecondOperand().accept(this);

                BinaryOps op = binaryOp.getOperator();
                switch (op)
                {
                        // Relational
                        case LT:
                        case LTE:
                        case GT:
                        case GTE:
                        {
                                if ((left instanceof TypeTable.IntType) && 
                                        (right instanceof TypeTable.IntType))
                                {
                                        return TypeTable.Table.booleanType;
                                }
                        
                                throw new SemanticError("Types not applicable for this operator", binaryOp);
                        }
                        
                        // Equality Comparison
                        case EQUAL:
                        case NEQUAL:
                        {
                                if (left == right ||
                                        left.SubType(right) ||
                                        right.SubType(left))
                                {
                                        return TypeTable.Table.booleanType;
                                }
                                
                                throw new SemanticError("Types not applicable for this operator", binaryOp);
                        }
                        
                        // Conditional
                        case LAND:
                        case LOR:
                        {
                                if (left == right &&
                                        left == TypeTable.Table.booleanType)
                                {
                                        return TypeTable.Table.booleanType;
                                }
                                
                                throw new SemanticError("Types not applicable for this operator", binaryOp);
                        }
                        
                        default:
                                return null;
                }
        }

        @Override
        public Object visit(MathUnaryOp unaryOp) 
        {
                Type operandType = (Type)unaryOp.getOperand().accept(this);
                
                if ((unaryOp.getOperator() == UnaryOps.UMINUS) && operandType.SubType(TypeTable.Table.intType) )
                {
                        return TypeTable.Table.intType;
                }
                else
                {
                        throw new SemanticError("The logical expression is not valid", unaryOp);
                }

        }

        @Override
        public Object visit(LogicalUnaryOp unaryOp) {
                TypeTable.Type  operandType = (Type) unaryOp.getOperand().accept(this);
                
                if ((unaryOp.getOperator() == UnaryOps.LNEG) && operandType.SubType(TypeTable.Table.booleanType) )
                {
                        return TypeTable.Table.booleanType;
                }
                else
                {
                        throw new SemanticError("The logical expression is not valid", unaryOp);
                }
        }

        @Override
        public Object visit(Literal literal) 
        {
                switch (literal.getType())
                {
                        case FALSE:
                        case TRUE:
                                return TypeTable.Table.booleanType;
                        case STRING:
                                return TypeTable.Table.stringType;
                        case NULL:
                                return TypeTable.Table.nullType;
                        case INTEGER:
                                return TypeTable.Table.intType;
                        default:
                                throw new RuntimeException("Unexpected Error");
                }
        }

        @Override
        public Object visit(ExpressionBlock expressionBlock) {
                // TODO Auto-generated method stub
                return null;
        }
}