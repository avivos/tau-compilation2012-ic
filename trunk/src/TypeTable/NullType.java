package TypeTable;

public class NullType extends Type {

        @Override
        public boolean SubType(Type type) {
                return (type == this ||  type instanceof StringType ||  type instanceof ClassType ||  type instanceof ArrayType);
        }
        
        @Override
        public String toString()
        {
                return "null";
        }
}