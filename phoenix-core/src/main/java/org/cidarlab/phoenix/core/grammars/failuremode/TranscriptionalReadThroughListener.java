// Generated from TranscriptionalReadThrough.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TranscriptionalReadThroughParser}.
 */
public interface TranscriptionalReadThroughListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(TranscriptionalReadThroughParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(TranscriptionalReadThroughParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(TranscriptionalReadThroughParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(TranscriptionalReadThroughParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#transcriptional_readthrough}.
	 * @param ctx the parse tree
	 */
	void enterTranscriptional_readthrough(TranscriptionalReadThroughParser.Transcriptional_readthroughContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#transcriptional_readthrough}.
	 * @param ctx the parse tree
	 */
	void exitTranscriptional_readthrough(TranscriptionalReadThroughParser.Transcriptional_readthroughContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#reverse_strand}.
	 * @param ctx the parse tree
	 */
	void enterReverse_strand(TranscriptionalReadThroughParser.Reverse_strandContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#reverse_strand}.
	 * @param ctx the parse tree
	 */
	void exitReverse_strand(TranscriptionalReadThroughParser.Reverse_strandContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#forward_strand}.
	 * @param ctx the parse tree
	 */
	void enterForward_strand(TranscriptionalReadThroughParser.Forward_strandContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#forward_strand}.
	 * @param ctx the parse tree
	 */
	void exitForward_strand(TranscriptionalReadThroughParser.Forward_strandContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(TranscriptionalReadThroughParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(TranscriptionalReadThroughParser.WildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard_type1}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_type1(TranscriptionalReadThroughParser.Wildcard_type1Context ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard_type1}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_type1(TranscriptionalReadThroughParser.Wildcard_type1Context ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard_type2}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_type2(TranscriptionalReadThroughParser.Wildcard_type2Context ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard_type2}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_type2(TranscriptionalReadThroughParser.Wildcard_type2Context ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard_type3}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_type3(TranscriptionalReadThroughParser.Wildcard_type3Context ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard_type3}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_type3(TranscriptionalReadThroughParser.Wildcard_type3Context ctx);
}