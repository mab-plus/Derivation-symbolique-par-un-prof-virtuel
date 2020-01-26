package edu.projet.calcul;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.projet.expressions.*;
import edu.projet.fonctions.*;
import edu.projet.interfaces.*;

public class Simplification implements FormuleSimplification, SimplificationVisitor<Expression> {
	

	/**
	 * Parcourt récursif de l'expression, de gauche à droite pour stocker les termes dans une liste
	 * exemple x + 1 + 2 = new Addition(new Addition(x, un), deux);	x1+2+
	 *
	*/	
   private void deCompose(List<Expression> termes, Expression expr) {
	   
	   Expression expressionSymbole;
	   
	   if (expr == null)
			return;
	   
	   if (expr instanceof Constante)
		   expressionSymbole = new Constante(((Constante) expr).getValeur() ); 
	   else
		   expressionSymbole = new Variable (expr.getSymbole());
	   
	   deCompose(termes, expr.exprG);
	   deCompose(termes, expr.exprD);
	   termes.add(expressionSymbole);  
   }
  
	/**
	 * Simplification des termes de la liste d'expressions générée par la méthode @see deCompose en une instance d'Expression
	*/
   private Expression simplifierTermes(List<Expression> termes) {
	   Stack<Expression> pile = new Stack<Expression>(); 
       Expression expr1, expr2; 
 
       for (int i = 0; i < termes.size(); i++) {
    	   // si operande -> la pile 
           if ( !Expression.isOperateur( termes.get(i).getSymbole() )  && !Expression.isFonction( termes.get(i).getSymbole() ) ) { 
               pile.push(termes.get(i));
           }
           // si operateur + - * / ^
           else if ( Expression.isOperateur( termes.get(i).getSymbole() ) ) { 
        	   // Pop les deux operandes de l'operateur
        	   expr1 = pile.pop();      
        	   expr2 = pile.pop();
        	
               switch(termes.get(i).getSymbole()) { 
                   case "+": 
                	   pile.push(new Addition(expr2, expr1).accept(this)); 
                   break; 
                     
                   case "-": 
                	   pile.push(new Soustraction(expr2, expr1).accept(this)); 
                   break; 
                     
                   case "*":           	   
                	   pile.push(new Multiplication(expr2, expr1).accept(this)); 
                   break; 
                     
                   case "/": 
                	   pile.push(new Division(expr2, expr1).accept(this)); 
                   break; 
                   
                   case "^": 
                	   pile.push(new Puissance(expr2, expr1).accept(this)); 
                   break;
             }
           }
           // si fonctions cos sin exp log etc...
           else {
        	   // Pop argument de la fonction
        	   expr1 = pile.pop();
        	   
               switch(termes.get(i).getSymbole()) { 
                   case "log": 
                	   pile.push(new Log(expr1.accept(this))); 
                   break;
                   case "exp": 
                	   pile.push(new Exp(expr1.accept(this))); 
                   break;
                   case "cos": 
                	   pile.push(new Cos(expr1.accept(this))); 
                   break;
                   case "sin": 
                	   pile.push(new Sin(expr1.accept(this))); 
                   break;
             } 
           }           
       } 
       Expression resultat = pile.pop();
       return resultat;   
   }
	/**
	 * On simplifie tant que le résultat est simplifiable
	 */
	@Override
	public Expression simplifier(Expression expression) {

		List<Expression> termesDecomposition = new ArrayList<>();
		
		this.deCompose(termesDecomposition, expression);
		
		if (expression.equals(this.simplifierTermes(termesDecomposition)))
			return expression;
		else {
			return simplifier(this.simplifierTermes(termesDecomposition));
		}

	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Constante, la méthode visit(Constante expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Constante cste) {
		return cste;
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Variable, la méthode visit(Variable expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Variable var) {
		return var;
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Addition, la méthode visit(Addition expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Addition add) {
		
		// c + c
		if (add.exprG instanceof Constante && add.exprD instanceof Constante)
				return new Constante( ((Constante) add.exprG).getValeur() + ((Constante) add.exprD).getValeur()  );
		
		// e + 0 = e
		if ( Expression.isZero(add.exprD) )
			return add.exprG.accept(this);
			
		// c + e
		if ( Expression.isConstante(add.exprG ) )
				return new Addition(add.exprD, add.exprG).accept(this);
		
		// e  + c
		if ( Expression.isConstante(add.exprD ) )
			return new Addition(add.exprG.accept(this), add.exprD );
		
		// e + e       -> 2 * expr
		if (add.exprD.equals(add.exprG))
			return new Multiplication(new Constante(2), add.exprG.accept(this));	
		
		// e - e       -> 0
		if (add.exprD instanceof Moins && add.exprD.exprD.equals(add.exprG))
			return new Constante(0);	
		
		// -e + e       -> 0
		if (add.exprG instanceof Moins && add.exprG.exprD.equals(add.exprD))
			return new Constante(0);
		
		// e + (-1) * e = e - e = 0     -> 0
		if ( add.exprD instanceof Multiplication && add.exprG.equals(add.exprD.exprD) && Expression.isMoinsUn(add.exprD.exprG) )		
			return new Constante(0);	
		
		// e + n*e =      -> (n + 1) * e
		if (add.exprD instanceof Multiplication && Expression.isConstante(add.exprD.exprG) && add.exprG.equals(add.exprD.exprD))		
			return new Multiplication(new Addition(add.exprD.exprG, new Constante(1)), add.exprG).accept(this);	
		
		// n*e  + e =      -> (n + 1) * e
		if (add.exprG instanceof Multiplication && Expression.isConstante(add.exprG.exprG) && add.exprD.equals(add.exprG.exprD))		
			return new Multiplication(new Addition(add.exprG.exprG, new Constante(1)), add.exprD).accept(this);	

		// Addition (expr1, expr2)                 -> Addition (simplifier expr1, simplifier expr2)
		return new Addition( add.exprG.accept(this), add.exprD.accept(this));
	}
	
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Moins, la méthode visit(Moins expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Moins expr) {
		return new Moins(expr.exprD.accept(this));
	}

	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Multiplication, la méthode visit(Multiplication expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Multiplication mult) {
		
		// c * c 
		if (Expression.isConstante(mult.exprG) && Expression.isConstante(mult.exprD))
			return new Constante(((Constante) mult.exprG).getValeur() * ((Constante) mult.exprD).getValeur());
		
		// 0 * e = 0
		if (Expression.isZero(mult.exprG))
			return new Constante(0);
		
		// 1 * e = e
		if (Expression.isUn(mult.exprG))
			return mult.exprD.accept(this);
		
		// e *(- e)       -> -e^2
		if (mult.exprD instanceof Moins && mult.exprD.exprD.equals(mult.exprG))
			return new Moins(new Puissance(mult.exprG.accept(this), 2));
		
		// (-e) * e       -> -e^2
		if (mult.exprG instanceof Moins && mult.exprG.exprD.equals(mult.exprD))
			return new Moins(new Puissance(mult.exprG.accept(this), 2));

		
		// (-1) * (-1) * e = e
		if (mult.exprD instanceof Multiplication && Expression.isMoinsUn(mult.exprD.exprG) && Expression.isMoinsUn(mult.exprG) )
			return mult.exprD.exprD.accept(this);
						
		// e * c = c * e
		if ( Expression.isConstante(mult.exprD) )
			return new Multiplication(mult.exprD, mult.exprG).accept(this);
		
		// c * e 
		if ( Expression.isConstante(mult.exprG) )
			return new Multiplication(mult.exprG, mult.exprD.accept(this));
		
		// c * c' * e = (c * c') * e
		if (mult.exprD instanceof Multiplication && Expression.isConstante(mult.exprD.exprG) && Expression.isConstante(mult.exprG))
			return new Multiplication( new Constante(((Constante) mult.exprG).getValeur() * ((Constante) mult.exprD).getValeur()), 
							mult.exprD.exprD.accept(this));
		
		// c * e * e' = c * (e * e')
		if (mult.exprG instanceof Multiplication && Expression.isConstante(mult.exprG.exprG))
			return new Multiplication( mult.exprG.exprG, new Multiplication(mult.exprG.exprD, mult.exprD).accept(this) );
		
		// c * e^n 
		if ( Expression.isConstante(mult.exprG) && mult.exprD instanceof Puissance)		
			return new Puissance(mult.exprG, mult.exprD.accept(this));	

		// e * e       -> e^2
		if (mult.exprD.equals(mult.exprG))
			return new Puissance(mult.exprG.accept(this), 2);	
		
		// e * e^n =      -> e^(n+1)
		if (mult.exprD instanceof Puissance && mult.exprG.equals(mult.exprD.exprG))		
			return new Puissance(mult.exprG, new Addition(mult.exprD.exprD, new Constante(1)) ).accept(this);	
		
		// e^n * e =      -> e^(n+1)
		if (mult.exprG instanceof Puissance && mult.exprD.equals(mult.exprG.exprG))		
			return new Puissance(mult.exprD, new Addition(mult.exprG.exprD, new Constante(1)) ).accept(this);
		
		// e^n * e^m =      -> e^(n+m)
		if (mult.exprG instanceof Puissance && mult.exprD instanceof Puissance && mult.exprD.exprG.equals(mult.exprG.exprG))
			return new Puissance(mult.exprG.exprG, new Addition(mult.exprG.exprD, mult.exprD.exprD).accept(this) );
	
		return new Multiplication(mult.exprG.accept(this), mult.exprD.accept(this));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Puissance, la méthode visit(Puissance expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Puissance pw) {
		
		//e^0 = 1
		if ( Expression.isZero(pw.exprD) )
			return new Constante(1);
		
		// o^n = 0
		if ( Expression.isZero(pw.exprG) )
			return new Constante(0);
		
		//e^1 = e
		if ( Expression.isUn(pw.exprD) )
			return pw.exprG.accept(this);

		//e^n^n = e^(n*n)
		if (pw.exprG instanceof Puissance)
			return new Puissance(((Puissance) pw.exprG).exprG.accept(this) , 
									new Multiplication( ((Puissance) pw.exprG).exprD, pw.exprD).accept(this) ).accept(this);
		
		//  e^(-n) = 1 / e^n
		/*if ( pw.exprD instanceof Constante && ((Constante) pw.exprD).getValeur() < 0) {
			int exposant = (int)((Constante) pw.exprD).getValeur() * -1;
			return new Division(new Constante(1), new Puissance(pw.exprG , exposant).accept(this)  );
		}*/
		
		// sinon puissance du facteur simplifié 
		return new Puissance(pw.exprG.accept(this) , pw.exprD.accept(this));

	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Log, la méthode visit(Log expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Log expr) {
		return new Log(expr.exprD.accept(this));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Exp, la méthode visit(Exp expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Exp expr) {
		return new Exp(expr.exprD.accept(this));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Cos, la méthode visit(Cos expr) est invoquée pour cet élément
	*/
	@Override
	public Expression visit(Cos expr) {		
		return new Cos(expr.exprD.accept(this));
	}
	/**
	* Lorsqu'un visiteur est passé à la méthode accept d'une instante de la classe Sin, la méthode visit(Sin expr) est invoquée pour cet élément
	**/
	@Override
	public Expression visit(Sin expr) {	
		return new Sin(expr.exprD.accept(this));
	}

}
