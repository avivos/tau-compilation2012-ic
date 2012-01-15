package TypeTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import IC.SemanticError;
import IC.AST.*;

public class Table {
	
	// primitives
    public static Type booleanType = new BooleanType();
    public static Type intType = new IntType();
    public static Type stringType = new StringType();
    public static Type nullType = new NullType();
    public static Type voidType = new VoidType();
        
	//tables for the types
    private static Map<String,ClassType> uniqueClassTypes = new LinkedHashMap<String,ClassType>();
    private static Map<Type,ArrayType> uniqueArrayTypes = new LinkedHashMap<Type,ArrayType>();
    private static Map<String,MethodType> uniqueMethodTypes = new LinkedHashMap<String,MethodType>();
    private static Map<String,Type> uniquePrimitiveTypes = new LinkedHashMap<String,Type>();

    
    static 
    {
        /* put primitives in primitives list */
        uniquePrimitiveTypes.put("int", intType);
        uniquePrimitiveTypes.put("boolean", booleanType);
        uniquePrimitiveTypes.put("null", nullType);
        uniquePrimitiveTypes.put("string", stringType);
        uniquePrimitiveTypes.put("void", voidType);
    }
    
    public static String toString(String fileName)
    {
        StringBuffer str = new StringBuffer();
        str.append("Type Table: " + fileName + "\n");
        //print all primitives 
        for (Map.Entry<String, Type> entry : uniquePrimitiveTypes.entrySet())
                str.append("\t" + entry.getValue().index + ": Primitive type: " + entry.getValue() + "\n");
        //classes
        for (Map.Entry<String, ClassType> entry : uniqueClassTypes.entrySet())
        {
                str.append("\t" + entry.getValue().index + ": Class: " + entry.getValue());
                ClassType superType = entry.getValue().superType;
                if (superType != null)
                        str.append(", Superclass ID: " +superType.index);
                str.append("\n");
        }
        //arrays
        for (Map.Entry<Type, ArrayType> entry : uniqueArrayTypes.entrySet())
                str.append("\t" +entry.getValue().index + ": Array type: " + entry.getValue() + "\n");
        //methods
        for (Map.Entry<String, MethodType> entry : uniqueMethodTypes.entrySet())
                str.append("\t" +entry.getValue().index + ": Method type: " + entry.getValue() + "\n");
        
        return str.toString();
    }
    
    public static Type getType(IC.AST.Type nodeType) {
        
        Type type;
        String typeName = nodeType.getName();
        type = uniquePrimitiveTypes.get(typeName);
        if (type == null) {
                type = uniqueClassTypes.get(typeName);
        } 
        if (type == null) {
                return null;
        }
        
        if (nodeType.getDimension() > 0) {
                ArrayType arr = arrayType(type);
                int dim = nodeType.getDimension();
                while (dim>1) {
                        arr = arrayType(arr);
                        dim--;
                }
                return arr;
        } 
        else {
                return type;
        }
        
    }
    
     public static ArrayType arrayType(Type obj) {
        if (uniqueArrayTypes.containsKey(obj)) {
                return uniqueArrayTypes.get(obj);
        }
        
        else {
                ArrayType arrType = new ArrayType(obj);
                uniqueArrayTypes.put(obj,arrType);
                return arrType;
        }
    }
    
    public static ClassType addClassType(ICClass icClass) {
        ClassType  classType = uniqueClassTypes.get(icClass.getName());
        //if previously defined - return it
        if (classType != null){ 
                return classType;
        }
        //check if super was defined
        ClassType superClass = null;
        if (icClass.hasSuperClass()) {
                superClass = uniqueClassTypes.get(icClass.getSuperClassName());
                        
                if (superClass == null) {
                        throw new SemanticError("Super class wasn't defined", icClass);
                }
        }
        //new class type
        ClassType newClass = new ClassType(superClass, icClass);
        newClass.useLine = icClass.getLine();
        uniqueClassTypes.put(icClass.getName(), newClass); 
        return newClass;
    }
    
    public static ClassType addClassType(ICClass icClass, boolean init) {
    	//the class already exist
        if (uniqueClassTypes.containsKey(icClass.getName()))
        {
            ClassType classType = uniqueClassTypes.get(icClass.getName());
            
            //if this was previously defined
            if (classType.initFlag == true )
                 throw new SemanticError("Class was already defined", icClass);
            else 
            {
            	//now init for previously USED but not defined
               classType.initFlag = true;
               classType.node = icClass;
               return classType;
            }
        }
        else
        {
        	ClassType superClass = null;
        	if (icClass.hasSuperClass()) {
        		superClass = uniqueClassTypes.get(icClass.getSuperClassName());
        		if (superClass == null || superClass.initFlag == false) {
        			throw new SemanticError("Super class was never defined", icClass);
        		}
        	}

        	ClassType newClass = new ClassType(superClass, icClass,init);

        	uniqueClassTypes.put(icClass.getName(), newClass);

        	return newClass;
        }
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
        
        MethodType mt_predef = uniqueMethodTypes.get(key);
		//this code checks if the entry was already defined 
        if (mt_predef == null) {
                uniqueMethodTypes.put(key, mt);
                return mt;
        } else {
        	Type.counter--; //this reduces the type index, because it was predefined
        	return mt_predef;}
        
    }
    
    public static Map<String,ClassType> getClassList()
    {
        return   uniqueClassTypes; 
    }
}