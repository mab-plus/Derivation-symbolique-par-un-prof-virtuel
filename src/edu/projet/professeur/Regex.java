package edu.projet.professeur;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	
	static String nettoyerQuestion(String str) {

        //  Do some input transformations first.
		str  =str.toLowerCase();	
		//espace de début et de fin supprimé
		str = str.trim();
		//on supprime tous les meta caracteres
        str = supprimerMetaCaractere(str, "@#$%^&*()_-+=~`{[}]|:;<>\\\"","                          "  );
        return str = supprimerMetaCaractere(str, ",?!", "...");
	}
	
	// pour matcher expression reguliere
	static Matcher match(String regex, String expression) {
		
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(expression);
	}
	
	
    private static String supprimerMetaCaractere(String str, String src, String dest) {
        
    	if (src.length() != dest.length()) {
            // impossible 
        }
    	
        for (int i = 0; i < src.length(); i++) {
            str = str.replace(src.charAt(i), dest.charAt(i));
        }
        
        return str;
    }  
    
    static String desaccentuer(String str) {  	
		// on désaccentue
		return Normalizer.normalize(str, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }  
    
   static String getRegex(String str) {
    	
    	//on fabrique les  regex, pour matcher les équations
    	if (str.equals("$e"))
    		return "^([a-zA-Z0-9+\\-*\\/^\\(\\)\\s*]+)$";
    	
    	if (str.equals("$eq")) 
    		return "([a-zA-Z])\\s*\\(([a-zA-Z])\\)\\s*=\\s*([a-zA-Z0-9+\\-*\\/^\\(\\)\\s*]+)";
    	
    	if (str.equals("$fn"))   
    		return "(sin|cos|exp|tan|log)\\s*\\((.*?)\\)\\s*";
    	
    	//on fabrique le premier regex, en remplcant $ * par le motif mais pas @
    	return match("\\s*\\*\\s*|\\$\\s*[^@]", str).replaceAll(  "([\\p{L}\\s\\']*)"   );
    	
    }
    
    static Stack< String> getRegexSynonymes(String str) { 
    	
    	List<String> fichierSynonymes = Fichier.getFichierSynonymes();
    	Stack<String> regex = new Stack<>();
    	System.out.println("str  =" +  str);
   		Pattern p = Pattern.compile("\\@(\\p{L}+)");
		Matcher m = p.matcher(str);
        if(m.find()) {
        	
            for (int i =0; i < fichierSynonymes.size(); i++) {  
            	List<String> synonymes = Arrays.asList(fichierSynonymes.get(i).split("\\|"));
            	
            	
            	if (synonymes.get(0).equals(m.group(1))) {
            		for (int j =0; j < synonymes.size(); j++) { 
            			regex.push( str.replaceAll("\\@(\\p{L}+)", "(" + synonymes.get(j) + ")") );
            		}
                }
            } 
        }

		return regex;
    }
    
    static Stack< String> getRegexEquations() { 
    	
    	List<String> fichierFiltresEquations = Fichier.getFichierFiltresEquations();
    	Stack<String> regex = new Stack<>();
    	
    	for (int i =0; i < fichierFiltresEquations.size(); i++) {
    		List<String> eq = Arrays.asList(fichierFiltresEquations.get(i).split("\\n"));
    		
    		regex.push(eq.get(0));		
    	}

		return regex;
    }	
 
    
}
