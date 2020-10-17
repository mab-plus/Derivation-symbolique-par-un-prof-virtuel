package edu.projet.professeur;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import edu.projet.calcul.Derivation;
import edu.projet.calcul.Simplification;
import edu.projet.expressions.Expression;

/**
 * @author BAKHTAOUI Michel
 * @version 1.0
 */
public class Calcul {

	/**
	 * pour stocker l'équation trouvée dans l'entrée utilisateur.
	 */
	private static Stack<String> memoireEquation = new Stack<String>();

	/**
	 * pour stocker les variables de l'équation trouvée dans l'entrée
	 * utilisateur
	 */
	private static Stack<String> memoireVariable = new Stack<String>();

	/**
	 * @return une pile contenant toutes les variables de l'équation trouvée
	 *         dans l'entrée utilisateur
	 */
	static Stack<String> getMemoireVariable() {
		return memoireVariable;
	}

	/**
	 * @param data,
	 *            enregistre les variables de l'équation dans memoireVariable.
	 */
	static void setMemoireVariable(String data) {
		memoireVariable.push(data);
	}

	/**
	 * @return une pile contenantle le ou les équations trouvées dans l'entrée
	 *         utilisateur
	 */
	static Stack<String> getMemoireEquation() {
		return memoireEquation;
	}

	/**
	 * @param data,
	 *            enregistre l'équation dans memoireEquation.
	 */
	static void setMemoireEquation(String data) {
		memoireEquation.push(data);
	}

	/**
	 * @param equation
	 *            une fois l'équation trouvée, extraction des variables :
	 *            exemple x^2 + y --> x,y.
	 * 
	 **/
	static void getVariables(String equation) {
		// on tranforme l'expression trouvée en notation polonaise inversée
		// exemple x^2 + y ---> x2^y+
		List<String> termes = Expression.equationToPostfix(equation);

		// on inverse x2^y+ ---> +y^2x
		Collections.sort(termes, Collections.reverseOrder());
		String tmp = "";

		for (int i = 0; i < termes.size(); i++) {
			// si un terme n'est pas operateur ni une fonction (sin cos log ...)
			if (!Expression.isOperateur(termes.get(i))
					&& !Expression.isFonction(termes.get(i))) {

				// on regarde si c'est une constante float par une conversion
				try {
					Double.valueOf(termes.get(i));
				} catch (NumberFormatException e) {
					// en cas d'echec de la conversion alors c'est une variable
					// on ne garde que les variables distinctes
					if (!tmp.equals(termes.get(i)))
						setMemoireVariable(termes.get(i));
					tmp = termes.get(i);
				}
			}
		}
		if (Professeur.activeTrace)
			System.out.println("class Calcul getVariables(" + equation + ")="
					+ getMemoireVariable().firstElement());
	}

	/**
	 * la dérivée de l'équation par rapport à ses variables trouvées : exemple
	 * x^2 + y --> x,y --> d/dx(^2 + y) = 2*x, d/dy(^2 + y) = 1.
	 * 
	 * @return le résultat de la dérivée sous forme d'une réponse
	 *
	 **/
	static String getDerivee() {
		String variable, equation;
		String resultat = "";
		Expression eEquation;
		Simplification simp = new Simplification();
		Derivation df = new Derivation();
		boolean exemple = false;

		if (getMemoireEquation().size() == 3)
			equation = getMemoireEquation().get(0);
		else
			equation = getMemoireEquation()
					.get(getMemoireEquation().size() - 1);

		getVariables(equation);

		if (getMemoireVariable().size() == 0)
			return resultat + "(" + equation + ")' = 0";

		if (getMemoireVariable().size() == 1) {
			variable = getMemoireVariable().pop();

			// on suppose que la variable est du style x, y ou n'importe quelle
			// lettre mais
			// pas un mot
			if (variable.length() == 1)
				eEquation = Expression.formuleToExpression(equation);
			// si c'est un mot, on fabrique une équation polynome du 4eme degré
			// pour que le
			// professeur fasse une démonstration
			else {
				// constante
				int coefficient = (int) (Math.random() * 8 + 2);
				equation = Integer.toString(coefficient);

				// premier degré
				coefficient = (int) (Math.random() * 9 + 2);
				equation += "+" + Integer.toString(coefficient) + "*x";

				// le reste
				for (int i = 2; i < 5; i++) {
					coefficient = (int) (Math.random() * 10 + 2);
					equation += "+" + Integer.toString(coefficient) + "*x^"
							+ Integer.toString(i);
				}
				eEquation = Expression.formuleToExpression(equation);
				variable = "x";
				exemple = true;
			}

			if (Professeur.activeTrace)
				System.out.print("class Derivation:Expression simplifier("
						+ eEquation.asString() + ")=");

			eEquation = simp.simplifier(eEquation);

			if (Professeur.activeTrace)
				System.out.println(eEquation.asString());
			if (Professeur.activeTrace)
				System.out.print("class Derivation:Expression Deriver("
						+ eEquation.asString() + "," + variable + ")=");

			eEquation = df.deriver(eEquation, variable);

			if (Professeur.activeTrace)
				System.out.println(eEquation.asString());
			if (Professeur.activeTrace)
				System.out.print("class Derivation:Expression simplifier("
						+ eEquation.asString() + ")=");

			eEquation = simp.simplifier(eEquation);

			if (Professeur.activeTrace)
				System.out.println(eEquation.asString());

			// Heuristique :=) : retour dans la moulinette pour bien simplifier
			// la dérivée
			/*
			 * eEquation = Expression.formuleToExpression(eEquation.string());
			 * eEquation = simp.simplifier(eEquation);
			 */

			if (equation.equals(variable))
				resultat += "(d" + equation + "/d" + variable + ") = 1";
			else {
				if (exemple)
					resultat += "Voici un petit exemple, [" + equation + "]' = "
							+ eEquation.asString();
				else
					resultat += "[" + equation + "]' = " + eEquation.asString();
			}
		}

		if (getMemoireVariable().size() > 1) {
			eEquation = Expression.formuleToExpression(equation);
			eEquation = simp.simplifier(eEquation);
			int memoireVariable = getMemoireVariable().size();
			Expression expr;

			for (int i = 0; i < memoireVariable; i++) {
				variable = getMemoireVariable().pop();
				// on suppose que la variable est du style x, y ou n'importe
				// quelle lettre mais
				// pas un mot
				if (variable.length() == 1) {
					expr = df.deriver(eEquation, variable);
					resultat += "(d/d" + variable + ")[" + equation + "] = "
							+ simp.simplifier(expr).asString() + ", ";
				}
			}
		}
		getMemoireEquation().clear();
		getMemoireVariable().clear();
		if (Professeur.activeTrace)
			System.out
					.println("class Calcul:String getDerivee() ()=" + resultat);
		return resultat;
	}
}
