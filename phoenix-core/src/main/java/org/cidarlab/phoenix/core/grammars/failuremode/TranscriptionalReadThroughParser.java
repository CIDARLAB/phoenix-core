// Generated from TranscriptionalReadThrough.g4 by ANTLR 4.3

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
public class TranscriptionalReadThroughParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final String[] tokenNames = {
		"<INVALID>", "'<p'", "'p'", "'<r'", "'r'", "REVERSE_CDS", "FORWARD_CDS", 
		"'<t'", "'t'", "WS"
	};
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_transcriptional_readthrough = 2, 
		RULE_wildcard = 3;
	public static final String[] ruleNames = {
		"root", "module", "transcriptional_readthrough", "wildcard"
	};

	@Override
	public String getGrammarFileName() { return "TranscriptionalReadThrough.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TranscriptionalReadThroughParser(TokenStream input) {
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
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8); module();
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
		public WildcardContext wildcard(int i) {
			return getRuleContext(WildcardContext.class,i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.REVERSE_PROMOTER); }
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.FORWARD_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_PROMOTER, i);
		}
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_PROMOTER, i);
		}
		public List<WildcardContext> wildcard() {
			return getRuleContexts(WildcardContext.class);
		}
		public List<Transcriptional_readthroughContext> transcriptional_readthrough() {
			return getRuleContexts(Transcriptional_readthroughContext.class);
		}
		public Transcriptional_readthroughContext transcriptional_readthrough(int i) {
			return getRuleContext(Transcriptional_readthroughContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		int _la;
		try {
			int _alt;
			setState(87);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(20); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(11); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(10); match(FORWARD_PROMOTER);
						}
						}
						setState(13); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==FORWARD_PROMOTER );
					setState(16); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(15); wildcard();
						}
						}
						setState(18); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					}
					}
					}
					setState(22); 
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
					{
					setState(25); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(24); wildcard();
						}
						}
						setState(27); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					setState(30); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(29); match(REVERSE_PROMOTER);
						}
						}
						setState(32); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==REVERSE_PROMOTER );
					}
					}
					}
					setState(36); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(48); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(39); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(38); wildcard();
						}
						}
						setState(41); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					setState(44); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(43); match(REVERSE_PROMOTER);
						}
						}
						setState(46); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==REVERSE_PROMOTER );
					}
					}
					}
					setState(50); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				setState(62); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(53); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(52); match(FORWARD_PROMOTER);
						}
						}
						setState(55); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==FORWARD_PROMOTER );
					setState(58); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(57); wildcard();
						}
						}
						setState(60); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					}
					}
					}
					setState(64); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(69);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(66); wildcard();
						}
						} 
					}
					setState(71);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				}
				setState(83); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(73); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(72); transcriptional_readthrough();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(75); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(80);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(77); wildcard();
							}
							} 
						}
						setState(82);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
					}
					}
					}
					setState(85); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
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

	public static class Transcriptional_readthroughContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.FORWARD_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_TERMINATOR, i);
		}
		public WildcardContext wildcard(int i) {
			return getRuleContext(WildcardContext.class,i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.REVERSE_PROMOTER); }
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.REVERSE_TERMINATOR); }
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.FORWARD_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_PROMOTER, i);
		}
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_TERMINATOR, i);
		}
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_PROMOTER, i);
		}
		public List<WildcardContext> wildcard() {
			return getRuleContexts(WildcardContext.class);
		}
		public Transcriptional_readthroughContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transcriptional_readthrough; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterTranscriptional_readthrough(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitTranscriptional_readthrough(this);
		}
	}

	public final Transcriptional_readthroughContext transcriptional_readthrough() throws RecognitionException {
		Transcriptional_readthroughContext _localctx = new Transcriptional_readthroughContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_transcriptional_readthrough);
		int _la;
		try {
			int _alt;
			setState(169);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(105); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(90); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(89); match(FORWARD_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(92); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						setState(97);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
							{
							{
							setState(94); wildcard();
							}
							}
							setState(99);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(101); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(100); match(FORWARD_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(103); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(107); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(125); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(110); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(109); match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(112); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						setState(117);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
							{
							{
							setState(114); wildcard();
							}
							}
							setState(119);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(121); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(120); match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(123); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(127); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(145); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(130); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(129); match(FORWARD_PROMOTER);
							}
							}
							setState(132); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						setState(137);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(134); wildcard();
								}
								} 
							}
							setState(139);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
						}
						setState(141); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(140); match(REVERSE_TERMINATOR);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(143); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(147); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(165); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(150); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(149); match(FORWARD_TERMINATOR);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(152); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						setState(157);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
							{
							{
							setState(154); wildcard();
							}
							}
							setState(159);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(161); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(160); match(REVERSE_PROMOTER);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(163); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(167); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
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
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.FORWARD_TERMINATOR); }
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_RBS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_CDS); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_CDS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.REVERSE_TERMINATOR); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_RBS, i);
		}
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_CDS, i);
		}
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_RBS); }
		public WildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitWildcard(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_wildcard);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(172); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(171);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(174); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\u00b3\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\3\6\3\16\n\3\r\3\16\3\17\3\3\6\3\23"+
		"\n\3\r\3\16\3\24\6\3\27\n\3\r\3\16\3\30\3\3\6\3\34\n\3\r\3\16\3\35\3\3"+
		"\6\3!\n\3\r\3\16\3\"\6\3%\n\3\r\3\16\3&\3\3\6\3*\n\3\r\3\16\3+\3\3\6\3"+
		"/\n\3\r\3\16\3\60\6\3\63\n\3\r\3\16\3\64\3\3\6\38\n\3\r\3\16\39\3\3\6"+
		"\3=\n\3\r\3\16\3>\6\3A\n\3\r\3\16\3B\3\3\7\3F\n\3\f\3\16\3I\13\3\3\3\6"+
		"\3L\n\3\r\3\16\3M\3\3\7\3Q\n\3\f\3\16\3T\13\3\6\3V\n\3\r\3\16\3W\5\3Z"+
		"\n\3\3\4\6\4]\n\4\r\4\16\4^\3\4\7\4b\n\4\f\4\16\4e\13\4\3\4\6\4h\n\4\r"+
		"\4\16\4i\6\4l\n\4\r\4\16\4m\3\4\6\4q\n\4\r\4\16\4r\3\4\7\4v\n\4\f\4\16"+
		"\4y\13\4\3\4\6\4|\n\4\r\4\16\4}\6\4\u0080\n\4\r\4\16\4\u0081\3\4\6\4\u0085"+
		"\n\4\r\4\16\4\u0086\3\4\7\4\u008a\n\4\f\4\16\4\u008d\13\4\3\4\6\4\u0090"+
		"\n\4\r\4\16\4\u0091\6\4\u0094\n\4\r\4\16\4\u0095\3\4\6\4\u0099\n\4\r\4"+
		"\16\4\u009a\3\4\7\4\u009e\n\4\f\4\16\4\u00a1\13\4\3\4\6\4\u00a4\n\4\r"+
		"\4\16\4\u00a5\6\4\u00a8\n\4\r\4\16\4\u00a9\5\4\u00ac\n\4\3\5\6\5\u00af"+
		"\n\5\r\5\16\5\u00b0\3\5\2\2\6\2\4\6\b\2\3\3\2\5\n\u00d5\2\n\3\2\2\2\4"+
		"Y\3\2\2\2\6\u00ab\3\2\2\2\b\u00ae\3\2\2\2\n\13\5\4\3\2\13\3\3\2\2\2\f"+
		"\16\7\4\2\2\r\f\3\2\2\2\16\17\3\2\2\2\17\r\3\2\2\2\17\20\3\2\2\2\20\22"+
		"\3\2\2\2\21\23\5\b\5\2\22\21\3\2\2\2\23\24\3\2\2\2\24\22\3\2\2\2\24\25"+
		"\3\2\2\2\25\27\3\2\2\2\26\r\3\2\2\2\27\30\3\2\2\2\30\26\3\2\2\2\30\31"+
		"\3\2\2\2\31Z\3\2\2\2\32\34\5\b\5\2\33\32\3\2\2\2\34\35\3\2\2\2\35\33\3"+
		"\2\2\2\35\36\3\2\2\2\36 \3\2\2\2\37!\7\3\2\2 \37\3\2\2\2!\"\3\2\2\2\""+
		" \3\2\2\2\"#\3\2\2\2#%\3\2\2\2$\33\3\2\2\2%&\3\2\2\2&$\3\2\2\2&\'\3\2"+
		"\2\2\'Z\3\2\2\2(*\5\b\5\2)(\3\2\2\2*+\3\2\2\2+)\3\2\2\2+,\3\2\2\2,.\3"+
		"\2\2\2-/\7\3\2\2.-\3\2\2\2/\60\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\63"+
		"\3\2\2\2\62)\3\2\2\2\63\64\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65@\3\2"+
		"\2\2\668\7\4\2\2\67\66\3\2\2\289\3\2\2\29\67\3\2\2\29:\3\2\2\2:<\3\2\2"+
		"\2;=\5\b\5\2<;\3\2\2\2=>\3\2\2\2><\3\2\2\2>?\3\2\2\2?A\3\2\2\2@\67\3\2"+
		"\2\2AB\3\2\2\2B@\3\2\2\2BC\3\2\2\2CZ\3\2\2\2DF\5\b\5\2ED\3\2\2\2FI\3\2"+
		"\2\2GE\3\2\2\2GH\3\2\2\2HU\3\2\2\2IG\3\2\2\2JL\5\6\4\2KJ\3\2\2\2LM\3\2"+
		"\2\2MK\3\2\2\2MN\3\2\2\2NR\3\2\2\2OQ\5\b\5\2PO\3\2\2\2QT\3\2\2\2RP\3\2"+
		"\2\2RS\3\2\2\2SV\3\2\2\2TR\3\2\2\2UK\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2"+
		"\2\2XZ\3\2\2\2Y\26\3\2\2\2Y$\3\2\2\2Y\62\3\2\2\2YG\3\2\2\2Z\5\3\2\2\2"+
		"[]\7\4\2\2\\[\3\2\2\2]^\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_c\3\2\2\2`b\5\b\5"+
		"\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2dg\3\2\2\2ec\3\2\2\2fh\7\4\2"+
		"\2gf\3\2\2\2hi\3\2\2\2ig\3\2\2\2ij\3\2\2\2jl\3\2\2\2k\\\3\2\2\2lm\3\2"+
		"\2\2mk\3\2\2\2mn\3\2\2\2n\u00ac\3\2\2\2oq\7\3\2\2po\3\2\2\2qr\3\2\2\2"+
		"rp\3\2\2\2rs\3\2\2\2sw\3\2\2\2tv\5\b\5\2ut\3\2\2\2vy\3\2\2\2wu\3\2\2\2"+
		"wx\3\2\2\2x{\3\2\2\2yw\3\2\2\2z|\7\3\2\2{z\3\2\2\2|}\3\2\2\2}{\3\2\2\2"+
		"}~\3\2\2\2~\u0080\3\2\2\2\177p\3\2\2\2\u0080\u0081\3\2\2\2\u0081\177\3"+
		"\2\2\2\u0081\u0082\3\2\2\2\u0082\u00ac\3\2\2\2\u0083\u0085\7\4\2\2\u0084"+
		"\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2"+
		"\2\2\u0087\u008b\3\2\2\2\u0088\u008a\5\b\5\2\u0089\u0088\3\2\2\2\u008a"+
		"\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008f\3\2"+
		"\2\2\u008d\u008b\3\2\2\2\u008e\u0090\7\t\2\2\u008f\u008e\3\2\2\2\u0090"+
		"\u0091\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0094\3\2"+
		"\2\2\u0093\u0084\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0093\3\2\2\2\u0095"+
		"\u0096\3\2\2\2\u0096\u00ac\3\2\2\2\u0097\u0099\7\n\2\2\u0098\u0097\3\2"+
		"\2\2\u0099\u009a\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b"+
		"\u009f\3\2\2\2\u009c\u009e\5\b\5\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2"+
		"\2\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a3\3\2\2\2\u00a1"+
		"\u009f\3\2\2\2\u00a2\u00a4\7\3\2\2\u00a3\u00a2\3\2\2\2\u00a4\u00a5\3\2"+
		"\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7"+
		"\u0098\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2"+
		"\2\2\u00aa\u00ac\3\2\2\2\u00abk\3\2\2\2\u00ab\177\3\2\2\2\u00ab\u0093"+
		"\3\2\2\2\u00ab\u00a7\3\2\2\2\u00ac\7\3\2\2\2\u00ad\u00af\t\2\2\2\u00ae"+
		"\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2"+
		"\2\2\u00b1\t\3\2\2\2%\17\24\30\35\"&+\60\649>BGMRWY^cimrw}\u0081\u0086"+
		"\u008b\u0091\u0095\u009a\u009f\u00a5\u00a9\u00ab\u00b0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}