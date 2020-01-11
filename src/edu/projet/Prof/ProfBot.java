package edu.projet.Prof;

import org.jibble.pircbot.PircBot;

//inspiration http://www.jibble.org/pircbot.php
public class ProfBot extends PircBot {
	
	public ProfBot(String name){
		setName(name);
	}
	
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
    	
		String reponse = Prof.reponse(message); 		
		
		sendMessage(channel, reponse);
		if (!Prof.getMemoireEquation().isEmpty())
			sendMessage(channel, Prof.calcul (message));
		
    }
}
