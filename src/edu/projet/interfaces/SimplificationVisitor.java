package edu.projet.interfaces;

import edu.projet.expressions.Addition;
import edu.projet.expressions.Constante;
import edu.projet.expressions.Moins;
import edu.projet.expressions.Multiplication;
import edu.projet.expressions.Puissance;
import edu.projet.expressions.Variable;
import edu.projet.fonctions.Cos;
import edu.projet.fonctions.Cosh;
import edu.projet.fonctions.Cotan;
import edu.projet.fonctions.Cotanh;
import edu.projet.fonctions.Exp;
import edu.projet.fonctions.Log;
import edu.projet.fonctions.Sin;
import edu.projet.fonctions.Sinh;
import edu.projet.fonctions.Tan;
import edu.projet.fonctions.Tanh;

/**
 * @author BAKHTAOUI Michel
 *
 * @param <R> type de retour des méthodes de ce visiteur.
 */
public interface SimplificationVisitor<R> {
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Constante expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Variable expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Addition expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Moins expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Multiplication expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Puissance expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Log expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Exp expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Cos expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Sin expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Tan expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Cotan expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Cosh expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Sinh expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Tanh expr);

	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R> Lorsqu'un visiteur est passé à
	 *         la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Cotanh expr);
}