package edu.projet.interfaces;

import edu.projet.expressions.*;
import edu.projet.fonctions.*;

public interface SimplificationVisitor<R> {
	
	public R visit(Constante expr);
	public R visit(Variable expr);
	public R visit(Addition expr);
	public R visit(Moins expr);
	public R visit(Multiplication expr);
	public R visit(Puissance expr);
	public R visit(Log expr);
	public R visit(Exp expr);
	public R visit(Cos expr);
	public R visit(Sin expr);
}