package edu.projet.professeur;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Regex {

	/**
	 * @param in entrée utilisateur en minuscule.
	 * @return espace de début et de fin supprimés suppression de tous les meta
	 *         caracteres.
	 */
	static String nettoyerQuestion(String in) {

		in = in.toLowerCase();
		in = in.trim();
		in = remplacerMetaCaractere(in, "@#$%^&*()_-+=~`{[}]|:;,.<>\\\"", "                            ");
		return in = remplacerMetaCaractere(in, ",?!", "...");
	}

	/**
	 * @param in   On parcourir l'entrée, caractère par caractère.
	 * @param src  on remplace le caractère d'entrée.
	 * @param dest par celui de destination.
	 * @return chaîne formatée.
	 * 
	 */
	private static String remplacerMetaCaractere(String in, String src, String dest) {

		if (src.length() != dest.length()) {
			// impossible
		}

		for (int i = 0; i < src.length(); i++) {
			in = in.replace(src.charAt(i), dest.charAt(i));
		}

		return in;
	}

	/**
	 * source : https://www.drillio.com/en/2011/java-remove-accent-diacritic/
	 * 
	 * @param in
	 * @return chaîne désaccentuée.
	 */
	static String desaccentuer(String in) {

		return Normalizer.normalize(in, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	/**
	 * @param filtre
	 * @return l'expression régulière (regex) construite à partir du filtre.
	 */
	static String getRegex(String filtre) {
		// on supprime le $ s'il existe
		filtre = filtre.replaceFirst("$", "");
		// Expressions régulières pour matcher autres sauf celles commençant par @
		String regex = "([\\\\p{L}\\\\s']*)";
		return match("\\s*\\*\\s*|\\$\\s*[^@]", filtre).replaceAll(regex);

	}

	/**
	 * @param filtre Filtres commençant par @. On récupère les synonymes
	 * @return les expressions régulières à partir de chaque synonyme.
	 */
	static Stack<String> getRegexSynonymes(String filtre) {
		List<String> fichierSynonymes = Fichier.getFichierSynonymes();
		Stack<String> regex = new Stack<>();

		Pattern p = Pattern.compile("@(\\p{L}+)");
		Matcher m = p.matcher(filtre);
		if (m.find()) {

			for (int i = 0; i < fichierSynonymes.size(); i++) {
				List<String> synonymes = Arrays.asList(fichierSynonymes.get(i).split("\\|"));
				if (synonymes.get(0).equals(m.group(1))) {
					for (int j = 0; j < synonymes.size(); j++) {
						regex.push(filtre.replaceAll("@(\\p{L}+)", "(" + synonymes.get(j) + ")"));
					}
				}
			}
		}
		System.out.println("class Regex:getRegexSynonymes(String " + filtre + ")=" + regex);
		return regex;
	}

	/**
	 * @param regex
	 * @param expression
	 * @return match d'une expression régulière
	 */
	static Matcher match(String regex, String expression) {
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(expression);
	}

}
