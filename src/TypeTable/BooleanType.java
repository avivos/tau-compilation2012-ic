package TypeTable;

public class BooleanType extends Type {

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
