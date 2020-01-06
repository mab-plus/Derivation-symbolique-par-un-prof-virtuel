import java.util.Scanner;

import edu.projet.LeProf.*;
import edu.projet.calcul.Derivation;
import edu.projet.calcul.Simplification;
import edu.projet.expressions.*;

public class Main {
	
	public static void main(String[] args) {
            
		/*Constante zero = new Constante(0);
		Constante un = new Constante(1); 
		Constante deux = new Constante(2);
		Constante trois = new Constante(3);
		Expression x = new Variable("x");
		Expression y = new Variable("y");
		Expression z = new Variable("z");*/
		
		/*
		// -2xy^2
		Expression a = new Multiplication( new Multiplication(new Constante(-2), x), new Puissance(y, 2) );
		
		// x^3z
		Expression b = new Multiplication(new Puissance(x, 3), z );
		
		// 3x/y^2
		Expression c = new Division( new Multiplication(new Constante(3), x), new Puissance(y, 2) );
		
		// -2xy^2 + x^3z - 3x/y^2
		Expression d = new Addition(a, new Addition(b, c));
		
		Expression e = new Addition(new Addition(x, deux), new Addition(trois, x));
		afficher(e);
		afficher(deriver(e, "y"));
		
		afficher(b);
		System.out.println("Dérivation :");
		afficher(deriver(b, "x"));
		System.out.println("-------------------");
		
		afficher(c);
		System.out.println("Dérivation :");
		afficher(deriver(c, "x"));
		System.out.println("-------------------");
		
		afficher(d);
		System.out.println("Dérivation :");
		afficher(deriver(d, "x"));*/
		//System.out.println(LeProf.reflet("I love you"));
		//System.out.println(LeProf.analyse("je voudrais dériver la fonction f(x) = x^2 + 3"));*/
		
		   
		/*String reponse;  
		Scanner sc = new Scanner(System.in);
		System.out.println(">Bonjour, je suis Prof, prêt à vous aider à dériver.");
		    
		  
		while (sc.hasNextLine()) {
			reponse = LeProf.analyse(sc.nextLine()); 
			System.out.println(">" + reponse);
			if(reponse.equals("exit")) {
		          break;
		      
			}
		}
		System.out.println("Au revoir");
		sc.close();*/
    }
	static void afficher(Expression expr) {
		Simplification simp = new Simplification();
		
		System.out.printf("Expression : %s\n", expr.asString());
		System.out.printf("Sortie simplifiée : %s\n", simp.simplifier(expr).asString());
	}
	
	static Expression deriver(Expression expr, String dx) {
		return new Derivation().deriver(expr, dx);
	}

}

