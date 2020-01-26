package edu.projet.expressions;

import java.util.HashMap;

import edu.projet.interfaces.DerivationVisitor;
import edu.projet.interfaces.SimplificationVisitor;

/**
 * Constante @see Expression
 * 
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Constante extends Expression {
	
	/**
	 * Expression de la valeur de la constante en chaîne de caractère
	 */
	private String constante;
	/**
	 * Valeur de la constante
	 */
	private double valeur;


	/**
	 * constructeur
	 * @param valeur
	 */
	public Constante ( double valeur ) {
		super(null, Double.toString(valeur), null);
		this.constante = Double.toString(valeur);
		this.valeur = valeur;
	}
	
    /**
     * @return la constante, sous la forme d'une chaîne de caractère
     * en supprimant le point si c'est une entier à afficher
     */
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
	
	/**
	 * @return valeur de la constante
	 */
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
