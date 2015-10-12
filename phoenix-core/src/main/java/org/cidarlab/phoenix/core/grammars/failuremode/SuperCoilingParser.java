// Generated from SuperCoiling.g4 by ANTLR 4.5.1

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
public class SuperCoilingParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_super_coiling = 2, RULE_wildcard = 3;
	public static final String[] ruleNames = {
		"root", "module", "super_coiling", "wildcard"
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
	public String getGrammarFileName() { return "SuperCoiling.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SuperCoilingParser(TokenStream input) {
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
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
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
		public Super_coilingContext super_coiling() {
			return getRuleContext(Super_coilingContext.class,0);
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
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).exitModule(this);
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
			{
			setState(13);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(10);
					wildcard();
					}
					} 
				}
				setState(15);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			{
			setState(16);
			super_coiling();
			}
			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
				{
				{
				setState(17);
				wildcard();
				}
				}
				setState(22);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class Super_coilingContext extends ParserRuleContext {
		public TerminalNode FORWARD_PROMOTER() { return getToken(SuperCoilingParser.FORWARD_PROMOTER, 0); }
		public TerminalNode REVERSE_PROMOTER() { return getToken(SuperCoilingParser.REVERSE_PROMOTER, 0); }
		public Super_coilingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_super_coiling; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).enterSuper_coiling(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).exitSuper_coiling(this);
		}
	}

	public final Super_coilingContext super_coiling() throws RecognitionException {
		Super_coilingContext _localctx = new Super_coilingContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_super_coiling);
		try {
			setState(27);
			switch (_input.LA(1)) {
			case FORWARD_PROMOTER:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(23);
				match(FORWARD_PROMOTER);
				}
				{
				setState(24);
				match(REVERSE_PROMOTER);
				}
				}
				break;
			case REVERSE_PROMOTER:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(25);
				match(REVERSE_PROMOTER);
				}
				{
				setState(26);
				match(FORWARD_PROMOTER);
				}
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

	public static class WildcardContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(SuperCoilingParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(SuperCoilingParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(SuperCoilingParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(SuperCoilingParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(SuperCoilingParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(SuperCoilingParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(SuperCoilingParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(SuperCoilingParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(SuperCoilingParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(SuperCoilingParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(SuperCoilingParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(SuperCoilingParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(SuperCoilingParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(SuperCoilingParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(SuperCoilingParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(SuperCoilingParser.REVERSE_PROMOTER, i);
		}
		public WildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SuperCoilingListener ) ((SuperCoilingListener)listener).exitWildcard(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_wildcard);
		int _la;
		try {
			int _alt;
			setState(128);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(30); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(29);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
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
					setState(32); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(44); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(35); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(34);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(37); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(40); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(39);
								match(FORWARD_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(42); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(46); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(58); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(49); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(48);
							match(FORWARD_PROMOTER);
							}
							}
							setState(51); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						}
						setState(54); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(53);
								_la = _input.LA(1);
								if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
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
							setState(56); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(60); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(77); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(63); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(62);
							match(FORWARD_PROMOTER);
							}
							}
							setState(65); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						}
						setState(68); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(67);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(70); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
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
								match(FORWARD_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(75); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(79); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(91); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(82); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(81);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(84); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(87); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(86);
								match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(89); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(93); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(105); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(96); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(95);
							match(REVERSE_PROMOTER);
							}
							}
							setState(98); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==REVERSE_PROMOTER );
						}
						setState(101); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(100);
								_la = _input.LA(1);
								if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
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
							setState(103); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(107); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(124); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(110); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(109);
							match(REVERSE_PROMOTER);
							}
							}
							setState(112); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==REVERSE_PROMOTER );
						}
						setState(115); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(114);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(117); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(120); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(119);
								match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(122); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(126); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\u0085\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\3\7\3\16\n\3\f\3\16\3\21\13\3\3\3\3"+
		"\3\7\3\25\n\3\f\3\16\3\30\13\3\3\4\3\4\3\4\3\4\5\4\36\n\4\3\5\6\5!\n\5"+
		"\r\5\16\5\"\3\5\6\5&\n\5\r\5\16\5\'\3\5\6\5+\n\5\r\5\16\5,\6\5/\n\5\r"+
		"\5\16\5\60\3\5\6\5\64\n\5\r\5\16\5\65\3\5\6\59\n\5\r\5\16\5:\6\5=\n\5"+
		"\r\5\16\5>\3\5\6\5B\n\5\r\5\16\5C\3\5\6\5G\n\5\r\5\16\5H\3\5\6\5L\n\5"+
		"\r\5\16\5M\6\5P\n\5\r\5\16\5Q\3\5\6\5U\n\5\r\5\16\5V\3\5\6\5Z\n\5\r\5"+
		"\16\5[\6\5^\n\5\r\5\16\5_\3\5\6\5c\n\5\r\5\16\5d\3\5\6\5h\n\5\r\5\16\5"+
		"i\6\5l\n\5\r\5\16\5m\3\5\6\5q\n\5\r\5\16\5r\3\5\6\5v\n\5\r\5\16\5w\3\5"+
		"\6\5{\n\5\r\5\16\5|\6\5\177\n\5\r\5\16\5\u0080\5\5\u0083\n\5\3\5\2\2\6"+
		"\2\4\6\b\2\3\3\2\5\n\u009e\2\n\3\2\2\2\4\17\3\2\2\2\6\35\3\2\2\2\b\u0082"+
		"\3\2\2\2\n\13\5\4\3\2\13\3\3\2\2\2\f\16\5\b\5\2\r\f\3\2\2\2\16\21\3\2"+
		"\2\2\17\r\3\2\2\2\17\20\3\2\2\2\20\22\3\2\2\2\21\17\3\2\2\2\22\26\5\6"+
		"\4\2\23\25\5\b\5\2\24\23\3\2\2\2\25\30\3\2\2\2\26\24\3\2\2\2\26\27\3\2"+
		"\2\2\27\5\3\2\2\2\30\26\3\2\2\2\31\32\7\4\2\2\32\36\7\3\2\2\33\34\7\3"+
		"\2\2\34\36\7\4\2\2\35\31\3\2\2\2\35\33\3\2\2\2\36\7\3\2\2\2\37!\t\2\2"+
		"\2 \37\3\2\2\2!\"\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#\u0083\3\2\2\2$&\t\2\2"+
		"\2%$\3\2\2\2&\'\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(*\3\2\2\2)+\7\4\2\2*)\3"+
		"\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3\2\2\2-/\3\2\2\2.%\3\2\2\2/\60\3\2\2\2\60"+
		".\3\2\2\2\60\61\3\2\2\2\61\u0083\3\2\2\2\62\64\7\4\2\2\63\62\3\2\2\2\64"+
		"\65\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\679\t\2\2\28\67\3"+
		"\2\2\29:\3\2\2\2:8\3\2\2\2:;\3\2\2\2;=\3\2\2\2<\63\3\2\2\2=>\3\2\2\2>"+
		"<\3\2\2\2>?\3\2\2\2?\u0083\3\2\2\2@B\7\4\2\2A@\3\2\2\2BC\3\2\2\2CA\3\2"+
		"\2\2CD\3\2\2\2DF\3\2\2\2EG\t\2\2\2FE\3\2\2\2GH\3\2\2\2HF\3\2\2\2HI\3\2"+
		"\2\2IK\3\2\2\2JL\7\4\2\2KJ\3\2\2\2LM\3\2\2\2MK\3\2\2\2MN\3\2\2\2NP\3\2"+
		"\2\2OA\3\2\2\2PQ\3\2\2\2QO\3\2\2\2QR\3\2\2\2R\u0083\3\2\2\2SU\t\2\2\2"+
		"TS\3\2\2\2UV\3\2\2\2VT\3\2\2\2VW\3\2\2\2WY\3\2\2\2XZ\7\3\2\2YX\3\2\2\2"+
		"Z[\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\^\3\2\2\2]T\3\2\2\2^_\3\2\2\2_]\3\2\2"+
		"\2_`\3\2\2\2`\u0083\3\2\2\2ac\7\3\2\2ba\3\2\2\2cd\3\2\2\2db\3\2\2\2de"+
		"\3\2\2\2eg\3\2\2\2fh\t\2\2\2gf\3\2\2\2hi\3\2\2\2ig\3\2\2\2ij\3\2\2\2j"+
		"l\3\2\2\2kb\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2n\u0083\3\2\2\2oq\7\3"+
		"\2\2po\3\2\2\2qr\3\2\2\2rp\3\2\2\2rs\3\2\2\2su\3\2\2\2tv\t\2\2\2ut\3\2"+
		"\2\2vw\3\2\2\2wu\3\2\2\2wx\3\2\2\2xz\3\2\2\2y{\7\3\2\2zy\3\2\2\2{|\3\2"+
		"\2\2|z\3\2\2\2|}\3\2\2\2}\177\3\2\2\2~p\3\2\2\2\177\u0080\3\2\2\2\u0080"+
		"~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0083\3\2\2\2\u0082 \3\2\2\2\u0082"+
		".\3\2\2\2\u0082<\3\2\2\2\u0082O\3\2\2\2\u0082]\3\2\2\2\u0082k\3\2\2\2"+
		"\u0082~\3\2\2\2\u0083\t\3\2\2\2\33\17\26\35\"\',\60\65:>CHMQV[_dimrw|"+
		"\u0080\u0082";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}