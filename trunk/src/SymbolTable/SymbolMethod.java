package SymbolTable;

import IC.AST.ASTNode;
import TypeTable.Type;


public class SymbolMethod extends Symbol 
{
        private MethodKind m_MethodKind;
        
        public enum MethodKind
        {
                Virtual("Virtual"),
                Static("Static"),
                Library("Static");
                
                private String m_Desc;
                
                private MethodKind(String desc)
                {
                        m_Desc = desc;
                }
                
                public String toString()
                {
                        return m_Desc;
                }
        }
        
        public SymbolMethod(String name, ASTNode node, SymbolKind kind, Type type, MethodKind methodKind) 
        {
                super(name, node, kind, type);
                
                m_MethodKind = methodKind;
        }
        
        public MethodKind getMethodKind()
        {
                return m_MethodKind;
        }
        
        @Override
        public String toString()
        {
                return String.format("%s method: %s %s",
                                                         m_MethodKind,
                                                         m_Name,
                                                         m_Type);
        }
}