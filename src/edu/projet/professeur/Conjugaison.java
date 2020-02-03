package edu.projet.professeur;

import java.util.Arrays;
import java.util.List;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Conjugaison {
	


	/**
	 * @param cheminFichierDeTraitement
	 * @param question
	 * l'entrée utilisateur est formatée de façon à améliorer la formulation exemple : 
	 * tu --> vous 
	 * pk --> pourquoi 
	 * etc ... 
	 * voir fichier "conjugaison-traduction.csv".
	 * 
	 * @return la réponse est formatée de façon refleter le sujet en objet d'une partie de l'entrée utilisateur exemple :
	 * entrée : je pense que je suis perdu
	 * Professeur match (je suis perdu) -> traitement (vous êtes perdu)
	 * réponse: Pourqoui pensez-vous que (vous êtes perdu) 
	 * voir fichier "sujet-objet.csv".
	 * 
	 **/
	static String conjuger(String question, String cheminFichierDeTraitement) {

		/*
		 * on décompose l'entrée utilisateur en mots 
		 * exemple : Je voudrais savoir ... ->  [je, voudrais, savoir, ..]
		 * 
		*/
		List<String> termesQuestion = Arrays.asList(question.toLowerCase().split("\\s+"));
		//fichier de conjugaison par exemple
		List<String> FichierDeTraitement = Fichier.getfichier(cheminFichierDeTraitement);
		
		/*
		 * on remplace chaque mots par son correspondant dans le fichier cheminFichierDeTraitement
		 * en entrée "conjugaison-traduction.csv"
		 * en sortie "sujet-objet.csv"
		 * 
		*/
		for(int i = 0; i < termesQuestion.size(); i++) {
			
			for(int j = 0; j < FichierDeTraitement.size(); j++) {	
				List<String> prepost = Arrays.asList(FichierDeTraitement.get(j).split("\\|"));

				if (termesQuestion.get(i).equals(prepost.get(0))) {
					termesQuestion.set(i, prepost.get(1));
					//on trouve une correspondance on sort du fichier de traitement
					break;
				}

			}
		}

		/* On reforme la chaîne pour pouvoir plus tard la splitter en mots clés*/
		question = String.join(" ", termesQuestion);		
		return  question;
	}
	
	/**
	 * @param reponse
	 * @return une meilleure orthographe de la réponse
	 */
	static String orthographe(String reponse) {
		
		reponse = reponse.replaceAll("(j)e\\s+([aeiou])", "$1'$2");
		reponse = reponse.replaceAll("(m)e\\s+([aeiou])", "$1'$2");
		return  reponse;
	}
	

}
