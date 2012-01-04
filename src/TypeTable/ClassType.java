package TypeTable;

import IC.AST.ASTNode;
import IC.AST.ICClass;
import SymbolTable.SymbolTable;

public class ClassType extends Type {

        public ClassType superType;
        //public SymbolTable table;
        public ASTNode node;
        public boolean _initialize = false;
        public int _lineOfError = -1;
        
        public ClassType(ClassType superType, ASTNode node) {
                this.superType = superType;
                this.node = node;
        }
        public ClassType(ClassType superType, ASTNode node,boolean initalize) {
                this.superType = superType;
                this.node = node;
                _initialize = initalize;
        }
        
        @Override
        public boolean SubType(Type type) {
                if (this == type) {
                        return true;
                }
                if (this.superType == null) {
                        return false;
                }
                return this.superType.SubType(type);
        }
        
        public SymbolTable getSymbolTable() {
                return node.getSymbolTable();
        }
        
        @Override 
        public String toString()
        {
                if (node instanceof ICClass)
                        return ((ICClass)node).getName();
                
                return super.toString();
        }

}