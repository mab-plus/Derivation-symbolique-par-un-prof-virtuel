package edu.projet.interfaces;

import java.util.HashMap;

/**
 * @author BAKHTAOUI Michel
 */
public interface Formule {
	
	/**
	 * @return l'expression, sous la forme d'une chaîne de caractère.
	 */
	public String asString();
	
	/**
	 * @param liste
	 * @return l'évaluation d'ube expression à partir d'une collection liste de (variables, valeurs)
	 */
	public double evaluer(HashMap<String,Double> liste);
	
	/**
	 * @param visitor
	 * @return méthode accept(@see SimplificationVisitor<R> visitor) pour accepter un visiteur visitor
	 *
	 */
	public <R> R accept(SimplificationVisitor<R> visitor);
	
	/**
	 * @param visitor
	 * @param dx
	 * @return méthode accept(@see DerivationVisitor<R> visitor, String dx) pour accepter un visiteur visitor
	 *
	 */
	public <R> R accept(DerivationVisitor<R> visitor, String dx);
}
