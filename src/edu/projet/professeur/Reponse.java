package edu.projet.professeur;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.projet.expressions.Expression;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Reponse {
	
	static List<String> fichierFiltresReponses = Fichier.getFichierFiltresReponses();
	
    /**
     * Pile mémoire de la réponse
     */
    private static Stack<String> memoireReponse;
	

	/**
	 * @param getMotsClesQuestion
	 * @param question
	 * @return
	 * Pour un mot-clé donné, un filtre correspondant est recherché, un regex composé, une question extraite est construite
	 * par les résultats du match du regex sur la question de l'utilisateur.
	 * Le premier trouvé est sélectionné. Si le match ne rend rien, on passe au  mot clé suivant.
	 */
	public static String getReponse (Map<String, Integer> motsClesQuestion, String question) {
		
		//on récupère les mots-clés de la question pour trouver la réponse adéquate
		String reponse = null;
		for (Map.Entry<String, Integer> motCle : motsClesQuestion.entrySet()) {	

			reponse = getReponse(motCle.getKey(), question);
			
			if ( reponse != null) {
				return reponse;
			}
		}	
        //Pas de réponse, on regarde la dernière stocké en mémoire.
        if (memoireReponse != null)
        	return memoireReponse.pop();
        else        
        	//si rien de rien alors.
        	return getReponse("xrien", question);
	}
	

	/**
	 * @param motCleQuestion
	 * @param question
	 * @return
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
	* Renvoyez la réponse.
	*/
	private static String getReponse(String motCleQuestion, String question) {

		String reponse = null;
		for(int i = 0; i < fichierFiltresReponses.size(); i++) {
			//on splitte chaque ligne du fichier FiltresReponses
			List<String> filtresReponses = Arrays.asList(fichierFiltresReponses.get(i).split("\\|"));
	   
		    //si le mot-clé de la question est égale au mot-clé de la ligne du fichier
		    if ( motCleQuestion.equals( filtresReponses.get(0) ) ) { 
		    	//on recupère le filtre
		    	String filtre = filtresReponses.get(2);
		      
		    	//on choisit au hasard une réponse dans la lite des réponses du filtre
		    	reponse = filtresReponses.get( (int) (Math.random() * (filtresReponses.size() - 3) + 3));
		      
		    	String derivee = null;
		    	Stack<String> regex = new Stack<>();
		    	Matcher matcher;

		    	//Si le filtre commence par $ 
		    	if (filtre.matches("^\\$")) {
		    		matcher = Regex.match(Regex.getRegex(filtre), question); 
		    		memoireReponse.push(assemblageReponse(matcher, question)); 
		    	}
		     
		    	//Si la réponse contient goto, on extrait le mot clé derrière le goto et on recommence
		    	//m.group(1) correspond au terme derrière goto
		    	Pattern p = Pattern.compile("goto\\s+(\\p{L}+)");
		    	Matcher m = p.matcher(reponse.trim());
		    	if(m.find()) {
		    		reponse = getReponse(m.group(1), question);
		    	}
		      
		    	//Si le filtre contient dériver 
		    	if (filtre.equals("dériver")) {
		    		regex.push("*");        
		    		derivee = Calcul.getDerivee(question);
		    	}
		    	
		    	//Si on peut encore extraire une équation de la question
		    	if (motCleQuestion.equals("xrien"))
		    	{
			    	String eq = Expression.formuleToExpression(question).asString();
			    	if (eq != "" && !eq.equals(question)) {
			    		System.out.println("eq=" + eq);
			    		regex.push("*");
			    		derivee = Calcul.getDerivee(eq);
			    	}
		    	}

		    	//Si la réponse contient @, on tous les synonymes du terme derrière @
		    	//m.group(1) correspond au terme derrière @
		    	p = Pattern.compile("(@\\p{L}+)");
		    	m = p.matcher(filtre);
		    	if(m.find()) {
		    		regex = Regex.getRegexSynonymes(m.group(1));
		    	}
		    	else
		    		regex.push(filtre);
    			
		    	Iterator<String> regexiTerator = regex.iterator();
		    	while (regexiTerator.hasNext()) { 	    			
		        filtre = regexiTerator.next();
		        filtre = Regex.getRegex(filtre);
		          
		        /* * Si le match donne une reponse non null
		        * * * si il y a une dérivée on renvoie la réponse avec le résultat de la dérivée
		        * * * Sinon juste la réponse
		        * * on essaie une autre regex de la pile
		        * */
		        matcher = Regex.match(filtre, question);
		        String tmp = assemblageReponse(matcher, reponse);
		        if ( tmp != null) {
		        	reponse =tmp;
		        	if (derivee != null)
		        		return reponse + " " + derivee;
		        	else
		        		return reponse; 
		        }
		      }
		    }
		}
		return reponse;
	}

	/**
	 * @param matcher
	 * @param reponse
	 * @return
	 * à partir du matching du regex réussi, on construit la réponse en matchant la numérotation entre parenthèses.
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
