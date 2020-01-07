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
		
		if ( Prof.getMemoire() != null ) {
			Simplification simp = new Simplification();
			Derivation df = new Derivation();
			
			String fn = Prof.getMemoire().pop();
			String dx = Prof.getMemoire().pop();
			String f = Prof.getMemoire().pop();
			
			Expression expr = Expression.formuleToExpression(fn);
			expr = simp.simplifier(expr);
			expr = df.deriver(expr, dx);
			return f + "'" + "(" + dx + ")=" + simp.simplifier(expr).asString();
		}
		return "";
	}
	
	public static String analyse(String question) {
		
		List< List<String> > mathsBlaba = MathsBlaba.mathsBlaba();
		String reponse = "...", regex;
		Matcher matcher;
		question = deAccentuer(question);
		matchEquation(question);
		
		System.out.println("***** phrase = " + question);
		
	    for(int i = 0; i < mathsBlaba.size(); i++) {
	    	regex = deAccentuer(mathsBlaba.get(i).get(0));
	    	regex = Filtres.regex(regex);
	    	
	    	System.out.println("***** filtre = " + regex);
	    	matcher = match(regex, question);
	    	
	    	if (matcher.find() ) {
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
	    }
		return reponse;
	}
	
	//miroir "je" deviens "vous" et reciproquement ...  lorsque  Prof reprend la question
	public static String reflet(String phrase) {
		
		String[] termes = phrase.toLowerCase().split(" ");
	
	    for(int i=0; i < termes.length; i++) {
	    	if (SujetObjet.sujetObjet(termes[i]) != "") 
	    		termes[i] = SujetObjet.sujetObjet(termes[i]);
	    }

	    return String.join("", termes);
	}
	
	//inspiration source https://www.programcreek.com/java-api-examples/java.text.Normalizer
	private static String deAccentuer(String text) {
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
		System.out.println("\n***** DEbug *****");
		if ( matcher.find() ) {
			System.out.println("***** matchEquation equation : " + eq);
			System.out.println("***** matchEquation regex : " + regex);
			System.out.println("***** matchEquation matcher.groupCount() : " + matcher.groupCount());
            if (matcher.groupCount() != 0) {
            	equation.add(matcher.group(1));
            	equation.add(matcher.group(2));
            	equation.add(matcher.group(3));
            }
    	}		
		Prof.setMemoire(equation);
	}
}
