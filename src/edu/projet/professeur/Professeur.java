package edu.projet.professeur;

import java.util.Map;
import java.util.Stack;

public class Professeur {
	
	public static Stack<String> memoireDerniereQuestion = new Stack<>();
	private static Stack<String> memoireEquation = new Stack<String>();
	
	static Stack<String> getMemoireEquation() {
		return memoireEquation;
	}
	
	static void setMemoireEquation(Stack<String> data) {
		memoireEquation.addAll(data);
	}
	
    public static String reponse (String question) {
    	
    	/*
    	if (memoireDerniereQuestion.isEmpty())
    		memoireDerniereQuestion.push(question);
    	else if (question.equals(memoireDerniereQuestion.pop()))
    		Professeur.reponse(question);
    	*/
    	
    	//String laReponse = Calcul.getEquation(question);
    	
     	Map<String, Integer> motsClesQuestion = MotsCles.getMotsClesQuestion (question);
     	System.out.println("process3=motsClesQuestion=" + motsClesQuestion );
     	
     	String laReponse = Reponse.getReponse(motsClesQuestion, question);
     	 
     	return laReponse;
     }
}
