package edu.projet.professeur;

import org.jibble.pircbot.PircBot;

/**
 * ProfBot utilise l'API IRC Java
 * PircBot est un framework Java pour écrire des bots IRC rapidement et facilement. 
 * Ses fonctionnalités incluent une architecture événementielle pour gérer les événements IRC courants. 
 * Son format de fichier journal complet peut être utilisé avec pisg pour générer des statistiques de canal. 
 * Lorsque vous compilez ou exécutez votre propre Bot qui utilise le package pircbot, vous devez vous assurer d'inclure le fichier pircbot.jar dans votre chemin de classe.
 * Comment faire un Bot IRC
 * Le package pircbot contient (entre autres) une classe abstraite nommée PircBot. Parce que c'est abstrait, cela signifie que vous ne pouvez pas en créer une instance, 
 * mais vous pouvez l'étendre et hériter de ses fonctionnalités. 
 * @author BAKHTAOUI Michel
 * @version 1.0
 * @see PircBot, http://www.jibble.org/pircbot.php
*/
public class ProfBot extends PircBot {
	
	public ProfBot(String name){
		setName(name);
	}
	
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
    	
		String reponse = Professeur.reponse(message); 		
		sendMessage(channel, reponse);		
    }
}
