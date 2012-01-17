package TypeTable;

public class StringType extends Type {

        @Override
        public boolean SubType(Type type) {
            if (this == type) 
                return true;
            
            if (type instanceof NullType)
            	return true;
            
            return false;
        }

        @Override
        public String toString()
        {
                return "string";
        }
}