// Generated from Structural.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.structural;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link StructuralParser}.
 */
public interface StructuralListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link StructuralParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(StructuralParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(StructuralParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link StructuralParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(StructuralParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(StructuralParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link StructuralParser#tu}.
	 * @param ctx the parse tree
	 */
	void enterTu(StructuralParser.TuContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#tu}.
	 * @param ctx the parse tree
	 */
	void exitTu(StructuralParser.TuContext ctx);
	/**
	 * Enter a parse tree produced by {@link StructuralParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(StructuralParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(StructuralParser.WildcardContext ctx);
}