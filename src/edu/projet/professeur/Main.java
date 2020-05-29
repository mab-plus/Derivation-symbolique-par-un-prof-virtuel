package edu.projet.professeur;
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
		
		ProfBot bot = new ProfBot("LeProfesseur");        
		String serveur = null, canal;
		
		try 
		{		
			if ( args.length == 2)
			{
				serveur = args[0];
		        canal = args[1];
			}
			else
			{
				System.out.println("Usage: server channel");
				System.out.println("Connexion au serveur et au canal par défaut.");
				serveur = "michel-bakhtaoui.fr";
		        canal = "#ircprojet";
			}
			
			// Enable debugging output.
	        bot.setVerbose(true);
	        
	        // Connect to the IRC server.
	        bot.connect(serveur);

	        // Join the #pircbot channel.
	        bot.joinChannel(canal);
		}
		catch (Exception e) 
		{
			System.out.printf("Impossible de se connecter au serveur %s\n", serveur);
			e.printStackTrace();
		}
	


	}
 
}