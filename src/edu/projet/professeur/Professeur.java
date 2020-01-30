package edu.projet.professeur;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Professeur {
	
    /**
     * Pour tester s'il répète la même question
     */
    private static String QuestionPrecedente ="";
	
    /**
     * @param question
     * @return la réponse du Professeur.
     */
    public static String reponse (String question) {
    	
    	Map<String, Integer> motsClesQuestion = new HashMap<>();
    	question=  Conjugaison.conjuger(question, Fichier.getCheminFichierConjugaison());
    	
    	if (QuestionPrecedente.equals(question))
         	motsClesQuestion.put("xrepetition", -1);
    	else {
         	motsClesQuestion = MotsCles.getMotsClesQuestion (question);
         	QuestionPrecedente = question;
    	}


    	return Reponse.getReponse(motsClesQuestion, question);
     }
}
