package edu.projet.interfaces;

import edu.projet.expressions.Expression;

public interface FormuleDerivation {
	
	Expression deriver(Expression expression, String dx);
}
