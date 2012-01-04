package TypeTable;

public class ArrayType extends Type {
        Type elemType;
        
        ArrayType(Type elemType) {
                this.elemType = elemType;
        }

        public Type getElemType() {
                return elemType;
        }
        
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
                return elemType + "[]";
        }
}
