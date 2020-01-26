package edu.projet.professeur;

import java.util.Arrays;
import java.util.List;

public class Conjugaison {
	

	/**
	 * l'entrée utilisateur est formatée de façon à améliorer la formulation exemple : 
	 * tu --> vous 
	 * pk --> pourquoi 
	 * etc ... 
	 * voir fichier "conjugaison-traduction.csv"
	 * 
	 * En sortie, la réponse est formatée de façon refleter le sujet en objet d'une partie de l'entrée utilisateur exemple :
	 * entrée : je pense que je suis perdu
	 * Professeur match (je suis perdu) -> traitement (vous êtes perdu)
	 * réponse: Pourqoui pensez-vous que (vous êtes perdu) 
	 * voir fichier "sujet-objet.csv"
	 * 
	 **/
	static String conjuger(String question, String cheminFichierDeTraitement) {

		/*
		 * on décompose l'entrée utilisateur en mots 
		 * exemple : Je voudrais savoir ... ->  [je, voudrais, savoir, ..]
		 * 
		*/
		List<String> termesQuestion = Arrays.asList(question.toLowerCase().split("\\s+"));
					
		for(int i = 0; i < Fichier.getfichier(cheminFichierDeTraitement).size(); i++) {	

			/*
			 * on remplace chaque mots par son correspondant dans le fichier cheminFichierDeTraitement
			 * en entrée "conjugaison-traduction.csv"
			 * en sortie "sujet-objet.csv"
			 * 
			*/
			for(int j = 0; j < termesQuestion.size(); j++) {
				List<String> prepost = Arrays.asList(Fichier.getfichier(cheminFichierDeTraitement).get(i).split("\\|"));
				
				if (termesQuestion.get(j).equals(prepost.get(0))) {
					termesQuestion.set(j, prepost.get(1));
				}
			}	
			
		}
		
		/* On reforme la chaîne pour pouvoir plus tard la splitter en mots clés*/
		question = String.join(" ", termesQuestion);		
		return  question;
	}
	

}
