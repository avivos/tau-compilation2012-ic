package Lir;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.AST.*;

public class ClassLayout {

	Map<Method, Integer> methodToOffset = new HashMap<Method, Integer>();

	Map<Field, Integer> fieldToOffset = new HashMap<Field, Integer>();


	public ClassLayout(List<Method> methodList, List<Field> fieldList, ClassLayout superLayout) {

		if (superLayout != null){
			// run over the super class' layout and add what it has first
			// Fields 
			Map<Field, Integer> superFieldToOffset = superLayout.getFieldOffMap();
			Map<String, Field> fieldNameToNode = new HashMap<String, Field>();
			for (Field field : superFieldToOffset.keySet()){
				this.fieldToOffset.put(field, superFieldToOffset.get(field));
				fieldNameToNode.put(field.getName(), field);
			}
			// Methods
			Map<Method, Integer> superMehtodToOffset = superLayout.getMethodOffMap();
			Map<String, Method> methodNameToNode = new HashMap<String, Method>();
			for (Method method : superMehtodToOffset.keySet()){
				this.methodToOffset.put(method, superFieldToOffset.get(method));
				methodNameToNode.put(method.getName(), method);
			}

			int i = fieldToOffset.size()+1;
			for (Field field : fieldList){
				Field superField = fieldNameToNode.get(field.getName());
				if (superField != null){
					int index = fieldToOffset.get(superField);
					fieldToOffset.remove(superField);
					fieldToOffset.put(field, index);
				}

			}

			i = methodToOffset.size()+1;
			for (Method method : methodList){
				Method superMethod = methodNameToNode.get(method.getName());
				if (superMethod != null){
					int index = methodToOffset.get(superMethod);
					methodToOffset.remove(superMethod);
					methodToOffset.put(method, index);
				}

			}
		}
		else {
			int i = 0;
			// map fields to offsets
			for (Field field : fieldList){
				fieldToOffset.put(field, i);
				i++;
			}
			// map methods to offsets
			i = 0;
			for (Method method : methodList){
				methodToOffset.put(method, i);
				i++;
			}
		}
	}


	private Map<Method, Integer> getMethodOffMap() {
		return this.methodToOffset;
	}


	private Map<Field, Integer> getFieldOffMap() {
		return this.fieldToOffset;
	}


	

}
