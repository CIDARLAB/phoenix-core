// Generated from TranscriptionalInterference.g4 by ANTLR 4.5.1

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
public class TranscriptionalInterferenceParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_transcriptional_interference = 2, 
		RULE_wildcard_rbs_cds_term = 3, RULE_wildcard_rbs_cds = 4, RULE_wildcard_rbs_cds_ft = 5, 
		RULE_wildcard_rbs_cds_rt = 6;
	public static final String[] ruleNames = {
		"root", "module", "transcriptional_interference", "wildcard_rbs_cds_term", 
		"wildcard_rbs_cds", "wildcard_rbs_cds_ft", "wildcard_rbs_cds_rt"
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
	public String getGrammarFileName() { return "TranscriptionalInterference.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TranscriptionalInterferenceParser(TokenStream input) {
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
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
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
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalInterferenceParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_PROMOTER, i);
		}
		public List<Wildcard_rbs_cds_termContext> wildcard_rbs_cds_term() {
			return getRuleContexts(Wildcard_rbs_cds_termContext.class);
		}
		public Wildcard_rbs_cds_termContext wildcard_rbs_cds_term(int i) {
			return getRuleContext(Wildcard_rbs_cds_termContext.class,i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalInterferenceParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_PROMOTER, i);
		}
		public List<Transcriptional_interferenceContext> transcriptional_interference() {
			return getRuleContexts(Transcriptional_interferenceContext.class);
		}
		public Transcriptional_interferenceContext transcriptional_interference(int i) {
			return getRuleContext(Transcriptional_interferenceContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		int _la;
		try {
			int _alt;
			setState(93);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(26); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(17); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(16);
						match(FORWARD_PROMOTER);
						}
						}
						setState(19); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==FORWARD_PROMOTER );
					setState(22); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(21);
						wildcard_rbs_cds_term();
						}
						}
						setState(24); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					}
					}
					}
					setState(28); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
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
					{
					setState(31); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(30);
						wildcard_rbs_cds_term();
						}
						}
						setState(33); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					setState(36); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(35);
						match(REVERSE_PROMOTER);
						}
						}
						setState(38); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==REVERSE_PROMOTER );
					}
					}
					}
					setState(42); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(54); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(45); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(44);
						wildcard_rbs_cds_term();
						}
						}
						setState(47); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					setState(50); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(49);
						match(REVERSE_PROMOTER);
						}
						}
						setState(52); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==REVERSE_PROMOTER );
					}
					}
					}
					setState(56); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
				setState(68); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					{
					setState(59); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(58);
						match(FORWARD_PROMOTER);
						}
						}
						setState(61); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==FORWARD_PROMOTER );
					setState(64); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(63);
						wildcard_rbs_cds_term();
						}
						}
						setState(66); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0) );
					}
					}
					}
					setState(70); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
					{
					{
					setState(72);
					wildcard_rbs_cds_term();
					}
					}
					setState(77);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(89); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(79); 
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(78);
							transcriptional_interference();
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(81); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					setState(86);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR) | (1L << FORWARD_TERMINATOR))) != 0)) {
						{
						{
						setState(83);
						wildcard_rbs_cds_term();
						}
						}
						setState(88);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					setState(91); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
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

	public static class Transcriptional_interferenceContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(TranscriptionalInterferenceParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_PROMOTER, i);
		}
		public List<Wildcard_rbs_cdsContext> wildcard_rbs_cds() {
			return getRuleContexts(Wildcard_rbs_cdsContext.class);
		}
		public Wildcard_rbs_cdsContext wildcard_rbs_cds(int i) {
			return getRuleContext(Wildcard_rbs_cdsContext.class,i);
		}
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(TranscriptionalInterferenceParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_PROMOTER, i);
		}
		public List<Wildcard_rbs_cds_ftContext> wildcard_rbs_cds_ft() {
			return getRuleContexts(Wildcard_rbs_cds_ftContext.class);
		}
		public Wildcard_rbs_cds_ftContext wildcard_rbs_cds_ft(int i) {
			return getRuleContext(Wildcard_rbs_cds_ftContext.class,i);
		}
		public List<Wildcard_rbs_cds_rtContext> wildcard_rbs_cds_rt() {
			return getRuleContexts(Wildcard_rbs_cds_rtContext.class);
		}
		public Wildcard_rbs_cds_rtContext wildcard_rbs_cds_rt(int i) {
			return getRuleContext(Wildcard_rbs_cds_rtContext.class,i);
		}
		public Transcriptional_interferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transcriptional_interference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).enterTranscriptional_interference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).exitTranscriptional_interference(this);
		}
	}

	public final Transcriptional_interferenceContext transcriptional_interference() throws RecognitionException {
		Transcriptional_interferenceContext _localctx = new Transcriptional_interferenceContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_transcriptional_interference);
		int _la;
		try {
			int _alt;
			setState(155);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(111); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(96); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(95);
							match(FORWARD_PROMOTER);
							}
							}
							setState(98); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						setState(103);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS))) != 0)) {
							{
							{
							setState(100);
							wildcard_rbs_cds();
							}
							}
							setState(105);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(107); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(106);
							match(REVERSE_PROMOTER);
							}
							}
							setState(109); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==REVERSE_PROMOTER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(113); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(131); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(116); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(115);
							match(FORWARD_PROMOTER);
							}
							}
							setState(118); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						setState(123);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) {
							{
							{
							setState(120);
							wildcard_rbs_cds_ft();
							}
							}
							setState(125);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(127); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(126);
							match(REVERSE_PROMOTER);
							}
							}
							setState(129); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==REVERSE_PROMOTER );
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
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(151); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(136); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(135);
							match(FORWARD_PROMOTER);
							}
							}
							setState(138); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==FORWARD_PROMOTER );
						setState(143);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
							{
							{
							setState(140);
							wildcard_rbs_cds_rt();
							}
							}
							setState(145);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(147); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(146);
							match(REVERSE_PROMOTER);
							}
							}
							setState(149); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==REVERSE_PROMOTER );
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(153); 
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

	public static class Wildcard_rbs_cds_termContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalInterferenceParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalInterferenceParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_TERMINATOR, i);
		}
		public Wildcard_rbs_cds_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rbs_cds_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).enterWildcard_rbs_cds_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).exitWildcard_rbs_cds_term(this);
		}
	}

	public final Wildcard_rbs_cds_termContext wildcard_rbs_cds_term() throws RecognitionException {
		Wildcard_rbs_cds_termContext _localctx = new Wildcard_rbs_cds_termContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_wildcard_rbs_cds_term);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(158); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(157);
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
				setState(160); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
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

	public static class Wildcard_rbs_cdsContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_CDS, i);
		}
		public Wildcard_rbs_cdsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rbs_cds; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).enterWildcard_rbs_cds(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).exitWildcard_rbs_cds(this);
		}
	}

	public final Wildcard_rbs_cdsContext wildcard_rbs_cds() throws RecognitionException {
		Wildcard_rbs_cdsContext _localctx = new Wildcard_rbs_cdsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_wildcard_rbs_cds);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(163); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(162);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS))) != 0)) ) {
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
				setState(165); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
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

	public static class Wildcard_rbs_cds_ftContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(TranscriptionalInterferenceParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_TERMINATOR, i);
		}
		public Wildcard_rbs_cds_ftContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rbs_cds_ft; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).enterWildcard_rbs_cds_ft(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).exitWildcard_rbs_cds_ft(this);
		}
	}

	public final Wildcard_rbs_cds_ftContext wildcard_rbs_cds_ft() throws RecognitionException {
		Wildcard_rbs_cds_ftContext _localctx = new Wildcard_rbs_cds_ftContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_wildcard_rbs_cds_ft);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(168); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(167);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << FORWARD_TERMINATOR))) != 0)) ) {
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
				setState(170); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
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

	public static class Wildcard_rbs_cds_rtContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_RBS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(TranscriptionalInterferenceParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.FORWARD_CDS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(TranscriptionalInterferenceParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(TranscriptionalInterferenceParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(TranscriptionalInterferenceParser.REVERSE_TERMINATOR, i);
		}
		public Wildcard_rbs_cds_rtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard_rbs_cds_rt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).enterWildcard_rbs_cds_rt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TranscriptionalInterferenceListener ) ((TranscriptionalInterferenceListener)listener).exitWildcard_rbs_cds_rt(this);
		}
	}

	public final Wildcard_rbs_cds_rtContext wildcard_rbs_cds_rt() throws RecognitionException {
		Wildcard_rbs_cds_rtContext _localctx = new Wildcard_rbs_cds_rtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_wildcard_rbs_cds_rt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(173); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(172);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_RBS) | (1L << FORWARD_RBS) | (1L << REVERSE_CDS) | (1L << FORWARD_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) ) {
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
				setState(175); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\u00b4\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\3\6\3\24\n"+
		"\3\r\3\16\3\25\3\3\6\3\31\n\3\r\3\16\3\32\6\3\35\n\3\r\3\16\3\36\3\3\6"+
		"\3\"\n\3\r\3\16\3#\3\3\6\3\'\n\3\r\3\16\3(\6\3+\n\3\r\3\16\3,\3\3\6\3"+
		"\60\n\3\r\3\16\3\61\3\3\6\3\65\n\3\r\3\16\3\66\6\39\n\3\r\3\16\3:\3\3"+
		"\6\3>\n\3\r\3\16\3?\3\3\6\3C\n\3\r\3\16\3D\6\3G\n\3\r\3\16\3H\3\3\7\3"+
		"L\n\3\f\3\16\3O\13\3\3\3\6\3R\n\3\r\3\16\3S\3\3\7\3W\n\3\f\3\16\3Z\13"+
		"\3\6\3\\\n\3\r\3\16\3]\5\3`\n\3\3\4\6\4c\n\4\r\4\16\4d\3\4\7\4h\n\4\f"+
		"\4\16\4k\13\4\3\4\6\4n\n\4\r\4\16\4o\6\4r\n\4\r\4\16\4s\3\4\6\4w\n\4\r"+
		"\4\16\4x\3\4\7\4|\n\4\f\4\16\4\177\13\4\3\4\6\4\u0082\n\4\r\4\16\4\u0083"+
		"\6\4\u0086\n\4\r\4\16\4\u0087\3\4\6\4\u008b\n\4\r\4\16\4\u008c\3\4\7\4"+
		"\u0090\n\4\f\4\16\4\u0093\13\4\3\4\6\4\u0096\n\4\r\4\16\4\u0097\6\4\u009a"+
		"\n\4\r\4\16\4\u009b\5\4\u009e\n\4\3\5\6\5\u00a1\n\5\r\5\16\5\u00a2\3\6"+
		"\6\6\u00a6\n\6\r\6\16\6\u00a7\3\7\6\7\u00ab\n\7\r\7\16\7\u00ac\3\b\6\b"+
		"\u00b0\n\b\r\b\16\b\u00b1\3\b\2\2\t\2\4\6\b\n\f\16\2\6\3\2\5\n\3\2\5\b"+
		"\4\2\5\b\n\n\3\2\5\t\u00d1\2\20\3\2\2\2\4_\3\2\2\2\6\u009d\3\2\2\2\b\u00a0"+
		"\3\2\2\2\n\u00a5\3\2\2\2\f\u00aa\3\2\2\2\16\u00af\3\2\2\2\20\21\5\4\3"+
		"\2\21\3\3\2\2\2\22\24\7\4\2\2\23\22\3\2\2\2\24\25\3\2\2\2\25\23\3\2\2"+
		"\2\25\26\3\2\2\2\26\30\3\2\2\2\27\31\5\b\5\2\30\27\3\2\2\2\31\32\3\2\2"+
		"\2\32\30\3\2\2\2\32\33\3\2\2\2\33\35\3\2\2\2\34\23\3\2\2\2\35\36\3\2\2"+
		"\2\36\34\3\2\2\2\36\37\3\2\2\2\37`\3\2\2\2 \"\5\b\5\2! \3\2\2\2\"#\3\2"+
		"\2\2#!\3\2\2\2#$\3\2\2\2$&\3\2\2\2%\'\7\3\2\2&%\3\2\2\2\'(\3\2\2\2(&\3"+
		"\2\2\2()\3\2\2\2)+\3\2\2\2*!\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3\2\2\2-`\3"+
		"\2\2\2.\60\5\b\5\2/.\3\2\2\2\60\61\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62"+
		"\64\3\2\2\2\63\65\7\3\2\2\64\63\3\2\2\2\65\66\3\2\2\2\66\64\3\2\2\2\66"+
		"\67\3\2\2\2\679\3\2\2\28/\3\2\2\29:\3\2\2\2:8\3\2\2\2:;\3\2\2\2;F\3\2"+
		"\2\2<>\7\4\2\2=<\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@B\3\2\2\2AC\5\b"+
		"\5\2BA\3\2\2\2CD\3\2\2\2DB\3\2\2\2DE\3\2\2\2EG\3\2\2\2F=\3\2\2\2GH\3\2"+
		"\2\2HF\3\2\2\2HI\3\2\2\2I`\3\2\2\2JL\5\b\5\2KJ\3\2\2\2LO\3\2\2\2MK\3\2"+
		"\2\2MN\3\2\2\2N[\3\2\2\2OM\3\2\2\2PR\5\6\4\2QP\3\2\2\2RS\3\2\2\2SQ\3\2"+
		"\2\2ST\3\2\2\2TX\3\2\2\2UW\5\b\5\2VU\3\2\2\2WZ\3\2\2\2XV\3\2\2\2XY\3\2"+
		"\2\2Y\\\3\2\2\2ZX\3\2\2\2[Q\3\2\2\2\\]\3\2\2\2][\3\2\2\2]^\3\2\2\2^`\3"+
		"\2\2\2_\34\3\2\2\2_*\3\2\2\2_8\3\2\2\2_M\3\2\2\2`\5\3\2\2\2ac\7\4\2\2"+
		"ba\3\2\2\2cd\3\2\2\2db\3\2\2\2de\3\2\2\2ei\3\2\2\2fh\5\n\6\2gf\3\2\2\2"+
		"hk\3\2\2\2ig\3\2\2\2ij\3\2\2\2jm\3\2\2\2ki\3\2\2\2ln\7\3\2\2ml\3\2\2\2"+
		"no\3\2\2\2om\3\2\2\2op\3\2\2\2pr\3\2\2\2qb\3\2\2\2rs\3\2\2\2sq\3\2\2\2"+
		"st\3\2\2\2t\u009e\3\2\2\2uw\7\4\2\2vu\3\2\2\2wx\3\2\2\2xv\3\2\2\2xy\3"+
		"\2\2\2y}\3\2\2\2z|\5\f\7\2{z\3\2\2\2|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2"+
		"~\u0081\3\2\2\2\177}\3\2\2\2\u0080\u0082\7\3\2\2\u0081\u0080\3\2\2\2\u0082"+
		"\u0083\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0086\3\2"+
		"\2\2\u0085v\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088"+
		"\3\2\2\2\u0088\u009e\3\2\2\2\u0089\u008b\7\4\2\2\u008a\u0089\3\2\2\2\u008b"+
		"\u008c\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u0091\3\2"+
		"\2\2\u008e\u0090\5\16\b\2\u008f\u008e\3\2\2\2\u0090\u0093\3\2\2\2\u0091"+
		"\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2"+
		"\2\2\u0094\u0096\7\3\2\2\u0095\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"+
		"\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u009a\3\2\2\2\u0099\u008a\3\2"+
		"\2\2\u009a\u009b\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c"+
		"\u009e\3\2\2\2\u009dq\3\2\2\2\u009d\u0085\3\2\2\2\u009d\u0099\3\2\2\2"+
		"\u009e\7\3\2\2\2\u009f\u00a1\t\2\2\2\u00a0\u009f\3\2\2\2\u00a1\u00a2\3"+
		"\2\2\2\u00a2\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\t\3\2\2\2\u00a4\u00a6"+
		"\t\3\2\2\u00a5\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7"+
		"\u00a8\3\2\2\2\u00a8\13\3\2\2\2\u00a9\u00ab\t\4\2\2\u00aa\u00a9\3\2\2"+
		"\2\u00ab\u00ac\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\r"+
		"\3\2\2\2\u00ae\u00b0\t\5\2\2\u00af\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1"+
		"\u00af\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\17\3\2\2\2$\25\32\36#(,\61\66"+
		":?DHMSX]_diosx}\u0083\u0087\u008c\u0091\u0097\u009b\u009d\u00a2\u00a7"+
		"\u00ac\u00b1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}