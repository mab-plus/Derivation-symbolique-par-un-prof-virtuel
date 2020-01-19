package edu.projet.professeur;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MotsCles {
	
	
	public static Map<String, Integer> getMotsClesQuestion (String question) {
		
		question= Regex.nettoyerQuestion(question);   				
     	question=  Conjugaison.conjuger(question, Fichier.getCheminFichierConjugaison());
     	
     	List< String>  termesQuestion = enleverDoublon(question);
     	
		List< String> fichierFiltresReponses = Fichier.getFichierFiltresReponses();
		Hashtable<String, Integer> motsClesQuestion = new Hashtable<String, Integer>();
		
		for(int i = 0; i < termesQuestion.size(); i++) {
			
			for(int j = 0; j < fichierFiltresReponses.size(); j++) {		
				
				List<String> filtresReponses = Arrays.asList(fichierFiltresReponses.get(j).split("\\|"));	

				if (termesQuestion.get(i).equals(filtresReponses.get(0))) {	
					motsClesQuestion.put(filtresReponses.get(0), Integer.parseInt(filtresReponses.get(1)));
					break;
				}
			}
		}

		return  trieParValeur(motsClesQuestion);
	}
	
	
	private static Map<String, Integer> trieParValeur(final Map<String, Integer> motsClesQuestion) {

        return motsClesQuestion.entrySet().stream()
        		.sorted((Map.Entry.<String, Integer>comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (q1, q2) -> q1, LinkedHashMap::new));
    }
	
	private static List<String> enleverDoublon(String question) {
		
		List<String> termesQuestion = Arrays.asList(question.toLowerCase().split("\\s+"));

		return  termesQuestion.stream().distinct().collect(Collectors.toList());
	}

}
