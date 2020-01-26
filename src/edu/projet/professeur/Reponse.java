package edu.projet.professeur;

import java.util.ArrayList;
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
	private static String setReponse (String motCleQuestion, String question) {
		
		
		List<String> filtreReponse = getFiltreReponse(motCleQuestion);
		
		String filtre = filtreReponse.get(0);
		String reponse = filtreReponse.get(1);
		String derivee = null;
		Stack<String> regex = new Stack<>();
		Matcher matcher;
		
		regex.clear();
			
		//Si le filtre commence par $ 
    	if (filtre.matches("^\\$")) {
    		System.out.println("le filtre commence par $  = filtresReponses.get(2)= " + filtre);
    		matcher = Regex.match(Regex.getRegex(filtre), question); 
        	memoireReponse.push(assemblageReponse(matcher, question)); 
    	} 	
		
    	//Si la réponse contient goto, on extrait le mot clé derrière le goto et on recommence
		Pattern p = Pattern.compile("goto\\s+(\\p{L}+)");
		Matcher m = p.matcher(reponse);
        if(m.find()) {
        	System.out.println("goto= " + m.group(1));
        	//setReponse(m.group(1), question);
        	System.out.println("***filtre= " + filtre);
			System.out.println("***question= " + question);
			System.out.println("***reponse= " + reponse);
			
			filtreReponse = getFiltreReponse(m.group(1));
			filtre = filtreReponse.get(0);
			reponse = filtreReponse.get(1);	
        }
        
    	//Si le filtre contient @dériver 
		if (filtre.equals("@dériver")) {
        	System.out.println("le filtre contient @dériver = filtresReponses.get(2)= " + filtre);
			regex.push("*");
			derivee = Calcul.getDerivee(question);
    	}
		
		//Si le filtre contient @
		if (filtre.matches("^.*\\@")) {
        	System.out.println("le filtre contient @ = getRegexSynonymes = " + regex);
			regex = Regex.getRegexSynonymes(filtre);
    	}
		else {
			regex.push(filtre);
			System.out.println("getRegex " + regex  + " ligne ");
		}
		
		System.out.println("------------On parcourt la pile des regex----------------");
		Iterator<String> regexiTerator = regex.iterator();
		while (regexiTerator.hasNext()) { 
			filtre = regexiTerator.next();
			filtre = Regex.getRegex(filtre);
			
        	System.out.println("w + Regex= " + filtre);
            matcher = Regex.match(filtre, question);
        	System.out.println("w + reponse =" + reponse);
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
			System.out.println("-------------On parcourt la pile des regex ---------------");
		}	
		// Renvoyez la réponse
        return reponse;
    }
	
	
	private static List<String> getFiltreReponse(String motCleQuestion) {
		List<String> fichierFiltresReponses = Fichier.getFichierFiltresReponses();
		List<String> resultat = new ArrayList<>();
		
		for(int i = 0; i < fichierFiltresReponses.size(); i++) {
			
			List<String> filtresReponses = Arrays.asList(fichierFiltresReponses.get(i).split("\\|"));
			
			if ( motCleQuestion.equals( filtresReponses.get(0) ) ) { 
				//filtre
				resultat.add(filtresReponses.get(2));
				//reponse
				resultat.add(filtresReponses.get( (int) (Math.random() * (filtresReponses.size() - 3) + 3)));
				break;
			}
			
		}
		return resultat;
		
	}
	
	

	/**
	 * à partir du matching du regex réussi, on construit la réponse en matchant la numérotation entre parenthèses
	 */
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
