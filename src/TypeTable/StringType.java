package TypeTable;

public class StringType extends Type {

        @Override
        public boolean SubType(Type type) {
                return (type == this);
        }

        @Override
        public String toString()
        {
                return "string";
        }
}