package TypeTable;

public class ArrayType extends Type {
        Type objectT;
        
        ArrayType(Type obj) {
                this.objectT = obj;
        }

        public Type getObjType() {
                return objectT;
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
                return objectT + "[]";
        }
}
