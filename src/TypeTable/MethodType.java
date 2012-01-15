package TypeTable;

import java.util.List;

public class MethodType extends Type {

		List<Type> args;        
        Type returnValue;

        
        public MethodType(Type returnVal, List<Type> args) {
                this.returnValue = returnVal;
                this.args = args;
        }

        public Type getRetType() {
                return returnValue;
        }
        
        public List<Type> getArgTypes()
        {
                return args;
        }
        
        @Override
        public boolean SubType(Type type) {
                return (this==type);
        }
        @Override 
        public String toString()
        {
                StringBuffer str = new StringBuffer();
                str.append("{");
                for (int i = 0; i < this.args.size(); i++)
                {
                    str.append(this.args.get(i));
                    if (i != this.args.size()-1)
                       str.append(", ");
                }
                str.append(" -> " + this.returnValue);
                str.append("}");
                return str.toString();
        }
}