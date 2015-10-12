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
		public List<WildcardContext> wildcard() {
			return getRuleContexts(WildcardContext.class);
		}
		public WildcardContext wildcard(int i) {
			return getRuleContext(WildcardContext.class,i);
		}
		public List<Super_coilingContext> super_coiling() {
			return getRuleContexts(Super_coilingContext.class);
		}
		public Super_coilingContext super_coiling(int i) {
			return getRuleContext(Super_coilingContext.class,i);
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
			setState(17); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(16);
					super_coiling();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(19); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(24);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
				{
				{
				setState(21);
				wildcard();
				}
				}
				setState(26);
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
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(SuperCoilingParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(SuperCoilingParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(SuperCoilingParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(SuperCoilingParser.FORWARD_PROMOTER, i);
		}
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
			int _alt;
			setState(53);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
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
						{
						setState(27);
						match(REVERSE_PROMOTER);
						}
						{
						setState(28);
						match(FORWARD_PROMOTER);
						}
						{
						setState(29);
						match(REVERSE_PROMOTER);
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
				setState(37); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(34);
						match(FORWARD_PROMOTER);
						}
						{
						setState(35);
						match(REVERSE_PROMOTER);
						}
						{
						setState(36);
						match(FORWARD_PROMOTER);
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(39); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(43); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(41);
						match(FORWARD_PROMOTER);
						}
						{
						setState(42);
						match(REVERSE_PROMOTER);
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(45); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(49); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(47);
						match(REVERSE_PROMOTER);
						}
						{
						setState(48);
						match(FORWARD_PROMOTER);
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(51); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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
			setState(154);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(56); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(55);
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
					setState(58); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(70); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(61); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(60);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(63); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(66); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(65);
								match(FORWARD_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(68); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(72); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(84); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(75); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(74);
							match(FORWARD_PROMOTER);
							}
							}
							setState(77); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						}
						setState(80); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(79);
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
							setState(82); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(103); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(89); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(88);
							match(FORWARD_PROMOTER);
							}
							}
							setState(91); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						}
						setState(94); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(93);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(96); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(99); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(98);
								match(FORWARD_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(101); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(105); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(117); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(108); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(107);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(110); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(113); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(112);
								match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(115); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(131); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(122); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(121);
							match(REVERSE_PROMOTER);
							}
							}
							setState(124); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==REVERSE_PROMOTER );
						}
						setState(127); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(126);
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
							setState(129); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(133); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(150); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						{
						setState(136); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(135);
							match(REVERSE_PROMOTER);
							}
							}
							setState(138); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==REVERSE_PROMOTER );
						}
						setState(141); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(140);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(143); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(146); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(145);
								match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(148); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(152); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\u009f\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\3\7\3\16\n\3\f\3\16\3\21\13\3\3\3\6"+
		"\3\24\n\3\r\3\16\3\25\3\3\7\3\31\n\3\f\3\16\3\34\13\3\3\4\3\4\3\4\6\4"+
		"!\n\4\r\4\16\4\"\3\4\3\4\3\4\6\4(\n\4\r\4\16\4)\3\4\3\4\6\4.\n\4\r\4\16"+
		"\4/\3\4\3\4\6\4\64\n\4\r\4\16\4\65\5\48\n\4\3\5\6\5;\n\5\r\5\16\5<\3\5"+
		"\6\5@\n\5\r\5\16\5A\3\5\6\5E\n\5\r\5\16\5F\6\5I\n\5\r\5\16\5J\3\5\6\5"+
		"N\n\5\r\5\16\5O\3\5\6\5S\n\5\r\5\16\5T\6\5W\n\5\r\5\16\5X\3\5\6\5\\\n"+
		"\5\r\5\16\5]\3\5\6\5a\n\5\r\5\16\5b\3\5\6\5f\n\5\r\5\16\5g\6\5j\n\5\r"+
		"\5\16\5k\3\5\6\5o\n\5\r\5\16\5p\3\5\6\5t\n\5\r\5\16\5u\6\5x\n\5\r\5\16"+
		"\5y\3\5\6\5}\n\5\r\5\16\5~\3\5\6\5\u0082\n\5\r\5\16\5\u0083\6\5\u0086"+
		"\n\5\r\5\16\5\u0087\3\5\6\5\u008b\n\5\r\5\16\5\u008c\3\5\6\5\u0090\n\5"+
		"\r\5\16\5\u0091\3\5\6\5\u0095\n\5\r\5\16\5\u0096\6\5\u0099\n\5\r\5\16"+
		"\5\u009a\5\5\u009d\n\5\3\5\2\2\6\2\4\6\b\2\3\3\2\5\n\u00bf\2\n\3\2\2\2"+
		"\4\17\3\2\2\2\6\67\3\2\2\2\b\u009c\3\2\2\2\n\13\5\4\3\2\13\3\3\2\2\2\f"+
		"\16\5\b\5\2\r\f\3\2\2\2\16\21\3\2\2\2\17\r\3\2\2\2\17\20\3\2\2\2\20\23"+
		"\3\2\2\2\21\17\3\2\2\2\22\24\5\6\4\2\23\22\3\2\2\2\24\25\3\2\2\2\25\23"+
		"\3\2\2\2\25\26\3\2\2\2\26\32\3\2\2\2\27\31\5\b\5\2\30\27\3\2\2\2\31\34"+
		"\3\2\2\2\32\30\3\2\2\2\32\33\3\2\2\2\33\5\3\2\2\2\34\32\3\2\2\2\35\36"+
		"\7\3\2\2\36\37\7\4\2\2\37!\7\3\2\2 \35\3\2\2\2!\"\3\2\2\2\" \3\2\2\2\""+
		"#\3\2\2\2#8\3\2\2\2$%\7\4\2\2%&\7\3\2\2&(\7\4\2\2\'$\3\2\2\2()\3\2\2\2"+
		")\'\3\2\2\2)*\3\2\2\2*8\3\2\2\2+,\7\4\2\2,.\7\3\2\2-+\3\2\2\2./\3\2\2"+
		"\2/-\3\2\2\2/\60\3\2\2\2\608\3\2\2\2\61\62\7\3\2\2\62\64\7\4\2\2\63\61"+
		"\3\2\2\2\64\65\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\67 \3\2"+
		"\2\2\67\'\3\2\2\2\67-\3\2\2\2\67\63\3\2\2\28\7\3\2\2\29;\t\2\2\2:9\3\2"+
		"\2\2;<\3\2\2\2<:\3\2\2\2<=\3\2\2\2=\u009d\3\2\2\2>@\t\2\2\2?>\3\2\2\2"+
		"@A\3\2\2\2A?\3\2\2\2AB\3\2\2\2BD\3\2\2\2CE\7\4\2\2DC\3\2\2\2EF\3\2\2\2"+
		"FD\3\2\2\2FG\3\2\2\2GI\3\2\2\2H?\3\2\2\2IJ\3\2\2\2JH\3\2\2\2JK\3\2\2\2"+
		"K\u009d\3\2\2\2LN\7\4\2\2ML\3\2\2\2NO\3\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3"+
		"\2\2\2QS\t\2\2\2RQ\3\2\2\2ST\3\2\2\2TR\3\2\2\2TU\3\2\2\2UW\3\2\2\2VM\3"+
		"\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y\u009d\3\2\2\2Z\\\7\4\2\2[Z\3\2\2"+
		"\2\\]\3\2\2\2][\3\2\2\2]^\3\2\2\2^`\3\2\2\2_a\t\2\2\2`_\3\2\2\2ab\3\2"+
		"\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2\2df\7\4\2\2ed\3\2\2\2fg\3\2\2\2ge\3\2"+
		"\2\2gh\3\2\2\2hj\3\2\2\2i[\3\2\2\2jk\3\2\2\2ki\3\2\2\2kl\3\2\2\2l\u009d"+
		"\3\2\2\2mo\t\2\2\2nm\3\2\2\2op\3\2\2\2pn\3\2\2\2pq\3\2\2\2qs\3\2\2\2r"+
		"t\7\3\2\2sr\3\2\2\2tu\3\2\2\2us\3\2\2\2uv\3\2\2\2vx\3\2\2\2wn\3\2\2\2"+
		"xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2z\u009d\3\2\2\2{}\7\3\2\2|{\3\2\2\2}~\3"+
		"\2\2\2~|\3\2\2\2~\177\3\2\2\2\177\u0081\3\2\2\2\u0080\u0082\t\2\2\2\u0081"+
		"\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2"+
		"\2\2\u0084\u0086\3\2\2\2\u0085|\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0085"+
		"\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u009d\3\2\2\2\u0089\u008b\7\3\2\2\u008a"+
		"\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\u008f\3\2\2\2\u008e\u0090\t\2\2\2\u008f\u008e\3\2\2\2\u0090"+
		"\u0091\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0094\3\2"+
		"\2\2\u0093\u0095\7\3\2\2\u0094\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096"+
		"\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0099\3\2\2\2\u0098\u008a\3\2"+
		"\2\2\u0099\u009a\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b"+
		"\u009d\3\2\2\2\u009c:\3\2\2\2\u009cH\3\2\2\2\u009cV\3\2\2\2\u009ci\3\2"+
		"\2\2\u009cw\3\2\2\2\u009c\u0085\3\2\2\2\u009c\u0098\3\2\2\2\u009d\t\3"+
		"\2\2\2 \17\25\32\")/\65\67<AFJOTX]bgkpuy~\u0083\u0087\u008c\u0091\u0096"+
		"\u009a\u009c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}