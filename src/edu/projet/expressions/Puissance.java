package edu.projet.expressions;

import java.util.HashMap;

import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

public class Puissance extends Expression {
	    
	public Puissance(Expression exprG, Expression exprD) {
		super(exprG, "^", exprD);
	}
    
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
			return "(1/" + this.exprG.asString() + this.getSymbole() + "[" + exp.asString() + "])";
		}
		
		return this.exprG.asString() + this.getSymbole() + "[" + this.exprD.asString() + "]";
		
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
