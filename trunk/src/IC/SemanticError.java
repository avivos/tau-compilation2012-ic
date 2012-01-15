package IC;

import IC.AST.ASTNode;

public class SemanticError extends RuntimeException {


        
        private ASTNode node;
        
        public SemanticError(String message, ASTNode n)
        {
                super(message);
                
                node = n;
        }

        @Override
        public String toString()
        {
                return String.format("semantic error at line %d: %s\n", node.getLine(), getMessage());
        }
}
