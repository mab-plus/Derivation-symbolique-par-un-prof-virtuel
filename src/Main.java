import java.util.Scanner;

import edu.projet.Prof.*;
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
		
		/*String question = "je voudrais dériver la fonction f(x) = x^2 + 3";
		String reponse = LeProf.analyse(question);
		System.out.println("-------------------");
		System.out.println("-------------------");
		Expression q = Expression.formuleToExpression(reponse);
		afficher(q);
		afficher(deriver(q, "x"));*/
		
		/*afficher(new Puissance(new Variable("x"), 2));
		afficher(deriver(new Puissance(new Variable("x"), 2), "x"));*/
		
		String reponse, question;  
		Scanner sc = new Scanner(System.in);
		System.out.println(">  LeProf : Bonjour, je suis prêt à vous aider à dériver des fonctions");
		System.out.println(">  LeProf : Cependant, je suis débutant et je voudrais connatre votre niveau scolaire.");  
		  
		while (sc.hasNextLine()) {
			question = sc.nextLine();
			reponse = Prof.analyse(question); 
			
			if(reponse.equals("quit")) {
				System.out.println("> Au revoir");
				break;
			}
			else {
				System.out.println("***** DEbug fin *****\n");			
				System.out.println("> LeProf : " + reponse); 
				
				if (!Prof.getMemoire().isEmpty())
					System.out.println(">> LeProf : Voici le résultat : " + Prof.calcul (question));
			}
		}
		sc.close();

		
    }
	static void afficher(Expression expr) {
		Simplification simp = new Simplification();
		
		System.out.println(simp.simplifier(expr).asString());
	}
	
	static Expression deriver(Expression expr, String dx) {
		return new Derivation().deriver(expr, dx);
	}

}

