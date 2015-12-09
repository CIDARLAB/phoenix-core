// Generated from TranscriptionalReadThrough.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;


import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class provides an empty implementation of {@link TranscriptionalReadThroughListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class TranscriptionalReadThroughBaseListener implements TranscriptionalReadThroughListener {
    
        
    @Getter
    @Setter
    private int transcriptionalReadThroughCount;
    
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRoot(TranscriptionalReadThroughParser.RootContext ctx) {
            this.transcriptionalReadThroughCount=0;
        }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRoot(TranscriptionalReadThroughParser.RootContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterModule(TranscriptionalReadThroughParser.ModuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitModule(TranscriptionalReadThroughParser.ModuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTranscriptional_readthrough(TranscriptionalReadThroughParser.Transcriptional_readthroughContext ctx) {
            this.transcriptionalReadThroughCount++;
        }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTranscriptional_readthrough(TranscriptionalReadThroughParser.Transcriptional_readthroughContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterReverse_strand(TranscriptionalReadThroughParser.Reverse_strandContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitReverse_strand(TranscriptionalReadThroughParser.Reverse_strandContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterForward_strand(TranscriptionalReadThroughParser.Forward_strandContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitForward_strand(TranscriptionalReadThroughParser.Forward_strandContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterWildcard(TranscriptionalReadThroughParser.WildcardContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitWildcard(TranscriptionalReadThroughParser.WildcardContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterWildcard_type1(TranscriptionalReadThroughParser.Wildcard_type1Context ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitWildcard_type1(TranscriptionalReadThroughParser.Wildcard_type1Context ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterWildcard_type2(TranscriptionalReadThroughParser.Wildcard_type2Context ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitWildcard_type2(TranscriptionalReadThroughParser.Wildcard_type2Context ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterWildcard_type3(TranscriptionalReadThroughParser.Wildcard_type3Context ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitWildcard_type3(TranscriptionalReadThroughParser.Wildcard_type3Context ctx) { }

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