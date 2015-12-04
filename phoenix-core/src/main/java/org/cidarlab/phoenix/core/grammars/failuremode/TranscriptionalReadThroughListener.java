// Generated from TranscriptionalReadThrough.g4 by ANTLR 4.3

    package org.cidarlab.phoenix.core.grammars.failuremode;

import org.antlr.v4.runtime.misc.NotNull;
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
	void enterRoot(@NotNull TranscriptionalReadThroughParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(@NotNull TranscriptionalReadThroughParser.RootContext ctx);

	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(@NotNull TranscriptionalReadThroughParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(@NotNull TranscriptionalReadThroughParser.ModuleContext ctx);

	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#transcriptional_readthrough}.
	 * @param ctx the parse tree
	 */
	void enterTranscriptional_readthrough(@NotNull TranscriptionalReadThroughParser.Transcriptional_readthroughContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#transcriptional_readthrough}.
	 * @param ctx the parse tree
	 */
	void exitTranscriptional_readthrough(@NotNull TranscriptionalReadThroughParser.Transcriptional_readthroughContext ctx);

	/**
	 * Enter a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(@NotNull TranscriptionalReadThroughParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalReadThroughParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(@NotNull TranscriptionalReadThroughParser.WildcardContext ctx);
}