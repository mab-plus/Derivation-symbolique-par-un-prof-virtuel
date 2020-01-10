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
	public static String calcul (String question) {
		String variable, equation;
		Expression eEquation;
		String reponse ="Voici le résultat petite tête :=), ";
		
		System.out.println("getMemoireEquation()=" + getMemoireEquation().size() );
		
		if ( getMemoireEquation().size() == 0)
			return "";
		else {
			Simplification simp = new Simplification();
			Derivation df = new Derivation();
			
			if ( getMemoireEquation().size() == 1) {
				
                equation = Prof.getMemoireEquation().pop();
                getVariables(equation);
                System.out.println(equation);
				if(getMemoireVariable() == null )
					return reponse + "(" + equation + ")' = 0";
				
				else if(getMemoireVariable().size() == 1) {
					variable = getMemoireVariable().pop();
					eEquation = Expression.formuleToExpression(equation);
					eEquation = df.deriver(eEquation, variable);
					return  reponse + "(" + equation + ")' = " + simp.simplifier(eEquation).asString();
				}
				
				else {
					reponse = "J'ai plusieurs variables pour cette équation ! ";
					int g = getMemoireVariable().size();
					for(int i = 0; i < g - 1 ; i++)
						reponse+= ", " + getMemoireVariable().pop();
					reponse+= " et " + getMemoireVariable().pop();
					
					return reponse;
				}
			}
			
			else {
				equation = Prof.getMemoireEquation().pop();
				getVariables(equation);
				
                System.out.println(equation);
				if(getMemoireVariable() == null )
					return reponse + "(" + equation + ")' = 0";
				
				else if(getMemoireVariable().size() == 1) {
					variable = getMemoireVariable().pop();
					eEquation = Expression.formuleToExpression(equation);
					eEquation = df.deriver(eEquation, variable);
					return  reponse + "(" + equation + ")' = " + simp.simplifier(eEquation).asString();
				}
				
				else {
					reponse = "J'ai plusieurs variables pour cette équation : \n";
					int g = getMemoireVariable().size();
					eEquation = Expression.formuleToExpression(equation);
					Expression e;
					for(int i = 0; i < g; i++) {
						variable = getMemoireVariable().pop();
						e = df.deriver(eEquation, variable);
						reponse += "dérivée par rapport  à " + variable + " : (" + equation + ")' = " + simp.simplifier(e).asString() + "\n";
					}

					return reponse;
				}
			}
		}

	}
	
	
	//inspiration source https://www.programcreek.com/java-api-examples/java.text.Normalizer
	private static String desAccentuer(String texte) {
		
		//espace de début et de fin supprimé
		texte = texte.trim();
		//on supprime tout separateur des fichiers données
		texte = texte.replaceAll(ExtractionParsing.sep, "");
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


