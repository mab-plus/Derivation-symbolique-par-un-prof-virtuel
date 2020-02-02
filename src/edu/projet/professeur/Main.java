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
		
		/*Variable x = new Variable("x");
		Constante un = new Constante(1);
		Expression fx  = new Cosh(x);
		
		Derivation d = new Derivation();	
		System.out.println(d.deriver(fx, "x").asString());
		
		Expression u = Expression.formuleToExpression("tan(x)");
		
		System.out.println(u.asString());
		System.out.println(d.deriver(u, "x").asString());*/
		
		//test
		/*System.out.println("> Professeur : " + Professeur.reponse("je suis étudiant et j'ai besoin d'aide et je veux dériver sin(x)"));
		System.out.println("> Professeur : " + Professeur.reponse("j'ai besoin de dériver cette fonction sin(1/x)"));
		System.out.println("> Professeur : " + Professeur.reponse("sh(sin(x))", ""));
		System.out.println("> Professeur : " + Professeur.reponse("oui, derives moi sh(sin(x)) par exemple ", ""));*/
		System.out.println("> Professeur : " + Professeur.reponse("je reves de vous", "Abderahmane"));

		/*Scanner sc = new Scanner(System.in);	 
		System.out.println("> Professeur :" + Professeur.reponse("xdebut"));
		while (sc.hasNextLine()) {
			String question = sc.nextLine();			

			if (!question.trim().equals("")){	
				String reponse = Professeur.reponse(question);
				if(reponse.equals("quitter")) {
					System.out.println("> Professeur : Au revoir, merci et bonne journée!");	
					break;
				}
				System.out.println("> Professeur : " + reponse); 
			}
		}
		sc.close();
		
		
		ProfBot bot=new ProfBot("Prof");
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