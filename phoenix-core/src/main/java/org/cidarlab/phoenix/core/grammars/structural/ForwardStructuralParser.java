// Generated from ForwardStructural.g4 by ANTLR 4.5.1

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
public class ForwardStructuralParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_forward_tu = 2, RULE_module_wildcard = 3, 
		RULE_wildcard_fp = 4, RULE_wildcard_fc = 5, RULE_wildcard_rp = 6, RULE_wildcard_rc = 7;
	public static final String[] ruleNames = {
		"root", "module", "forward_tu", "module_wildcard", "wildcard_fp", "wildcard_fc", 
		"wildcard_rp", "wildcard_rc"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, "'<r'", "'r'", null, null, "'<t'", "'t'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "REVERSE_PROMOTER", "FORWARD_PROMOTER", "REVERSE_RBS", "FORWARD_RBS", 
		"REVERSE_CDS", "FORWARD_CDS", "REVERSE_TERMINATOR", "FORWARD_TERMINATOR", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ForwardStructural.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ForwardStructuralParser(TokenStream input) {
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
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			module();
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
		public List<Module_wildcardContext> module_wildcard() {
			return getRuleContexts(Module_wildcardContext.class);
		}
		public Module_wildcardContext module_wildcard(int i) {
			return getRuleContext(Module_wildcardContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		int _la;
		try {
			int _alt;
			setState(74);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(19); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(18);
					forward_tu();
					}
					}
					setState(21); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(34); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(24); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(23);
							forward_tu();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(26); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(31);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(28);
							module_wildcard();
							}
							} 
						}
						setState(33);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
					}
					}
					}
					setState(36); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(49); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(41);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(38);
							module_wildcard();
							}
							} 
						}
						setState(43);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
					}
					setState(45); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(44);
							forward_tu();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(47); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					setState(51); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(70); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(56);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(53);
							module_wildcard();
							}
							} 
						}
						setState(58);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
					}
					setState(60); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(59);
							forward_tu();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(62); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(67);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(64);
							module_wildcard();
							}
							} 
						}
						setState(69);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
					}
					}
					}
					setState(72); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
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
		public TerminalNode FORWARD_TERMINATOR() { return getToken(ForwardStructuralParser.FORWARD_TERMINATOR, 0); }
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ForwardStructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.FORWARD_PROMOTER, i);
		}
		public List<Wildcard_fpContext> wildcard_fp() {
			return getRuleContexts(Wildcard_fpContext.class);
		}
		public Wildcard_fpContext wildcard_fp(int i) {
			return getRuleContext(Wildcard_fpContext.class,i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(ForwardStructuralParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ForwardStructuralParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_CDS, i);
		}
		public List<Wildcard_fcContext> wildcard_fc() {
			return getRuleContexts(Wildcard_fcContext.class);
		}
		public Wildcard_fcContext wildcard_fc(int i) {
			return getRuleContext(Wildcard_fcContext.class,i);
		}
		public Forward_tuContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forward_tu; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterForward_tu(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitForward_tu(this);
		}
	}

	public final Forward_tuContext forward_tu() throws RecognitionException {
		Forward_tuContext _localctx = new Forward_tuContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_forward_tu);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(76);
				match(FORWARD_PROMOTER);
				}
				}
				setState(79); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==FORWARD_PROMOTER );
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
				{
				{
				setState(81);
				wildcard_fp();
				}
				}
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(89); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(87);
				match(FORWARD_RBS);
				setState(88);
				match(FORWARD_CDS);
				}
				}
				setState(91); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==FORWARD_RBS );
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
				{
				{
				setState(93);
				wildcard_fc();
				}
				}
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(99);
			match(FORWARD_TERMINATOR);
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

	public static class Module_wildcardContext extends ParserRuleContext {
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(ForwardStructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(ForwardStructuralParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(ForwardStructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(ForwardStructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(ForwardStructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(ForwardStructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(ForwardStructuralParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(ForwardStructuralParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ForwardStructuralParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(ForwardStructuralParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(ForwardStructuralParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ForwardStructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.FORWARD_PROMOTER, i);
		}
		public Module_wildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module_wildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterModule_wildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitModule_wildcard(this);
		}
	}

	public final Module_wildcardContext module_wildcard() throws RecognitionException {
		Module_wildcardContext _localctx = new Module_wildcardContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_module_wildcard);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(102); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(101);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(104); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
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
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(ForwardStructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(ForwardStructuralParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(ForwardStructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(ForwardStructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(ForwardStructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(ForwardStructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(ForwardStructuralParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ForwardStructuralParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_CDS, i);
		}
		public Wildcard_fpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_fp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterWildcard_fp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitWildcard_fp(this);
		}
	}

	public final Wildcard_fpContext wildcard_fp() throws RecognitionException {
		Wildcard_fpContext _localctx = new Wildcard_fpContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_wildcard_fp);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(107); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(106);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(109); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
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
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(ForwardStructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(ForwardStructuralParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(ForwardStructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(ForwardStructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(ForwardStructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(ForwardStructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(ForwardStructuralParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ForwardStructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.FORWARD_PROMOTER, i);
		}
		public Wildcard_fcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_fc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterWildcard_fc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitWildcard_fc(this);
		}
	}

	public final Wildcard_fcContext wildcard_fc() throws RecognitionException {
		Wildcard_fcContext _localctx = new Wildcard_fcContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_wildcard_fc);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(112); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(111);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(114); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
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
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ForwardStructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(ForwardStructuralParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ForwardStructuralParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(ForwardStructuralParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(ForwardStructuralParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(ForwardStructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(ForwardStructuralParser.REVERSE_CDS, i);
		}
		public Wildcard_rpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterWildcard_rp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitWildcard_rp(this);
		}
	}

	public final Wildcard_rpContext wildcard_rp() throws RecognitionException {
		Wildcard_rpContext _localctx = new Wildcard_rpContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_wildcard_rp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(116);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(119); 
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
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ForwardStructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(ForwardStructuralParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ForwardStructuralParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ForwardStructuralParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(ForwardStructuralParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(ForwardStructuralParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(ForwardStructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(ForwardStructuralParser.REVERSE_PROMOTER, i);
		}
		public Wildcard_rcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).enterWildcard_rc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ForwardStructuralListener ) ((ForwardStructuralListener)listener).exitWildcard_rc(this);
		}
	}

	public final Wildcard_rcContext wildcard_rc() throws RecognitionException {
		Wildcard_rcContext _localctx = new Wildcard_rcContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_wildcard_rc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(121);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(124); 
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\u0081\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\3\6"+
		"\3\26\n\3\r\3\16\3\27\3\3\6\3\33\n\3\r\3\16\3\34\3\3\7\3 \n\3\f\3\16\3"+
		"#\13\3\6\3%\n\3\r\3\16\3&\3\3\7\3*\n\3\f\3\16\3-\13\3\3\3\6\3\60\n\3\r"+
		"\3\16\3\61\6\3\64\n\3\r\3\16\3\65\3\3\7\39\n\3\f\3\16\3<\13\3\3\3\6\3"+
		"?\n\3\r\3\16\3@\3\3\7\3D\n\3\f\3\16\3G\13\3\6\3I\n\3\r\3\16\3J\5\3M\n"+
		"\3\3\4\6\4P\n\4\r\4\16\4Q\3\4\7\4U\n\4\f\4\16\4X\13\4\3\4\3\4\6\4\\\n"+
		"\4\r\4\16\4]\3\4\7\4a\n\4\f\4\16\4d\13\4\3\4\3\4\3\5\6\5i\n\5\r\5\16\5"+
		"j\3\6\6\6n\n\6\r\6\16\6o\3\7\6\7s\n\7\r\7\16\7t\3\b\6\bx\n\b\r\b\16\b"+
		"y\3\t\6\t}\n\t\r\t\16\t~\3\t\2\2\n\2\4\6\b\n\f\16\20\2\7\3\2\3\n\5\2\3"+
		"\3\5\5\7\t\5\2\3\5\7\7\t\t\5\2\4\4\6\b\n\n\6\2\3\4\6\6\b\b\n\n\u008f\2"+
		"\22\3\2\2\2\4L\3\2\2\2\6O\3\2\2\2\bh\3\2\2\2\nm\3\2\2\2\fr\3\2\2\2\16"+
		"w\3\2\2\2\20|\3\2\2\2\22\23\5\4\3\2\23\3\3\2\2\2\24\26\5\6\4\2\25\24\3"+
		"\2\2\2\26\27\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2\2\30M\3\2\2\2\31\33\5\6"+
		"\4\2\32\31\3\2\2\2\33\34\3\2\2\2\34\32\3\2\2\2\34\35\3\2\2\2\35!\3\2\2"+
		"\2\36 \5\b\5\2\37\36\3\2\2\2 #\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\"%\3\2\2"+
		"\2#!\3\2\2\2$\32\3\2\2\2%&\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\'M\3\2\2\2(*\5"+
		"\b\5\2)(\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,/\3\2\2\2-+\3\2\2\2.\60"+
		"\5\6\4\2/.\3\2\2\2\60\61\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\64\3\2\2"+
		"\2\63+\3\2\2\2\64\65\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\66M\3\2\2\2\67"+
		"9\5\b\5\28\67\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;>\3\2\2\2<:\3\2\2"+
		"\2=?\5\6\4\2>=\3\2\2\2?@\3\2\2\2@>\3\2\2\2@A\3\2\2\2AE\3\2\2\2BD\5\b\5"+
		"\2CB\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2FI\3\2\2\2GE\3\2\2\2H:\3\2\2"+
		"\2IJ\3\2\2\2JH\3\2\2\2JK\3\2\2\2KM\3\2\2\2L\25\3\2\2\2L$\3\2\2\2L\63\3"+
		"\2\2\2LH\3\2\2\2M\5\3\2\2\2NP\7\4\2\2ON\3\2\2\2PQ\3\2\2\2QO\3\2\2\2QR"+
		"\3\2\2\2RV\3\2\2\2SU\5\n\6\2TS\3\2\2\2UX\3\2\2\2VT\3\2\2\2VW\3\2\2\2W"+
		"[\3\2\2\2XV\3\2\2\2YZ\7\6\2\2Z\\\7\b\2\2[Y\3\2\2\2\\]\3\2\2\2][\3\2\2"+
		"\2]^\3\2\2\2^b\3\2\2\2_a\5\f\7\2`_\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2"+
		"\2ce\3\2\2\2db\3\2\2\2ef\7\n\2\2f\7\3\2\2\2gi\t\2\2\2hg\3\2\2\2ij\3\2"+
		"\2\2jh\3\2\2\2jk\3\2\2\2k\t\3\2\2\2ln\t\3\2\2ml\3\2\2\2no\3\2\2\2om\3"+
		"\2\2\2op\3\2\2\2p\13\3\2\2\2qs\t\4\2\2rq\3\2\2\2st\3\2\2\2tr\3\2\2\2t"+
		"u\3\2\2\2u\r\3\2\2\2vx\t\5\2\2wv\3\2\2\2xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2"+
		"z\17\3\2\2\2{}\t\6\2\2|{\3\2\2\2}~\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177"+
		"\21\3\2\2\2\27\27\34!&+\61\65:@EJLQV]bjoty~";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}