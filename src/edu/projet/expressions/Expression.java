package edu.projet.expressions;

import java.util.Stack;

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
  
  // conversion formule donnée à LeProf en Expression:/
  // inspiration https://www.geeksforgeeks.org/stack-set-2-infix-to-postfix/
  public static String formuleToExpression(String frm) { 
	   
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


}
