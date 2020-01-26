package edu.projet.expressions;

import java.util.HashMap;

import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

public class Variable extends Expression {
	
	private String variable;

	public Variable ( String variable) {
		super(null, variable, null);
		this.variable = variable;
	}
	
	@Override
	public String asString() {
		return this.variable;
	}

	@Override
	public double evaluer(HashMap<String, Double> liste) {
		return  liste.get(this.variable);
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
