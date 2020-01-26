package edu.projet.professeur;
	
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fichier {
	
    private static String cheminFichierConjugaison = "/conjugaison-traduction.csv";
    private static String cheminFichierSujetObjet = "/sujet-objet.csv";
    private static String cheminSynonymes = "/synonymes.csv";
    private static String cheminFiltresReponses = "/filtres-reponses.csv";
    private static String cheminFiltresEquations = "/filtres-equations.csv";
     
    
    //extraction des fichiers
	static List< String>  getfichier(String chemin ) {
			
		InputStream s = Fichier.class.getResourceAsStream(chemin);
		
		try (Stream<String> stream = new BufferedReader(new InputStreamReader(s, Charset.forName("UTF-8"))).lines()) {
			//on ne prend les lignes vides du fichier
			return stream.filter(x -> !x.trim().equals("")).collect(Collectors.toList());
		}
		
	}
	
	static List< String>  getFichierConjugaison() {
		
		return getfichier(cheminFichierConjugaison);
	}
	
	static List< String>  getFichierSujetObjet() {
		
		return getfichier(cheminFichierSujetObjet);
	}

	static List< String>  getFichierSynonymes() {
		
		return getfichier(cheminSynonymes);
	}
	
	static List< String>  getFichierFiltresReponses() {
		
		return getfichier(cheminFiltresReponses);
	}
	
	static List< String>  getFichierFiltresEquations() {
		
		return getfichier(cheminFiltresEquations);
	}
	
	static String getCheminFichierConjugaison() {
		return cheminFichierConjugaison;
	}

	static String getCheminFichierSujetObjet() {
		return cheminFichierSujetObjet;
	}
	
	static String getCheminSynonymes() {
		return cheminSynonymes;
	}

	static String getCheminFiltresReponses() {
		return cheminFiltresReponses;
	}
	
	static String getCheminFiltresEquations() {
		return cheminFiltresEquations;
	}
}
