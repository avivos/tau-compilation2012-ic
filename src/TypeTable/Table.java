package TypeTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import IC.SemanticError;
import IC.AST.*;

public class Table {
        
    private static Map<String,ClassType> uniqueClassTypes = new LinkedHashMap<String,ClassType>();
    private static Map<Type,ArrayType> uniqueArrayTypes = new LinkedHashMap<Type,ArrayType>();
    
    
    private static Map<String,MethodType> uniqueMethodTypes = new LinkedHashMap<String,MethodType>();
    private static Map<String,Type> uniquePrimitiveTypes = new LinkedHashMap<String,Type>();

    /* these types are used to represent the IC primitives */
    public static Type boolType = new BoolType();
    public static Type intType = new IntType();
    public static Type stringType = new StringType();
    public static Type voidType = new VoidType();
    public static Type nullType = new NullType();

    
    static 
    {
        /* put primitives in primitives list */
        uniquePrimitiveTypes.put("int", intType);
        uniquePrimitiveTypes.put("boolean", boolType);
        uniquePrimitiveTypes.put("null", nullType);
        uniquePrimitiveTypes.put("string", stringType);
        uniquePrimitiveTypes.put("void", voidType);
    }
    
    public static String toString(String fileName)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Type Table: " + fileName + "\n");
        
        for (Map.Entry<String, Type> entry : uniquePrimitiveTypes.entrySet())
                builder.append("\t" + /*entry.getValue().hashCode()*/ entry.getValue().index + ": Primitive type: " + entry.getValue() + "\n");
        
        for (Map.Entry<String, ClassType> entry : uniqueClassTypes.entrySet())
        {
                builder.append("\t" + /*entry.getValue().hashCode()*/ entry.getValue().index + ": Class: " + entry.getValue());
                
                ClassType superType = entry.getValue().superType;
                if (superType != null)
                        builder.append(", Superclass ID: " + /*superType.hashCode() */ superType.index);
                
                builder.append("\n");
        }
        
        for (Map.Entry<Type, ArrayType> entry : uniqueArrayTypes.entrySet())
                builder.append("\t" + /*entry.getValue().hashCode()*/ entry.getValue().index + ": Array type: " + entry.getValue() + "\n");
        
        for (Map.Entry<String, MethodType> entry : uniqueMethodTypes.entrySet())
                builder.append("\t" + /*entry.getValue().hashCode()*/ entry.getValue().index + ": Method type: " + entry.getValue() + "\n");
        
        return builder.toString();
    }
    
    public static Type getType(IC.AST.Type nodeType) {
        
        Type t;
        String typeName = nodeType.getName();
        t = uniquePrimitiveTypes.get(typeName);
        if (t == null) {
                t = uniqueClassTypes.get(typeName);
        } 
        if (t == null) {
                //throw new SemanticException("Unrecognized type", nodeType);
                return null;
        }
        
        if (nodeType.getDimension() > 0) {
                
                ArrayType arr = arrayType(t);
                int i = nodeType.getDimension();
                while (i>1) {
                        arr = arrayType(arr);
                        --i;
                }
                return arr;
                 
                //return arrayType(t);
        } 
        else {
                return t;
        }
        
    }
    
     public static ArrayType arrayType(Type elemType) {
        if (uniqueArrayTypes.containsKey(elemType)) {
                // array type object already created return it
                return uniqueArrayTypes.get(elemType);
        }
        
        else {
                // object doesnt exist create and return it
                ArrayType arrt = new ArrayType(elemType);
                uniqueArrayTypes.put(elemType,arrt);
                return arrt;
        }
    }
    
    
    public static ClassType addClassType(ICClass icClass) {
        ClassType  ct = uniqueClassTypes.get(icClass.getName());
        if (ct != null){ 
               //throw new SemanticException("Class was already defined", icClass);
                return ct;
        }
        
        ClassType superClass = null;
        if (icClass.hasSuperClass()) {
                        superClass = uniqueClassTypes.get(icClass.getSuperClassName());
                        
                if (superClass == null) {
                        throw new SemanticError("Super class was never defined", icClass);
                }
        }
        
        ClassType newClass = new ClassType(superClass, icClass);
        newClass._lineOfError = icClass.getLine();
        uniqueClassTypes.put(icClass.getName(), newClass);
        
        return newClass;
    }
    
    public static ClassType addClassType(ICClass icClass, boolean initialize) {
        if (uniqueClassTypes.containsKey(icClass.getName()))
        {
                 ClassType ct = uniqueClassTypes.get(icClass.getName());
                 if (ct._initialize == true )
                {
                        throw new SemanticError("Class was already defined", icClass);
                }
                else 
                {
               ct._initialize = true;
               ct.node = icClass;
               return ct;
                }
        }

        ClassType superClass = null;
        if (icClass.hasSuperClass()) {
                        superClass = uniqueClassTypes.get(icClass.getSuperClassName());
                        
                if (superClass == null || superClass._initialize == false) {
                        throw new SemanticError("Super class was never defined", icClass);
                }
        }
        
        ClassType newClass = new ClassType(superClass, icClass,initialize);
        uniqueClassTypes.put(icClass.getName(), newClass);
        
        return newClass;
    }

  
    public static ClassType getClassType(String name, ASTNode parentNode) {
        ClassType c = uniqueClassTypes.get(name);
        
        if (c == null) {
                throw new SemanticError("Class was never defined", parentNode);
        }
        
        else return c;
    }
    
    public static MethodType methodType(Method nodeMethod)
    {
        Type returnType = getType(nodeMethod.getType());
        
        ArrayList<Type> params = new ArrayList<Type>();
        
        for (Formal formal : nodeMethod.getFormals())
        {
                params.add(getType(formal.getType()));
        }
        
        return methodType(returnType, params);
    }
    
    public static MethodType methodType(Type returnType, List<Type> paramTypes){
        MethodType mt = new MethodType(returnType,paramTypes);
        String key = mt.toString();
        
        MethodType mt2 = uniqueMethodTypes.get(key);
        if (mt2 == null) {
                uniqueMethodTypes.put(key, mt);
                return mt;
        } else return mt2;
        
    }
    
    public static Map<String,ClassType> getClassList()
    {
        return   uniqueClassTypes; 
    }
}