package TypeTable;

import java.util.List;

public class MethodType extends Type {

        
        Type retType;
        List<Type> argsType;
        
        public MethodType(Type retType, List<Type> argsType) {
                this.retType = retType;
                this.argsType = argsType;
        }
        
        @Override
        public boolean SubType(Type type) {
                return (this==type);
        }
        
        
        public Type getReturnType() {
                return retType;
        }
        
        public List<Type> getArgTypes()
        {
                return argsType;
        }
        
        @Override 
        public String toString()
        {
                StringBuilder builder = new StringBuilder();
                builder.append("{ ");
                
                for (int i = 0; i < argsType.size(); i++)
                {
                        if (argsType.size() == 1 && 
                                argsType.get(i) instanceof VoidType)
                        {
                                break;
                        }
                        
                        builder.append(argsType.get(i));
                        if (i != argsType.size() - 1)
                                builder.append(", ");
                }
                
                builder.append(" -> " + retType);
                builder.append(" }");
                
                return builder.toString();
        }
}