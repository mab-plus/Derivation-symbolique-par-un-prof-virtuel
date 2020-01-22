package edu.projet.professeur;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	/**
	 * entrée(in) utilisateur en minuscule
	 * espace de début et de fin supprimés
	 * suppression de tous les meta caracteres
	 */
	static String nettoyerQuestion(String in) {

		in  =in.toLowerCase();	
		in = in.trim();
        in = remplacerMetaCaractere(in, "@#$%^&*()_-+=~`{[}]|:;<>\\\"","                          "  );
        
        return in = remplacerMetaCaractere(in, ",?!", "...");
	}	
	
    /**
     * On parcourir l'entrée, caractère par caractère
     * on remplace le caractère d'entrée (src) par celui de destination (dest)
     */
	private static String remplacerMetaCaractere(String in, String src, String dest) {
        
    	if (src.length() != dest.length()) {
            // impossible 
        }
    	
        for (int i = 0; i < src.length(); i++) {
            in = in.replace(src.charAt(i), dest.charAt(i));
        }
        
        return in;
    }  
    
	/**
     * https://www.drillio.com/en/2011/java-remove-accent-diacritic/
    */
    static String desaccentuer(String in) {  	
    	
		return Normalizer.normalize(in, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }  
    
    /**
     * On fabrique l'expression régulière (regex) à partir du filtre
    */   
    static String getRegex(String filtre) {
    	
    	//Expressions régulières pour matcher expressions mathématiques
    	if (filtre.equals("$expression"))
    		return "^([a-zA-Z0-9+\\-*\\/^\\(\\)\\s*]+)$";
    	
    	if (filtre.equals("$equation")) 
    		return "([a-zA-Z])\\s*\\(([a-zA-Z])\\)\\s*=\\s*([a-zA-Z0-9+\\-*\\/^\\(\\)\\s*]+)";
    	
    	if (filtre.equals("$fonction"))   
    		return "(sin|cos|exp|tan|log)\\s*\\((.*?)\\)\\s*";
    	
    	//Expressions régulières pour matcher autres sauf celles commençant par @
    	String regex = "([\\\\p{L}\\\\s']*)";
    	return match("\\s*\\*\\s*|\\$\\s*[^@]", filtre).replaceAll(  regex   );
    	
    }
    
    /**
     * Filtres commençant par @
     * On récupère les synonymes
     * On fabrique les expressions régulières (regex) à partir de chaque synonyme
     */ 
    
    static Stack< String> getRegexSynonymes(String filtre) { 
    	
    	List<String> fichierSynonymes = Fichier.getFichierSynonymes();
    	Stack<String> regex = new Stack<>();
    	System.out.println("filtre  =" +  filtre);
   		Pattern p = Pattern.compile("\\@(\\p{L}+)");
		Matcher m = p.matcher(filtre);
        if(m.find()) {
        	
            for (int i =0; i < fichierSynonymes.size(); i++) {  
            	List<String> synonymes = Arrays.asList(fichierSynonymes.get(i).split("\\|"));
            	
            	
            	if (synonymes.get(0).equals(m.group(1))) {
            		for (int j =0; j < synonymes.size(); j++) { 
            			regex.push( filtre.replaceAll("\\@(\\p{L}+)", "(" + synonymes.get(j) + ")") );
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
    
	/**
	 * fonction de matching à partir d'une expression régulière
	 */
	static Matcher match(String regex, String expression) {
		
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(expression);
	}
 
    
}
