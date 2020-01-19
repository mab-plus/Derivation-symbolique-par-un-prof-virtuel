package edu.projet.professeur;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reponse {
	
    private static Stack<String> memoireReponse;

	
	/*
	* Pour un mot-clé donné, un filtre correspondant est recherché, un regex composé, une question extraite est construite
	* par les résultats du match du regex sur la question de l'utilisateur.
	* Le premier trouvé est sélectionné. Si le match ne rend rien, on passe au  mot clé suivant
	*/
	public static String getReponse (Map<String, Integer> getMotsClesQuestion, String question) {
		
		//on récupère les mots-clés de la question pour trouver la réponse adéquate
		String reponse = null;
		for (Map.Entry<String, Integer> motCle : getMotsClesQuestion.entrySet()) {	

			reponse = setReponse(motCle.getKey(), question);
			
			if ( reponse != null) {
				return reponse;
			}
		}	

        //Pas de péponse, on regarde la dernière stocké en mémoire.
        if (memoireReponse != null)
        	return memoireReponse.pop();
        else
        	setReponse("xrien", question);
        
        //si rien de rien alors.
        return "Quoi vous dire.... !";
	}
	
	/**
	* Pour chaque ligne du fichier filtres réponses on extrait un un mot-clé pour comparaison au mot-clé utilisateur
	* si la comparaison réussi, on extrait le filtre pour fabriquer l'expression régulière (regex)
	* 
	* Si le filtre commence par $ 
	* * on construit le regex, si le match réussi on mémorise la réponse ou sinon null 
	* 
	* Si le filtre contient @dériver 
	* * on met dans une pile le regex construit
	* * on cherche une fonction à dériver, si oui, on garde le résultat de la dérivation
	* 
	* Si le filtre contient @
	* * on met dans une pile les regex construit à partir de l'extraction des synonymes
	*
	* On parcourt la pile des regex
	* * On retire un regexde la pile
	* * on match
	* * On prend au hasard une réponse dans les réponses correspondantes au filtre choisi
	* * Si la réponse contient goto, on extrait le mot clé derrière le goto et on recommence
	* * Si le match donne une reponse non null
	* * * si il y a une dérivée on renvoie la réponse avec le résultat de la dérivée
	* * * Sinon juste la réponse
	* * on essaie une autre regex de la pile
	* Renvoyez la réponse;
	*/
	private static String setReponse (String motsClesQuestion, String question) {
		System.out.println("3- motsClesQuestion=" + motsClesQuestion);
		List<String> fichierFiltresReponses = Fichier.getFichierFiltresReponses();
		String reponse = null;
		String derivee = null;
		Stack<String> regex = new Stack<>();
		Matcher matcher;
		
		//Pour chaque ligne du fichier filtres réponses on extrait un un mot-clé pour comparaison au mot-clé utilisateur
		for(int i = 0; i < fichierFiltresReponses.size(); i++) {
			
			List<String> filtresReponses = Arrays.asList(fichierFiltresReponses.get(i).split("\\|"));	
			
			System.out.println("filtresReponses" + i  + "=" + filtresReponses.get(0) + " == " +  motsClesQuestion);
			
			//si la comparaison réussi, on extrait le filtre pour fabriquer l'expression régulière (regex)
			if ( motsClesQuestion.equals( filtresReponses.get(0) ) ) {  

    			String reg = filtresReponses.get(2);
    			regex.clear();
    			
				//Si le filtre commence par $ 
        		if (reg.matches("^\\$")) {
            		System.out.println("le filtre commence par $  = filtresReponses.get(2)= " + reg);
        			matcher = Regex.match(Regex.getRegex(reg), question); 
        			reponse = filtresReponses.get( (int) (Math.random() * (filtresReponses.size() - 3) + 3));
            		memoireReponse.push(assemblageReponse(matcher, question)); 
        		} 	
        		//Si le filtre contient @dériver 
    			if (reg.equals("@dériver")) {
            		System.out.println("le filtre contient @dériver = filtresReponses.get(2)= " + reg);
    				regex.push("*");
    				derivee = Calcul.getDerivee(question);
        		}
    			//Si le filtre contient @
    			if (reg.matches("^.*\\@(\\w+).*$")) {
            		System.out.println("le filtre contient @ = getRegexSynonymes = " + regex);
    				regex = Regex.getRegexSynonymes(reg);
        		}
    			else {
    				regex.push(reg);
    				System.out.println("getRegex " + regex  + " ligne " + i);
    			}
    			
    			System.out.println("------------On parcourt la pile des regex----------------");
    			Iterator<String> regexiTerator = regex.iterator();
    	        while (regexiTerator.hasNext()) { 
    	        	reg = regexiTerator.next();
    	        	reg = Regex.getRegex(reg);
    	        	
    	        	System.out.println("Regex= " + reg);
    	            matcher = Regex.match(reg, question);
    	            reponse = filtresReponses.get( (int) (Math.random() * (filtresReponses.size() - 3) + 3));
        			
    	            //Si la réponse contient goto, on extrait le mot clé derrière le goto et on recommence
            		Pattern p = Pattern.compile("goto\\s+(\\w+)");
            		Matcher m = p.matcher(reponse);
                    if(m.find()) {
                    	System.out.println("goto= " + m.group(1));
                    	setReponse(m.group(1), question);
                    }
                    
        			reponse = assemblageReponse(matcher, reponse);	
        			/* * Si le match donne une reponse non null
        			* * * si il y a une dérivée on renvoie la réponse avec le résultat de la dérivée
        			* * * Sinon juste la réponse
        			* * on essaie une autre regex de la pile
        			* */
                    if ( reponse != null) {
                    	
                    	if (derivee != null)
                    		return reponse + " " + derivee;
                    	else
                    		return reponse; 
                    }
                    
    	        } 
    	        System.out.println("-------------On parcourt la pile des regex ---------------");
        	}
        }
		// Renvoyez la réponse
        return reponse;  	
    }	
	

	// à partir du matching du regex réussi, on construit la réponse en matchant la numérotation entre parenthèses
	private static String assemblageReponse(Matcher matcher, String reponse) {
		 
		while (matcher.find()) {
            for (int j = 0; j <= matcher.groupCount() ; j++) {
                
            	// sous groupe j matché
	    		if (matcher.group(j) != null) {
	    			
	    			//Seconde substitution, elle permute le sujet et l’objet
	    			String sousReponse = Conjugaison.conjuger(matcher.group(j).trim() , Fichier.getCheminFichierSujetObjet());
	    			//On match la numérotation entre parenthèses avec les groupes trouvés
	    			reponse = Regex.match("\\(" + Integer.toString(j) + "\\)", reponse).replaceAll( String.join(" ", sousReponse)   );
	    		}
            }
            // Si réponse, on la retourne
            if (reponse != null)
            		return reponse;
		}
		//Sinon null
		return null; 
	}	
	
}
