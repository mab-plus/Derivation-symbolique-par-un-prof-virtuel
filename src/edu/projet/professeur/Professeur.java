package edu.projet.professeur;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Professeur {

	public static String user;

	/**
	 * Pour tester si l'utilisateur répète la même question
	 */
	private static String questionPrecedente = "";

	/**
	 * @param question
	 * @return la réponse du Professeur.
	 */
	public static String reponse(String question, String sender) {

		Professeur.user = sender;
		Map<String, Integer> motsClesQuestion = new HashMap<>();
		question = Conjugaison.conjuger(question, Fichier.getCheminFichierConjugaison());

		if (questionPrecedente.equals(question))
			motsClesQuestion.put("xrepetition", -1);
		else {
			motsClesQuestion = MotsCles.getMotsClesQuestion(question);
			questionPrecedente = question;
		}
		return Reponse.getReponse(motsClesQuestion, question);
	}
}
