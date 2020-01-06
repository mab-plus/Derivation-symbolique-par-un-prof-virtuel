package edu.projet.expressions;

import java.util.Stack;

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
		
		if (this.getSymbole()  == "/")	
			return this.exprG.asString() + this.getSymbole() + this.exprD.asString();
			
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
      
      else
    	  return -1;   
  }
  
  //inspiration https://www.geeksforgeeks.org/stack-set-2-infix-to-postfix
  //infix notation normale, postfix est la notation polonaise inversée qui servira à contruire l'Expression correspondante
  private static String infixToPostfix(String frm) { 
	   
	  String[] termes = frm.toLowerCase().split("");
	  String resultat = new String(""); 
	  Stack<String> pile = new Stack<>(); 
       
     for (int i = 0; i < termes.length; ++i) {
    	   String terme =termes[i];
           
           if (!isOperateur(terme) && !terme.equals("(") && !terme.equals(")"))
               resultat += terme; 
           
           else if ( terme.equals("(") )
               pile.push(terme); 
           
           else if ( terme.equals(")") ) { 
        	   
               while ( !pile.isEmpty() && !pile.peek().equals("(") )
                   resultat += pile.pop(); 
                 
               if ( !pile.isEmpty() && !pile.peek().equals("(") ) 
            	   return "Expression non valable";  
               
               else
                   pile.pop(); 
           } 
           
           // si operateur 
           else { 
        	   while (!pile.isEmpty() && isPrioritaire(terme) <= isPrioritaire(pile.peek())) { 
                   if( pile.peek().equals("(") ) 
                       return "Expression non valable"; 
                   resultat += pile.pop();             
        	   }
               pile.push(terme); 
           }
     }

     //on vide la pile pour terminer
     while (!pile.isEmpty()) { 
    	 if( pile.peek().equals("(") ) 
             return "Expression non valable"; 
    	 
         resultat += pile.pop();  
     }
      
     return resultat; 
  }
  
  // conversion formule donnée à LeProf en Expression:/
  public static Expression formuleToExpression(String formule) {
	  Stack<Expression> pile = new Stack<Expression>(); 
      Expression expr1, expr2; 
      
	  String[] termes = infixToPostfix(formule).toLowerCase().split("");

      for (int i = 0; i < termes.length; i++) {
   	   // si operande -> la pile 
          if ( !isOperateur( termes[i] ) && !isFonction( termes[i] ) ) { 
   	    
        	  try {
        		  pile.push(new Constante(Double.valueOf(termes[i]) ));
        	  }
        	  catch (Exception e) {
      	        pile.push(new Variable(termes[i]));
        	  }
          }
          // operateur + - * / ^
          else if ( isOperateur( termes[i] ) ) { 
       	   // Pop les deux operandes de l'operateur
       	   expr1 = pile.pop();      
       	   expr2 = pile.pop();
       	   
       	   System.out.printf("operateur : %s\n", termes[i]);
       	   System.out.printf("expr1 : %s\n", expr1.asString());
       	   System.out.printf("expr2 : %s\n", expr2.asString());
       	
              switch(termes[i]) { 
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
       	   
              switch(termes[i]) { 
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
