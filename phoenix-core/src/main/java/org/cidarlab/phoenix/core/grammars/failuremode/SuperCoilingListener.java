// Generated from SuperCoiling.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SuperCoilingParser}.
 */
public interface SuperCoilingListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SuperCoilingParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(SuperCoilingParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link SuperCoilingParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(SuperCoilingParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link SuperCoilingParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(SuperCoilingParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link SuperCoilingParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(SuperCoilingParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link SuperCoilingParser#super_coiling}.
	 * @param ctx the parse tree
	 */
	void enterSuper_coiling(SuperCoilingParser.Super_coilingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SuperCoilingParser#super_coiling}.
	 * @param ctx the parse tree
	 */
	void exitSuper_coiling(SuperCoilingParser.Super_coilingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SuperCoilingParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(SuperCoilingParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link SuperCoilingParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(SuperCoilingParser.WildcardContext ctx);
}