package edu.projet.professeur;

import java.util.Arrays;
import java.util.List;

public class Conjugaison {
	
	// on améliore la formulation de la question exemple : tu --> vous pk --> pourquoi etc ... voir fichier 
	static String conjuger(String question, String cheminFichierDeTraitement) {

		List<String> termesQuestion = Arrays.asList(question.toLowerCase().split("\\s+"));
			
			
		for(int i = 0; i < Fichier.getfichier(cheminFichierDeTraitement).size(); i++) {	

			for(int j = 0; j < termesQuestion.size(); j++) {
				
				List<String> prepost = Arrays.asList(Fichier.getfichier(cheminFichierDeTraitement).get(i).split("\\|"));
				if (termesQuestion.get(j).equals(prepost.get(0))) {
					termesQuestion.set(j, prepost.get(1));
				}

			}	
		}
		
		// Après conjugaison, on reforme la chaine pour pouvoir plus tard la splitter en mots clés
		question = String.join(" ", termesQuestion);		
		return  question;
	}
	

}
