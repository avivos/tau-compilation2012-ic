package SymbolTable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import IC.AST.ASTNode;
import SymbolTable.Symbol.SymbolKind;

public class SymbolTable 
{
        public enum SymbolTableKind
        {
                Global("Global"),
                Class("Class"),
                Method("Method"),
                Block("Statement Block");
                
                private String m_Desc;
                
                private SymbolTableKind(String desc)
                {
                        m_Desc = desc;
                }
                
                public String toString()
                {
                        return m_Desc;
                }
        }
        
        private Map<String, Symbol> entries;
        private String id;
        private SymbolTableKind kind;
        private SymbolTable parent;
        private Set<SymbolTable> children;
        
        public SymbolTable(String id, SymbolTableKind kind)
        {
                this.id = id;
                this.kind = kind;
                
                entries = new LinkedHashMap<String, Symbol>();
                children = new LinkedHashSet<SymbolTable>();
        }
        
        public Map<String, Symbol> getEntries()
        {
                return entries;
        }
        
        public String getId()
        {
                return id;
        }
        
        public SymbolTableKind getKind()
        {
                return kind;
        }
        
        public SymbolTable getParent()
        {
                return parent;
        }
        
        public void setParent(SymbolTable symbolTable)
        {
                symbolTable.addChild(this);

                parent = symbolTable;
        }
        
        public void addChild(SymbolTable symbolTable)
        {
                children.add(symbolTable);
        }
        
        public void insert(Symbol sym)
        {
                entries.put(sym.getName(), sym);
        }
        
        public Symbol lookup(String name, ASTNode currentNode)
        {
                Symbol symbol = lookupLocal(name);
                
                if (symbol != null && currentNode.getLine() >= symbol.getNode().getLine())
                        return symbol;
                else if ( symbol != null && symbol.getKind() == SymbolKind.Method)
                        return symbol;
                else if (parent != null)
                        return parent.lookup(name, currentNode);
                else
                        return null;
        }
        
        public Symbol lookupLocal(String name)
        {
                return entries.get(name);
        }
        
        public SymbolTable getClassSymbolTable()
        {
                if (kind == SymbolTableKind.Class)
                        return this;
                else if (parent != null)
                        return parent.getClassSymbolTable();
                else
                        return null;
        }
        
        @Override
        public String toString()
        {
                StringBuilder builder = new StringBuilder();
                
                if (kind == SymbolTableKind.Block)
                {
                        builder.append(kind + " Symbol Table ( located in ");
                        
                        SymbolTable cur = getParent();
                        while (cur.getKind() == SymbolTableKind.Block)
                        {
                                builder.append("statement block in ");
                                cur = cur.getParent();
                        }
                        
                        builder.append(cur.getId() + " )");
                }
                else
                {
                        builder.append(kind + " Symbol Table: " + id);
                }
                
                for (Map.Entry<String, Symbol> entry : entries.entrySet())
                {
                        Symbol sym = entry.getValue();
                        
                        if (sym.getKind() != SymbolKind.ReturnType)
                                builder.append("\n\t" + sym);
                }
                
                boolean childrenPrinted = false;
                int i = 0;
                
                for (SymbolTable childTable : children)
                {
                        if (!childrenPrinted)
                        {
                                builder.append("\nChildren tables: ");
                                childrenPrinted = true;
                        }
                        
                        if (childTable.getKind() == SymbolTableKind.Block)
                        {
                                SymbolTable cur = childTable;
                                while (cur.getKind() == SymbolTableKind.Block)
                                {
                                        builder.append("statement block in");
                                        cur = cur.getParent();
                                }
                                
                                builder.append(cur.getId());
                        } 
                        else
                        {
                                builder.append(childTable.getId());
                        }
                        
                        if (i < children.size() - 1)
                                builder.append(", ");
                        
                        i++;
                }
                
                for (SymbolTable childTable : children)
                {
                        builder.append("\n\n" + childTable);
                }
                
                return builder.toString();
        }
}