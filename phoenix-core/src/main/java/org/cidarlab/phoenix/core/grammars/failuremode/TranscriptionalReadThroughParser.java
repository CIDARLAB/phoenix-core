// Generated from TranscriptionalReadThrough.g4 by ANTLR 4.5.1

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
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_transcriptional_readthrough = 2, 
		RULE_reverse_strand = 3, RULE_forward_strand = 4, RULE_wildcard = 5, RULE_wildcard_type1 = 6, 
		RULE_wildcard_type2 = 7, RULE_wildcard_type3 = 8;
	public static final String[] ruleNames = {
		"root", "module", "transcriptional_readthrough", "reverse_strand", "forward_strand", 
		"wildcard", "wildcard_type1", "wildcard_type2", "wildcard_type3"
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
	public String getGrammarFileName() { return "TranscriptionalReadThrough.g4"; }

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
			setState(18);
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
		public List<Transcriptional_readthroughContext> transcriptional_readthrough() {
			return getRuleContexts(Transcriptional_readthroughContext.class);
		}
		public Transcriptional_readthroughContext transcriptional_readthrough(int i) {
			return getRuleContext(Transcriptional_readthroughContext.class,i);
		}
		public List<WildcardContext> wildcard() {
			return getRuleContexts(WildcardContext.class);
		}
		public WildcardContext wildcard(int i) {
			return getRuleContext(WildcardContext.class,i);
		}
		public List<Reverse_strandContext> reverse_strand() {
			return getRuleContexts(Reverse_strandContext.class);
		}
		public Reverse_strandContext reverse_strand(int i) {
			return getRuleContext(Reverse_strandContext.class,i);
		}
		public List<Forward_strandContext> forward_strand() {
			return getRuleContexts(Forward_strandContext.class);
		}
		public Forward_strandContext forward_strand(int i) {
			return getRuleContext(Forward_strandContext.class,i);
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
			setState(102);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(30); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(21); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(20);
							transcriptional_readthrough();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(23); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(26); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(25);
							wildcard();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(28); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					setState(32); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==REVERSE_PROMOTER || _la==FORWARD_PROMOTER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(44); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
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
							wildcard();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(37); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(40); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(39);
							transcriptional_readthrough();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(42); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					setState(46); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(63); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(49); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(48);
							wildcard();
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
					setState(54); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(53);
							transcriptional_readthrough();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(56); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(59); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(58);
							wildcard();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(61); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					setState(65); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(78); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(70);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
						{
						{
						setState(67);
						reverse_strand();
						}
						}
						setState(72);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(74); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(73);
							forward_strand();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(76); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					}
					setState(80); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(93); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(83); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(82);
							reverse_strand();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(85); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(90);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) {
						{
						{
						setState(87);
						forward_strand();
						}
						}
						setState(92);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					}
					setState(95); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				{
				setState(98); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(97);
					wildcard();
					}
					}
					setState(100); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
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
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_PROMOTER, i);
		}
		public List<Wildcard_type1Context> wildcard_type1() {
			return getRuleContexts(Wildcard_type1Context.class);
		}
		public Wildcard_type1Context wildcard_type1(int i) {
			return getRuleContext(Wildcard_type1Context.class,i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_PROMOTER, i);
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
			setState(134);
			switch (_input.LA(1)) {
			case FORWARD_PROMOTER:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(105); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(104);
					match(FORWARD_PROMOTER);
					}
					}
					setState(107); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
				setState(110); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(109);
					wildcard_type1();
					}
					}
					setState(112); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				setState(115); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(114);
						match(FORWARD_PROMOTER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(117); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				}
				break;
			case REVERSE_PROMOTER:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(120); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(119);
					match(REVERSE_PROMOTER);
					}
					}
					setState(122); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==REVERSE_PROMOTER );
				setState(125); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(124);
					wildcard_type1();
					}
					}
					setState(127); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				setState(130); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(129);
						match(REVERSE_PROMOTER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(132); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

	public static class Reverse_strandContext extends ParserRuleContext {
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_TERMINATOR, i);
		}
		public Reverse_strandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reverse_strand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterReverse_strand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitReverse_strand(this);
		}
	}

	public final Reverse_strandContext reverse_strand() throws RecognitionException {
		Reverse_strandContext _localctx = new Reverse_strandContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_reverse_strand);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(137); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(136);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) ) {
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
				setState(139); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
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

	public static class Forward_strandContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_TERMINATOR, i);
		}
		public Forward_strandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forward_strand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterForward_strand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitForward_strand(this);
		}
	}

	public final Forward_strandContext forward_strand() throws RecognitionException {
		Forward_strandContext _localctx = new Forward_strandContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_forward_strand);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(142); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(141);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << FORWARD_RBS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
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
				setState(144); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_TERMINATOR, i);
		}
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
		enterRule(_localctx, 10, RULE_wildcard);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(147); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(146);
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
				setState(149); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
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

	public static class Wildcard_type1Context extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_TERMINATOR, i);
		}
		public Wildcard_type1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_type1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterWildcard_type1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitWildcard_type1(this);
		}
	}

	public final Wildcard_type1Context wildcard_type1() throws RecognitionException {
		Wildcard_type1Context _localctx = new Wildcard_type1Context(_ctx, getState());
		enterRule(_localctx, 12, RULE_wildcard_type1);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(152); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(151);
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
				setState(154); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
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
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_PROMOTER, i);
		}
		public Wildcard_type2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_type2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterWildcard_type2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitWildcard_type2(this);
		}
	}

	public final Wildcard_type2Context wildcard_type2() throws RecognitionException {
		Wildcard_type2Context _localctx = new Wildcard_type2Context(_ctx, getState());
		enterRule(_localctx, 14, RULE_wildcard_type2);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(156);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(159); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0) );
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

	public static class Wildcard_type3Context extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalReadThroughParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalReadThroughParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalReadThroughParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalReadThroughParser.REVERSE_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalReadThroughParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalReadThroughParser.FORWARD_PROMOTER, i);
		}
		public Wildcard_type3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_type3; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).enterWildcard_type3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalReadThroughListener ) ((TranscriptionalReadThroughListener)listener).exitWildcard_type3(this);
		}
	}

	public final Wildcard_type3Context wildcard_type3() throws RecognitionException {
		Wildcard_type3Context _localctx = new Wildcard_type3Context(_ctx, getState());
		enterRule(_localctx, 16, RULE_wildcard_type3);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(161);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				}
				setState(164); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FORWARD_PROMOTER) | (1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\u00a9\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3"+
		"\2\3\3\6\3\30\n\3\r\3\16\3\31\3\3\6\3\35\n\3\r\3\16\3\36\6\3!\n\3\r\3"+
		"\16\3\"\3\3\6\3&\n\3\r\3\16\3\'\3\3\6\3+\n\3\r\3\16\3,\6\3/\n\3\r\3\16"+
		"\3\60\3\3\6\3\64\n\3\r\3\16\3\65\3\3\6\39\n\3\r\3\16\3:\3\3\6\3>\n\3\r"+
		"\3\16\3?\6\3B\n\3\r\3\16\3C\3\3\7\3G\n\3\f\3\16\3J\13\3\3\3\6\3M\n\3\r"+
		"\3\16\3N\6\3Q\n\3\r\3\16\3R\3\3\6\3V\n\3\r\3\16\3W\3\3\7\3[\n\3\f\3\16"+
		"\3^\13\3\6\3`\n\3\r\3\16\3a\3\3\6\3e\n\3\r\3\16\3f\5\3i\n\3\3\4\6\4l\n"+
		"\4\r\4\16\4m\3\4\6\4q\n\4\r\4\16\4r\3\4\6\4v\n\4\r\4\16\4w\3\4\6\4{\n"+
		"\4\r\4\16\4|\3\4\6\4\u0080\n\4\r\4\16\4\u0081\3\4\6\4\u0085\n\4\r\4\16"+
		"\4\u0086\5\4\u0089\n\4\3\5\6\5\u008c\n\5\r\5\16\5\u008d\3\6\6\6\u0091"+
		"\n\6\r\6\16\6\u0092\3\7\6\7\u0096\n\7\r\7\16\7\u0097\3\b\6\b\u009b\n\b"+
		"\r\b\16\b\u009c\3\t\6\t\u00a0\n\t\r\t\16\t\u00a1\3\n\6\n\u00a5\n\n\r\n"+
		"\16\n\u00a6\3\n\2\2\13\2\4\6\b\n\f\16\20\22\2\b\6\2\3\3\5\5\7\7\t\t\6"+
		"\2\4\4\6\6\b\b\n\n\3\2\3\n\3\2\5\n\5\2\3\3\5\b\n\n\3\2\4\t\u00c2\2\24"+
		"\3\2\2\2\4h\3\2\2\2\6\u0088\3\2\2\2\b\u008b\3\2\2\2\n\u0090\3\2\2\2\f"+
		"\u0095\3\2\2\2\16\u009a\3\2\2\2\20\u009f\3\2\2\2\22\u00a4\3\2\2\2\24\25"+
		"\5\4\3\2\25\3\3\2\2\2\26\30\5\6\4\2\27\26\3\2\2\2\30\31\3\2\2\2\31\27"+
		"\3\2\2\2\31\32\3\2\2\2\32\34\3\2\2\2\33\35\5\f\7\2\34\33\3\2\2\2\35\36"+
		"\3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37!\3\2\2\2 \27\3\2\2\2!\"\3\2\2"+
		"\2\" \3\2\2\2\"#\3\2\2\2#i\3\2\2\2$&\5\f\7\2%$\3\2\2\2&\'\3\2\2\2\'%\3"+
		"\2\2\2\'(\3\2\2\2(*\3\2\2\2)+\5\6\4\2*)\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-"+
		"\3\2\2\2-/\3\2\2\2.%\3\2\2\2/\60\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61"+
		"i\3\2\2\2\62\64\5\f\7\2\63\62\3\2\2\2\64\65\3\2\2\2\65\63\3\2\2\2\65\66"+
		"\3\2\2\2\668\3\2\2\2\679\5\6\4\28\67\3\2\2\29:\3\2\2\2:8\3\2\2\2:;\3\2"+
		"\2\2;=\3\2\2\2<>\5\f\7\2=<\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@B\3\2"+
		"\2\2A\63\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2Di\3\2\2\2EG\5\b\5\2FE\3"+
		"\2\2\2GJ\3\2\2\2HF\3\2\2\2HI\3\2\2\2IL\3\2\2\2JH\3\2\2\2KM\5\n\6\2LK\3"+
		"\2\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2\2\2OQ\3\2\2\2PH\3\2\2\2QR\3\2\2\2RP\3"+
		"\2\2\2RS\3\2\2\2Si\3\2\2\2TV\5\b\5\2UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3"+
		"\2\2\2X\\\3\2\2\2Y[\5\n\6\2ZY\3\2\2\2[^\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2"+
		"]`\3\2\2\2^\\\3\2\2\2_U\3\2\2\2`a\3\2\2\2a_\3\2\2\2ab\3\2\2\2bi\3\2\2"+
		"\2ce\5\f\7\2dc\3\2\2\2ef\3\2\2\2fd\3\2\2\2fg\3\2\2\2gi\3\2\2\2h \3\2\2"+
		"\2h.\3\2\2\2hA\3\2\2\2hP\3\2\2\2h_\3\2\2\2hd\3\2\2\2i\5\3\2\2\2jl\7\4"+
		"\2\2kj\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2np\3\2\2\2oq\5\16\b\2po\3"+
		"\2\2\2qr\3\2\2\2rp\3\2\2\2rs\3\2\2\2su\3\2\2\2tv\7\4\2\2ut\3\2\2\2vw\3"+
		"\2\2\2wu\3\2\2\2wx\3\2\2\2x\u0089\3\2\2\2y{\7\3\2\2zy\3\2\2\2{|\3\2\2"+
		"\2|z\3\2\2\2|}\3\2\2\2}\177\3\2\2\2~\u0080\5\16\b\2\177~\3\2\2\2\u0080"+
		"\u0081\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0084\3\2\2"+
		"\2\u0083\u0085\7\3\2\2\u0084\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0084"+
		"\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088k\3\2\2\2\u0088"+
		"z\3\2\2\2\u0089\7\3\2\2\2\u008a\u008c\t\2\2\2\u008b\u008a\3\2\2\2\u008c"+
		"\u008d\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\t\3\2\2\2"+
		"\u008f\u0091\t\3\2\2\u0090\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0090"+
		"\3\2\2\2\u0092\u0093\3\2\2\2\u0093\13\3\2\2\2\u0094\u0096\t\4\2\2\u0095"+
		"\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2"+
		"\2\2\u0098\r\3\2\2\2\u0099\u009b\t\5\2\2\u009a\u0099\3\2\2\2\u009b\u009c"+
		"\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\17\3\2\2\2\u009e"+
		"\u00a0\t\6\2\2\u009f\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u009f\3\2"+
		"\2\2\u00a1\u00a2\3\2\2\2\u00a2\21\3\2\2\2\u00a3\u00a5\t\7\2\2\u00a4\u00a3"+
		"\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7"+
		"\23\3\2\2\2!\31\36\"\',\60\65:?CHNRW\\afhmrw|\u0081\u0086\u0088\u008d"+
		"\u0092\u0097\u009c\u00a1\u00a6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}