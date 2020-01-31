package edu.projet.professeur;

import java.util.Scanner;

/**
 * Application Professeur qui se connecte sur l'irc.
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//test
		/*System.out.println("> Professeur : " + Professeur.reponse("je suis étudiant et j'ai besoin d'aide et je veux dériver sin(x)"));
		System.out.println("> Professeur : " + Professeur.reponse("j'ai besoin de dériver cette fonction sin(1/x)"));
		System.out.println("> Professeur : " + Professeur.reponse("oui la dérivée de x^2 + 1"));
		System.out.println("> Professeur : " + Professeur.reponse("je ne comprends pas"));*/
		
		String reponse, question;  
		Scanner sc = new Scanner(System.in);	 
		System.out.println("> Professeur :" + Professeur.reponse("xdebut"));
		while (sc.hasNextLine()) {
			question = sc.nextLine();
			reponse = Professeur.reponse(question); 
			
			if(reponse.equals("quitter")) {
				System.out.println("> Professeur : Au revoir, merci et bonne journée!");
				break;
			}
			else {		
				System.out.println("> Professeur : " + reponse); 
			}
		}
		sc.close();
		
		
		/*ProfBot bot=new ProfBot("Prof");
		try {		
			// Enable debugging output.
	        bot.setVerbose(true);
	        
	        // Connect to the IRC server.
	        bot.connect("michel-bakhtaoui.fr");

	        // Join the #pircbot channel.
	        bot.joinChannel("#ircprojet");
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
 
}