package SymbolTable;

import IC.AST.ASTNode;
import TypeTable.Type;


public class SymbolMethod extends Symbol 
{
        private MethodKind methodKind;
        
        public enum MethodKind
        {
                Virtual("Virtual"),
                Static("Static"),
                Library("Static");
                
                private String mType;
                
                private MethodKind(String type)
                {
                        mType = type;
                }
                
                public String toString()
                {
                        return mType;
                }
        }
        
        public SymbolMethod(String name, ASTNode node, SymbolKind kind, Type type, MethodKind mKind) 
        {
                super(name, node, kind, type);
                
                methodKind = mKind;
        }
        
        public MethodKind getMethodKind()
        {
                return methodKind;
        }
        
        @Override
        public String toString()
        {
               // return String.format("%s method: %s %s",
               //                                          methodKind,
               //                                          symName,
               //                                          symType);
        	return methodKind+" method: "+symName+" "+symType;
        }
}