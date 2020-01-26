package edu.projet.calcul;

import edu.projet.expressions.*;
import edu.projet.fonctions.*;
import edu.projet.interfaces.*;

public class Derivation implements FormuleDerivation, DerivationVisitor<Expression> {
	
	@Override
	public Expression deriver(Expression expression, String dx) {
		return expression.accept(this, dx);
	}

	@Override
	public Expression visit(Constante expr, String dx) {
		return new Constante(0);
	}
	
	@Override
	public Expression visit(Variable expr, String dx) {
		if (expr.getSymbole().equals(dx))
			return new Constante(1);
		else 
			return new Constante(0);
    }
	
	@Override
	public Expression visit(Addition expr, String dx) {
		return new Addition( expr.exprG.accept(this, dx), expr.exprD.accept(this, dx) );
	}
	
	@Override
	public Expression visit(Soustraction expr, String dx) {
		// Addition superclass de la classe Soustraction donc etc...
		return visit((Addition) expr, dx);
	}

	@Override
	public Expression visit(Moins expr, String dx) {
		return new Moins(expr.exprD.accept(this,dx));
	}
	
	@Override
	public Expression visit(Multiplication expr, String dx) {
		Expression u = expr.exprG;
		Expression v = expr.exprD;
		Expression du = u.accept(this, dx);
		Expression dv = v.accept(this, dx);
		
		// u*v -> u'v + uv'
		return new Addition ( new Multiplication(du, v), new Multiplication(u, dv) );
	}
	
	@Override
	public Expression visit(Division expr, String dx) {

		//Multiplication superclass de la classe Division donc etc...
		return visit((Multiplication) expr, dx);
	}

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

	@Override
	public Expression visit(Log expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);
		
		//log u -> u'/ u
		return new Division(du, u);
	}

	@Override
	public Expression visit(Exp expr, String dx) {
		Expression du = expr.exprD.accept(this, dx);
		//e^u -> u' e^u
		return new Multiplication(du, expr);
	}

	@Override
	public Expression visit(Cos expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);
		
		//cos(u)  -> -u' sin(u)
		return new Moins(new Multiplication(du, new Sin(u)));
	}

	@Override
	public Expression visit(Sin expr, String dx) {
		Expression u = expr.exprD;
		Expression du = u.accept(this, dx);
		
		//sin(u)  -> u' cos(u)
		return new Multiplication(du, new Cos(u));
	}
	
}