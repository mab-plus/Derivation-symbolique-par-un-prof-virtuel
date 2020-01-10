package edu.projet.interfaces;

import edu.projet.expressions.*;
import edu.projet.fonctions.*;

public interface DerivationVisitor<R> {
	
	public R visit(Constante expr, String dx);
	public R visit(Variable expr, String dx);
	public R visit(Addition expr, String dx);
	public R visit(Soustraction expr, String dx);
	public R visit(Moins expr, String dx);
	public R visit(Multiplication expr, String dx);
	public R visit(Division expr, String dx);
	public R visit(Puissance expr, String dx);
	public R visit(Log expr, String dx);
	public R visit(Exp expr, String dx);
	public R visit(Cos expr, String dx);
	public R visit(Sin expr, String dx);
}