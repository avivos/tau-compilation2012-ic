package Lir;

import IC.AST.Expression;

public class LirExpression {

	int Register;
	String translation = null;

	public int getRegister(){
		return this.Register;
	}

	public String gerTranslation(){
		return this.translation;
	}

	public void setTranslation(String translation){
		this.translation = translation;
	}
	
	public void setRegister(int reg){
		this.Register = reg;
	}
}
