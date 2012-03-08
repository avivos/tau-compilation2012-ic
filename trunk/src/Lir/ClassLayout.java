package Lir;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.AST.*;

public class ClassLayout {
	
	//offset handling
	
	String className = null;

	Map<Method, Integer> methodToOffset = new HashMap<Method, Integer>();
	Map<Integer, Method> offsetToMethod = new HashMap<Integer, Method>();
	Map<Method, String> methodToClassName = new HashMap<Method, String>();
	Map<String, Method> methodNameToNode = new HashMap<String, Method>();

	Map<Field, Integer> fieldToOffset = new HashMap<Field, Integer>();
	Map<String, Field> fieldNameToNode = new HashMap<String, Field>();


	public ClassLayout(List<Method> methodList, List<Field> fieldList, ClassLayout superLayout) {

		if (superLayout != null){
			// run over the super class' layout and add what it has first
			// Fields 
			Map<Field, Integer> superFieldToOffset = superLayout.getFieldOffMap();
			
			for (Field field : superFieldToOffset.keySet()){
				this.fieldToOffset.put(field, new Integer(superFieldToOffset.get(field)));
				fieldNameToNode.put(field.getName(), field);
			}
			// Methods
			Map<Method, Integer> superMehtodToOffset = superLayout.getMethodOffMap();
			Map<String, Method> methodNameToNode = new HashMap<String, Method>();
			for (Method method : superMehtodToOffset.keySet()){
				this.methodToOffset.put(method, new Integer(superMehtodToOffset.get(method)));
				this.offsetToMethod.put(new Integer(superMehtodToOffset.get(method)), method);
				this.methodNameToNode.put(method.getName(), method);
			}

			// hidden fields is not in the spec.
			int i = fieldToOffset.size()+1;
			for (Field field : fieldList){
				fieldToOffset.put(field, new Integer(i));
				fieldNameToNode.put(field.getName(), field);
				i++;
			}

			i = methodToOffset.size();
			for (Method method : methodList){
				Method superMethod = methodNameToNode.get(method.getName());
				if (superMethod != null){
					int index = methodToOffset.get(superMethod);
					methodToOffset.remove(superMethod);
					methodToOffset.put(method, new Integer(index));
					offsetToMethod.put(new Integer(index), method);
					this.methodNameToNode.put(method.getName(), method);
				}
				else {
					methodToOffset.put(method, new Integer(i));
					offsetToMethod.put(new Integer(i), method);
					this.methodNameToNode.put(method.getName(), method);
					i++;
				}
			}
		}
		else {
			int i = 1;
			// map fields to offsets
			for (Field field : fieldList){
				fieldNameToNode.put(field.getName(), field);
				fieldToOffset.put(field, new Integer(i));
				i++;
			}
			// map methods to offsets
			i = 0;
			for (Method method : methodList){
				this.methodNameToNode.put(method.getName(), method);
				this.methodToOffset.put(method, new Integer(i));
				this.offsetToMethod.put(new Integer(i), method);
				i++;
			}
		}
	}


	public Map<Method, Integer> getMethodOffMap() {
		return this.methodToOffset;
	}


	public Map<Field, Integer> getFieldOffMap() {
		return this.fieldToOffset;
	}

	public Map<String, Field> getFieldNameToNode() {
		return this.fieldNameToNode;
	}
	
	public Map<String, Method> getmethodNameToNode() {
		return this.methodNameToNode;
	}

}
