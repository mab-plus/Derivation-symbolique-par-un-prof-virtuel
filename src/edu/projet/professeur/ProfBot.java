package edu.projet.professeur;

import org.jibble.pircbot.PircBot;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 * @see PircBot, http://www.jibble.org/pircbot.php ProfBot utilise l'API IRC
 *      PircBot qui est un framework Java pour écrire des bots IRC rapidement et
 *      facilement. Ses fonctionnalités incluent une architecture événementielle
 *      pour gérer les événements IRC courants. Le package pircbot contient
 *      (entre autres) une classe abstraite nommée PircBot dont on peut étendre
 *      et hériter les fonctionnalités.
 */

public class ProfBot extends PircBot {

	public ProfBot(String name) {
		setName(name);
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {

		if (!message.trim().equals("") && channel.equals("#ircprojet")) {
			String reponse = Professeur.reponse(message, sender);
			sendMessage(channel, reponse);
		}
	}
}
