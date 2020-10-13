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
	 * Pile mémoire des réponses des filtres $, utilisées en cas d'absences totales
	 * de réponses
	 */
	private static Stack<String> memoireReponse;

	/**
	 * pour stocker l'index de la derniere réponse récupérée d'un filtre, 3 étant la
	 * première réponse possible voir fichier filtres-reponses.csv
	 */
	private static int indexDerniereReponse = 3;
	/**
	 * idem index du filtre
	 */
	private static int indexDernierFiltre = 0;

	/**
	 * @param getMotsClesQuestion
	 * @param question
	 * @return Pour un mot-clé donné, un filtre correspondant est recherché, un
	 *         regex composé, une question extraite est construite par les résultats
	 *         du match du regex sur la question de l'utilisateur. Le premier trouvé
	 *         est sélectionné. Si le match ne rend rien, on passe au mot clé
	 *         suivant.
	 */
	public static String getReponse(Map<String, Integer> motsClesQuestion, String question) {

		System.out.println("1-class Reponse:String getReponse (" + motsClesQuestion + ", " + question + ")");
		// on récupère les mots-clés de la question pour trouver la réponse adéquate
		String[] reponse = { null, "" };
		String derivee;

		for (Map.Entry<String, Integer> motCle : motsClesQuestion.entrySet()) {
			reponse = getReponse(motCle.getKey(), question);
			derivee = reponse[1];
			if (reponse[0] != null) {
				// on match le tag (dv) dans la réponse pour y mettre le résultat de la dérivée
				// ou rien
				return reponse[0].replaceAll("\\(dv\\)", derivee);
			}
		}

		// Pas de réponse, on retourne une réponse $ stockée
		if (memoireReponse != null)
			return memoireReponse.pop();
		else {
			// si rien de rien alors.
			if (memoireReponse != null)
				return memoireReponse.pop();
			else {
				reponse = getReponse("xrien", question);
				derivee = reponse[1];
				return reponse[0].replaceAll("\\(dv\\)", derivee);
			}

		}
	}

	/**
	 * @param motCleQuestion
	 * @param question
	 * @return Pour chaque ligne du fichier filtres réponses on extrait un un
	 *         mot-clé pour comparaison au mot-clé utilisateur si la comparaison
	 *         réussi, on extrait le filtre pour fabriquer l'expression régulière
	 *         (regex)
	 * 
	 *         Si le filtre commence par $ * on construit le regex, si le match
	 *         réussi on mémorise la réponse ou sinon null
	 * 
	 *         Si le filtre contient @dériver * on met dans une pile le regex
	 *         construit * on cherche une fonction à dériver, si oui, on garde le
	 *         résultat de la dérivation
	 * 
	 *         Si le filtre contient @ * on met dans une pile les regex construit à
	 *         partir de l'extraction des synonymes
	 *
	 *         On parcourt la pile des regex * On retire un regexde la pile * on
	 *         match * On prend au hasard une réponse dans les réponses
	 *         correspondantes au filtre choisi * Si la réponse contient goto, on
	 *         extrait le mot clé derrière le goto et on recommence * Si le match
	 *         donne une reponse non null * * si il y a une dérivée on renvoie la
	 *         réponse avec le résultat de la dérivée * * Sinon juste la réponse *
	 *         on essaie une autre regex de la pile Renvoyez la réponse.
	 */

	private static String[] getReponse(String motCleQuestion, String question) {

		System.out.println("2-class Reponse:String[] getReponse (" + motCleQuestion + ", " + question + ")");
		System.out.println("**********************************");
		// on y stocke la réponse et une éventuelle dérivée
		String[] reponse = { null, "" };

		for (int i = 0; i < fichierFiltresReponses.size(); i++) {

			// on splitte chaque ligne du fichier FiltresReponses
			List<String> filtresReponses = Arrays.asList(fichierFiltresReponses.get(i).split("\\|"));

			// si le mot-clé de la question est égale au mot-clé de la ligne du fichier
			if (motCleQuestion.equals(filtresReponses.get(0))) {
				System.out.println("\nligne=" + i);
				System.out.println("3-class Reponse:String[] getReponse motCleQuestion=filtre:(" + motCleQuestion + "="
						+ filtresReponses.get(0) + ")");
				// on choisit la réponse suivante dans la liste des réponses du filtre avant de
				// revenir au point de départ
				int indexEnCours = 3;
				if (indexDernierFiltre == i) {

					if (indexDerniereReponse + 1 < filtresReponses.size())
						indexEnCours = indexDerniereReponse + 1;
					else
						indexEnCours = indexDerniereReponse + 1 - filtresReponses.size() + 3;
				} else {
					// sinon on choisit au hasard une réponse dans la liste des réponses du filtre
					indexEnCours = (int) (Math.random() * (filtresReponses.size() - 3) + 3);
					indexDernierFiltre = i;
				}
				// si le filtre n'a qu'une réponse (après les 3 premiers champs)
				if (filtresReponses.size() == 4)
					reponse[0] = filtresReponses.get(3);
				else
					reponse[0] = filtresReponses.get(indexEnCours);
				System.out.println("4-class Reponse:String[] getReponse réponse choisie:(" + reponse[0] + ")");

				// on stocke l'index de la réponse en cours du filtre choisi
				indexDerniereReponse = indexEnCours;

				// on recupère le filtre
				String filtre = filtresReponses.get(2);

				Stack<String> regex = new Stack<>();
				Matcher matcher;
				// Si le filtre commence par $
				if (filtre.matches("^\\$")) {
					matcher = Regex.match(Regex.getRegex(filtre), question);
					memoireReponse.push(assemblageReponse(matcher, question));
				}

				// Si la réponse contient goto, on extrait le mot clé derrière le goto et on
				// recommence
				// m.group(1) correspond au terme derrière goto
				Pattern p = Pattern.compile("goto\\s+(\\p{L}+)");
				Matcher m = p.matcher(reponse[0].trim());
				String derivee = "";
				if (m.find()) {
					reponse = getReponse(m.group(1), question);
					derivee = reponse[1];
				}

				//// Si le filtre contient dériver ou si on peut encore extraire une équation de
				//// la question
				if (filtre.equals("dériver") || motCleQuestion.equals("xrien") || motCleQuestion.equals("fonction")) {
					// reformulation fonctions
					question = question.replaceAll("tg(\\(.*?\\))", "tan($1)");
					question = question.replaceAll("cotg(\\(.*?\\))", "cotan($1)");
					question = question.replaceAll("cosh(\\(.*?\\))", "ch($1)");
					question = question.replaceAll("sinh(\\(.*?\\))", "sh($1)");
					question = question.replaceAll("tanh(\\(.*?\\))", "th($1)");
					question = question.replaceAll("cotanh(\\(.*?\\))", "coth($1)");

					String eq = Expression.formuleToExpression(question).asString();
					if (!eq.equals("0")) {
						Calcul.setMemoireEquation(eq);
						reponse[1] = Calcul.getDerivee();
						derivee = reponse[1];
						System.out.println("derivee=" + derivee);
					}
				}

				// Si le filtre contient @, on récupère tous les synonymes du terme derrière @
				// m.group(1) correspond au terme derrière @
				System.out.println("filtre=" + filtre);
				p = Pattern.compile("@\\p{L}+");
				m = p.matcher(filtre);
				if (m.find()) {
					System.out.println(
							"Si le filtre contient @, on récupère tous les synonymes du terme derrière @=true");
					regex = Regex.getRegexSynonymes(filtre);
				} else {
					System.out.println(
							"Si le filtre contient @, on récupère tous les synonymes du terme derrière @=false");
					regex.push(filtre);
				}
				System.out.println("regex=" + regex);
				System.out.println("reponse=" + reponse[0]);
				System.out.println("derivee=" + derivee);
				Iterator<String> regexiTerator = regex.iterator();
				while (regexiTerator.hasNext()) {
					filtre = regexiTerator.next();
					filtre = Regex.getRegex(filtre);

					/*
					 * Si le match donne une reponse, on renvoie le tableau de réponse [reponse,
					 * dérivée] sinon on essaie une autre regex de la pile
					 */
					matcher = Regex.match(filtre, question);
					String rep = assemblageReponse(matcher, reponse[0]);

					if (rep != null) {
						System.out.println("matching(" + filtre + "," + question + ")=ok");
						reponse[0] = rep;
						reponse[1] = derivee;
						return reponse;
					} else
						System.out.println("matching(" + filtre + "," + question + ")=nok");
				}
			}
		}
		return reponse;
	}

	/**
	 * @param matcher
	 * @param reponse
	 * @return à partir du matching du regex réussi, on construit la réponse en
	 *         matchant la numérotation entre parenthèses.
	 */
	private static String assemblageReponse(Matcher matcher, String reponse) {

		reponse = reponse.replaceAll("\\(user\\)", Professeur.user);
		while (matcher.find()) {
			for (int j = 0; j <= matcher.groupCount(); j++) {

				// sous groupe j matché
				if (matcher.group(j) != null) {
					// System.out.println("matcher.group(j) =" + matcher.group(j) + ":" + j);

					// Seconde substitution, elle permute le sujet et l'objet
					String sousReponse = Conjugaison.conjuger(matcher.group(j).trim(),
							Fichier.getCheminFichierSujetObjet());
					// On remplace la numérotation entre parenthèses avec les groupes trouvés
					reponse = reponse.replaceAll("\\(" + Integer.toString(j) + "\\)", String.join(" ", sousReponse));
				}
			}
			// System.out.println("tmp=" + reponse);
			// Si réponse, on la retourne
			if (reponse != null)
				return Conjugaison.orthographe(reponse);
		}
		// Si rien
		return null;
	}

}
