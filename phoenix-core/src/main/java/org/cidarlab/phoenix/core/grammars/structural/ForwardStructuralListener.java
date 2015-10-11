// Generated from ForwardStructural.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.structural;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ForwardStructuralParser}.
 */
public interface ForwardStructuralListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ForwardStructuralParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ForwardStructuralParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(ForwardStructuralParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(ForwardStructuralParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#forward_tu}.
	 * @param ctx the parse tree
	 */
	void enterForward_tu(ForwardStructuralParser.Forward_tuContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#forward_tu}.
	 * @param ctx the parse tree
	 */
	void exitForward_tu(ForwardStructuralParser.Forward_tuContext ctx);
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#module_wildcard}.
	 * @param ctx the parse tree
	 */
	void enterModule_wildcard(ForwardStructuralParser.Module_wildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#module_wildcard}.
	 * @param ctx the parse tree
	 */
	void exitModule_wildcard(ForwardStructuralParser.Module_wildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#wildcard_fp}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_fp(ForwardStructuralParser.Wildcard_fpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#wildcard_fp}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_fp(ForwardStructuralParser.Wildcard_fpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#wildcard_fc}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_fc(ForwardStructuralParser.Wildcard_fcContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#wildcard_fc}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_fc(ForwardStructuralParser.Wildcard_fcContext ctx);
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#wildcard_rp}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rp(ForwardStructuralParser.Wildcard_rpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#wildcard_rp}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rp(ForwardStructuralParser.Wildcard_rpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ForwardStructuralParser#wildcard_rc}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rc(ForwardStructuralParser.Wildcard_rcContext ctx);
	/**
	 * Exit a parse tree produced by {@link ForwardStructuralParser#wildcard_rc}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rc(ForwardStructuralParser.Wildcard_rcContext ctx);
}