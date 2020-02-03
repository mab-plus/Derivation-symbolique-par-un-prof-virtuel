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
		Expression u = expr.exprG;
		Expression v = expr.exprD;
		Expression du = u.accept(this, dx);
		Expression dv = v.accept(this, dx);
		
		return new Addition( du, dv );
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
		Expression v = expr.exprD;
		Expression dv = v.accept(this, dx);
		
		return new Moins(dv);
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
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Tan, la méthode visit(Tan expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Tan expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);

		//tan(u)  -> u'/cos²(u)
		return new Division(du, new Puissance(new Cos(u), 2));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Cotan, la méthode visit(Cotan expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Cotan expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);

		//cotan(u)  -> -u'/sin²(u)
		return new Division(new Moins(du), new Puissance(new Sin(u), 2));
	}
	
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Ch, la méthode visit(Ch expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Cosh expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);

		//ch(u)  -> u' sh(u)
		return new Multiplication(du, new Sinh(u));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Sh, la méthode visit(Sh expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Sinh expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);

		//sh(u)  -> u' ch(u)
		return new Multiplication(du, new Cosh(u));
	}
	
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Tanh, la méthode visit(Tanh expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Tanh expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);

		//tanh(u)  -> u'/ch²(u)
		return new Division(du, new Puissance(new Cosh(u), 2));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Cotanh, la méthode visit(Cotanh expr, String dx) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Cotanh expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);

		//cotanh(u)  -> -u'/sh²(u)
		return new Division(new Moins(du), new Puissance(new Sinh(u), 2));
	}

}