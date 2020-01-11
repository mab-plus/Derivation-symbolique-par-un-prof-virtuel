package edu.projet.Prof;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.projet.Prof.data.ExtractionParsing;
import edu.projet.Prof.data.Filtres;
import edu.projet.calcul.Derivation;
import edu.projet.calcul.Simplification;
import edu.projet.expressions.Expression;

public class Prof {
	
	private static Stack<String> memoireEquation = new Stack<String>();
	private static Stack<String> memoireVariable = new Stack<String>();
	
	public static Stack<String> getMemoireEquation() {
		return memoireEquation;
	}
	
	public static Stack<String> getMemoireVariable() {
		return memoireVariable;
	}

	public static void setMemoireEquation(Stack<String> data) {
		Prof.memoireEquation.addAll(data);
	}
	
	public static void setMemoireVariable(String data) {
		Prof.memoireVariable.add(data);
	}
	
	public static void getVariables(String equation) {
		List<String> termes = Expression.equationToPostfix(equation);
		Collections.sort(termes, Collections.reverseOrder()); 
		String tmp ="";
		
		for (int i = 0; i < termes.size(); i++) {		
			// si operande -> la pile 
			if ( !Expression.isOperateur( termes.get(i) ) && !Expression.isFonction( termes.get(i) )) { 	
				 
				try {					  
					Double.valueOf(termes.get(i));								
				} 
				catch (NumberFormatException e) {
					if ( !tmp.equals(termes.get(i)))
						setMemoireVariable(termes.get(i) );
					tmp = termes.get(i);
				}
			}
		}
	}
	
	//analyse et reponse de la question, au moyen des fichiers filtres 
	public static String reponse(String question) {
		
		List< List<String> > mathsBlaba = ExtractionParsing.mathsBlabla();
		String reponse = "...", regex;
		Matcher matcher;
		boolean siEquation;
		question = desAccentuer(question);
		
		System.out.println("\n***** DEbug *****");
		System.out.println("***** question = " + question);
		
		//prof met en mémoire l'équation demandée
		//extractEquation(question);
		
	    for(int i = 0; i < mathsBlaba.size(); i++) {
	    	regex = desAccentuer(mathsBlaba.get(i).get(0));
	    	regex = Filtres.regex(regex); 	
	    	matcher = match(regex, question);
			reponse = mathsBlaba.get(i).get( (int) (Math.random() * (mathsBlaba.get(i).size() - 1) + 1));
			
			System.out.printf("***** filtre%d = %s\n", i, regex + " *****");
			
			//prof met en mémoire l'équation demandée
		    if (regex.equals(Filtres.$eq.regex) || regex.equals(Filtres.$e.regex) 
		    		|| regex.equals(Filtres.$fn.regex) )
		    	siEquation = true;
		    else
		    	siEquation = false;
		    
	    	while (matcher.find() ) {
	            for (int j = 0; j <= matcher.groupCount() ; j++) {
		    		System.out.printf("!!!!!!!!!!!!! Groupe %d/%d: %s\n", j, matcher.groupCount(), matcher.group(j));
	                // sous groupe j
		    		reponse = match("\\{" + Integer.toString(j) + "\\}", reponse).replaceAll( reflet(matcher.group(j)) );
		    		
		    		//prof met en mémoire l'équation demandée
		    		if (siEquation)
		    			Prof.memoireEquation.push(matcher.group(j));
		    			
	            }
	            return reponse;
		    }
	    }
		return reponse;
	}
	
	//prof renvoie l équation trouvée et la variable ou les variables de cette équation	
	public static String calcul(String question) {
		String variable, equation;
		String reponse ="Voici le résultat petit scarabé : \n";
		Expression eEquation;
		Simplification simp = new Simplification();
		Derivation df = new Derivation();
		
		if ( getMemoireEquation().size() == 2)
			equation = getMemoireEquation().get(1);
		else
			equation = getMemoireEquation().get(getMemoireEquation().size() - 1);
		
		getVariables(equation);
		
	    if(getMemoireVariable().size() == 0)
			return reponse + "(" + equation + ")' = 0";
	    
		if(getMemoireVariable().size() == 1) {
			variable = getMemoireVariable().pop();
			eEquation = Expression.formuleToExpression(equation);
			eEquation = df.deriver(eEquation, variable);
			reponse += "(" + equation + ")' = " + simp.simplifier(eEquation).asString();
		}
		
		if(getMemoireVariable().size() > 1) {
			eEquation = Expression.formuleToExpression(equation);
			int memoireVariable = getMemoireVariable().size();
			Expression expr; 
			
			for(int i = 0; i < memoireVariable; i++) {
				variable = getMemoireVariable().pop();
				expr = df.deriver(eEquation, variable);
				reponse +="d/d" + variable + "(" + equation + ") = " + simp.simplifier(expr).asString() + "\n";
			}
		}
		
		getMemoireEquation().clear();
		getMemoireVariable().clear();
		return reponse;
		
	}
	

	
	
	//inspiration source https://www.programcreek.com/java-api-examples/java.text.Normalizer
	private static String desAccentuer(String texte) {
		
		//espace de début et de fin supprimé
		texte = texte.trim();
		//on supprime tout separateur des fichiers données
		texte = texte.replaceAll(ExtractionParsing.sep, "");
		//on supprime tous les "[" ou "]"
		texte = texte.replaceAll("\\[", "");
		texte = texte.replaceAll("\\]", "");
		// on désaccentue
	    return texte == null ? null :
	        Normalizer.normalize(texte, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	// pour matcher expression reguliere
	private static Matcher match(String regex, String expression) {
		
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(expression);
	}
	
	//miroir "je" deviens "vous" et reciproquement ...  lorsque  Prof reprend la question
	private static String reflet(String phrase) {
		
		String[] termes = phrase.toLowerCase().split(" ");
	
	    for(int i=0; i < termes.length; i++) {
	    	if (ExtractionParsing.sujetObjet(termes[i]) != "") 
	    		termes[i] = ExtractionParsing.sujetObjet(termes[i]);
	    }

	    return String.join(" ", termes);
	}
	
}


