package TypeTable;

public class VoidType extends Type {

        @Override
        public boolean SubType(Type type) {
                return (type == this);
        }
        
        @Override
        public String toString()
        {
                return "void";
        }
}