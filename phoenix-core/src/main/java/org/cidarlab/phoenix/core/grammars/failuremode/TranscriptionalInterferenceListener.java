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
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds_term}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rbs_cds_term(TranscriptionalInterferenceParser.Wildcard_rbs_cds_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds_term}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rbs_cds_term(TranscriptionalInterferenceParser.Wildcard_rbs_cds_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rbs_cds(TranscriptionalInterferenceParser.Wildcard_rbs_cdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rbs_cds(TranscriptionalInterferenceParser.Wildcard_rbs_cdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds_ft}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rbs_cds_ft(TranscriptionalInterferenceParser.Wildcard_rbs_cds_ftContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds_ft}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rbs_cds_ft(TranscriptionalInterferenceParser.Wildcard_rbs_cds_ftContext ctx);
	/**
	 * Enter a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds_rt}.
	 * @param ctx the parse tree
	 */
	void enterWildcard_rbs_cds_rt(TranscriptionalInterferenceParser.Wildcard_rbs_cds_rtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TranscriptionalInterferenceParser#wildcard_rbs_cds_rt}.
	 * @param ctx the parse tree
	 */
	void exitWildcard_rbs_cds_rt(TranscriptionalInterferenceParser.Wildcard_rbs_cds_rtContext ctx);
}