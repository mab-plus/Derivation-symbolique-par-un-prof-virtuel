package edu.projet.calcul;

import edu.projet.expressions.*;
import edu.projet.fonctions.*;
import edu.projet.interfaces.*;

/**
 * 
 * @author Michel BAKHTAOUI
 *
 */
public class Derivation implements FormuleDerivation, DerivationVisitor<Expression> {
	
	/**
	 * Dérivée d'une expression
	 * 
	 */
	@Override
	public Expression deriver(Expression expression, String dx) {
		return expression.accept(this, dx);
	}

	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Constante, la méthode visit(Constante expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Constante expr, String dx) {
		return new Constante(0);
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Variable, la méthode visit(Variable expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Variable expr, String dx) {
		if (expr.getSymbole().equals(dx))
			return new Constante(1);
		else 
			return new Constante(0);
    }
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante d'Addition, la méthode visit(Addition expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Addition expr, String dx) {
		return new Addition( expr.exprG.accept(this, dx), expr.exprD.accept(this, dx) );
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Soustraction, la méthode visit(Soustraction expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Soustraction expr, String dx) {
		// Addition superclass de la classe Soustraction donc etc...
		return visit((Addition) expr, dx);
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Moins, la méthode visit(Moins expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Moins expr, String dx) {
		return new Moins(expr.exprD.accept(this,dx));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Multiplication, la méthode visit(Multiplication expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Multiplication expr, String dx) {
		Expression u = expr.exprG;
		Expression v = expr.exprD;
		Expression du = u.accept(this, dx);
		Expression dv = v.accept(this, dx);
		
		// u*v -> u'v + uv'
		return new Addition ( new Multiplication(du, v), new Multiplication(u, dv) );
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Division, la méthode visit(Division expr, String dx) est invoquée pour cet élément
	*/	
	@Override
	public Expression visit(Division expr, String dx) {

		//Multiplication superclass de la classe Division donc etc...
		return visit((Multiplication) expr, dx);
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Puissance, la méthode visit(Puissance expr, String dx) est invoquée pour cet élément
	*/	
	@Override
	public Expression visit(Puissance expr, String dx) {
		Expression u = expr.exprG;
		Expression v = expr.exprD;
		
		// u^v = v * (u' * u^(v-1)) si v = cste
		if (v instanceof Constante ) {
			int exposant = (int)((Constante) v).getValeur();
			Expression du = u.accept(this, dx);
			
			return new Multiplication(v, new Multiplication(du, new Puissance(u, exposant - 1)) );
		}
		
		// u^v -> (v log(u) )' u^v
		return new Multiplication( new Multiplication(v, new Log(u) ).accept(this, dx), expr );
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Log, la méthode visit(Log expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Log expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);
		
		//log u -> u'/ u
		return new Division(du, u);
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Exp, la méthode visit(Exp expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Exp expr, String dx) {
		Expression du = expr.exprD.accept(this, dx);
		//e^u -> u' e^u
		return new Multiplication(du, expr);
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Cos, la méthode visit(Cos expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Cos expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);
		
		//cos(u)  -> -u' sin(u)
		return new Moins(new Multiplication(du, new Sin(u)));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Sin, la méthode visit(Sin expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Sin expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);
		
		//sin(u)  -> u' cos(u)
		return new Multiplication(du, new Cos(u));
	}
	
}