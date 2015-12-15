// Generated from ReverseStrandTerminators.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ReverseStrandTerminatorsParser}.
 */
public interface ReverseStrandTerminatorsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ReverseStrandTerminatorsParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ReverseStrandTerminatorsParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReverseStrandTerminatorsParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ReverseStrandTerminatorsParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ReverseStrandTerminatorsParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(ReverseStrandTerminatorsParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReverseStrandTerminatorsParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(ReverseStrandTerminatorsParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ReverseStrandTerminatorsParser#reverse_strand_terminators}.
	 * @param ctx the parse tree
	 */
	void enterReverse_strand_terminators(ReverseStrandTerminatorsParser.Reverse_strand_terminatorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReverseStrandTerminatorsParser#reverse_strand_terminators}.
	 * @param ctx the parse tree
	 */
	void exitReverse_strand_terminators(ReverseStrandTerminatorsParser.Reverse_strand_terminatorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ReverseStrandTerminatorsParser#wildcard_type1}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_type1(ReverseStrandTerminatorsParser.Wildcard_type1Context ctx);
	/**
	 * Exit a parse tree produced by {@link ReverseStrandTerminatorsParser#wildcard_type1}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_type1(ReverseStrandTerminatorsParser.Wildcard_type1Context ctx);
	/**
	 * Enter a parse tree produced by {@link ReverseStrandTerminatorsParser#wildcard_type2}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_type2(ReverseStrandTerminatorsParser.Wildcard_type2Context ctx);
	/**
	 * Exit a parse tree produced by {@link ReverseStrandTerminatorsParser#wildcard_type2}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_type2(ReverseStrandTerminatorsParser.Wildcard_type2Context ctx);
	/**
	 * Enter a parse tree produced by {@link ReverseStrandTerminatorsParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(ReverseStrandTerminatorsParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link ReverseStrandTerminatorsParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(ReverseStrandTerminatorsParser.WildcardContext ctx);
}