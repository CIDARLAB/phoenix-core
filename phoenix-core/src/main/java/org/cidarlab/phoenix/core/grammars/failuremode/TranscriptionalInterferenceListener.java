// Generated from TranscriptionalInterference.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TranscriptionalInterferenceParser}.
 */
public interface TranscriptionalInterferenceListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(TranscriptionalInterferenceParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(TranscriptionalInterferenceParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(TranscriptionalInterferenceParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(TranscriptionalInterferenceParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#transcriptional_interference}.
	 * @param ctx the parse tree
	 */
	void enterTranscriptional_interference(TranscriptionalInterferenceParser.Transcriptional_interferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#transcriptional_interference}.
	 * @param ctx the parse tree
	 */
	void exitTranscriptional_interference(TranscriptionalInterferenceParser.Transcriptional_interferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(TranscriptionalInterferenceParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(TranscriptionalInterferenceParser.WildcardContext ctx);
}