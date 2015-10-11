// Generated from Structural.g4 by ANTLR 4.5.1

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
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REVERSE_PROMOTER=1, FORWARD_PROMOTER=2, REVERSE_RBS=3, FORWARD_RBS=4, 
		REVERSE_CDS=5, FORWARD_CDS=6, REVERSE_TERMINATOR=7, FORWARD_TERMINATOR=8, 
		WS=9;
	public static final int
		RULE_root = 0, RULE_module = 1, RULE_tu = 2, RULE_wildcard = 3;
	public static final String[] ruleNames = {
		"root", "module", "tu", "wildcard"
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
	public String getGrammarFileName() { return "Structural.g4"; }

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
		public List<TuContext> tu() {
			return getRuleContexts(TuContext.class);
		}
		public TuContext tu(int i) {
			return getRuleContext(TuContext.class,i);
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
			setState(67);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(11); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(10);
					tu();
					}
					}
					setState(13); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(16); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(15);
					wildcard();
					}
					}
					setState(18); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(30); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(21); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(20);
						wildcard();
						}
						}
						setState(23); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
					setState(26); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(25);
						tu();
						}
						}
						setState(28); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==FORWARD_PROMOTER );
					}
					}
					setState(32); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(44); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(35); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(34);
						tu();
						}
						}
						setState(37); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==FORWARD_PROMOTER );
					setState(40); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(39);
						wildcard();
						}
						}
						setState(42); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
					}
					}
					setState(46); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FORWARD_PROMOTER );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(63); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(49); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(48);
						wildcard();
						}
						}
						setState(51); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
					setState(54); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(53);
						tu();
						}
						}
						setState(56); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==FORWARD_PROMOTER );
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
						_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
					} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
					}
					}
					setState(65); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0) );
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

	public static class TuContext extends ParserRuleContext {
		public List<TerminalNode> FORWARD_TERMINATOR() { return getTokens(StructuralParser.FORWARD_TERMINATOR); }
		public TerminalNode FORWARD_TERMINATOR(int i) {
			return getToken(StructuralParser.FORWARD_TERMINATOR, i);
		}
		public List<TerminalNode> FORWARD_PROMOTER() { return getTokens(StructuralParser.FORWARD_PROMOTER); }
		public TerminalNode FORWARD_PROMOTER(int i) {
			return getToken(StructuralParser.FORWARD_PROMOTER, i);
		}
		public List<TerminalNode> FORWARD_RBS() { return getTokens(StructuralParser.FORWARD_RBS); }
		public TerminalNode FORWARD_RBS(int i) {
			return getToken(StructuralParser.FORWARD_RBS, i);
		}
		public List<TerminalNode> FORWARD_CDS() { return getTokens(StructuralParser.FORWARD_CDS); }
		public TerminalNode FORWARD_CDS(int i) {
			return getToken(StructuralParser.FORWARD_CDS, i);
		}
		public List<WildcardContext> wildcard() {
			return getRuleContexts(WildcardContext.class);
		}
		public WildcardContext wildcard(int i) {
			return getRuleContext(WildcardContext.class,i);
		}
		public TuContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tu; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterTu(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitTu(this);
		}
	}

	public final TuContext tu() throws RecognitionException {
		TuContext _localctx = new TuContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_tu);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(76); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				{
				setState(69);
				match(FORWARD_PROMOTER);
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
					{
					{
					setState(70);
					wildcard();
					}
					}
					setState(75);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(78); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==FORWARD_PROMOTER );
			setState(89); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				{
				setState(80);
				match(FORWARD_RBS);
				setState(81);
				match(FORWARD_CDS);
				}
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REVERSE_PROMOTER) | (1L << REVERSE_RBS) | (1L << REVERSE_CDS) | (1L << REVERSE_TERMINATOR))) != 0)) {
					{
					{
					setState(83);
					wildcard();
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
			} while ( _la==FORWARD_RBS );
			setState(100); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(93);
				match(FORWARD_TERMINATOR);
				setState(97);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(94);
						wildcard();
						}
						} 
					}
					setState(99);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
				}
				}
				}
				setState(102); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==FORWARD_TERMINATOR );
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
		public List<TerminalNode> REVERSE_PROMOTER() { return getTokens(StructuralParser.REVERSE_PROMOTER); }
		public TerminalNode REVERSE_PROMOTER(int i) {
			return getToken(StructuralParser.REVERSE_PROMOTER, i);
		}
		public List<TerminalNode> REVERSE_RBS() { return getTokens(StructuralParser.REVERSE_RBS); }
		public TerminalNode REVERSE_RBS(int i) {
			return getToken(StructuralParser.REVERSE_RBS, i);
		}
		public List<TerminalNode> REVERSE_CDS() { return getTokens(StructuralParser.REVERSE_CDS); }
		public TerminalNode REVERSE_CDS(int i) {
			return getToken(StructuralParser.REVERSE_CDS, i);
		}
		public List<TerminalNode> REVERSE_TERMINATOR() { return getTokens(StructuralParser.REVERSE_TERMINATOR); }
		public TerminalNode REVERSE_TERMINATOR(int i) {
			return getToken(StructuralParser.REVERSE_TERMINATOR, i);
		}
		public WildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StructuralListener ) ((StructuralListener)listener).exitWildcard(this);
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
			setState(105); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(104);
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
				setState(107); 
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13p\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\3\6\3\16\n\3\r\3\16\3\17\3\3\6\3\23\n\3\r"+
		"\3\16\3\24\3\3\6\3\30\n\3\r\3\16\3\31\3\3\6\3\35\n\3\r\3\16\3\36\6\3!"+
		"\n\3\r\3\16\3\"\3\3\6\3&\n\3\r\3\16\3\'\3\3\6\3+\n\3\r\3\16\3,\6\3/\n"+
		"\3\r\3\16\3\60\3\3\6\3\64\n\3\r\3\16\3\65\3\3\6\39\n\3\r\3\16\3:\3\3\6"+
		"\3>\n\3\r\3\16\3?\6\3B\n\3\r\3\16\3C\5\3F\n\3\3\4\3\4\7\4J\n\4\f\4\16"+
		"\4M\13\4\6\4O\n\4\r\4\16\4P\3\4\3\4\3\4\3\4\7\4W\n\4\f\4\16\4Z\13\4\6"+
		"\4\\\n\4\r\4\16\4]\3\4\3\4\7\4b\n\4\f\4\16\4e\13\4\6\4g\n\4\r\4\16\4h"+
		"\3\5\6\5l\n\5\r\5\16\5m\3\5\2\2\6\2\4\6\b\2\3\6\2\3\3\5\5\7\7\t\t\u0082"+
		"\2\n\3\2\2\2\4E\3\2\2\2\6N\3\2\2\2\bk\3\2\2\2\n\13\5\4\3\2\13\3\3\2\2"+
		"\2\f\16\5\6\4\2\r\f\3\2\2\2\16\17\3\2\2\2\17\r\3\2\2\2\17\20\3\2\2\2\20"+
		"F\3\2\2\2\21\23\5\b\5\2\22\21\3\2\2\2\23\24\3\2\2\2\24\22\3\2\2\2\24\25"+
		"\3\2\2\2\25F\3\2\2\2\26\30\5\b\5\2\27\26\3\2\2\2\30\31\3\2\2\2\31\27\3"+
		"\2\2\2\31\32\3\2\2\2\32\34\3\2\2\2\33\35\5\6\4\2\34\33\3\2\2\2\35\36\3"+
		"\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37!\3\2\2\2 \27\3\2\2\2!\"\3\2\2\2"+
		"\" \3\2\2\2\"#\3\2\2\2#F\3\2\2\2$&\5\6\4\2%$\3\2\2\2&\'\3\2\2\2\'%\3\2"+
		"\2\2\'(\3\2\2\2(*\3\2\2\2)+\5\b\5\2*)\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3"+
		"\2\2\2-/\3\2\2\2.%\3\2\2\2/\60\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61F\3"+
		"\2\2\2\62\64\5\b\5\2\63\62\3\2\2\2\64\65\3\2\2\2\65\63\3\2\2\2\65\66\3"+
		"\2\2\2\668\3\2\2\2\679\5\6\4\28\67\3\2\2\29:\3\2\2\2:8\3\2\2\2:;\3\2\2"+
		"\2;=\3\2\2\2<>\5\b\5\2=<\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@B\3\2\2"+
		"\2A\63\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2DF\3\2\2\2E\r\3\2\2\2E\22"+
		"\3\2\2\2E \3\2\2\2E.\3\2\2\2EA\3\2\2\2F\5\3\2\2\2GK\7\4\2\2HJ\5\b\5\2"+
		"IH\3\2\2\2JM\3\2\2\2KI\3\2\2\2KL\3\2\2\2LO\3\2\2\2MK\3\2\2\2NG\3\2\2\2"+
		"OP\3\2\2\2PN\3\2\2\2PQ\3\2\2\2Q[\3\2\2\2RS\7\6\2\2ST\7\b\2\2TX\3\2\2\2"+
		"UW\5\b\5\2VU\3\2\2\2WZ\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y\\\3\2\2\2ZX\3\2\2"+
		"\2[R\3\2\2\2\\]\3\2\2\2][\3\2\2\2]^\3\2\2\2^f\3\2\2\2_c\7\n\2\2`b\5\b"+
		"\5\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2dg\3\2\2\2ec\3\2\2\2f_\3\2"+
		"\2\2gh\3\2\2\2hf\3\2\2\2hi\3\2\2\2i\7\3\2\2\2jl\t\2\2\2kj\3\2\2\2lm\3"+
		"\2\2\2mk\3\2\2\2mn\3\2\2\2n\t\3\2\2\2\26\17\24\31\36\"\',\60\65:?CEKP"+
		"X]chm";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}