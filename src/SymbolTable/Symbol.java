package SymbolTable;

import IC.AST.ASTNode;
import TypeTable.Type;

public class Symbol 
{
        public enum SymbolKind
        {
                Program("Program"),
                Class("Class"),
                Method("Method"),
                Variable("Local variable"),
                Parameter("Parameter"),
                Field("Field"),
                ReturnType("Return type");
                
                private String sType;
                
                private SymbolKind(String type)
                {
                        sType = type;
                }
                
                public String toString()
                {
                        return sType;
                }
        }
        
        protected String symName;
        protected SymbolKind symKind;
        protected Type symType;
        protected ASTNode symNode;
        
        public Symbol(String name, ASTNode node, SymbolKind kind, Type type)
        {
                symName = name;
                symKind = kind;
                symType = type;
                symNode = node;
        }
        
        public String getName()
        {
                return symName;
        }
        
        public SymbolKind getKind()
        {
                return symKind;
        }
        
        public Type getType()
        {
                return symType;
        }
        
        public ASTNode getNode()
        {
                return symNode;
        }
        
        @Override
        public String toString()
        {
                if (symKind == SymbolKind.Class)
                {
                        //return String.format("%s: %s", symKind, symName); 
                	return symKind + ": " + symName; 
                        		
                }
                
                //return String.format("%s: %s %s", symKind, symType, symName);
                return symKind+": "+symType+" "+symName;
        }
}