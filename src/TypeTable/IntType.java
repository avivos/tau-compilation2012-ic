package TypeTable;

public class IntType extends Type {

        @Override
        public boolean SubType(Type type) {
                return (type == this);
        }
        
        @Override 
        public String toString()
        {
                return "int";
        }
}