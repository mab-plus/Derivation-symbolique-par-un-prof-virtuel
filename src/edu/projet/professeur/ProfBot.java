package edu.projet.professeur;

import org.jibble.pircbot.PircBot;


/**
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
