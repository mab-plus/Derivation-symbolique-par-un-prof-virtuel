package edu.projet.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.projet.fonctions.Cos;
import edu.projet.fonctions.Cosh;
import edu.projet.fonctions.Cotan;
import edu.projet.fonctions.Cotanh;
import edu.projet.fonctions.Exp;
import edu.projet.fonctions.Log;
import edu.projet.fonctions.Sin;
import edu.projet.fonctions.Sinh;
import edu.projet.fonctions.Tan;
import edu.projet.fonctions.Tanh;
import edu.projet.interfaces.Formule;
import edu.projet.professeur.Professeur;

/**
 * Une expression algébrique peut être définie récursivement de la manière
 * suivante : une constante est une expression une variable est une expression
 * l’addition de deux expressions est une expression la multiplication de deux
 * expressions est une expression etc. On définit donc une classe Expression
 * récursif pour les expressions algébrique. C'est une classe récursive car
 * chaque objet instance de cette classe a des variables qui contiennent
 * d'autres instances de la classe.
 * 
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public abstract class Expression implements Formule {

	/**
	 * @param Opérande
	 *            gauche et droite
	 */
	public Expression exprG, exprD;

	/**
	 * @param Opérateur
	 */
	public String symbole;

	/**
	 * Constructeur Expression.
	 * 
	 * @param exprG
	 *            opérande gauche
	 * @param symbole
	 *            opérateur
	 * @param exprD
	 *            opérande droite
	 */
	public Expression(Expression exprG, String symbole, Expression exprD) {
		this.exprG = exprG;
		this.symbole = symbole;
		this.exprD = exprD;
	}

	/**
	 * @return le symbole de l'opérateur de l'expression
	 */
	public String getSymbole() {
		return this.symbole;
	}

	/**
	 * @return L'expression, sous la forme d'une chaîne de caractère.
	 */
	@Override
	public String asString() {

		if (this.exprG == null)
			return this.getSymbole() + "(" + this.exprD.asString() + ")";

		return this.exprG.asString() + " " + this.getSymbole() + " "
				+ this.exprD.asString();
	}

	/**
	 * @return L'expression, sous la forme d'une chaîne de caractère.
	 */
	public String string() {

		if (this instanceof Constante)
			return this.asString();

		if (this instanceof Variable)
			return this.asString();

		if (this.exprG == null)
			return this.getSymbole() + "(" + this.exprD.string() + ")";

		return this.exprG.string() + " " + this.getSymbole() + " "
				+ this.exprD.string();
	}

	/**
	 * @return true si deux objets sont égaux sinon false. Pour comparer les
	 *         expressions
	 */
	@Override
	public boolean equals(Object objet) {

		if (this == objet)
			return true;

		if (objet == null)
			return false;

		if (!(objet instanceof Expression))
			return false;

		Expression autreObjet = (Expression) objet;

		if (this.exprG == null) {

			if (autreObjet.exprG != null)
				return false;
		} else if (!this.exprG.equals(autreObjet.exprG))
			return false;

		if (this.exprD == null) {

			if (autreObjet.exprD != null)
				return false;
		} else if (!this.exprD.equals(autreObjet.exprD))
			return false;

		if (this.symbole == null) {

			if (autreObjet.symbole != null)
				return false;
		} else if (!this.symbole.equals(autreObjet.symbole))
			return false;

		return true;
	}

	/**
	 * @param expr
	 * @return true si l'expression expr est la constante 0
	 */
	public static boolean isZero(Expression expr) {
		return (expr instanceof Constante)
				&& (((Constante) expr).getValeur() == 0);
	}

	/**
	 * @param expr
	 * @return true si l'expression expr est la constante 1
	 */
	public static boolean isUn(Expression expr) {
		return (expr instanceof Constante)
				&& (((Constante) expr).getValeur() == 1);
	}

	/**
	 * @param expr
	 * @return true si l'expression expr est la constante -1
	 */
	public static boolean isMoinsUn(Expression expr) {
		return (expr instanceof Constante)
				&& (((Constante) expr).getValeur() == -1);
	}

	/**
	 * @param expr
	 * @return true si l'expression expr est une constante
	 */
	public static boolean isConstante(Expression expr) {
		return expr instanceof Constante;
	}

	/**
	 * @param expr
	 * @return true si l'expression expr est une variable
	 */
	public static boolean isVariable(Expression expr) {
		return expr instanceof Variable;
	}

	/**
	 * @param op
	 * @return true si expr est un opérateur
	 */
	public static boolean isOperateur(String op) {

		if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")
				|| op.equals("^"))
			return true;

		else
			return false;
	}

	/**
	 * @param fn
	 * @return true si fn est une fonction (cos, sin, etc.)
	 */
	public static boolean isFonction(String fn) {

		if (fn.equals("log") || fn.equals("exp") || fn.equals("cos")
				|| fn.equals("sin") || fn.equals("tan") || fn.equals("cotan")
				|| fn.equals("ch") || fn.equals("sh") || fn.equals("th")
				|| fn.equals("coth"))
			return true;

		else
			return false;
	}

	/**
	 * @param op
	 * @return la priorité de l'opération ou de la fonction op
	 */
	public static int Priorite(String op) {

		if (op.equals("+") || op.equals("-"))
			return 1;

		else if (op.equals("*") || op.equals("/"))
			return 2;

		else if (op.equals("^"))
			return 3;

		else if (isFonction(op))
			return 4;

		else
			return -1;
	}

	/**
	 * @param equation
	 * @return une liste de String des termes de l'équation
	 */
	private static List<String> splitter(String equation) {

		Pattern pattern;
		Matcher matcher;
		List<String> termes = new ArrayList<>();
		List<String> blocs = Arrays.asList(equation.split("\\s"));

		if (Professeur.activeTrace)
			System.out.println("\nblocs = " + blocs);

		// chaque bloc suivant en termes d'équation
		for (String bloc : blocs) {
			for (String str : bloc.replaceAll("\\s+", "")
					.split("(?<=[-+*/^()?!.])|(?=[-+*/^()?!.])")) {
				pattern = Pattern.compile("(\\d+)(\\w+)");
				matcher = pattern.matcher(str);
				if (matcher.find()) {
					if (Professeur.activeTrace) {
						System.out.println("group 1: " + matcher.group(1));
						System.out.println("group 2: " + matcher.group(2));
					}
					termes.add(matcher.group(1));
					termes.add("*");
					termes.add(matcher.group(2));
				} else if (isOperateur(str) || isFonction(str)
						|| str.matches("[\\d|\\w]") || str.equals("(")
						|| str.equals(")"))
					termes.add(str);
			}

		}
		if (Professeur.activeTrace)
			System.out.println("class Expression:List<String> splitter("
					+ equation + ")=" + termes);
		return termes;
	}

	/**
	 * C'est un signe négatif, lorsque le signe moins est au début d'une
	 * expression, ou après une parenthèse ouvrante ou après un opérateur
	 * binaire, sinon c'est une soustraction.
	 */
	static boolean is_negatif(String str, String avantDernierTerme) {

		if (!str.equals("-"))
			return false;

		if (isOperateur(avantDernierTerme) || isFonction(avantDernierTerme)
				|| avantDernierTerme.equals(""))
			return true;
		else
			return false;
	}

	/**
	 * @param equation
	 * @return en notation postfix dans une liste de String l'équation
	 */
	public static List<String> equationToPostfix(String equation) {
		List<String> termes = splitter(equation);
		Stack<String> pile = new Stack<>();
		ArrayList<String> resultat = new ArrayList<>();

		String variable = "";
		boolean is_variable = false;
		String signe = "";
		String avantDernierTerme = "";

		for (String str : termes) {
			if (!isOperateur(str) && !isFonction(str)
					&& str.matches("[\\d|\\w]")) {
				is_variable = true;
				variable = str;
			} else {
				if (is_variable) {
					resultat.add(signe + variable);
					is_variable = false;
					variable = "";
					signe = "";
				}

				if (str.equals("(")) {
					pile.push(str);
				} else if (str.equals(")")) {
					while (!pile.empty()) {
						String s = pile.pop();
						if (s.equals("("))
							break;
						resultat.add(s);
					}
				} else if (Priorite(str) > 0) {
					// Distinction entre le signe négatif et le signe de
					// soustraction
					if (is_negatif(str, avantDernierTerme)) {
						signe = "-";
					} else {
						int p = Priorite(str);
						while (!pile.isEmpty() && Priorite(pile.peek()) >= p)
							resultat.add(pile.pop());
						pile.push(str);
					}
				} else {
					resultat.add(str);
				}
			}
			avantDernierTerme = str;
		}

		if (is_variable) {
			resultat.add(signe + variable);
		}

		while (!pile.isEmpty()) {
			resultat.add(pile.pop());
		}

		if (Professeur.activeTrace)
			System.out
					.println("class Expression:List<String> equationToPostfix("
							+ equation + ")=" + resultat);
		return resultat;
	}

	/**
	 * @param formule
	 * @return conversion d'une équation ou formule en Expression:
	 */

	public static Expression formuleToExpression(String formule) {

		Stack<Expression> pile = new Stack<Expression>();
		Expression expr1, expr2, tmp;

		List<String> termes = equationToPostfix(formule);
		for (int i = 0; i < termes.size(); i++) {
			// si operande -> la pile
			if (!isOperateur(termes.get(i)) && !isFonction(termes.get(i))) {

				try {
					pile.push(new Constante(Double.valueOf(termes.get(i))));
				} catch (Exception e) {
					pile.push(new Variable(termes.get(i)));
				}
			}
			// operateur + - * / ^
			else if (isOperateur(termes.get(i))) {

				if (Professeur.activeTrace)
					System.out.println("pile.size=" + pile.size());

				if (pile.size() == 1 && termes.get(i).equals("-")) {

					// operateur unaire moins
					expr1 = pile.pop();
					if (expr1 instanceof Moins)
						pile.push(expr1);
					else
						pile.push(new Moins(expr1));
				} else {
					// Pop les deux operandes de l'operateur
					expr1 = pile.pop();
					expr2 = pile.pop();

					switch (termes.get(i)) {
						case "+" :
							pile.push(new Addition(expr2, expr1));
							break;

						case "-" :
							pile.push(new Soustraction(expr2, expr1));
							break;

						case "*" :
							pile.push(new Multiplication(expr2, expr1));
							break;

						case "/" :
							pile.push(new Division(expr2, expr1));
							break;

						case "^" :
							pile.push(new Puissance(expr2, expr1));
							break;
					}
				}

			}
			// fonctions cos sin exp log etc...
			else {
				// Pop argment de la fonction
				expr1 = pile.pop();

				switch (termes.get(i)) {
					case "log" :
						pile.push(new Log(expr1));
						break;
					case "exp" :
						pile.push(new Exp(expr1));
						break;
					case "cos" :
						pile.push(new Cos(expr1));
						break;
					case "sin" :
						pile.push(new Sin(expr1));
						break;

					case "tan" :
						pile.push(new Tan(expr1));
						break;
					case "cotan" :
						pile.push(new Cotan(expr1));
						break;

					case "ch" :
						pile.push(new Cosh(expr1));
						break;
					case "sh" :
						pile.push(new Sinh(expr1));
						break;

					case "th" :
						pile.push(new Tanh(expr1));
						break;
					case "coth" :
						pile.push(new Cotanh(expr1));
						break;
				}
			}
		}

		if (!pile.isEmpty()) {
			tmp = pile.pop();
			// return pile.pop();
		} else {
			tmp = new Constante(0);
			// return new Constante(0);
		}
		if (Professeur.activeTrace)
			System.out
					.println("class Expression:Expression formuleToExpression("
							+ formule + ")=" + tmp.asString());
		return tmp;
	}

}
