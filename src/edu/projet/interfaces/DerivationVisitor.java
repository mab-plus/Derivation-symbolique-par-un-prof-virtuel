package edu.projet.interfaces;

import edu.projet.expressions.*;
import edu.projet.fonctions.*;

/**
 * @author BAKHTAOUI Michel
 *
 * @param <R> type de retour des méthodes de ce visiteur.
 */
public interface DerivationVisitor<R> {
	
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Constante expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Variable expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Addition expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Soustraction expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Moins expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Multiplication expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Division expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Puissance expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Log expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Exp expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Cos expr, String dx);
	/**
	 * @param expr
	 * @param dx
	 * @return interface publique ElementVisitor <R>
	 * Lorsqu'un visiteur est passé à la méthode accept d'un élément, la méthode visit est invoquée.
	 */
	public R visit(Sin expr, String dx);
}