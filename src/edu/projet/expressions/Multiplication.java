package edu.projet.expressions;

import java.util.HashMap;

import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

public class Multiplication extends Expression {

    public Multiplication(Expression exprG, Expression exprD) {
		super(exprG, "*", exprD);
	}
     
	@Override
	public String asString() {	
		
		if (isZero(this.exprD))
			return "0";
		
		if (isZero(this.exprG))
			return "0";
		
		if (isUn(this.exprD))
			return this.exprG.asString();
		
		if (isUn(this.exprG))
			return this.exprD.asString();
	
		if (isMoinsUn(this.exprD))
			return "-" + this.exprG.asString();	
		
		if (isMoinsUn(this.exprG))
			return "-" + this.exprD.asString();	
		
		String mult = this.exprG.asString() + "*" + this.exprD.asString();
		mult = mult.replaceAll("\\*1\\/", "/");
		return mult;
	}
	
	@Override
	public double evaluer(HashMap<String, Double> liste) {
		return  this.exprG.evaluer(liste) * this.exprD.evaluer(liste);
	}
	
	@Override
	public <R> R accept(SimplificationVisitor<R> visitor) {
		try{
			return (visitor).visit(this);
		} 
		catch(ClassCastException exception) {
			return null;
		}
	}
	
	@Override
	public <R> R accept(DerivationVisitor<R> visitor, String dx) {
		try{
			return (visitor).visit(this, dx);
		} 
		catch(ClassCastException exception) {
			return null;
		}
	}	

}
