package edu.projet.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.projet.fonctions.*;
import edu.projet.interfaces.Formule;

public abstract class Expression implements Formule {
	
    public Expression exprG, exprD;
    public String symbole;
    
	public Expression(Expression exprG, String symbole, Expression exprD) {
		this.exprG = exprG;
		this.symbole = symbole;
		this.exprD = exprD;
	}
	
	public String getSymbole() {
		return this.symbole;
	}

	@Override
	public String asString() {
		
		if (this.exprG == null)	
			return this.getSymbole() + "(" + this.exprD.asString() + ")";
			
		return this.exprG.asString() + " " + this.getSymbole() + " " + this.exprD.asString();
	}
	
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
  
   public static boolean isZero (Expression expr) { 
	   return (expr instanceof Constante) && (((Constante) expr).getValeur() == 0);
   } 

   public static boolean isUn (Expression expr) { 
	   return (expr instanceof Constante) && (((Constante) expr).getValeur() == 1);
   } 
   
   public static boolean isMoinsUn (Expression expr) { 
	   return (expr instanceof Constante) && (((Constante) expr).getValeur() == -1);
   } 
   
   public static boolean isConstante (Expression expr) { 
	   return expr instanceof Constante;
   } 
   
   public static boolean isVariable (Expression expr) { 
	   return expr instanceof Variable;
   }
   
   public static boolean isOperateur(String op) { 
	   
       if ( op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("^") )
           return true; 
       
       else
    	   return false; 
   }  
   
 
  public static boolean isFonction(String fn) { 
	  
      if (fn.equals("log") || fn.equals("exp") || fn.equals("cos") || fn.equals("sin") )
          return true; 
      
      else
    	  return false; 
  }
  
  // test priorité des opérations
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
  
  private static List<String> splitter(String equation) {
	  
      ArrayList<String> termes = new ArrayList<>();
      //on essaie de bien couper l'équation en termes
      Matcher matcher = Pattern.compile("\\(|\\)|[A-Za-z]+|\\d*\\.\\d*|\\d+|\\S+?").matcher(equation);
     
      while(matcher.find()) {
        termes.add(matcher.group());
      }
      
      return termes;
  }
  
  //notation normale vers postfix qui est la notation polonaise inversée qui servira à contruire l'Expression correspondante
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
    
  // conversion formule donnée au Prof en Expression:/
  public static Expression formuleToExpression(String formule) {
	  
	  Stack<Expression> pile = new Stack<Expression>(); 
	  Expression expr1, expr2; 
	  	
	  List<String> termes = equationToPostfix(formule);
	  System.out.println("equationToPostfix=" + termes);
	  for (int i = 0; i < termes.size(); i++) {
		  System.out.printf("termes.get(%d) : %s\n", i, termes.get(i));
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
	  	  	  
	  	  	  System.out.printf("operateur : %s\n", termes.get(i));
	  	  	  System.out.printf("expr1 : %s\n", expr1.asString());
	  	  	  System.out.printf("expr2 : %s\n", expr2.asString());
	   	
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
	  	  	  System.out.printf("RESULT : %s\n", pile.lastElement().asString());
	  	  }
	  	  // fonctions cos sin exp log etc...
	  	  else {
	  	  	  // Pop argment e la fonction
	  	  	  expr1 = pile.pop();
	  	  	  
	  	  	  System.out.printf("fonction : %s\n", termes.get(i));
	  	  	  System.out.printf("expr1 : %s\n", expr1.asString());
	  	  	  
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
	  	  	  
	  	  	  System.out.printf("RESULT : %s\n", pile.lastElement().asString());
	  	  }	  	   
	  } 
	  
	  Expression resultat = pile.pop();
	  System.out.printf("pop = %s \n", resultat.asString());
	  System.out.println("-------------------");
	   
	  return resultat;   
  }
  
}
