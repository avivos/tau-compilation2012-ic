package Visitors;


import java.util.List;
import java.util.Map;


import IC.SemanticError;
import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.BinaryOp;
import IC.AST.Break;
import IC.AST.Call;
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
import IC.AST.Location;
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
import IC.AST.UnaryOp;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import SymbolTable.Symbol;
import SymbolTable.SymbolMethod;
import SymbolTable.SymbolTable;
import SymbolTable.Symbol.SymbolKind;
import SymbolTable.SymbolMethod.MethodKind;
import SymbolTable.SymbolTable.TableKind;
import TypeTable.ClassType;
import TypeTable.MethodType;
import TypeTable.Type;

public class SymbolTableCreator implements Visitor 
{
        private ASTNode m_LibraryNode;
        private String m_FileName;
        
        int mainCounter = 0;
        boolean libraryStatus;
        int varCounter = 0;
        
        public void setLibraryStatus(boolean status)
        {
                libraryStatus = status;
        }
        
        public SymbolTableCreator(String fileName, ASTNode libraryNode)
        {
                m_LibraryNode = libraryNode;
                m_FileName = fileName;
        }
        
        private void handleLibrary(Program program)
        {
                SymbolTable libSymbolTable = m_LibraryNode.getSymbolTable();
                Symbol libSym = libSymbolTable.lookup("Library", program);
                
                if (libSym == null)
                {
                        throw new SemanticError("Can't locate library class", m_LibraryNode);
                }
                
                program.getSymbolTable().insert(libSym);
                libSym.getNode().getSymbolTable().setParent(program.getSymbolTable());
        }
        
        @Override
        public Object visit(Program program) throws SemanticError 
        {
                SymbolTable globalSymbolTable = new SymbolTable(m_FileName, TableKind.Global);
                program.setSymbolTable(globalSymbolTable);
                
                if (m_LibraryNode != null)
                {
                        handleLibrary(program);
                }
                
                for (ICClass icClass : program.getClasses())
                {
                        String superClassName = icClass.getSuperClassName();
                        
                        SymbolTable classParentSymbolTable = null;
                        
                        if (superClassName != null)
                        {
                                Symbol superSym = globalSymbolTable.lookup(superClassName, icClass);
                                if (superSym == null)
                                {
                                        throw new SemanticError("Super class not defined", icClass);
                                }

                                classParentSymbolTable = superSym.getNode().getSymbolTable();
                        }
                        else
                        {
                                classParentSymbolTable = globalSymbolTable;
                        }
                        
                        ClassType classType = TypeTable.Table.addClassType(icClass,true);
                        
                        icClass.setSymbolTable(classParentSymbolTable);
                        SymbolTable symbolTableChild = (SymbolTable)icClass.accept(this);
                        
                        symbolTableChild.setParent(classParentSymbolTable);

                        Symbol sym = new Symbol(icClass.getName(),  
                                                                        icClass,
                                                                        SymbolKind.Class,
                                                                        classType);
                        
                        globalSymbolTable.insert(sym);
                        
                }
                if (libraryStatus ==  true && mainCounter > 0)
                        throw new SemanticError("The Library should not contain main function ", program);
                        
                if (mainCounter  ==  0 && libraryStatus ==  false )
                        throw new SemanticError("The program contains no main function ", program);
        
                for (Map.Entry<String, ClassType> pairs : TypeTable.Table.getClassList().entrySet())
                {
                        if (((ClassType) pairs.getValue()).initFlag == false)
                                throw new SemanticError("Using undefined class", ((ClassType) pairs.getValue()).node);
                }       
                return globalSymbolTable;
        }
        
        private void verifyMemberField(ICClass icClass, ASTNode node, String name)
        {
                SymbolTable symbolTable = icClass.getSymbolTable();
                Symbol existingSym = symbolTable.lookup(name, node);
                
                if (existingSym != null &&
                        existingSym.getKind() == SymbolKind.Field )
                {
                        throw new SemanticError("Field with same name already defined", node);
                }
        }
        private void verifyMemberMethod(ICClass icClass, Method node, String name)
        {
                SymbolTable symbolTable = icClass.getSymbolTable();
                Symbol existingSym = symbolTable.lookupScope(name);
                
                if (existingSym != null &&
                        existingSym.getKind() == SymbolKind.Method)
                {
                        throw new SemanticError("Method with same name or sign already defined", node);
                }
                
                existingSym = symbolTable.lookup(name, node);
                
                if (existingSym != null && existingSym.getKind() == SymbolKind.Method &&
                                        (!compareList(((Method)existingSym.getNode()).getFormals(),node.getFormals())||
                                                        (!node.getType().getName().equalsIgnoreCase(((Method)existingSym.getNode()).getType().getName()))
                                                        ||(node.getType().getDimension()!= ((Method)existingSym.getNode()).getType().getDimension())))
                {
                        throw new SemanticError("overloading Method is not allowed",node);
                }
        }
        
         boolean compareList(List<Formal> list1, List<Formal> list2)
        {
                if (list1.size() == list2.size())
                {
                        for (int i = 0; i < list1.size() ; i++)
                        {
                                if ( list1.get(i).getType().getName().equals(((Formal) list2.get(i)).getType().getName()) &&  
                                                list1.get(i).getType().getDimension() == list2.get(i).getType().getDimension())
                                        continue;
                                else
                                        return false;
                        }
                        return true;
                }
                return false;
        }


        @Override
        public Object visit(ICClass icClass)  
        {
                SymbolTable symbolTable = new SymbolTable(icClass.getName(), TableKind.Class);
                symbolTable.setParent(icClass.getSymbolTable());
                icClass.setSymbolTable(symbolTable);
                
                for (Field field : icClass.getFields())
                {
                        verifyMemberField(icClass, field, field.getName());
                        
                        field.setSymbolTable(symbolTable);
                        field.accept(this);
                        
                        Type type = TypeTable.Table.getType(field.getType());
                        if (type == null)
                           {
                            ICClass c = new ICClass(field.getLine(), field.getType().getName(), null, null);
                            type =  TypeTable.Table.addClassType(c);
                          }
                        Symbol sym = new Symbol(field.getName(), 
                                                                        field,
                                                                        SymbolKind.Field,
                                                                        type);
                        
                        symbolTable.insert(sym);
                }
                
                for (Method method : icClass.getMethods())
                {
                        verifyMemberMethod(icClass, method, method.getName());
                        
                        MethodType type = TypeTable.Table.methodType(method);
                        
                        method.setSymbolTable(symbolTable);
                        SymbolTable symbolTableChild = (SymbolTable)method.accept(this);
                        symbolTableChild.setParent(symbolTable);
                        
                        MethodKind kind = method instanceof StaticMethod ? MethodKind.Static
                                                                : method instanceof VirtualMethod ? MethodKind.Virtual :
                                                                        MethodKind.Library;
                        
                        Symbol sym = new SymbolMethod(method.getName(), 
                                                                                  method,
                                                                                  SymbolKind.Method,
                                                                                  type,
                                                                                  kind);
                        
                        symbolTable.insert(sym);
                }
                
                return symbolTable;
        }

        @Override
        public Object visit(Field field) 
        {
                return null;
        }
        
        public Object visit(Method method, MethodKind methodKind) 
        {
                
                SymbolTable symbolTable = new SymbolTable(method.getName(), TableKind.Method);
                method.setSymbolTable(symbolTable);
                
                Type returnType = TypeTable.Table.getType(method.getType());
                
                if ( method.getName().equals("main") && methodKind  == MethodKind.Static  &&
                        returnType ==  TypeTable.Table.voidType  &&
                        method.getFormals().size() == 1 && method.getFormals().get(0).getType().getDimension() == 1 &&
                        method.getFormals().get(0).getType().getName().equals("string"))
                        mainCounter ++;
                
                if (mainCounter > 1)
                        throw new SemanticError("The program contains more than one main", method);
                
                Symbol returnSym = new Symbol("-method",
                                                                method,
                                                                SymbolKind.ReturnType,
                                                                returnType);
                
                symbolTable.insert(returnSym);
                
                
                for (Formal formal : method.getFormals())
                {
                        formal.setSymbolTable(symbolTable);
                        formal.accept(this);
                        
                        if (symbolTable.lookupScope(formal.getName()) != null)
                                throw new SemanticError("Variable already defined in this scope", formal);
                        
                        Type type = TypeTable.Table.getType(formal.getType());
                        
                        Symbol sym = new Symbol(formal.getName(),
                                                                        formal,
                                                                        SymbolKind.Parameter,
                                                                        type);
                        
                        symbolTable.insert(sym);
                }
                
                for (Statement statement : method.getStatements())
                {
                        statement.setSymbolTable(symbolTable);
                        Object result = statement.accept(this);
                        
                        if (result != null)
                        {
                                SymbolTable symbolTableChild = (SymbolTable)result;
                                symbolTableChild.setParent(symbolTable);
                        }
                }

                return symbolTable;
        }
        

        @Override
        public Object visit(StatementsBlock statementsBlock) 
        {
                SymbolTable symbolTable = new SymbolTable("block." + statementsBlock.getLine(), TableKind.Block);
                //statementsBlock.setSymbolTable(symbolTable);
                
                for (Statement statement : statementsBlock.getStatements())
                {
                        statement.setSymbolTable(symbolTable);
                        Object result = statement.accept(this);
                        
                        if (result != null)
                        {
                                SymbolTable symbolTableChild = (SymbolTable)result;
                                symbolTableChild.setParent(symbolTable);
                        }
                }
                
                return symbolTable;
        }
        
        @Override
        public Object visit(ExpressionBlock expressionBlock) 
        {
                Expression expr = expressionBlock.getExpression();
                expr.setSymbolTable(expressionBlock.getSymbolTable());
                
                return expr.accept(this);
        }

        @Override
        public Object visit(VirtualMethod method) 
        {
                return visit(method, MethodKind.Virtual);
        }

        @Override
        public Object visit(StaticMethod method) 
        {
                return visit(method, MethodKind.Static);
        }

        @Override
        public Object visit(LibraryMethod method) 
        {
                return visit(method, MethodKind.Library);
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
        public Object visit(Assignment assignment) 
        {
                Expression expr = assignment.getAssignment();
                expr.setSymbolTable(assignment.getSymbolTable());
                expr.accept(this);
                
                Location location = assignment.getVariable();
                location.setSymbolTable(assignment.getSymbolTable());
                location.accept(this);
                
                return null;
        }

        @Override
        public Object visit(CallStatement callStatement) 
        {
                Call call = callStatement.getCall();
                call.setSymbolTable(callStatement.getSymbolTable());
                call.accept(this);
                
                return null;
        }

        @Override
        public Object visit(Return returnStatement) 
        {
                Expression expr = returnStatement.getValue();
                
                if (expr != null)
                {
                        expr.setSymbolTable(returnStatement.getSymbolTable());
                        return expr.accept(this);
                }
                        
                return null;
        }

        @Override
        public Object visit(If ifStatement) 
        {
                // Condition
                Expression exprCondition = ifStatement.getCondition();
                exprCondition.setSymbolTable(ifStatement.getSymbolTable());
                exprCondition.accept(this);
                
                // Operation
                Statement exprOpStatement = ifStatement.getOperation();
                exprOpStatement.setSymbolTable(ifStatement.getSymbolTable());
                
                Object result = exprOpStatement.accept(this);
                
                if (result != null)
                {
                        SymbolTable symbolTableChild = (SymbolTable)result;
                        symbolTableChild.setParent(ifStatement.getSymbolTable());
                }
                
                // Else Operation
                if (ifStatement.hasElse())
                {
                        Statement exprElseStatement = ifStatement.getElseOperation();
                        exprElseStatement.setSymbolTable(ifStatement.getSymbolTable());
                        
                        Object resultElse = exprElseStatement.accept(this);
                        
                        if (result != null)
                        {
                                SymbolTable symbolTableChild = (SymbolTable)resultElse;
                                symbolTableChild.setParent(ifStatement.getSymbolTable());
                        }
                }
                
                return null;
        }

        @Override
        public Object visit(While whileStatement) 
        {
                // Condition
                Expression exprCondition = whileStatement.getCondition();
                exprCondition.setSymbolTable(whileStatement.getSymbolTable());
                exprCondition.accept(this);
                
                // Operation
                Statement exprOpStatement = whileStatement.getOperation();
                exprOpStatement.setSymbolTable(whileStatement.getSymbolTable());
                
                Object result = exprOpStatement.accept(this);
                
                if (result != null)
                {
                        SymbolTable symbolTableChild = (SymbolTable)result;
                        symbolTableChild.setParent(whileStatement.getSymbolTable());
                }
                return null;
        }

        @Override
        public Object visit(Break breakStatement) {
                return null;
        }

        @Override
        public Object visit(Continue continueStatement) {
                return null;
        }


        @Override
        public Object visit(LocalVariable localVariable) 
        {
                Symbol existingSym = localVariable.getSymbolTable().lookupScope(localVariable.getName());
                
                if (existingSym != null &&
                        existingSym.getNode() != localVariable)
                {
                        throw new SemanticError("Variable already defined in this scope", localVariable);
                }
                
                Type type = TypeTable.Table.getType(localVariable.getType());
                
                Symbol sym = new Symbol(localVariable.getName(),
                                                                localVariable,
                                                                SymbolKind.Variable,
                                                                type);
                
                localVariable.getSymbolTable().insert(sym);
                localVariable.id = varCounter++;
                
                Expression initValue = localVariable.getInitValue();
                
                if (initValue != null)
                {
                        initValue.setSymbolTable(localVariable.getSymbolTable());
                        initValue.accept(this);
                }
                
                return null;
        }

        
        @Override
        public Object visit(VariableLocation location) 
        {
                Expression exprLocation = location.getLocation();
                
                if (exprLocation != null)
                {
                        exprLocation.setSymbolTable(location.getSymbolTable());
                        exprLocation.accept(this);
                }
                
                return null;
        }

        @Override
        public Object visit(ArrayLocation location) 
        {
                Expression exprArray = location.getArray();
                exprArray.setSymbolTable(location.getSymbolTable());
                exprArray.accept(this);
                
                Expression exprIndex = location.getIndex();
                exprIndex.setSymbolTable(location.getSymbolTable());
                exprIndex.accept(this);
                
                return null;
        }

        @Override
        public Object visit(StaticCall call) 
        {
                for (Expression expr : call.getArguments())
                {
                        expr.setSymbolTable(call.getSymbolTable());
                        expr.accept(this);
                }
                
                return null;
        }

        @Override
        public Object visit(VirtualCall call) 
        {
                Expression location = call.getLocation();
                
                if (location != null)
                {
                        location.setSymbolTable(call.getSymbolTable());
                        location.accept(this);
                }
                
                for (Expression expr : call.getArguments())
                {
                        expr.setSymbolTable(call.getSymbolTable());
                        expr.accept(this);
                }
                
                return null;
        }

        @Override
        public Object visit(This thisExpression) {
                return null;
        }

        @Override
        public Object visit(NewClass newClass) {
                return null;
        }

        @Override
        public Object visit(NewArray newArray) 
        {
                Expression expr = newArray.getSize();
                expr.setSymbolTable(newArray.getSymbolTable());
                expr.accept(this);
                
                return null;
        }

        @Override
        public Object visit(Length length) 
        {
                Expression expr = length.getArray();
                expr.setSymbolTable(length.getSymbolTable());
                expr.accept(this);
                
                return null;
        }

        public Object visit(BinaryOp binaryOp)
        {
                Expression expr1 = binaryOp.getFirstOperand();
                expr1.setSymbolTable(binaryOp.getSymbolTable());
                expr1.accept(this);
                
                Expression expr2 = binaryOp.getSecondOperand();
                expr2.setSymbolTable(binaryOp.getSymbolTable());
                expr2.accept(this);     
                
                return null;
        }
        
        public Object visit(UnaryOp unaryOp)
        {
                Expression expr = unaryOp.getOperand();
                expr.setSymbolTable(unaryOp.getSymbolTable());
                expr.accept(this);
                
                return null;
        }
        
        @Override
        public Object visit(MathBinaryOp binaryOp) 
        {
                return visit((BinaryOp)binaryOp);
        }

        @Override
        public Object visit(LogicalBinaryOp binaryOp) 
        {
                return visit((BinaryOp)binaryOp);
        }

        @Override
        public Object visit(MathUnaryOp unaryOp) 
        {
                return visit((UnaryOp)unaryOp);
        }

        @Override
        public Object visit(LogicalUnaryOp unaryOp) 
        {
                return visit((UnaryOp)unaryOp);
        }

        @Override
        public Object visit(Literal literal) {
                return null;
        }
}