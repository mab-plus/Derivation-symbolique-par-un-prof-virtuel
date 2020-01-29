package edu.projet.professeur;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

import edu.projet.calcul.*;
import edu.projet.expressions.Expression;


/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Calcul {
	
	/**
	 * pour stocker l'équation trouvée dans l'entrée utilisateur.
	 */
	private static Stack<String> memoireEquation = new Stack<String>();
	
	/**
	 * pour stocker les variables de l'équation trouvée dans l'entrée utilisateur
	 */
	private static Stack<String> memoireVariable = new Stack<String>();
		
	/**
	 * @return une pile contenant toutes les variables de l'équation trouvée dans l'entrée utilisateur
	 */
	static Stack<String> getMemoireVariable() {
		return memoireVariable;
	}
	
	/**
	 * @param data, eneregistre les variables de l'équation dans memoireVariable.
	 */
	static void setMemoireVariable(String data) {
		memoireVariable.add(data);
	}
	
	/**
	 * @return une pile contenantle le ou les équations trouvées dans l'entrée utilisateur
	 */
	static Stack<String> getMemoireEquation() {
		return memoireEquation;
	}
	

	/**
	 * @param equation
	 * une fois l'équation trouvée, extraction des variables :
	 * exemple x^2 + y --> x,y.
	 * 
	 **/
	static void getVariables(String equation) {
		//on tranforme l'expression  trouvée en notation polonaise inversée
		//exemple x^2 + y  ---> x2^y+
		List<String> termes = Expression.equationToPostfix(equation);
		
		// on inverse x2^y+ ---> +y^2x
		Collections.sort(termes, Collections.reverseOrder()); 
		String tmp ="";
		
		for (int i = 0; i < termes.size(); i++) {		
			// si un terme n'est pas operateur ni une fonction (sin cos log ...)
			if ( !Expression.isOperateur( termes.get(i) ) && !Expression.isFonction( termes.get(i) )) { 	
				 
				//on regarde si c'est une constante float par une conversion
				try {					  
					Double.valueOf(termes.get(i));								
				} 
				catch (NumberFormatException e) {
					//en cas d'echec de la conversion alors c'est une variable
					//on ne garde que les variables distinctes
					if ( !tmp.equals(termes.get(i)))
						setMemoireVariable(termes.get(i) );
					tmp = termes.get(i);
				}
			}
		}
	}



	/**
	 * @param question
	 * @return	la dérivée de l'équation  par rapport à ses variables trouvées :
	 * exemple x^2 + y --> x,y --> d/dx(^2 + y) = 2*x, d/dy(^2 + y) = 1.
	 **/
	static String getDerivee(String question) {

		List<String> fichierFiltresEquations= Fichier.getFichierFiltresEquations();
		Matcher matcher;
		
		// Essayez chaque regex dans l'ordre.
		for(int i = 0; i < fichierFiltresEquations.size(); i++) {
			
			List<String> filtresEquations = Arrays.asList(fichierFiltresEquations.get(i).split("\n"));		

			//matching équation
			String reg = filtresEquations.get(0);
			matcher = Regex.match(reg, question);
			System.out.println("Calcul " + i + " : reg=" + reg);
			System.out.println("     matcher.groupCount()=" + matcher.groupCount());
			//on retourne l'équation
			if (matcher.find()) {
	            for (int j = 0; j <= matcher.groupCount() ; j++) {
	                // sous groupe j
		    		if (matcher.group(j) != null) {
		    			memoireEquation.push(matcher.group(j));
		    		}

	            }
	            // Si réponse, on la retourne
	            if (!memoireEquation.isEmpty())
	            		return derivation();
			}	
        }
		return null;
    }
	

	/**
	 * @return le résultat de la dérivée sous forme d'une réponse
	 *
	 **/
	static String derivation() {
		String variable, equation;
		String resultat="";
		Expression eEquation;
		Simplification simp = new Simplification();
		Derivation df = new Derivation();
		
		if ( getMemoireEquation().size() == 3)
			equation = getMemoireEquation().get(0);
		else
			equation = getMemoireEquation().get(getMemoireEquation().size() - 1);
		
		System.out.println("Calcul : derivation() : equation=" + equation);
		getVariables(equation);
		System.out.println("Calcul : derivation() : getMemoireVariable()=" + getMemoireVariable());
		
	    if(getMemoireVariable().size() == 0)
			return resultat + "(" + equation + ")' = 0";
	    
		if(getMemoireVariable().size() == 1) {
			variable = getMemoireVariable().pop();
			//on suppose que la variable est du style x, y ou n'importe quelle lettre mais pas un mot
			if (variable.length() == 1) {
				eEquation = Expression.formuleToExpression(equation);
				eEquation = simp.simplifier(eEquation);
				eEquation = df.deriver(eEquation, variable);
				System.out.println("Calcul : derivation() : eEquation=" + eEquation.asString());
				resultat += "(" + equation + ")' = " + simp.simplifier(eEquation).asString();
			}
		}
		
		if(getMemoireVariable().size() > 1) {
			resultat ="Voici les résultats petit scarabée : ";
			eEquation = Expression.formuleToExpression(equation);
			eEquation = simp.simplifier(eEquation);
			int memoireVariable = getMemoireVariable().size();
			Expression expr; 
			
			for(int i = 0; i < memoireVariable; i++) {
				variable = getMemoireVariable().pop();
				//on suppose que la variable est du style x, y ou n'importe quelle lettre mais pas un mot
				if (variable.length() == 1) {
					expr = df.deriver(eEquation, variable);
					resultat +="(d/d" + variable + ")(" + equation + ") = " + simp.simplifier(expr).asString() + ", ";
				}
			}
		}
		
		getMemoireEquation().clear();
		getMemoireVariable().clear();
		return resultat;
	}
	
}
