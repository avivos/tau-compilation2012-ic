package Visitors;


import java.util.List;
import java.util.Map;

import SymbolTable.Symbol;
import SymbolTable.SymbolMethod;
import SymbolTable.SymbolTable;
import SymbolTable.Symbol.SymbolKind;
import SymbolTable.SymbolMethod.MethodKind;
import SymbolTable.SymbolTable.TableKind;
import TypeTable.ClassType;
import TypeTable.MethodType;
import TypeTable.Type;

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


public class SymbolTableCreator implements Visitor 
{
        private ASTNode libraryNode;
        private String fileName;
        int mains = 0;
        boolean libraryFlag;
        int variables = 0;
        
        public void setLibraryFlag(boolean flag)
        {
                libraryFlag = flag;
        }
        
        public SymbolTableCreator(String fname, ASTNode library)
        {
                libraryNode = library;
                fileName = fname;
        }
        
        private void addLibraryToProgram(Program program)
        {
                SymbolTable librarySymbolTable = libraryNode.getSymbolTable();
                Symbol libSym = librarySymbolTable.lookupScope("Library");
                if (libSym == null)
                        throw new SemanticError("Can't find library class in library file", libraryNode);
                program.getSymbolTable().insert(libSym);
                libSym.getNode().getSymbolTable().setParent(program.getSymbolTable());
        }
        
        private void checkDuplicateFields(ICClass cls, ASTNode node, String name)
        {
                Symbol sym = cls.getSymbolTable().lookup(name, node);
                if (sym != null && sym.getKind() == SymbolKind.Field )
                        throw new SemanticError("Field with same name already defined", node);
        }
        
        private void checkDuplicateMethods(ICClass cls, Method node, String name)
        {
                Symbol sym = cls.getSymbolTable().lookupScope(name);
                if (sym != null && sym.getKind() == SymbolKind.Method)
                        throw new SemanticError("Method with same name was already defined ", node);
                
                sym = cls.getSymbolTable().lookup(name, node);
                
                if (sym != null && sym.getKind() == SymbolKind.Method )
                	if	(!compareFormals(((Method)sym.getNode()).getFormals(),node.getFormals()) //compare formals
                          ||(!node.getType().getName().equalsIgnoreCase(((Method)sym.getNode()).getType().getName())) //compare return val
                          ||(node.getType().getDimension()!= ((Method)sym.getNode()).getType().getDimension())) //compare dim
                		throw new SemanticError("overloading Method is not allowed ",node);
        }
        
        private boolean compareFormals(List<Formal> list1, List<Formal> list2)
        {
                if (list1.size() == list2.size()){
                        for (int i = 0; i < list1.size() ; i++){
                                if ( !(list1.get(i).getType().getName().equals(((Formal) list2.get(i)).getType().getName())) ||  
                                                !(list1.get(i).getType().getDimension() == list2.get(i).getType().getDimension()))
                                	return false;
                                        
                        }
                        return true;
                }
                return false;
        }
        
        @Override
        public Object visit(Program node) throws SemanticError 
        {
                SymbolTable globalTable = new SymbolTable(fileName, TableKind.Global);
                node.setSymbolTable(globalTable);
                if (libraryNode != null)
                        addLibraryToProgram(node);
          
                for (ICClass cls : node.getClasses())
                {
                        String superName = cls.getSuperClassName();
                        SymbolTable superSymbolTable = null;
                        if (superName != null)
                        {
                                Symbol superSymbol = globalTable.lookup(superName, cls);
                                if (superSymbol == null)
                                        throw new SemanticError("Super class is undefined ", cls);
                                
                                superSymbolTable = superSymbol.getNode().getSymbolTable();
                        }
                        else
                           superSymbolTable = globalTable;
                        
                        ClassType classT = TypeTable.Table.addClassTypeDef(cls);
                        cls.setSymbolTable(superSymbolTable);
                        //recursive traversal
                        SymbolTable symbolTableChild = (SymbolTable)cls.accept(this);
                        
                        //link the 2 tables (program-class)
                        symbolTableChild.setParent(superSymbolTable);
                        Symbol sym = new Symbol(cls.getName(),cls,SymbolKind.Class,classT);
                        globalTable.insert(sym);
                        
                }
                
                if (libraryFlag ==  true && mains > 0)
                        throw new SemanticError("The Library should not contain main function ", node);
                        
                if (mains  ==  0 && libraryFlag ==  false )
                        throw new SemanticError("The program contains no main function ", node);
        
                for (Map.Entry<String, ClassType> pairs : TypeTable.Table.getClassList().entrySet())
                {
                        if (((ClassType) pairs.getValue()).initFlag == false)
                                throw new SemanticError("Undefined class used ", ((ClassType) pairs.getValue()).node);
                }       
                return globalTable;
        }
        



        @Override
        public Object visit(ICClass node)  
        {
                SymbolTable classSymTable = new SymbolTable(node.getName(), TableKind.Class);
                //AST linkage
                classSymTable.setParent(node.getSymbolTable());
                node.setSymbolTable(classSymTable);
                //fields
                for (Field field : node.getFields()){
                        checkDuplicateFields(node, field, field.getName()); //throws error if already exist
                        field.setSymbolTable(classSymTable);
                        //recursive traversal
                        field.accept(this);
                        
                        Type fieldType = TypeTable.Table.getType(field.getType());
                        //if undefined type
                        if (fieldType == null)
                           {
                            ICClass cls = new ICClass(field.getLine(), field.getType().getName(), null, null);
                            fieldType =  TypeTable.Table.addClassType(cls);
                          }
                        Symbol sym = new Symbol(field.getName(),field,SymbolKind.Field,fieldType);
                        classSymTable.insert(sym);
                }
                //methods
                for (Method method : node.getMethods())
                {
                        checkDuplicateMethods(node, method, method.getName());
                        
                        MethodType mtype = TypeTable.Table.methodType(method);
                        
                        method.setSymbolTable(classSymTable);
                        //recursive traversal
                        SymbolTable symbolTableChild = (SymbolTable)method.accept(this);
                        symbolTableChild.setParent(classSymTable);
                        
                        MethodKind kind;
                        if (method instanceof StaticMethod)
                        	kind = MethodKind.Static;
                        else
                        	if (method instanceof VirtualMethod)
                        		kind = MethodKind.Virtual;
                        	else
                        		kind = MethodKind.Library;
                        
                        Symbol sym = new SymbolMethod(method.getName(),method,SymbolKind.Method,mtype,kind);
                        classSymTable.insert(sym);
                }
                
                return classSymTable;
        }

        @Override
        public Object visit(Field field) 
        {
                return null;
        }
        
        public Object visit(Method m, MethodKind mKind) 
        {
                SymbolTable methodlTable = new SymbolTable(m.getName(), TableKind.Method);
                m.setSymbolTable(methodlTable);
                Type retType = TypeTable.Table.getType(m.getType());
                
                if ( m.getName().equals("main") && mKind  == MethodKind.Static  &&
                        retType ==  TypeTable.Table.voidType  &&
                        m.getFormals().size() == 1 && 
                        m.getFormals().get(0).getType().getName().equals("string") &&
                        m.getFormals().get(0).getType().getDimension() == 1 )
                        mains++; // add a main to counter
                
                if (mains > 1)
                        throw new SemanticError("The program contains more than one main", m);
                
                Symbol retSym = new Symbol("-return-type",m,SymbolKind.ReturnType,retType);
                methodlTable.insert(retSym);
                //params
                for (Formal formal : m.getFormals())
                {
                        formal.setSymbolTable(methodlTable); 
                        if (methodlTable.lookupScope(formal.getName()) != null)
                                throw new SemanticError("A parameter with the same name was already defined", formal);
                        Type type = TypeTable.Table.getType(formal.getType());
                        Symbol sym = new Symbol(formal.getName(),formal,SymbolKind.Parameter,type);
                        //add it to method table
                        methodlTable.insert(sym);
                }
                //func body
                for (Statement stmt : m.getStatements())
                {
                        stmt.setSymbolTable(methodlTable);
                        //recursive traversal (for statement blocks)
                        Object block = stmt.accept(this);
                        
                        //if stmt = statement block
                        if (block != null){
                                SymbolTable blockScopeTable = (SymbolTable)block;
                                blockScopeTable.setParent(methodlTable);
                        }
                }

                return methodlTable;
        }
        

        @Override
        public Object visit(StatementsBlock stmtBlock) 
        {
                SymbolTable blockTable = new SymbolTable("block-" + stmtBlock.getLine(), TableKind.Block);
                
                for (Statement statement : stmtBlock.getStatements())
                {
                        statement.setSymbolTable(blockTable);
                        //recursive traversal
                        Object returnTable = statement.accept(this);
                        if (returnTable != null){
                                SymbolTable symbolTableChild = (SymbolTable)returnTable;
                                symbolTableChild.setParent(blockTable);
                        }
                }
                return blockTable;
        }
        
        @Override
        public Object visit(ExpressionBlock expressionBlock) 
        {
//                Expression expr = expressionBlock.getExpression();
//                expr.setSymbolTable(expressionBlock.getSymbolTable());
//                
//                return expr.accept(this);
        	return null;
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
                Expression exp = assignment.getAssignment();
                exp.setSymbolTable(assignment.getSymbolTable());
                exp.accept(this);
                Location location = assignment.getVariable();
                location.setSymbolTable(assignment.getSymbolTable());
                location.accept(this);
                return null;
        }

        @Override
        public Object visit(CallStatement callStmt) 
        {
                Call call = callStmt.getCall();
                call.setSymbolTable(callStmt.getSymbolTable());
                call.accept(this);
                return null;
        }

        @Override
        public Object visit(Return returnStmt) 
        {
                Expression exp = returnStmt.getValue();
                if (exp != null){
                        exp.setSymbolTable(returnStmt.getSymbolTable());
                        return exp.accept(this);
                }    
                return null;
        }

        @Override
        public Object visit(If ifStatement) 
        {
                Expression exp = ifStatement.getCondition();
                exp.setSymbolTable(ifStatement.getSymbolTable());
                exp.accept(this);
                
                // "then"
                Statement thenStmt = ifStatement.getOperation();
                thenStmt.setSymbolTable(ifStatement.getSymbolTable());
                Object thenTable = thenStmt.accept(this);
                if (thenTable != null){
                        SymbolTable symThenTable = (SymbolTable)thenTable;
                        symThenTable.setParent(ifStatement.getSymbolTable());
                }
                
                // "else"
                if (ifStatement.hasElse())
                {
                        Statement elseStmt = ifStatement.getElseOperation();
                        elseStmt.setSymbolTable(ifStatement.getSymbolTable());
                        Object elseTable = elseStmt.accept(this);
                        if (thenTable != null)
                        {
                                SymbolTable symElseTable = (SymbolTable)elseTable;
                                symElseTable.setParent(ifStatement.getSymbolTable());
                        }
                }
                
                return null;
        }

        @Override
        public Object visit(While whileStmt) 
        {
                Expression exp = whileStmt.getCondition();
                exp.setSymbolTable(whileStmt.getSymbolTable());
                exp.accept(this);
                // loop
                Statement loopStmt = whileStmt.getOperation();
                loopStmt.setSymbolTable(whileStmt.getSymbolTable());
                Object loopTable = loopStmt.accept(this);
                if (loopTable != null){
                        SymbolTable symLoopTable = (SymbolTable)loopTable;
                        symLoopTable.setParent(whileStmt.getSymbolTable());
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
        public Object visit(LocalVariable var) 
        {
                Symbol tableSym = var.getSymbolTable().lookupScope(var.getName());
                if (tableSym != null && tableSym.getNode() != var)
                        throw new SemanticError("Variable with the same name was already defined", var);
                Type type = TypeTable.Table.getType(var.getType());
                Symbol varSym = new Symbol(var.getName(),var,SymbolKind.Variable,type);
                var.getSymbolTable().insert(varSym);
                //handle assignment declaration
                Expression assgV = var.getInitValue();                
                if (assgV != null){
                        assgV.setSymbolTable(var.getSymbolTable());
                        assgV.accept(this);
                }
                return null;
        }

        
        @Override
        public Object visit(VariableLocation location) 
        {
                Expression expLocation = location.getLocation();
                if (expLocation != null){
                        expLocation.setSymbolTable(location.getSymbolTable());
                        expLocation.accept(this);
                }
                
                return null;
        }

        @Override
        public Object visit(ArrayLocation location) 
        {
                Expression expArr = location.getArray();
                expArr.setSymbolTable(location.getSymbolTable());
                expArr.accept(this);
                // Arr[i]
                Expression i = location.getIndex();
                i.setSymbolTable(location.getSymbolTable());
                i.accept(this);
                return null;
        }

        @Override
        public Object visit(StaticCall call) 
        {
                for (Expression expr : call.getArguments()){
                        expr.setSymbolTable(call.getSymbolTable());
                        expr.accept(this);
                }
                
                return null;
        }

        @Override
        public Object visit(VirtualCall call) 
        {
                Expression loc = call.getLocation();
                if (loc != null){
                        loc.setSymbolTable(call.getSymbolTable());
                        loc.accept(this);
                }
                for (Expression expr : call.getArguments()){
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
        public Object visit(NewArray arr) 
        {
                Expression exp = arr.getSize();
                exp.setSymbolTable(arr.getSymbolTable());
                exp.accept(this);
                return null;
        }

        @Override
        public Object visit(Length length) 
        {
                Expression exp = length.getArray();
                exp.setSymbolTable(length.getSymbolTable());
                exp.accept(this);
                return null;
        }

        public Object visit(BinaryOp op)
        {
                Expression expr1 = op.getFirstOperand();
                expr1.setSymbolTable(op.getSymbolTable());
                expr1.accept(this);
                Expression expr2 = op.getSecondOperand();
                expr2.setSymbolTable(op.getSymbolTable());
                expr2.accept(this);     
                return null;
        }
        
        public Object visit(UnaryOp op)
        {
                Expression exp = op.getOperand();
                exp.setSymbolTable(op.getSymbolTable());
                exp.accept(this);
                return null;
        }
        
        @Override
        public Object visit(MathBinaryOp op) 
        {
                return visit((BinaryOp)op);
        }

        @Override
        public Object visit(LogicalBinaryOp op) 
        {
                return visit((BinaryOp)op);
        }

        @Override
        public Object visit(MathUnaryOp op) 
        {
                return visit((UnaryOp)op);
        }

        @Override
        public Object visit(LogicalUnaryOp op) 
        {
                return visit((UnaryOp)op);
        }

        @Override
        public Object visit(Literal literal) {
                return null;
        }
}