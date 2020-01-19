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

	
	/**
	* Pour un mot-clé donné, un regex correspondant est recherché.
	* Le premier trouvé est sélectionné. Si le match ne rend rien, on passe au  mot clé suivant
	**/
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
	
	private static String setReponse (String motsClesQuestion, String question) {
		/**
		* Décomposer une chaîne selon la clé donnée.
		* Essayez chaque regex si match alors on renvoie la réponse
		* Si le match du regex échoue, essayez un autre regex
		* Si la réponse contient goto, traiter la clé du goto comme une question et on recommence
		* Sinon renvoyez la réponse;
		*/
		System.out.println("3- motsClesQuestion=" + motsClesQuestion);
		List<String> fichierFiltresReponses = Fichier.getFichierFiltresReponses();
		String reponse = null;
		String derivee = null;
		Stack<String> regex = new Stack<>();
		Matcher matcher;
		
		// Essayez chaque regex dans l'ordre.
		for(int i = 0; i < fichierFiltresReponses.size(); i++) {
			
			List<String> filtresReponses = Arrays.asList(fichierFiltresReponses.get(i).split("\\|"));	
			
    		System.out.println("4 -" + i + " : filtresReponses= " + filtresReponses.get(0) + " == " +  motsClesQuestion);
			
			if ( motsClesQuestion.equals( filtresReponses.get(0) ) ) {  

        		//si $ au début du regex d'abord, la réponse est construite normalement, mais enregistré en mémoire et on passe au mot-clé suivant
        		if (filtresReponses.get(2).matches("^\\$")) {
        			regex.push(Regex.getRegex(filtresReponses.get(2)));
        			String reg = regex.pop();
        			matcher = Regex.match(reg, question); 
            		System.out.println("^\\$ = filtresReponses.get(2)= " + reg);
            		//on prend une reponse au hasard
        			reponse = filtresReponses.get( (int) (Math.random() * (filtresReponses.size() - 3) + 3));
        			
        			// Si le match correspond, on met en mémoire la reponse
            		memoireReponse.push(assemblageReponse(matcher, question)); 
        		} 	
        		
    			String reg = filtresReponses.get(2);
    			regex.clear();
    			if (reg.equals("@dériver")) {
    				regex.push("*");
    				derivee = Calcul.getDerivee(question);
            		System.out.println("@dériver = filtresReponses.get(2)= " + reg);
        		}
    			
    			if (reg.matches("^.*\\@(\\w+).*$")) {
    				regex = Regex.getRegexSynonymes(reg);
            		System.out.println("^.*\\@(\\w+).*$ = getRegexSynonymes = " + regex);
        		}
    			else {
    				regex.push(reg);
    				System.out.println("getRegex " + regex  + " ligne " + i);
    			}
    			
    			System.out.println("----------------------------");
	        	System.out.println("Question = " + question );
    			Iterator<String> regexiTerator = regex.iterator();
    	        while (regexiTerator.hasNext()) { 
    	        	reg = regexiTerator.next();
    	        	reg = Regex.getRegex(reg);
    	        	
    	        	System.out.println("Regex= " + reg);
    	            matcher = Regex.match(reg, question);
    	            reponse = filtresReponses.get( (int) (Math.random() * (filtresReponses.size() - 3) + 3));
        			
        			System.out.println("reponse= " + reponse + ": ligne " +  i);
        			
            		//If reponse returned goto, try it
            		Pattern p = Pattern.compile("goto\\s+(\\w+)");
            		Matcher m = p.matcher(reponse);
                    if(m.find()) {
                    	System.out.println("goto= " + m.group(1));
                    	setReponse(m.group(1), question);
                    }
                    
        			reponse = assemblageReponse(matcher, reponse);
        			
                    // Si le match correspond, assemblageQuestion renvoie la reponse
                    if ( reponse != null) {
                    	if (derivee != null)
                    		return reponse + " " + derivee;
                    	else
                    	 return reponse; 
                    }
    	        } 
    	        System.out.println("----------------------------");
        	}
           	//If assembly fails, try another decomposition rule.
        }
        return reponse;  	
    }	
	

	// à partir du matching du regex réussi, on construit le patten de réponse ave le matching
	private static String assemblageReponse(Matcher matcher, String reponse) {
		 
		// S'il y a match on construit la réponse
		while (matcher.find()) {
            for (int j = 0; j <= matcher.groupCount() ; j++) {
                // sous groupe j
	    		if (matcher.group(j) != null) {
	    			
	    			String sousReponse = Conjugaison.conjuger(matcher.group(j).trim() , Fichier.getCheminFichierSujetObjet());
	    			reponse = Regex.match("\\(" + Integer.toString(j) + "\\)", reponse).replaceAll( String.join(" ", sousReponse)   );
	    		}
            }
            // Si réponse, on la retourne
            if (reponse != null)
            		return reponse;
		}
		return null; 
	}	
	
}
