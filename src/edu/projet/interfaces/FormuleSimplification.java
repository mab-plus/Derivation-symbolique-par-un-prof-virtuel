package edu.projet.interfaces;

import edu.projet.expressions.Expression;

/**
 * @author BAKHTAOUI Michel
 *
 */
public interface FormuleSimplification {
	
	/**
	 * @param expression
	 * @return simplification d'une expression.
	 */
	Expression simplifier(Expression expression);
}
