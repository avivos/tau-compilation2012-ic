package TypeTable;

import IC.AST.ASTNode;
import IC.AST.ICClass;
import SymbolTable.SymbolTable;

public class ClassType extends Type {

    	public ASTNode node;
        public boolean initFlag = false;
        public ClassType superType;
        public int useLine = -1; //saves the line number in case CLASS was never defined
        
        public ClassType(ClassType superT, ASTNode node) {
                this.superType = superT;
                this.node = node;
        }
        public ClassType(ClassType superT, ASTNode node,boolean init) {
            	this.initFlag = init;
        		this.superType = superT;
                this.node = node;
        }
        
  
        public SymbolTable getSymbolTable() {
                return this.node.getSymbolTable();
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
      
        
        @Override 
        public String toString()
        {
                if (node instanceof ICClass)
                        return ((ICClass)this.node).getName();
                
                return super.toString();
        }

}