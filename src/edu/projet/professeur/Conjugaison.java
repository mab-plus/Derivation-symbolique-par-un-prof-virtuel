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
	 *            l'entrée utilisateur est formatée de façon à améliorer la
	 *            formulation exemple : tu --> vous pk --> pourquoi etc ... voir
	 *            fichier "conjugaison-traduction.csv".
	 * 
	 * @return la réponse est formatée de façon refleter le sujet en objet d'une
	 *         partie de l'entrée utilisateur exemple : entrée : je pense que je
	 *         suis perdu Professeur match (je suis perdu) -> traitement (vous
	 *         êtes perdu) réponse: Pourqoui pensez-vous que (vous êtes perdu)
	 *         voir fichier "sujet-objet.csv".
	 * 
	 **/
	static String conjuger(String question, String cheminFichierDeTraitement) {

		/*
		 * on décompose l'entrée utilisateur en mots exemple : Je voudrais
		 * savoir ... -> [je, voudrais, savoir, ..]
		 * 
		 */
		question = question.replaceAll("-vous", " vous");
		question = question.replaceAll("-tu", " tu");

		List<String> termesQuestion = Arrays
				.asList(question.toLowerCase().split("\\s+"));
		// fichier de conjugaison par exemple
		List<String> FichierDeTraitement = Fichier
				.getfichier(cheminFichierDeTraitement);
		/*
		 * on remplace chaque mots par son correspondant dans le fichier
		 * cheminFichierDeTraitement en entrée "conjugaison-traduction.csv" en
		 * sortie "sujet-objet.csv"
		 * 
		 */
		for (int i = 0; i < termesQuestion.size(); i++) {

			for (int j = 0; j < FichierDeTraitement.size(); j++) {
				List<String> prepost = Arrays
						.asList(FichierDeTraitement.get(j).split("\\|"));

				if (termesQuestion.get(i).equals(prepost.get(0))) {
					termesQuestion.set(i, prepost.get(1));
					// on trouve une correspondance on sort du fichier de
					// traitement
					break;
				}
			}
		}

		/*
		 * On reforme la chaîne pour pouvoir plus tard la splitter en mots clés
		 */
		question = String.join(" ", termesQuestion);

		if (Professeur.activeTrace)
			System.out.println(
					"class Conjugaison:Conjugaison.conjuger(" + question + ","
							+ cheminFichierDeTraitement + ")=" + question);
		return question;
	}

	/**
	 * @param reponse
	 * @return une meilleure orthographe de la réponse
	 */
	static String orthographe(String reponse) {

		reponse = reponse.replaceAll("(j)e\\s+([aeiou])", "$1'$2");
		reponse = reponse.replaceAll("(m)e\\s+([aeiou])", "$1'$2");
		reponse = reponse.replaceAll("(n)e\\s+([aeiouê])", "$1'$2");
		reponse = reponse.replaceAll("(moi)\\s+(\\p{L}+(er|ir|oir|re))",
				"me $2");

		reponse = reponse.replaceAll("(vous)\\s+(\\p{L}+)(rais)",
				"vous $2riez");
		reponse = reponse.replaceAll("(vous)\\s+(\\p{L}+)(rais)",
				"vous $2riez");

		reponse = reponse.replaceAll("\\,", ",");
		reponse = reponse.replaceAll("' ", "'");
		reponse = reponse.replaceAll("à [Ll]es ", "aux ");
		reponse = reponse.replaceAll("à [Ll]e ", "au ");
		reponse = reponse.replaceAll(" en [Ll]e ", " en ");
		reponse = reponse.replaceAll(" de ([AEIOUHYaeiouhyéèêàùäëïöüœ])i",
				" d'$1");
		reponse = reponse.replaceAll(" que ([AEIOUHYaeiouhyéèêàùäëïöüœ])i",
				" qu'$1");
		reponse = reponse.replaceAll("plus des ([aeiouhyéèêàùäëïöüœ])i",
				"plus d'$1");
		reponse = reponse.replaceAll(" de [Ll]es ", " des ");
		reponse = reponse.replaceAll(" de de(s)? ([aeiouéèêàîïùyhœ])i",
				" d'$2");
		reponse = reponse.replaceAll(" de de(s)? ([^aeiouéèêàîïùyhœ])i",
				" de $2");
		reponse = reponse.replaceAll(" de [Dd]'", " d'");
		reponse = reponse.replaceAll(" de ([Ll]e|du) ", " du ");

		if (Professeur.activeTrace)
			System.out.println("class Conjugaison:Conjugaison.orthographe("
					+ reponse + "=" + reponse);
		return reponse;
	}

}
