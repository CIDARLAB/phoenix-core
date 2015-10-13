// Generated from RoadBlocking.g4 by ANTLR 4.5.1

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
public class RoadBlockingParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_road_blocking = 2, RULE_wildcard = 3;
	public static final String[] ruleNames = {
		"root", "module", "road_blocking", "wildcard"
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
	public String getGrammarFileName() { return "RoadBlocking.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RoadBlockingParser(TokenStream input) {
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
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).exitRoot(this);
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
		public List<Road_blockingContext> road_blocking() {
			return getRuleContexts(Road_blockingContext.class);
		}
		public Road_blockingContext road_blocking(int i) {
			return getRuleContext(Road_blockingContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).exitModule(this);
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
			setState(19);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(16);
					road_blocking();
					}
					} 
				}
				setState(21);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(25);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
				{
				{
				setState(22);
				wildcard();
				}
				}
				setState(27);
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

	public static class Road_blockingContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(RoadBlockingParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(RoadBlockingParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(RoadBlockingParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(RoadBlockingParser.REVERSE_PROMOTER, i);
		}
		public Road_blockingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_road_blocking; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).enterRoad_blocking(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).exitRoad_blocking(this);
		}
	}

	public final Road_blockingContext road_blocking() throws RecognitionException {
		Road_blockingContext _localctx = new Road_blockingContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_road_blocking);
		try {
			int _alt;
			setState(40);
			switch (_input.LA(1)) {
			case FORWARD_PROMOTER:
				enterOuterAlt(_localctx, 1);
				{
				{
				{
				setState(28);
				match(FORWARD_PROMOTER);
				}
				setState(30); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(29);
						match(FORWARD_PROMOTER);
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
				}
				break;
			case REVERSE_PROMOTER:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(35); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(34);
						match(REVERSE_PROMOTER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(37); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				{
				setState(39);
				match(REVERSE_PROMOTER);
				}
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
		public List<TerminalNode> FORWARD_RBS() { return getTokens(RoadBlockingParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(RoadBlockingParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(RoadBlockingParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(RoadBlockingParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(RoadBlockingParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(RoadBlockingParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(RoadBlockingParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(RoadBlockingParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(RoadBlockingParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(RoadBlockingParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(RoadBlockingParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(RoadBlockingParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(RoadBlockingParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(RoadBlockingParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(RoadBlockingParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(RoadBlockingParser.REVERSE_PROMOTER, i);
		}
		public WildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RoadBlockingListener ) ((RoadBlockingListener)listener).exitWildcard(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_wildcard);
		int _la;
		try {
			int _alt;
			setState(109);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(43); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(42);
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
					setState(45); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(53); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(48); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(47);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(50); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(52);
						match(FORWARD_PROMOTER);
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(55); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(63); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(57);
						match(FORWARD_PROMOTER);
						setState(59); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(58);
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
							setState(61); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(65); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(74); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(67);
						match(FORWARD_PROMOTER);
						setState(69); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(68);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(71); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						setState(73);
						match(FORWARD_PROMOTER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(76); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(84); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(79); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(78);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(81); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						{
						setState(83);
						match(REVERSE_PROMOTER);
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
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(94); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(88);
						match(REVERSE_PROMOTER);
						setState(90); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(89);
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
							setState(92); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(96); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(105); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(98);
						match(REVERSE_PROMOTER);
						setState(100); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(99);
							_la = _input.LA(1);
							if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
							_errHandler.recoverInline(this);
							} else {
								consume();
							}
							}
							}
							setState(102); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
						setState(104);
						match(REVERSE_PROMOTER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(107); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13r\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\3\7\3\16\n\3\f\3\16\3\21\13\3\3\3\7\3\24"+
		"\n\3\f\3\16\3\27\13\3\3\3\7\3\32\n\3\f\3\16\3\35\13\3\3\4\3\4\6\4!\n\4"+
		"\r\4\16\4\"\3\4\6\4&\n\4\r\4\16\4\'\3\4\5\4+\n\4\3\5\6\5.\n\5\r\5\16\5"+
		"/\3\5\6\5\63\n\5\r\5\16\5\64\3\5\6\58\n\5\r\5\16\59\3\5\3\5\6\5>\n\5\r"+
		"\5\16\5?\6\5B\n\5\r\5\16\5C\3\5\3\5\6\5H\n\5\r\5\16\5I\3\5\6\5M\n\5\r"+
		"\5\16\5N\3\5\6\5R\n\5\r\5\16\5S\3\5\6\5W\n\5\r\5\16\5X\3\5\3\5\6\5]\n"+
		"\5\r\5\16\5^\6\5a\n\5\r\5\16\5b\3\5\3\5\6\5g\n\5\r\5\16\5h\3\5\6\5l\n"+
		"\5\r\5\16\5m\5\5p\n\5\3\5\2\2\6\2\4\6\b\2\3\3\2\5\n\u0086\2\n\3\2\2\2"+
		"\4\17\3\2\2\2\6*\3\2\2\2\bo\3\2\2\2\n\13\5\4\3\2\13\3\3\2\2\2\f\16\5\b"+
		"\5\2\r\f\3\2\2\2\16\21\3\2\2\2\17\r\3\2\2\2\17\20\3\2\2\2\20\25\3\2\2"+
		"\2\21\17\3\2\2\2\22\24\5\6\4\2\23\22\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2"+
		"\2\25\26\3\2\2\2\26\33\3\2\2\2\27\25\3\2\2\2\30\32\5\b\5\2\31\30\3\2\2"+
		"\2\32\35\3\2\2\2\33\31\3\2\2\2\33\34\3\2\2\2\34\5\3\2\2\2\35\33\3\2\2"+
		"\2\36 \7\4\2\2\37!\7\4\2\2 \37\3\2\2\2!\"\3\2\2\2\" \3\2\2\2\"#\3\2\2"+
		"\2#+\3\2\2\2$&\7\3\2\2%$\3\2\2\2&\'\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2()\3"+
		"\2\2\2)+\7\3\2\2*\36\3\2\2\2*%\3\2\2\2+\7\3\2\2\2,.\t\2\2\2-,\3\2\2\2"+
		"./\3\2\2\2/-\3\2\2\2/\60\3\2\2\2\60p\3\2\2\2\61\63\t\2\2\2\62\61\3\2\2"+
		"\2\63\64\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\66\3\2\2\2\668\7\4\2\2"+
		"\67\62\3\2\2\289\3\2\2\29\67\3\2\2\29:\3\2\2\2:p\3\2\2\2;=\7\4\2\2<>\t"+
		"\2\2\2=<\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@B\3\2\2\2A;\3\2\2\2BC\3"+
		"\2\2\2CA\3\2\2\2CD\3\2\2\2Dp\3\2\2\2EG\7\4\2\2FH\t\2\2\2GF\3\2\2\2HI\3"+
		"\2\2\2IG\3\2\2\2IJ\3\2\2\2JK\3\2\2\2KM\7\4\2\2LE\3\2\2\2MN\3\2\2\2NL\3"+
		"\2\2\2NO\3\2\2\2Op\3\2\2\2PR\t\2\2\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3"+
		"\2\2\2TU\3\2\2\2UW\7\3\2\2VQ\3\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2\2Yp\3"+
		"\2\2\2Z\\\7\3\2\2[]\t\2\2\2\\[\3\2\2\2]^\3\2\2\2^\\\3\2\2\2^_\3\2\2\2"+
		"_a\3\2\2\2`Z\3\2\2\2ab\3\2\2\2b`\3\2\2\2bc\3\2\2\2cp\3\2\2\2df\7\3\2\2"+
		"eg\t\2\2\2fe\3\2\2\2gh\3\2\2\2hf\3\2\2\2hi\3\2\2\2ij\3\2\2\2jl\7\3\2\2"+
		"kd\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2np\3\2\2\2o-\3\2\2\2o\67\3\2\2"+
		"\2oA\3\2\2\2oL\3\2\2\2oV\3\2\2\2o`\3\2\2\2ok\3\2\2\2p\t\3\2\2\2\26\17"+
		"\25\33\"\'*/\649?CINSX^bhmo";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}