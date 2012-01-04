package TypeTable;

public class NullType extends Type {

        @Override
        public boolean SubType(Type type) {
                return (type == this || 
                                type instanceof ClassType || 
                                type instanceof ArrayType || 
                                type instanceof StringType);
        }
        
        @Override
        public String toString()
        {
                return "null";
        }
}