package edu.projet.fonctions;

import java.util.HashMap;

import edu.projet.expressions.Expression;
import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

/**
 * Cos @see Expression
 * 
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Cotanh  extends Expression {

	/**
	 * constructeur
	 * @param argument
	 */
	public Cotanh(Expression argument) {
		super(null, "coth", argument);
	}

	@Override
	public double evaluer(HashMap<String, Double> liste) {
		return 1 / Math.tanh(this.exprD.evaluer(liste));
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
