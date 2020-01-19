package edu.projet.professeur;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fichier {
	
    private static String cheminFichierConjugaison = "src/edu/projet/professeur/Data/conjugaison-traduction.csv";
    private static String cheminFichierSujetObjet = "src/edu/projet/professeur/Data/sujet-objet.csv";
    private static String cheminSynonymes = "src/edu/projet/professeur/Data/synonymes.csv";
    private static String cheminFiltresReponses = "src/edu/projet/professeur/Data/filtres-reponses.csv";
    private static String cheminFiltresEquations = "src/edu/projet/professeur/Data/filtres-equations.csv";
    
    //extraction des fichiers
	static List< String>  getfichier(String chemin ) {
		
		Path path = FileSystems.getDefault().getPath(chemin);
		
		try (Stream<String> stream = Files.lines(path)) {
			return stream.filter(x -> !x.trim().equals("")).collect(Collectors.toList());
		}
		catch (IOException e) {		
			e.printStackTrace();
		}
		return null;
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
