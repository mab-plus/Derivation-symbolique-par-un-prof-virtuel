package edu.projet.fonctions;

import java.util.HashMap;

import edu.projet.expressions.Expression;
import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

public class Sin  extends Expression {

	public Sin(Expression argument) {
		super(null, "sin", argument);
	}

	@Override
	public double evaluer(HashMap<String, Double> liste) {
		return Math.log(this.exprD.evaluer(liste));
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
