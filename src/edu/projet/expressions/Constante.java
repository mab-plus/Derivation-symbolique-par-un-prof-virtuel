package edu.projet.expressions;

import java.util.HashMap;

import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

public class Constante extends Expression {
	
	private String constante;
	private double valeur;

	public Constante ( double valeur ) {
		super(null, Double.toString(valeur), null);
		this.constante = Double.toString(valeur);
		this.valeur = valeur;
	}

	@Override
	public String asString()  {
		// pour supprimer le point si entier à afficher
		double d = Double.parseDouble(this.constante);
		
		if ( (double) (int) d == d ) {
			
			if (d < 0)
				return "(" + Integer.toString((int) d ) + ")";
			else
				return Integer.toString((int) d );
		}

		else
			return this.constante;
	}
	
	public double getValeur() {
		return this.valeur;
	}

	@Override
	public double evaluer(HashMap<String, Double> liste) {
		return this.valeur; 
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
