package edu.projet.Prof;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.projet.expressions.Expression;

public class Prof {
	
	Stack<Expression> memoire = new Stack<Expression>(); 

	public static String reflet(String phrase) {
		
		String[] termes = phrase.toLowerCase().split(" ");
	
	    for(int i=0; i < termes.length; i++) {
	    	if (SujetObjet.sujetObjet(termes[i]) != "") 
	    		termes[i] = SujetObjet.sujetObjet(termes[i]);
	    }

	    return String.join("", termes);
	}
	
	public static String analyse(String question) {
		
		List< List<String> > mathsBlaba = MathsBlaba.mathsBlaba();
		String reponse = "...", regex;
		Matcher matcher;
		question = deAccentuer(question);
		System.out.println("phrase = " + question);
	    for(int i = 0; i < mathsBlaba.size(); i++) {
	    	regex = deAccentuer(mathsBlaba.get(i).get(0));
	    	regex = Filtres.regex(regex);
	    	
	    	System.out.println("filtre = " + regex);
	    	matcher = match(regex, question);
	    	
	    	if (matcher.find() ) {
				reponse = mathsBlaba.get(i).get( (int) (Math.random() * (mathsBlaba.get(i).size() - 1) + 1));
	    		
	            if (matcher.groupCount() == 0) {
		            System.out.printf("Group Zero: %s\n", matcher.group());
	    			return match("\\{0\\}", reponse).replaceAll( reflet(matcher.group()) );
	            }
	    		else {   
					for (int j = 1; j <= matcher.groupCount(); j++) {
						System.out.printf("Group %d: %s\n", j, matcher.group(j));
						reponse = match("\\{" + Integer.toString(j) + "\\}", reponse).replaceAll( reflet(matcher.group(j)) );
					}
					return reponse;
	    		}
		    }
	    }
	    
		return reponse;
	}
	
	//inspiration source https://www.programcreek.com/java-api-examples/java.text.Normalizer
	private static String deAccentuer(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	private static Matcher match(String regex, String expression) {
		
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(expression);
	}
	
	private static String matchEquation(String equation) {
		String regex = Filtres.$eq.regex;
		Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(equation);
		
		System.out.println("matchEquation equation : " + equation);
		System.out.println("matchEquation regex : " + regex);
		if ( matcher.find() ) {
			System.out.println("matchEquation matcher.groupCount() : " + matcher.groupCount());
            if (matcher.groupCount() == 0)
    			return equation;
            
    		else
    			return matcher.group(2);  
    	}
		
		return "";
	}
}
