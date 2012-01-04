package TypeTable;

public class BoolType extends Type {

        @Override
        public boolean SubType(Type type) {
                if (this == type) {
                        return true;
                } 
                else {
                        return false;
                }
        }

        @Override 
        public String toString()
        {
                return "boolean";
        }
}
