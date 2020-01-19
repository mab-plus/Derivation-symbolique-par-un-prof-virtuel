package edu.projet.professeur;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

import edu.projet.calcul.*;
import edu.projet.expressions.Expression;


public class Calcul {
	
	private static Stack<String> memoireEquation = new Stack<String>();
	private static Stack<String> memoireVariable = new Stack<String>();
		
	static Stack<String> getMemoireVariable() {
		return memoireVariable;
	}

	static void setMemoireVariable(String data) {
		memoireVariable.add(data);
	}
	
	static Stack<String> getMemoireEquation() {
		return memoireEquation;
	}
	
	/**
	 * une fois l'équation trouvée, extraction des variables :
	 * exemple x^2 + y --> x,y
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
	 * une fois l'équation et ses variables trouvées, calcul de la dérivée :
	 * exemple x^2 + y --> x,y --> d/dx(^2 + y) = 2*x, d/dy(^2 + y) = 1,
	 * 
	 **/
	static String getDerivee(String question) {

		List<String> fichierFiltresEquations= Fichier.getFichierFiltresEquations();
		Matcher matcher;
		
		// Essayez chaque regex dans l'ordre.
		for(int i = 0; i < fichierFiltresEquations.size(); i++) {
			
			List<String> filtresEquations = Arrays.asList(fichierFiltresEquations.get(i).split("\n"));		

			//matching équation
			String reg = filtresEquations.get(0);
			System.out.println("Equation - reg= " + reg);
			matcher = Regex.match(reg, question);
			//on retourne l'équation
			while (matcher.find()) {
	            for (int j = 0; j <= matcher.groupCount() ; j++) {
	                // sous groupe j
		    		if (matcher.group(j) != null) {	    					
			    		System.out.printf("!!!!!!!!!!!!! Groupe %d/%d: %s\n", j, matcher.groupCount(), matcher.group(j).trim() );
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
	 * Professeur renvoie le résultat sous forme d'une réponse
	 **/
	static String derivation() {
		String variable, equation;
		String resultat ="Voici le résultat petit scarabée : ";
		Expression eEquation;
		Simplification simp = new Simplification();
		Derivation df = new Derivation();
		
		if ( getMemoireEquation().size() == 3)
			equation = getMemoireEquation().get(0);
		else
			equation = getMemoireEquation().get(getMemoireEquation().size() - 1);
		
		getVariables(equation);
		
	    if(getMemoireVariable().size() == 0)
			return resultat + "(" + equation + ")' = 0";
	    
		if(getMemoireVariable().size() == 1) {
			variable = getMemoireVariable().pop();
			eEquation = Expression.formuleToExpression(equation);
			eEquation = df.deriver(eEquation, variable);
			resultat += "(" + equation + ")' = " + simp.simplifier(eEquation).asString();
		}
		
		if(getMemoireVariable().size() > 1) {
			resultat ="Voici les résultats petit scarabée : ";
			eEquation = Expression.formuleToExpression(equation);
			int memoireVariable = getMemoireVariable().size();
			Expression expr; 
			
			for(int i = 0; i < memoireVariable; i++) {
				variable = getMemoireVariable().pop();
				expr = df.deriver(eEquation, variable);
				resultat +="(d/d" + variable + ")(" + equation + ") = " + simp.simplifier(expr).asString() + ", ";
			}
		}
		
		getMemoireEquation().clear();
		getMemoireVariable().clear();
		return resultat;
		
	}

	
}
