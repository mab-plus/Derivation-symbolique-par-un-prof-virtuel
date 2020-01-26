package edu.projet.interfaces;

import edu.projet.expressions.*;
import edu.projet.fonctions.*;

/**
 * @author BAKHTAOUI Michel
 *
 * @param <R> type de retour des méthodes de ce visiteur.
 */
public interface SimplificationVisitor<R> {
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Constante expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Variable expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Addition expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Moins expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Multiplication expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Puissance expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Log expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Exp expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Cos expr);
	/**
	 * @param expr
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Sin expr);
}