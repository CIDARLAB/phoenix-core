// Generated from Structural.g4 by ANTLR 4.3

    package org.cidarlab.phoenix.core.grammars.structural;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StructuralParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final String[] tokenNames = {
		"<INVALID>", "REVERSE_PROMOTER", "FORWARD_PROMOTER", "'<r'", "'r'", "REVERSE_CDS", 
		"FORWARD_CDS", "'<t'", "'t'", "WS"
	};
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_forward_tu = 2, RULE_module_wildcard_start = 3, 
		RULE_module_wildcard_end = 4, RULE_wildcard_fp = 5, RULE_wildcard_fc = 6, 
		RULE_wildcard_rp = 7, RULE_wildcard_rc = 8;
	public static final String[] ruleNames = {
		"root", "module", "forward_tu", "module_wildcard_start", "module_wildcard_end", 
		"wildcard_fp", "wildcard_fc", "wildcard_rp", "wildcard_rc"
	};

	@Override
	public String getGrammarFileName() { return "Structural.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public StructuralParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RootContext extends ParserRuleContext {
		public ModuleContext module() {
			return getRuleContext(ModuleContext.class,0);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18); module();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModuleContext extends ParserRuleContext {
		public List<Forward_tuContext> forward_tu() {
			return getRuleContexts(Forward_tuContext.class);
		}
		public Forward_tuContext forward_tu(int i) {
			return getRuleContext(Forward_tuContext.class,i);
		}
		public List<Module_wildcard_endContext> module_wildcard_end() {
			return getRuleContexts(Module_wildcard_endContext.class);
		}
		public List<Module_wildcard_startContext> module_wildcard_start() {
			return getRuleContexts(Module_wildcard_startContext.class);
		}
		public Module_wildcard_startContext module_wildcard_start(int i) {
			return getRuleContext(Module_wildcard_startContext.class,i);
		}
		public Module_wildcard_endContext module_wildcard_end(int i) {
			return getRuleContext(Module_wildcard_endContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
				{
				{
				setState(20); module_wildcard_start();
				}
				}
				setState(25);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(27); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(26); forward_tu();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(29); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
				{
				{
				setState(31); module_wildcard_end();
				}
				}
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Forward_tuContext extends ParserRuleContext {
		public TerminalNode FORWARD_TERMINATOR() { return getToken(StructuralParser.FORWARD_TERMINATOR, 0); }
		public List<TerminalNode> FORWARD_CDS() { return getTokens(StructuralParser.FORWARD_CDS); }
		public Wildcard_fpContext wildcard_fp(int i) {
			return getRuleContext(Wildcard_fpContext.class,i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(StructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(StructuralParser.FORWARD_RBS, i);
		}
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(StructuralParser.FORWARD_CDS, i);
		}
		public List<Wildcard_fcContext> wildcard_fc() {
			return getRuleContexts(Wildcard_fcContext.class);
		}
		public List<Wildcard_fpContext> wildcard_fp() {
			return getRuleContexts(Wildcard_fpContext.class);
		}
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(StructuralParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(StructuralParser.FORWARD_RBS); }
		public Wildcard_fcContext wildcard_fc(int i) {
			return getRuleContext(Wildcard_fcContext.class,i);
		}
		public Forward_tuContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forward_tu; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterForward_tu(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitForward_tu(this);
		}
	}

	public final Forward_tuContext forward_tu() throws RecognitionException {
		Forward_tuContext _localctx = new Forward_tuContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_forward_tu);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(37); match(FORWARD_PROMOTER);
				}
				}
				setState(40); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==FORWARD_PROMOTER );
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
				{
				{
				setState(42); wildcard_fp();
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(50); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(48); match(FORWARD_RBS);
				setState(49); match(FORWARD_CDS);
				}
				}
				setState(52); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==FORWARD_RBS );
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
				{
				{
				setState(54); wildcard_fc();
				}
				}
				setState(59);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(60); match(FORWARD_TERMINATOR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Module_wildcard_startContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(StructuralParser.FORWARD_TERMINATOR); }
		public List<TerminalNode> REVERSE_RBS() { return getTokens(StructuralParser.REVERSE_RBS); }
		public List<TerminalNode> REVERSE_CDS() { return getTokens(StructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(StructuralParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(StructuralParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(StructuralParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(StructuralParser.FORWARD_RBS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(StructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(StructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(StructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(StructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(StructuralParser.REVERSE_PROMOTER, i);
		}
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(StructuralParser.FORWARD_RBS, i);
		}
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(StructuralParser.FORWARD_TERMINATOR, i);
		}
		public Module_wildcard_startContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module_wildcard_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterModule_wildcard_start(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitModule_wildcard_start(this);
		}
	}

	public final Module_wildcard_startContext module_wildcard_start() throws RecognitionException {
		Module_wildcard_startContext _localctx = new Module_wildcard_startContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_module_wildcard_start);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(63); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(62);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(65); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Module_wildcard_endContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(StructuralParser.FORWARD_TERMINATOR); }
		public List<TerminalNode> REVERSE_RBS() { return getTokens(StructuralParser.REVERSE_RBS); }
		public List<TerminalNode> REVERSE_CDS() { return getTokens(StructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(StructuralParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(StructuralParser.FORWARD_CDS); }
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(StructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(StructuralParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(StructuralParser.FORWARD_RBS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(StructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(StructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(StructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(StructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(StructuralParser.REVERSE_PROMOTER, i);
		}
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(StructuralParser.FORWARD_RBS, i);
		}
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(StructuralParser.FORWARD_TERMINATOR, i);
		}
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(StructuralParser.FORWARD_PROMOTER, i);
		}
		public Module_wildcard_endContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module_wildcard_end; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterModule_wildcard_end(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitModule_wildcard_end(this);
		}
	}

	public final Module_wildcard_endContext module_wildcard_end() throws RecognitionException {
		Module_wildcard_endContext _localctx = new Module_wildcard_endContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_module_wildcard_end);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(68); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(67);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(70); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Wildcard_fpContext extends ParserRuleContext {
		public List<TerminalNode> REVERSE_RBS() { return getTokens(StructuralParser.REVERSE_RBS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(StructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(StructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(StructuralParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(StructuralParser.FORWARD_CDS); }
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(StructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(StructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(StructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(StructuralParser.REVERSE_PROMOTER, i);
		}
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(StructuralParser.FORWARD_CDS, i);
		}
		public Wildcard_fpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_fp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterWildcard_fp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitWildcard_fp(this);
		}
	}

	public final Wildcard_fpContext wildcard_fp() throws RecognitionException {
		Wildcard_fpContext _localctx = new Wildcard_fpContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_wildcard_fp);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(73); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(72);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(75); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Wildcard_fcContext extends ParserRuleContext {
		public List<TerminalNode> REVERSE_RBS() { return getTokens(StructuralParser.REVERSE_RBS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(StructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(StructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(StructuralParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(StructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(StructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(StructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(StructuralParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(StructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(StructuralParser.FORWARD_PROMOTER, i);
		}
		public Wildcard_fcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_fc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterWildcard_fc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitWildcard_fc(this);
		}
	}

	public final Wildcard_fcContext wildcard_fc() throws RecognitionException {
		Wildcard_fcContext _localctx = new Wildcard_fcContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_wildcard_fc);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(78); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(77);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(80); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Wildcard_rpContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(StructuralParser.FORWARD_TERMINATOR); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(StructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(StructuralParser.REVERSE_CDS); }
		public List<TerminalNode> FORWARD_CDS() { return getTokens(StructuralParser.FORWARD_CDS); }
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(StructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(StructuralParser.FORWARD_RBS, i);
		}
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(StructuralParser.FORWARD_CDS, i);
		}
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(StructuralParser.FORWARD_TERMINATOR, i);
		}
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(StructuralParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(StructuralParser.FORWARD_RBS); }
		public Wildcard_rpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterWildcard_rp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitWildcard_rp(this);
		}
	}

	public final Wildcard_rpContext wildcard_rp() throws RecognitionException {
		Wildcard_rpContext _localctx = new Wildcard_rpContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_wildcard_rp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(82);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				}
				setState(85); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Wildcard_rcContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(StructuralParser.FORWARD_TERMINATOR); }
		public List<TerminalNode> FORWARD_CDS() { return getTokens(StructuralParser.FORWARD_CDS); }
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(StructuralParser.REVERSE_PROMOTER); }
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(StructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(StructuralParser.FORWARD_RBS, i);
		}
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(StructuralParser.REVERSE_PROMOTER, i);
		}
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(StructuralParser.FORWARD_CDS, i);
		}
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(StructuralParser.FORWARD_TERMINATOR, i);
		}
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(StructuralParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(StructuralParser.FORWARD_RBS); }
		public Wildcard_rcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterWildcard_rc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitWildcard_rc(this);
		}
	}

	public final Wildcard_rcContext wildcard_rc() throws RecognitionException {
		Wildcard_rcContext _localctx = new Wildcard_rcContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_wildcard_rc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(87);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				}
				setState(90); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13_\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2\3\3"+
		"\7\3\30\n\3\f\3\16\3\33\13\3\3\3\6\3\36\n\3\r\3\16\3\37\3\3\7\3#\n\3\f"+
		"\3\16\3&\13\3\3\4\6\4)\n\4\r\4\16\4*\3\4\7\4.\n\4\f\4\16\4\61\13\4\3\4"+
		"\3\4\6\4\65\n\4\r\4\16\4\66\3\4\7\4:\n\4\f\4\16\4=\13\4\3\4\3\4\3\5\6"+
		"\5B\n\5\r\5\16\5C\3\6\6\6G\n\6\r\6\16\6H\3\7\6\7L\n\7\r\7\16\7M\3\b\6"+
		"\bQ\n\b\r\b\16\bR\3\t\6\tV\n\t\r\t\16\tW\3\n\6\n[\n\n\r\n\16\n\\\3\n\2"+
		"\2\13\2\4\6\b\n\f\16\20\22\2\b\4\2\3\3\5\n\3\2\3\n\5\2\3\3\5\5\7\t\5\2"+
		"\3\5\7\7\t\t\5\2\4\4\6\b\n\n\6\2\3\4\6\6\b\b\n\nb\2\24\3\2\2\2\4\31\3"+
		"\2\2\2\6(\3\2\2\2\bA\3\2\2\2\nF\3\2\2\2\fK\3\2\2\2\16P\3\2\2\2\20U\3\2"+
		"\2\2\22Z\3\2\2\2\24\25\5\4\3\2\25\3\3\2\2\2\26\30\5\b\5\2\27\26\3\2\2"+
		"\2\30\33\3\2\2\2\31\27\3\2\2\2\31\32\3\2\2\2\32\35\3\2\2\2\33\31\3\2\2"+
		"\2\34\36\5\6\4\2\35\34\3\2\2\2\36\37\3\2\2\2\37\35\3\2\2\2\37 \3\2\2\2"+
		" $\3\2\2\2!#\5\n\6\2\"!\3\2\2\2#&\3\2\2\2$\"\3\2\2\2$%\3\2\2\2%\5\3\2"+
		"\2\2&$\3\2\2\2\')\7\4\2\2(\'\3\2\2\2)*\3\2\2\2*(\3\2\2\2*+\3\2\2\2+/\3"+
		"\2\2\2,.\5\f\7\2-,\3\2\2\2.\61\3\2\2\2/-\3\2\2\2/\60\3\2\2\2\60\64\3\2"+
		"\2\2\61/\3\2\2\2\62\63\7\6\2\2\63\65\7\b\2\2\64\62\3\2\2\2\65\66\3\2\2"+
		"\2\66\64\3\2\2\2\66\67\3\2\2\2\67;\3\2\2\28:\5\16\b\298\3\2\2\2:=\3\2"+
		"\2\2;9\3\2\2\2;<\3\2\2\2<>\3\2\2\2=;\3\2\2\2>?\7\n\2\2?\7\3\2\2\2@B\t"+
		"\2\2\2A@\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\t\3\2\2\2EG\t\3\2\2FE"+
		"\3\2\2\2GH\3\2\2\2HF\3\2\2\2HI\3\2\2\2I\13\3\2\2\2JL\t\4\2\2KJ\3\2\2\2"+
		"LM\3\2\2\2MK\3\2\2\2MN\3\2\2\2N\r\3\2\2\2OQ\t\5\2\2PO\3\2\2\2QR\3\2\2"+
		"\2RP\3\2\2\2RS\3\2\2\2S\17\3\2\2\2TV\t\6\2\2UT\3\2\2\2VW\3\2\2\2WU\3\2"+
		"\2\2WX\3\2\2\2X\21\3\2\2\2Y[\t\7\2\2ZY\3\2\2\2[\\\3\2\2\2\\Z\3\2\2\2\\"+
		"]\3\2\2\2]\23\3\2\2\2\17\31\37$*/\66;CHMRW\\";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}