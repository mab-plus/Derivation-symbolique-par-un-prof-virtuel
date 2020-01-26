package edu.projet.interfaces;

import java.util.HashMap;

public interface Formule {
	
	public String asString();
	public double evaluer(HashMap<String,Double> liste);
	public <R> R accept(SimplificationVisitor<R> visitor);
	public <R> R accept(DerivationVisitor<R> visitor, String dx);
}
