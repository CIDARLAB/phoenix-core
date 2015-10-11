// Generated from Structural.g4 by ANTLR 4.3

    package org.cidarlab.phoenix.core.grammars.structural;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link StructuralParser}.
 */
public interface StructuralListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link StructuralParser#wildcard_rc}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rc(@NotNull StructuralParser.Wildcard_rcContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#wildcard_rc}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rc(@NotNull StructuralParser.Wildcard_rcContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#wildcard_rp}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rp(@NotNull StructuralParser.Wildcard_rpContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#wildcard_rp}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rp(@NotNull StructuralParser.Wildcard_rpContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#wildcard_fc}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_fc(@NotNull StructuralParser.Wildcard_fcContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#wildcard_fc}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_fc(@NotNull StructuralParser.Wildcard_fcContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(@NotNull StructuralParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(@NotNull StructuralParser.RootContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(@NotNull StructuralParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(@NotNull StructuralParser.ModuleContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#module_wildcard_start}.
	 * @param ctx the parse tree
	 */
	void enterModule_wildcard_start(@NotNull StructuralParser.Module_wildcard_startContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#module_wildcard_start}.
	 * @param ctx the parse tree
	 */
	void exitModule_wildcard_start(@NotNull StructuralParser.Module_wildcard_startContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#wildcard_fp}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_fp(@NotNull StructuralParser.Wildcard_fpContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#wildcard_fp}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_fp(@NotNull StructuralParser.Wildcard_fpContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#module_wildcard_end}.
	 * @param ctx the parse tree
	 */
	void enterModule_wildcard_end(@NotNull StructuralParser.Module_wildcard_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#module_wildcard_end}.
	 * @param ctx the parse tree
	 */
	void exitModule_wildcard_end(@NotNull StructuralParser.Module_wildcard_endContext ctx);

	/**
	 * Enter a parse tree produced by {@link StructuralParser#forward_tu}.
	 * @param ctx the parse tree
	 */
	void enterForward_tu(@NotNull StructuralParser.Forward_tuContext ctx);
	/**
	 * Exit a parse tree produced by {@link StructuralParser#forward_tu}.
	 * @param ctx the parse tree
	 */
	void exitForward_tu(@NotNull StructuralParser.Forward_tuContext ctx);
}