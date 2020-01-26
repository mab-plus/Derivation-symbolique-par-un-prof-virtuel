package edu.projet.professeur;
	
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Fichier {
	
    /**
     * Fichier conjugaison-traduction.csv
     */
    private static String cheminFichierConjugaison = "/conjugaison-traduction.csv";
    /**
     * Fichier sujet-objet.csv
     */
    private static String cheminFichierSujetObjet = "/sujet-objet.csv";
    /**
     * Fichier synonymes.csv
     */
    private static String cheminSynonymes = "/synonymes.csv";
    /**
     * Fichier filtres-reponses.csv
     */
    private static String cheminFiltresReponses = "/filtres-reponses.csv";
    /**
     * Fichier filtres-equations.csv
     */
    private static String cheminFiltresEquations = "/filtres-equations.csv";
     
    
	/**
	 * @param chemin
	 * @return extraction des fichiers.
	 */
	static List< String>  getfichier(String chemin ) {
			
		InputStream s = Fichier.class.getResourceAsStream(chemin);
		
		try (Stream<String> stream = new BufferedReader(new InputStreamReader(s, Charset.forName("UTF-8"))).lines()) {
			//on ne prend les lignes vides du fichier
			return stream.filter(x -> !x.trim().equals("")).collect(Collectors.toList());
		}
		
	}
	
	/**
	 * @return Fichier Conjugaison.
	 */
	static List< String>  getFichierConjugaison() {
		
		return getfichier(cheminFichierConjugaison);
	}
	
	/**
	 * @return Fichier SujetObjet.
	 */
	static List< String>  getFichierSujetObjet() {
		
		return getfichier(cheminFichierSujetObjet);
	}

	/**
	 * @return Fichier Synonymes.
	 */
	static List< String>  getFichierSynonymes() {
		
		return getfichier(cheminSynonymes);
	}
	
	/**
	 * @return Fichier FiltresReponses.
	 */
	static List< String>  getFichierFiltresReponses() {
		
		return getfichier(cheminFiltresReponses);
	}
	
	/**
	 * @return Fichier FiltresEquations.
	 */
	static List< String>  getFichierFiltresEquations() {
		
		return getfichier(cheminFiltresEquations);
	}
	
	
	/**
	 * @return Chemin Fichier Conjugaison.
	 */
	static String getCheminFichierConjugaison() {
		return cheminFichierConjugaison;
	}

	/**
	 * @return Chemin Fichier SujetObjet.
	 */
	static String getCheminFichierSujetObjet() {
		return cheminFichierSujetObjet;
	}
	
	/**
	 * @return Chemin Synonymes.
	 */
	static String getCheminSynonymes() {
		return cheminSynonymes;
	}

	/**
	 * @return Chemin FiltresReponses.
	 */
	static String getCheminFiltresReponses() {
		return cheminFiltresReponses;
	}
	
	/**
	 * @return Chemin FiltresEquations.
	 */
	static String getCheminFiltresEquations() {
		return cheminFiltresEquations;
	}
}
