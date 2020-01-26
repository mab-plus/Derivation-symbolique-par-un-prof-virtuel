package edu.projet.expressions;

import java.util.HashMap;

import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

/**
 * Puissance @see Expression
 * 
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Puissance extends Expression {
	    
	/**
	 * constructeur
	 * @param exprG
	 * @param exprD
	 */
	public Puissance(Expression exprG, Expression exprD) {
		super(exprG, "^", exprD);
	}
    
    /**
     * constructeur
     * @param exprG
     * @param exposant
     */
    public Puissance(Expression exprG, int exposant) {
		super(exprG, "^", new Constante(exposant));
	}
    
	@Override
	public String asString() {
		
		if (isZero(this.exprD))		
			return "1";
		
		if (isUn(this.exprD))
			return this.exprG.asString();

		if (isMoinsUn(this.exprD))
			return "1/" + this.exprG.asString();
		
		if (isConstante(this.exprD) && ((Constante) this.exprD).getValeur() < 0) {
			
			Constante exp = new Constante(-1 * ((Constante) this.exprD).getValeur());
			//return "(1/" + this.exprG.asString() + this.getSymbole() + "[" + exp.asString() + "])";
			return "1/" + this.exprG.asString() + exposant(exp.asString()) ;
		}
			
		//expression = this.exprG.asString() + this.getSymbole() + "[" + this.exprD.asString() + "]";
		return this.exprG.asString() + exposant( this.exprD.asString() ) ;
		
	}
	
	private String exposant(String e) {
		//encodage exposant unicode ⁰, ¹, ² ...
		char[] exposant = {'\u2070', '\u00b9', '\u00b2', '\u00b3', '\u2074', '\u2075', '\u2076', '\u2077', '\u2078', '\u2079'};
		char[] c = e.toCharArray();
		
		for(int i =0; i < c.length; i++) {
			e = e.replace(c[i], exposant[Character.getNumericValue(c[i])]);
		}
		return e;
	}


	
	@Override
	public double evaluer(HashMap<String, Double> liste) {
		return  Math.pow(this.exprG.evaluer(liste), this.exprD.evaluer(liste));
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
