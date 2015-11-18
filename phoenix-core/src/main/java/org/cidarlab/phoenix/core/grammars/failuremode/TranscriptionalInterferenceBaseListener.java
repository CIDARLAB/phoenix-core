// Generated from TranscriptionalInterference.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;


import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class provides an empty implementation of {@link TranscriptionalInterferenceListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class TranscriptionalInterferenceBaseListener implements TranscriptionalInterferenceListener {
    
    @Getter
    @Setter
    private int transcriptionalInterferenceCount;
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRoot(TranscriptionalInterferenceParser.RootContext ctx) {
            this.transcriptionalInterferenceCount = 0;
        }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRoot(TranscriptionalInterferenceParser.RootContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterModule(TranscriptionalInterferenceParser.ModuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitModule(TranscriptionalInterferenceParser.ModuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTranscriptional_interference(TranscriptionalInterferenceParser.Transcriptional_interferenceContext ctx) {
            this.transcriptionalInterferenceCount ++;
        }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTranscriptional_interference(TranscriptionalInterferenceParser.Transcriptional_interferenceContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterWildcard(TranscriptionalInterferenceParser.WildcardContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitWildcard(TranscriptionalInterferenceParser.WildcardContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) { }
}