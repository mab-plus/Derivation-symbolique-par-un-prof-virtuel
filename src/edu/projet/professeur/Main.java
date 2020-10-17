package edu.projet.professeur;

import java.util.Scanner;

/**
 * Application Professeur qui se connecte sur l'irc.
 * 
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (Professeur.irc) {
			// utilistion en IRC
			ProfBot bot = new ProfBot("LeProfesseur");
			String serveur = null, canal;
			try {
				if (args.length == 2) {
					serveur = args[0];
					canal = args[1];
				} else {
					System.out.println("Usage: server channel");
					System.out.println(
							"Connexion au serveur et au canal par défaut.");
					serveur = "michel-bakhtaoui.fr";
					canal = "#ircprojet";
				}
				// activer le debugging.
				bot.setVerbose(true);
				// Connection au serveur IRC.
				bot.connect(serveur);
				// Joindre le canal exempe:#pircbot.
				bot.joinChannel(canal);
			} catch (Exception e) {
				System.out.printf("Impossible de se connecter au serveur %s\n",
						serveur);
				e.printStackTrace();
			}

		} else {
			String reponse;
			Scanner sc = new Scanner(System.in);
			System.out.println(">Bonjour, je suis le Professeur.");
			while (sc.hasNextLine()) {
				reponse = Professeur.reponse(sc.nextLine(), "abarti");
				System.out.println(">" + reponse);
				if (reponse.equals("exit"))
					break;
			}
			System.out.println("Au revoir");
			sc.close();
		}

	}
}
