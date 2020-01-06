package edu.projet.LeProf;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeProf {
	

	public static String reflet(String phrase) {
		
		String[] termes = phrase.toLowerCase().split(" ");
	
	    for(int i=0; i < termes.length; i++) {
	    	if (SujetObjet.sujetObjet(termes[i]) != "") 
	    		termes[i] = SujetObjet.sujetObjet(termes[i]);
	    }

	    return String.join("", termes);
	}
	
	public static String analyse(String phrase) {
		
		List< List<String> > mathsBlaba = MathsBlaba.mathsBlaba();
		String response = "...";
		Matcher matcher;
		phrase = deAccentuer(phrase);
	    for(int i = 0; i < mathsBlaba.size(); i++) { 
	    	matcher = match(mathsBlaba.get(i).get(0), phrase);
	    	
	    	if (matcher.find() ) {	  
		    	System.out.println(matcher.groupCount());
				response = mathsBlaba.get(i).get( (int) (Math.random() * (mathsBlaba.get(i).size() - 1) + 1));
	    		
	            if (matcher.groupCount() == 0) {
		            System.out.printf("Group Zero Text: %s\n", matcher.group());
	    			return match("\\{0\\}", response).replaceAll( reflet(matcher.group()) );
	            }
	    		else {   
					for (int j = 1; j <= matcher.groupCount(); j++) {
						System.out.printf("Group %d Text: %s\n", j, matcher.group(j));
						response = match("\\{" + Integer.toString(j-1) + "\\}", response).replaceAll( reflet(matcher.group(j)) );
					}
					return response;	    			
	    		}
		    }
	    }
		return response;
	}
	
	private static Matcher match(String regex, String str) {
		
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str);
	}
	
	
	//inspiration source https://www.programcreek.com/java-api-examples/java.text.Normalizer
	public static String deAccentuer(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
