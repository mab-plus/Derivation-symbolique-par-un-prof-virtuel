package edu.projet.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.projet.fonctions.*;
import edu.projet.interfaces.Formule;

/**
 * Une expression algébrique peut être définie récursivement de la manière suivante :
 * une constante est une expression
 * une variable est une expression
 * l’addition de deux expressions est une expression
 * la multiplication de deux expressions est une expression
 * etc.
 * On définit donc une classe Expression récursif pour les expressions algébrique. 
 * C'est une classe récursive car chaque objet instance de cette classe a des variables qui contiennent d'autres instances de la classe.
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public abstract class Expression implements Formule {
	
    /**
     * @param Opérande gauche et droite
     */
    public Expression exprG, exprD;
    
    /**
     * @param Opérateur
     */
    public String symbole;
    
	/**
	 * Constructeur Expression.
	 * @param exprG opérande gauche  
	 * @param symbole opérateur
	 * @param exprD opérande droite  
	 */
	public Expression(Expression exprG, String symbole, Expression exprD) {
		this.exprG = exprG;
		this.symbole = symbole;
		this.exprD = exprD;
	}
	
	/**
	 * @return le symbole de l'opérateur de l'expression
	 */
	public String getSymbole() {
		return this.symbole;
	}
	
    /**
     * @return L'expression, sous la forme d'une chaîne de caractère.
     */
	@Override
	public String asString() {
		
		if (this.exprG == null)	
			return this.getSymbole() + "(" + this.exprD.asString() + ")";
			
		return this.exprG.asString() + " " + this.getSymbole() + " " + this.exprD.asString();
	}
	
	/**
	 * @return true si deux objets sont égaux sinon false. Pour comparer les expressions
	 */
	@Override
	public boolean equals(Object objet) { 
		
		if (this == objet) 
			return true; 
			
		if (objet == null) 
			return false; 
			
		if (!(objet instanceof Expression))
			return false; 
			
		Expression autreObjet = (Expression) objet; 
			
		if (this.exprG == null) { 
			
			if (autreObjet.exprG != null) 
				return false; 
		}
		else if (!this.exprG.equals(autreObjet.exprG)) 
			return false; 
		
		if (this.exprD == null) { 
			
			if (autreObjet.exprD != null) 
				return false; 
		}
		else if (!this.exprD.equals(autreObjet.exprD)) 
			return false; 
		
		if (this.symbole == null) { 
			
			if (autreObjet.symbole != null) 
				return false; 
		}
		else if (!this.symbole.equals(autreObjet.symbole)) 
			return false; 
		
		return true; 
	}
	
  /**
   * @param expr
   * @return true si l'expression expr est la constante 0
   */
   public static boolean isZero (Expression expr) { 
	   return (expr instanceof Constante) && (((Constante) expr).getValeur() == 0);
   } 
   /**
    * @param expr
    * @return true si l'expression expr est la constante 1
    */
   public static boolean isUn (Expression expr) { 
	   return (expr instanceof Constante) && (((Constante) expr).getValeur() == 1);
   } 
   /**
    * @param expr
    * @return true si l'expression expr est la constante -1
    */
   public static boolean isMoinsUn (Expression expr) { 
	   return (expr instanceof Constante) && (((Constante) expr).getValeur() == -1);
   } 
   /**
    * @param expr
    * @return true si l'expression expr est une constante
    */
   public static boolean isConstante (Expression expr) { 
	   return expr instanceof Constante;
   } 
   /**
    * @param expr
    * @return true si l'expression expr est une variable
    */
   public static boolean isVariable (Expression expr) { 
	   return expr instanceof Variable;
   }
   /**
    * @param op
    * @return true si expr est un opérateur
    */
   public static boolean isOperateur(String op) { 
	   
       if ( op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("^") )
           return true; 
       
       else
    	   return false; 
   }  
   
   /**
    * @param fn
    * @return true si fn est une fonction (cos, sin, etc.)
    */
  public static boolean isFonction(String fn) { 
	  
      if (fn.equals("log") || fn.equals("exp") || fn.equals("cos") || fn.equals("sin") )
          return true; 
      
      else
    	  return false; 
  }
  
  /**
 * @param op
 * @return la priorité de l'opération ou de la fonction op 
 */
  public static int isPrioritaire(String op) { 
	   
      if ( op.equals("+") || op.equals("-") )
    	  return 1;
      
      else if ( op.equals("*") || op.equals("/") )
   	   return 2;
      
      else if (op.equals("^") )
    	  return 3;
      
      else if (isFonction(op) )
    	  return 4;
      
      else
    	  return -1;
  }
  

  /**
 * @param equation
 * @return une liste de String des termes de l'équation
 */
  private static List<String> splitter(String equation) {
	  
      ArrayList<String> termes = new ArrayList<>();
      //on essaie de bien couper l'équation en termes
      Matcher matcher = Pattern.compile("\\(|\\)|[A-Za-z]+|\\d*\\.\\d*|\\d+|\\S+?").matcher(equation);
     
      while(matcher.find()) {
        termes.add(matcher.group());
      }
      
      return termes;
  }
  

  /**
 * @param equation
 * @return en notation postfix dans une liste de String l'équation
 */
  public static List<String> equationToPostfix(String equation) {
	  
	  List<String> termes = splitter(equation);
      Stack<String> pile = new Stack<>();
      ArrayList<String> resultat = new ArrayList<>();
      
      for(String str: termes) {
    	  if(str.equals("("))
              pile.push(str);
    	  
    	  else if(str.equals(")")) {
              while(!pile.empty() ) {
                  String s = pile.pop();
                  
                  if(s.equals("(")) 
                	  break;
                  resultat.add(s);
              }
          } 
    	  else if(isPrioritaire(str) > 0) {
              int p = isPrioritaire(str);
              
              while(!pile.isEmpty() && isPrioritaire(pile.peek()) >= p) 
            	  resultat.add(pile.pop());
              pile.push(str);
          } 
    	  else
    		  resultat.add(str);
      }
      
      while(!pile.isEmpty()) 
    	  resultat.add(pile.pop());
      
      return resultat;
  }
    
  /**
 * @param formule
 * @return  conversion d'une équation ou formule en Expression:
 */
  public static Expression formuleToExpression(String formule) {
	  
	  Stack<Expression> pile = new Stack<Expression>(); 
	  Expression expr1, expr2; 
	  	
	  List<String> termes = equationToPostfix(formule);

	  for (int i = 0; i < termes.size(); i++) {
		  //system.out.printf("termes.get(%d) : %s\n", i, termes.get(i));
	  	  // si operande -> la pile 
	  	  if ( !isOperateur( termes.get(i) ) && !isFonction( termes.get(i) ) ) { 
	  	   
	  	  	  try {
	  			  pile.push(new Constante(Double.valueOf(termes.get(i)) ));
	  	  	  }
	  	  	  catch (Exception e) {
	  	  	  	  pile.push(new Variable(termes.get(i)));
	  	  	  }
	  	  }
	  	  // operateur + - * / ^
	  	  else if ( isOperateur( termes.get(i) ) ) { 
	  	  	  // Pop les deux operandes de l'operateur
	  	  	  expr1 = pile.pop();	  
	  	  	  expr2 = pile.pop();
	  	  	  
	  	  	  //system.out.printf("operateur : %s\n", termes.get(i));
	  	  	  //system.out.printf("expr1 : %s\n", expr1.asString());
	  	  	  //system.out.printf("expr2 : %s\n", expr2.asString());
	   	
	  	  	  switch(termes.get(i)) { 
	  	  	  	  case "+": 
	  	  	  	  	  pile.push(new Addition(expr2, expr1)); 
	  	  	  	  break; 
	  	  	  	  	  
	  	  	  	  case "-": 
	  	  	  	  	  pile.push(new Soustraction(expr2, expr1)); 
	  	  	  	  break; 
	  	  	  	  	  
	  	  	  	  case "*":	  	  	  	  
	  	  	  	  	  pile.push(new Multiplication(expr2, expr1)); 
	  	  	  	  break; 
	  	  	  	  	  
	  	  	  	  case "/": 
	  	  	  	  	  pile.push(new Division(expr2, expr1)); 
	  	  	  	  break; 
	  	  	  	  
	  	  	  	  case "^": 
	  	  	  	  	  pile.push(new Puissance(expr2, expr1)); 
	  	  	  	  break;
	  	  	  }
	  	  }
	  	  // fonctions cos sin exp log etc...
	  	  else {
	  	  	  // Pop argment de la fonction
	  	  	  expr1 = pile.pop();
	  	  	  
	  	  	  //system.out.printf("fonction : %s\n", termes.get(i));
	  	  	  //system.out.printf("expr1 : %s\n", expr1.asString());
	  	  	  
	  	  	  switch(termes.get(i)) { 
	  	  	  	  case "log": 
	  	  	  	  	  pile.push(new Log(expr1)); 
	  	  	  	  break;
	  	  	  	  case "exp": 
	  	  	  	  	  pile.push(new Exp(expr1)); 
	  	  	  	  break;
	  	  	  	  case "cos": 
	  	  	  	  	  pile.push(new Cos(expr1)); 
	  	  	  	  break;
	  	  	  	  case "sin": 
	  	  	  	  	  pile.push(new Sin(expr1)); 
	  	  	  	  break;
	  	  	  } 
	  	  	  
	  	  	  //system.out.printf("RESULT : %s\n", pile.lastElement().asString());
	  	  }	  	   
	  } 
	  
	  Expression resultat = pile.pop();
	   
	  return resultat;   
  }
  
}
