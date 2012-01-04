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
                
                private String m_Desc;
                
                private SymbolKind(String desc)
                {
                        m_Desc = desc;
                }
                
                public String toString()
                {
                        return m_Desc;
                }
        }
        
        protected String m_Name;
        protected SymbolKind m_Kind;
        protected Type m_Type;
        protected ASTNode m_Node;
        
        public Symbol(String name, ASTNode node, SymbolKind kind, Type type)
        {
                m_Name = name;
                m_Kind = kind;
                m_Type = type;
                m_Node = node;
        }
        
        public String getName()
        {
                return m_Name;
        }
        
        public SymbolKind getKind()
        {
                return m_Kind;
        }
        
        public Type getType()
        {
                return m_Type;
        }
        
        public ASTNode getNode()
        {
                return m_Node;
        }
        
        @Override
        public String toString()
        {
                if (m_Kind == SymbolKind.Class)
                {
                        return String.format("%s: %s", m_Kind, m_Name);
                }
                
                return String.format("%s: %s %s", m_Kind, m_Type, m_Name);
        }
}