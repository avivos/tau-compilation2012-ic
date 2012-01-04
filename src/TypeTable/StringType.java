package TypeTable;

public class StringType extends Type {

        @Override
        public boolean SubType(Type type) {
                // TODO Auto-generated method stub
                return (type == this);
        }

        @Override
        public String toString()
        {
                return "string";
        }
}