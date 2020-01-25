package edu.projet.interfaces;

import edu.projet.expressions.Expression;

/**
 * @author BAKHTAOUI Michel
 *
 */
public interface FormuleDerivation {
	
	/**
	 * @param expression
	 * @param dx
	 * @return la dérivée 'une expression.
	 */
	Expression deriver(Expression expression, String dx);
}
