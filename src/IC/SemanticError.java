package IC;

import IC.AST.ASTNode;

public class SemanticError extends RuntimeException {


        
        private ASTNode m_Node;
        
        public SemanticError(String message, ASTNode node)
        {
                super(message);
                
                m_Node = node;
        }

        @Override
        public String toString()
        {
                return String.format("semantic error at line %d: %s\n", m_Node.getLine(), getMessage());
        }
}
