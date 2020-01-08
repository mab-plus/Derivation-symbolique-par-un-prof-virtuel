package edu.projet.Prof;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.projet.calcul.Derivation;
import edu.projet.calcul.Simplification;

import edu.projet.expressions.Expression;

public class Prof {
	
	private static Stack<String> memoire = new Stack<String>();
	
	public static Stack<String> getMemoire() {
		return memoire;
	}

	public static void setMemoire(Stack<String> data) {
		Prof.memoire.addAll(data);
	}
	
	public static String calcul (String question) {
		String f,x, fx;
		
		if ( Prof.getMemoire() != null ) {
			Simplification simp = new Simplification();
			Derivation df = new Derivation();
			
			//filtres deux equations 2 ou 3 maximum captures
			if( Prof.getMemoire().size() == 3) {
				fx = Prof.getMemoire().pop();
				System.out.println("fx=" + fx);
				
				x = Prof.getMemoire().pop();
				System.out.println("x=" + x);
				
				f = Prof.getMemoire().pop();
				System.out.println("f=" + f);	
			}
			else {
				x = Prof.getMemoire().pop();
				System.out.println("x=" + x);
				
				f = Prof.getMemoire().pop();
				System.out.println(f);
				
				fx = f +"(" + x + ")";
				System.out.println("fx=" + fx);	
				
			}
				
	
			System.out.println("+++++++++++++++++++++++++++++");
			   
			Expression expr = Expression.formuleToExpression(fx);
			expr = simp.simplifier(expr);
			expr = df.deriver(expr, x);
			return f + "'" + "(" + x + ")=" + simp.simplifier(expr).asString();
		}
		return "";
	}
	
	//analyse et reponse de la question, au moyen des fichiers filtres 
	public static String analyse(String question) {
		
		List< List<String> > mathsBlaba = ExtractionParsing.mathsBlabla();
		String reponse = "...", regex;
		Matcher matcher;
		question = deAccentuer(question);
		
		System.out.println("\n***** DEbug *****");
		System.out.println("***** question = " + question);
		
		//prof met en mémoire l'équation demandée
		matchEquation(question);
		
	    for(int i = 0; i < mathsBlaba.size(); i++) {
	    	regex = deAccentuer(mathsBlaba.get(i).get(0));
	    	regex = Filtres.regex(regex); 	
	    	matcher = match(regex, question);
	    	
	    	if (matcher.find() ) {
		    	System.out.printf("----> filtre%d = %s\n", i, regex + " <----");
				reponse = mathsBlaba.get(i).get( (int) (Math.random() * (mathsBlaba.get(i).size() - 1) + 1));
	    		
	            if (matcher.groupCount() == 0) {
		            System.out.printf("***** Groupe Zero: %s\n", matcher.group());
	    			return match("\\{1\\}", reponse).replaceAll( reflet(matcher.group()) );
	            }
	    		else {   
					for (int j = 1; j <= matcher.groupCount(); j++) {
						System.out.printf("***** Groupe %d/%d: %s\n", j, matcher.groupCount(), matcher.group(j ));
						reponse = match("\\{" + Integer.toString(j) + "\\}", reponse).replaceAll( reflet(matcher.group(j)) );
					}
					return reponse;
	    		}
		    }
	    	else
		    	System.out.printf("***** filtre%d = %s\n", i, regex);
	    }
		return reponse;
	}
	
	//miroir "je" deviens "vous" et reciproquement ...  lorsque  Prof reprend la question
	public static String reflet(String phrase) {
		
		String[] termes = phrase.toLowerCase().split(" ");
	
	    for(int i=0; i < termes.length; i++) {
	    	if (ExtractionParsing.sujetObjet(termes[i]) != "") 
	    		termes[i] = ExtractionParsing.sujetObjet(termes[i]);
	    }

	    return String.join(" ", termes);
	}
	
	//inspiration source https://www.programcreek.com/java-api-examples/java.text.Normalizer
	private static String deAccentuer(String text) {
		
		//on supprime tout separateur des fichiers données
		text = text.replaceAll(ExtractionParsing.sep, "");
		
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	private static Matcher match(String regex, String expression) {
		
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(expression);
	}
	
	private static void  matchEquation(String eq) {
		String regex = Filtres.$eq.regex;
		Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(eq);
		Stack<String> equation = new Stack<String>();
		
		System.out.println("***** matchEquation equation : " + eq);
		System.out.println("***** matchEquation regex : " + regex);
		if ( matcher.find() ) {
			System.out.println("***** matchEquation matcher.groupCount() : " + matcher.groupCount());
            if (matcher.groupCount() != 0) {
				for (int j = 1; j <= matcher.groupCount(); j++) {
					System.out.printf("***** Groupe %d/%d: %s\n", j, matcher.groupCount(), matcher.group(j ));
					equation.add(matcher.group(j));
				}
            }
    	}
		else {
			regex = Filtres.$fn.regex;
			matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(eq);
			equation = new Stack<String>();
			
			System.out.println("***** matchEquation regex : " + regex);
			if ( matcher.find() ) {
				System.out.println("***** matchEquation matcher.groupCount() : " + matcher.groupCount());
	            if (matcher.groupCount() != 0) {
					for (int j = 1; j <= matcher.groupCount(); j++) {
						System.out.printf("***** Groupe %d/%d: %s\n", j, matcher.groupCount(), matcher.group(j ));
						equation.add(matcher.group(j));
					}
	            }
	    	}
			
		}
		
		Prof.setMemoire(equation);
	}
	
}
