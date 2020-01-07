package edu.projet.Prof;

public enum Filtres {
	
	$$(".*"), 
	$x("([a-zA-Z]+)"), // match expression pour remplacement
	$eq("[a-zA-Z]\\s*\\(([a-zA-Z])\\)\\s*=\\s*([a-zA-Z0-9+\\-*\\/^\\(\\)\\s*]*)"), //match équation ex : f(x) = x^2 + 2
	$("[a-zA-Z]+?");

	public String regex = "";

	//Constructeur
	Filtres (String regex) {
		this.regex = regex;
	}
	
	public static String regex(String str) {
		
		for(Filtres f : Filtres.values()) {
			str = str.replace(f.name(), f.regex);
		}
	    return str;
	}
	
}
