// Generated from ReverseStrandTerminators.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ReverseStrandTerminatorsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_reverse_strand_terminators = 2, RULE_wildcard_type1 = 3, 
		RULE_wildcard_type2 = 4, RULE_wildcard = 5;
	public static final String[] ruleNames = {
		"root", "module", "reverse_strand_terminators", "wildcard_type1", "wildcard_type2", 
		"wildcard"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'<p'", "'p'", "'<r'", "'r'", null, null, "'<t'", "'t'"
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
	public String getGrammarFileName() { return "ReverseStrandTerminators.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ReverseStrandTerminatorsParser(TokenStream input) {
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
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
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
		public List<Reverse_strand_terminatorsContext> reverse_strand_terminators() {
			return getRuleContexts(Reverse_strand_terminatorsContext.class);
		}
		public Reverse_strand_terminatorsContext reverse_strand_terminators(int i) {
			return getRuleContext(Reverse_strand_terminatorsContext.class,i);
		}
		public List<WildcardContext> wildcard() {
			return getRuleContexts(WildcardContext.class);
		}
		public WildcardContext wildcard(int i) {
			return getRuleContext(WildcardContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		int _la;
		try {
			int _alt;
			setState(70);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(25); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(15); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(14);
							reverse_strand_terminators();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(17); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(22);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(19);
							wildcard();
							}
							} 
						}
						setState(24);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					}
					}
					}
					setState(27); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER || _la==FORWARD_TERMINATOR );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(40); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(32);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(29);
							wildcard();
							}
							} 
						}
						setState(34);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
					}
					setState(36); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(35);
							reverse_strand_terminators();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(38); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					setState(42); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(61); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(47);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(44);
							wildcard();
							}
							} 
						}
						setState(49);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
					}
					setState(51); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(50);
							reverse_strand_terminators();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(53); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(58);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(55);
							wildcard();
							}
							} 
						}
						setState(60);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					}
					}
					}
					setState(63); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(66); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(65);
					wildcard();
					}
					}
					setState(68); 
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

	public static class Reverse_strand_terminatorsContext extends ParserRuleContext {
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_PROMOTER, i);
		}
		public List<Wildcard_type1Context> wildcard_type1() {
			return getRuleContexts(Wildcard_type1Context.class);
		}
		public Wildcard_type1Context wildcard_type1(int i) {
			return getRuleContext(Wildcard_type1Context.class,i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_TERMINATOR, i);
		}
		public List<Wildcard_type2Context> wildcard_type2() {
			return getRuleContexts(Wildcard_type2Context.class);
		}
		public Wildcard_type2Context wildcard_type2(int i) {
			return getRuleContext(Wildcard_type2Context.class,i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_PROMOTER, i);
		}
		public Reverse_strand_terminatorsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reverse_strand_terminators; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).enterReverse_strand_terminators(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).exitReverse_strand_terminators(this);
		}
	}

	public final Reverse_strand_terminatorsContext reverse_strand_terminators() throws RecognitionException {
		Reverse_strand_terminatorsContext _localctx = new Reverse_strand_terminatorsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_reverse_strand_terminators);
		int _la;
		try {
			int _alt;
			setState(104);
			switch (_input.LA(1)) {
			case FORWARD_PROMOTER:
				enterOuterAlt(_localctx, 1);
				{
				setState(84); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(73); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(72);
							match(FORWARD_PROMOTER);
							}
							}
							setState(75); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						setState(80);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS))) != 0)) {
							{
							{
							setState(77);
							wildcard_type1();
							}
							}
							setState(82);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						{
						setState(83);
						match(REVERSE_TERMINATOR);
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(86); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case FORWARD_TERMINATOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(100); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(88);
						match(FORWARD_TERMINATOR);
						}
						setState(92);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS))) != 0)) {
							{
							{
							setState(89);
							wildcard_type2();
							}
							}
							setState(94);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(96); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(95);
								match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(98); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(102); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class Wildcard_type1Context extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_PROMOTER, i);
		}
		public Wildcard_type1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_type1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).enterWildcard_type1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).exitWildcard_type1(this);
		}
	}

	public final Wildcard_type1Context wildcard_type1() throws RecognitionException {
		Wildcard_type1Context _localctx = new Wildcard_type1Context(_ctx, getState());
		enterRule(_localctx, 6, RULE_wildcard_type1);
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
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS))) != 0)) ) {
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
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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

	public static class Wildcard_type2Context extends ParserRuleContext {
		public List<TerminalNode> REVERSE_CDS() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_PROMOTER, i);
		}
		public Wildcard_type2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_type2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).enterWildcard_type2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).exitWildcard_type2(this);
		}
	}

	public final Wildcard_type2Context wildcard_type2() throws RecognitionException {
		Wildcard_type2Context _localctx = new Wildcard_type2Context(_ctx, getState());
		enterRule(_localctx, 8, RULE_wildcard_type2);
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
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS))) != 0)) ) {
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
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
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

	public static class WildcardContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(ReverseStrandTerminatorsParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(ReverseStrandTerminatorsParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(ReverseStrandTerminatorsParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(ReverseStrandTerminatorsParser.REVERSE_TERMINATOR, i);
		}
		public WildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ReverseStrandTerminatorsListener ) ((ReverseStrandTerminatorsListener)listener).exitWildcard(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_wildcard);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(117); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(116);
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
				setState(119); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13|\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\3\6\3\22\n\3\r\3\16\3\23"+
		"\3\3\7\3\27\n\3\f\3\16\3\32\13\3\6\3\34\n\3\r\3\16\3\35\3\3\7\3!\n\3\f"+
		"\3\16\3$\13\3\3\3\6\3\'\n\3\r\3\16\3(\6\3+\n\3\r\3\16\3,\3\3\7\3\60\n"+
		"\3\f\3\16\3\63\13\3\3\3\6\3\66\n\3\r\3\16\3\67\3\3\7\3;\n\3\f\3\16\3>"+
		"\13\3\6\3@\n\3\r\3\16\3A\3\3\6\3E\n\3\r\3\16\3F\5\3I\n\3\3\4\6\4L\n\4"+
		"\r\4\16\4M\3\4\7\4Q\n\4\f\4\16\4T\13\4\3\4\6\4W\n\4\r\4\16\4X\3\4\3\4"+
		"\7\4]\n\4\f\4\16\4`\13\4\3\4\6\4c\n\4\r\4\16\4d\6\4g\n\4\r\4\16\4h\5\4"+
		"k\n\4\3\5\6\5n\n\5\r\5\16\5o\3\6\6\6s\n\6\r\6\16\6t\3\7\6\7x\n\7\r\7\16"+
		"\7y\3\7\2\2\b\2\4\6\b\n\f\2\5\4\2\3\3\5\b\3\2\4\b\3\2\3\n\u008d\2\16\3"+
		"\2\2\2\4H\3\2\2\2\6j\3\2\2\2\bm\3\2\2\2\nr\3\2\2\2\fw\3\2\2\2\16\17\5"+
		"\4\3\2\17\3\3\2\2\2\20\22\5\6\4\2\21\20\3\2\2\2\22\23\3\2\2\2\23\21\3"+
		"\2\2\2\23\24\3\2\2\2\24\30\3\2\2\2\25\27\5\f\7\2\26\25\3\2\2\2\27\32\3"+
		"\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2\31\34\3\2\2\2\32\30\3\2\2\2\33\21\3"+
		"\2\2\2\34\35\3\2\2\2\35\33\3\2\2\2\35\36\3\2\2\2\36I\3\2\2\2\37!\5\f\7"+
		"\2 \37\3\2\2\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#&\3\2\2\2$\"\3\2\2\2%\'"+
		"\5\6\4\2&%\3\2\2\2\'(\3\2\2\2(&\3\2\2\2()\3\2\2\2)+\3\2\2\2*\"\3\2\2\2"+
		"+,\3\2\2\2,*\3\2\2\2,-\3\2\2\2-I\3\2\2\2.\60\5\f\7\2/.\3\2\2\2\60\63\3"+
		"\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\64\66\5\6"+
		"\4\2\65\64\3\2\2\2\66\67\3\2\2\2\67\65\3\2\2\2\678\3\2\2\28<\3\2\2\29"+
		";\5\f\7\2:9\3\2\2\2;>\3\2\2\2<:\3\2\2\2<=\3\2\2\2=@\3\2\2\2><\3\2\2\2"+
		"?\61\3\2\2\2@A\3\2\2\2A?\3\2\2\2AB\3\2\2\2BI\3\2\2\2CE\5\f\7\2DC\3\2\2"+
		"\2EF\3\2\2\2FD\3\2\2\2FG\3\2\2\2GI\3\2\2\2H\33\3\2\2\2H*\3\2\2\2H?\3\2"+
		"\2\2HD\3\2\2\2I\5\3\2\2\2JL\7\4\2\2KJ\3\2\2\2LM\3\2\2\2MK\3\2\2\2MN\3"+
		"\2\2\2NR\3\2\2\2OQ\5\b\5\2PO\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3\2\2\2SU\3"+
		"\2\2\2TR\3\2\2\2UW\7\t\2\2VK\3\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2\2Yk\3"+
		"\2\2\2Z^\7\n\2\2[]\5\n\6\2\\[\3\2\2\2]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_"+
		"b\3\2\2\2`^\3\2\2\2ac\7\3\2\2ba\3\2\2\2cd\3\2\2\2db\3\2\2\2de\3\2\2\2"+
		"eg\3\2\2\2fZ\3\2\2\2gh\3\2\2\2hf\3\2\2\2hi\3\2\2\2ik\3\2\2\2jV\3\2\2\2"+
		"jf\3\2\2\2k\7\3\2\2\2ln\t\2\2\2ml\3\2\2\2no\3\2\2\2om\3\2\2\2op\3\2\2"+
		"\2p\t\3\2\2\2qs\t\3\2\2rq\3\2\2\2st\3\2\2\2tr\3\2\2\2tu\3\2\2\2u\13\3"+
		"\2\2\2vx\t\4\2\2wv\3\2\2\2xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2z\r\3\2\2\2\30"+
		"\23\30\35\"(,\61\67<AFHMRX^dhjoty";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}