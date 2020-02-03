package edu.projet.expressions;

import java.util.HashMap;

import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

/**
 * Moins @see Expression
 * 
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Moins extends Multiplication {

	/**
	 * constructeur
	 * @param expr
	 */
	public Moins(Expression expr) {
		super(new Constante(-1), expr);
	}
	
	@Override
	public String asString() {
		return "(-" + this.exprD.asString() + ")";
	}
	
	@Override
	public double evaluer(HashMap<String, Double> liste) {
		return  this.exprD.evaluer(liste) * -1;
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
