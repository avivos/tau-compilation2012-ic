package SymbolTable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import IC.AST.ASTNode;
import SymbolTable.Symbol.SymbolKind;

public class SymbolTable 
{
        public enum TableKind
        {
                Global("Global"),
                Class("Class"),
                Method("Method"),
                Block("Statement Block");
                
                private String tType;
                
                private TableKind(String type)
                {
                        tType = type;
                }
                
                public String toString()
                {
                        return tType;
                }
        }
        
        private Map<String, Symbol> entries;
        private String id;
        private SymbolTable parentSymbolTable;
        private Set<SymbolTable> children;
        private TableKind kind;
        
        public SymbolTable(String id, TableKind kind)
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
        
        public TableKind getKind()
        {
                return kind;
        }
        
        public SymbolTable getParent()
        {
                return parentSymbolTable;
        }
        
        public void setParent(SymbolTable symbolTable)
        {
                symbolTable.addChild(this);

                parentSymbolTable = symbolTable;
        }
        
        public void addChild(SymbolTable symbolTable)
        {
                children.add(symbolTable);
        }
        
        public void insert(Symbol sym)
        {
                entries.put(sym.getName(), sym);
        }
        
        public Symbol lookupScope(String name)
        {
                return entries.get(name);
        }
        
        public Symbol lookup(String name, ASTNode node)
        {
                Symbol sym = lookupScope(name);
                if (sym != null){
                	if (node.getLine() >= sym.getNode().getLine())
                        return sym;
                	if (sym.getKind() == SymbolKind.Method)
                        return sym;
                	if (sym.getKind() == SymbolKind.Field)
                        return sym;
                }
                if (this.parentSymbolTable != null)
                        return this.parentSymbolTable.lookup(name, node);
                
                return null;
        }
        

        
        public SymbolTable getClassSymbolTable()
        {
                if (this.kind == TableKind.Class)
                        return this;
                if (this.parentSymbolTable != null)
                        return this.parentSymbolTable.getClassSymbolTable();
                
                return null;
        }
        
        @Override
        public String toString()
        {
        		StringBuffer str = new StringBuffer();
                
                if (this.kind == TableKind.Block)
                {
                        str.append(this.kind+" Symbol Table ( located in ");
                        
                        SymbolTable parent = getParent();
                        while (parent.getKind() == TableKind.Block)
                        {
                                str.append("statement block in ");
                                parent = parent.getParent();
                        }
                        str.append(parent.getId() + " )");
                }
                else
                        str.append(this.kind + " Symbol Table: " + this.id);
                
                
                for (Map.Entry<String, Symbol> entry : entries.entrySet())
                {
                        Symbol sym = entry.getValue();
                        
                        if (sym.getKind() != SymbolKind.ReturnType)
                                str.append("\n\t" + sym);
                }
                
                boolean childrenflag = false;
                int i = 0;
                
                for (SymbolTable childTable : this.children)
                {
                        if (!childrenflag)
                        {
                                str.append("\nChildren tables: ");
                                childrenflag = true;
                        }
                        
                        if (childTable.getKind() == TableKind.Block)
                        {
                                SymbolTable cur = childTable;
                                while (cur.getKind() == TableKind.Block)
                                {
                                        str.append("statement block in ");
                                        cur = cur.getParent();
                                }
                                
                                str.append(cur.getId());
                        } 
                        else
                        {
                                str.append(childTable.getId());
                        }
                        
                        if (i < children.size() - 1)
                                str.append(", ");
                        
                        i++;
                }
                
                for (SymbolTable childTable : children)
                {
                        str.append("\n\n" + childTable);
                }
                
                return str.toString();
        }
}