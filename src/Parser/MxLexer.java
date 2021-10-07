// Generated from D:/IdeaProjects/Compiler/src/Parser\Mx.g4 by ANTLR 4.9.1
package Parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, Plus=2, Minus=3, Multiply=4, Divide=5, Modulo=6, Greater=7, Less=8, 
		GreaterEqual=9, LessEqual=10, NotEqual=11, Equal=12, AndAnd=13, OrOr=14, 
		Not=15, RightShift=16, LeftShift=17, And=18, Or=19, Caret=20, Tilde=21, 
		Assign=22, SelfPlus=23, SelfMinus=24, Dot=25, Semi=26, Comma=27, Question=28, 
		Colon=29, DbQuotation=30, LeftParen=31, RightParen=32, LeftBracket=33, 
		RightBracket=34, LeftBrace=35, RightBrace=36, Int=37, Bool=38, String=39, 
		Null=40, Void=41, True=42, False=43, If=44, Else=45, For=46, While=47, 
		Break=48, Continue=49, Return=50, New=51, Class=52, This=53, WhiteSpace=54, 
		NewLine=55, LineComment=56, BlockComment=57, Identifier=58, ConstInteger=59, 
		ConstString=60, ConstBool=61;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "Plus", "Minus", "Multiply", "Divide", "Modulo", "Greater", "Less", 
			"GreaterEqual", "LessEqual", "NotEqual", "Equal", "AndAnd", "OrOr", "Not", 
			"RightShift", "LeftShift", "And", "Or", "Caret", "Tilde", "Assign", "SelfPlus", 
			"SelfMinus", "Dot", "Semi", "Comma", "Question", "Colon", "DbQuotation", 
			"LeftParen", "RightParen", "LeftBracket", "RightBracket", "LeftBrace", 
			"RightBrace", "Int", "Bool", "String", "Null", "Void", "True", "False", 
			"If", "Else", "For", "While", "Break", "Continue", "Return", "New", "Class", 
			"This", "WhiteSpace", "NewLine", "LineComment", "BlockComment", "Identifier", 
			"ConstInteger", "ConstString", "ConstBool"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'main()'", "'+'", "'-'", "'*'", "'/'", "'%'", "'>'", "'<'", "'>='", 
			"'<='", "'!='", "'=='", "'&&'", "'||'", "'!'", "'>>'", "'<<'", "'&'", 
			"'|'", "'^'", "'~'", "'='", "'++'", "'--'", "'.'", "';'", "','", "'?'", 
			"':'", "'\"'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'int'", "'bool'", 
			"'string'", "'null'", "'void'", "'true'", "'false'", "'if'", "'else'", 
			"'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", "'class'", 
			"'this'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "Plus", "Minus", "Multiply", "Divide", "Modulo", "Greater", 
			"Less", "GreaterEqual", "LessEqual", "NotEqual", "Equal", "AndAnd", "OrOr", 
			"Not", "RightShift", "LeftShift", "And", "Or", "Caret", "Tilde", "Assign", 
			"SelfPlus", "SelfMinus", "Dot", "Semi", "Comma", "Question", "Colon", 
			"DbQuotation", "LeftParen", "RightParen", "LeftBracket", "RightBracket", 
			"LeftBrace", "RightBrace", "Int", "Bool", "String", "Null", "Void", "True", 
			"False", "If", "Else", "For", "While", "Break", "Continue", "Return", 
			"New", "Class", "This", "WhiteSpace", "NewLine", "LineComment", "BlockComment", 
			"Identifier", "ConstInteger", "ConstString", "ConstBool"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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


	public MxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2?\u0179\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7"+
		"\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3"+
		"\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3"+
		"\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3"+
		"\31\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3"+
		"\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3&\3&\3\'\3\'\3\'\3\'"+
		"\3\'\3(\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3+\3"+
		"+\3,\3,\3,\3,\3,\3,\3-\3-\3-\3.\3.\3.\3.\3.\3/\3/\3/\3/\3\60\3\60\3\60"+
		"\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62"+
		"\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64"+
		"\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\67\6\67"+
		"\u0132\n\67\r\67\16\67\u0133\3\67\3\67\38\38\58\u013a\n8\38\58\u013d\n"+
		"8\38\38\39\39\39\39\79\u0145\n9\f9\169\u0148\139\39\39\3:\3:\3:\3:\7:"+
		"\u0150\n:\f:\16:\u0153\13:\3:\3:\3:\3:\3:\3;\3;\7;\u015c\n;\f;\16;\u015f"+
		"\13;\3<\3<\7<\u0163\n<\f<\16<\u0166\13<\3<\5<\u0169\n<\3=\3=\3=\3=\7="+
		"\u016f\n=\f=\16=\u0172\13=\3=\3=\3>\3>\5>\u0178\n>\3\u0151\2?\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C"+
		"#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w"+
		"=y>{?\3\2\n\4\2\13\13\"\"\4\2\f\f\17\17\4\2C\\c|\6\2\62;C\\aac|\3\2\63"+
		";\3\2\62;\6\2\13\f\17\17$$^^\7\2$$^^ppttvv\2\u0183\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2"+
		"\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2"+
		"Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3"+
		"\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2"+
		"\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\3}\3\2\2\2\5"+
		"\u0084\3\2\2\2\7\u0086\3\2\2\2\t\u0088\3\2\2\2\13\u008a\3\2\2\2\r\u008c"+
		"\3\2\2\2\17\u008e\3\2\2\2\21\u0090\3\2\2\2\23\u0092\3\2\2\2\25\u0095\3"+
		"\2\2\2\27\u0098\3\2\2\2\31\u009b\3\2\2\2\33\u009e\3\2\2\2\35\u00a1\3\2"+
		"\2\2\37\u00a4\3\2\2\2!\u00a6\3\2\2\2#\u00a9\3\2\2\2%\u00ac\3\2\2\2\'\u00ae"+
		"\3\2\2\2)\u00b0\3\2\2\2+\u00b2\3\2\2\2-\u00b4\3\2\2\2/\u00b6\3\2\2\2\61"+
		"\u00b9\3\2\2\2\63\u00bc\3\2\2\2\65\u00be\3\2\2\2\67\u00c0\3\2\2\29\u00c2"+
		"\3\2\2\2;\u00c4\3\2\2\2=\u00c6\3\2\2\2?\u00c8\3\2\2\2A\u00ca\3\2\2\2C"+
		"\u00cc\3\2\2\2E\u00ce\3\2\2\2G\u00d0\3\2\2\2I\u00d2\3\2\2\2K\u00d4\3\2"+
		"\2\2M\u00d8\3\2\2\2O\u00dd\3\2\2\2Q\u00e4\3\2\2\2S\u00e9\3\2\2\2U\u00ee"+
		"\3\2\2\2W\u00f3\3\2\2\2Y\u00f9\3\2\2\2[\u00fc\3\2\2\2]\u0101\3\2\2\2_"+
		"\u0105\3\2\2\2a\u010b\3\2\2\2c\u0111\3\2\2\2e\u011a\3\2\2\2g\u0121\3\2"+
		"\2\2i\u0125\3\2\2\2k\u012b\3\2\2\2m\u0131\3\2\2\2o\u013c\3\2\2\2q\u0140"+
		"\3\2\2\2s\u014b\3\2\2\2u\u0159\3\2\2\2w\u0168\3\2\2\2y\u016a\3\2\2\2{"+
		"\u0177\3\2\2\2}~\7o\2\2~\177\7c\2\2\177\u0080\7k\2\2\u0080\u0081\7p\2"+
		"\2\u0081\u0082\7*\2\2\u0082\u0083\7+\2\2\u0083\4\3\2\2\2\u0084\u0085\7"+
		"-\2\2\u0085\6\3\2\2\2\u0086\u0087\7/\2\2\u0087\b\3\2\2\2\u0088\u0089\7"+
		",\2\2\u0089\n\3\2\2\2\u008a\u008b\7\61\2\2\u008b\f\3\2\2\2\u008c\u008d"+
		"\7\'\2\2\u008d\16\3\2\2\2\u008e\u008f\7@\2\2\u008f\20\3\2\2\2\u0090\u0091"+
		"\7>\2\2\u0091\22\3\2\2\2\u0092\u0093\7@\2\2\u0093\u0094\7?\2\2\u0094\24"+
		"\3\2\2\2\u0095\u0096\7>\2\2\u0096\u0097\7?\2\2\u0097\26\3\2\2\2\u0098"+
		"\u0099\7#\2\2\u0099\u009a\7?\2\2\u009a\30\3\2\2\2\u009b\u009c\7?\2\2\u009c"+
		"\u009d\7?\2\2\u009d\32\3\2\2\2\u009e\u009f\7(\2\2\u009f\u00a0\7(\2\2\u00a0"+
		"\34\3\2\2\2\u00a1\u00a2\7~\2\2\u00a2\u00a3\7~\2\2\u00a3\36\3\2\2\2\u00a4"+
		"\u00a5\7#\2\2\u00a5 \3\2\2\2\u00a6\u00a7\7@\2\2\u00a7\u00a8\7@\2\2\u00a8"+
		"\"\3\2\2\2\u00a9\u00aa\7>\2\2\u00aa\u00ab\7>\2\2\u00ab$\3\2\2\2\u00ac"+
		"\u00ad\7(\2\2\u00ad&\3\2\2\2\u00ae\u00af\7~\2\2\u00af(\3\2\2\2\u00b0\u00b1"+
		"\7`\2\2\u00b1*\3\2\2\2\u00b2\u00b3\7\u0080\2\2\u00b3,\3\2\2\2\u00b4\u00b5"+
		"\7?\2\2\u00b5.\3\2\2\2\u00b6\u00b7\7-\2\2\u00b7\u00b8\7-\2\2\u00b8\60"+
		"\3\2\2\2\u00b9\u00ba\7/\2\2\u00ba\u00bb\7/\2\2\u00bb\62\3\2\2\2\u00bc"+
		"\u00bd\7\60\2\2\u00bd\64\3\2\2\2\u00be\u00bf\7=\2\2\u00bf\66\3\2\2\2\u00c0"+
		"\u00c1\7.\2\2\u00c18\3\2\2\2\u00c2\u00c3\7A\2\2\u00c3:\3\2\2\2\u00c4\u00c5"+
		"\7<\2\2\u00c5<\3\2\2\2\u00c6\u00c7\7$\2\2\u00c7>\3\2\2\2\u00c8\u00c9\7"+
		"*\2\2\u00c9@\3\2\2\2\u00ca\u00cb\7+\2\2\u00cbB\3\2\2\2\u00cc\u00cd\7]"+
		"\2\2\u00cdD\3\2\2\2\u00ce\u00cf\7_\2\2\u00cfF\3\2\2\2\u00d0\u00d1\7}\2"+
		"\2\u00d1H\3\2\2\2\u00d2\u00d3\7\177\2\2\u00d3J\3\2\2\2\u00d4\u00d5\7k"+
		"\2\2\u00d5\u00d6\7p\2\2\u00d6\u00d7\7v\2\2\u00d7L\3\2\2\2\u00d8\u00d9"+
		"\7d\2\2\u00d9\u00da\7q\2\2\u00da\u00db\7q\2\2\u00db\u00dc\7n\2\2\u00dc"+
		"N\3\2\2\2\u00dd\u00de\7u\2\2\u00de\u00df\7v\2\2\u00df\u00e0\7t\2\2\u00e0"+
		"\u00e1\7k\2\2\u00e1\u00e2\7p\2\2\u00e2\u00e3\7i\2\2\u00e3P\3\2\2\2\u00e4"+
		"\u00e5\7p\2\2\u00e5\u00e6\7w\2\2\u00e6\u00e7\7n\2\2\u00e7\u00e8\7n\2\2"+
		"\u00e8R\3\2\2\2\u00e9\u00ea\7x\2\2\u00ea\u00eb\7q\2\2\u00eb\u00ec\7k\2"+
		"\2\u00ec\u00ed\7f\2\2\u00edT\3\2\2\2\u00ee\u00ef\7v\2\2\u00ef\u00f0\7"+
		"t\2\2\u00f0\u00f1\7w\2\2\u00f1\u00f2\7g\2\2\u00f2V\3\2\2\2\u00f3\u00f4"+
		"\7h\2\2\u00f4\u00f5\7c\2\2\u00f5\u00f6\7n\2\2\u00f6\u00f7\7u\2\2\u00f7"+
		"\u00f8\7g\2\2\u00f8X\3\2\2\2\u00f9\u00fa\7k\2\2\u00fa\u00fb\7h\2\2\u00fb"+
		"Z\3\2\2\2\u00fc\u00fd\7g\2\2\u00fd\u00fe\7n\2\2\u00fe\u00ff\7u\2\2\u00ff"+
		"\u0100\7g\2\2\u0100\\\3\2\2\2\u0101\u0102\7h\2\2\u0102\u0103\7q\2\2\u0103"+
		"\u0104\7t\2\2\u0104^\3\2\2\2\u0105\u0106\7y\2\2\u0106\u0107\7j\2\2\u0107"+
		"\u0108\7k\2\2\u0108\u0109\7n\2\2\u0109\u010a\7g\2\2\u010a`\3\2\2\2\u010b"+
		"\u010c\7d\2\2\u010c\u010d\7t\2\2\u010d\u010e\7g\2\2\u010e\u010f\7c\2\2"+
		"\u010f\u0110\7m\2\2\u0110b\3\2\2\2\u0111\u0112\7e\2\2\u0112\u0113\7q\2"+
		"\2\u0113\u0114\7p\2\2\u0114\u0115\7v\2\2\u0115\u0116\7k\2\2\u0116\u0117"+
		"\7p\2\2\u0117\u0118\7w\2\2\u0118\u0119\7g\2\2\u0119d\3\2\2\2\u011a\u011b"+
		"\7t\2\2\u011b\u011c\7g\2\2\u011c\u011d\7v\2\2\u011d\u011e\7w\2\2\u011e"+
		"\u011f\7t\2\2\u011f\u0120\7p\2\2\u0120f\3\2\2\2\u0121\u0122\7p\2\2\u0122"+
		"\u0123\7g\2\2\u0123\u0124\7y\2\2\u0124h\3\2\2\2\u0125\u0126\7e\2\2\u0126"+
		"\u0127\7n\2\2\u0127\u0128\7c\2\2\u0128\u0129\7u\2\2\u0129\u012a\7u\2\2"+
		"\u012aj\3\2\2\2\u012b\u012c\7v\2\2\u012c\u012d\7j\2\2\u012d\u012e\7k\2"+
		"\2\u012e\u012f\7u\2\2\u012fl\3\2\2\2\u0130\u0132\t\2\2\2\u0131\u0130\3"+
		"\2\2\2\u0132\u0133\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134"+
		"\u0135\3\2\2\2\u0135\u0136\b\67\2\2\u0136n\3\2\2\2\u0137\u0139\7\17\2"+
		"\2\u0138\u013a\7\f\2\2\u0139\u0138\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u013d"+
		"\3\2\2\2\u013b\u013d\7\f\2\2\u013c\u0137\3\2\2\2\u013c\u013b\3\2\2\2\u013d"+
		"\u013e\3\2\2\2\u013e\u013f\b8\2\2\u013fp\3\2\2\2\u0140\u0141\7\61\2\2"+
		"\u0141\u0142\7\61\2\2\u0142\u0146\3\2\2\2\u0143\u0145\n\3\2\2\u0144\u0143"+
		"\3\2\2\2\u0145\u0148\3\2\2\2\u0146\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147"+
		"\u0149\3\2\2\2\u0148\u0146\3\2\2\2\u0149\u014a\b9\2\2\u014ar\3\2\2\2\u014b"+
		"\u014c\7\61\2\2\u014c\u014d\7,\2\2\u014d\u0151\3\2\2\2\u014e\u0150\13"+
		"\2\2\2\u014f\u014e\3\2\2\2\u0150\u0153\3\2\2\2\u0151\u0152\3\2\2\2\u0151"+
		"\u014f\3\2\2\2\u0152\u0154\3\2\2\2\u0153\u0151\3\2\2\2\u0154\u0155\7,"+
		"\2\2\u0155\u0156\7\61\2\2\u0156\u0157\3\2\2\2\u0157\u0158\b:\2\2\u0158"+
		"t\3\2\2\2\u0159\u015d\t\4\2\2\u015a\u015c\t\5\2\2\u015b\u015a\3\2\2\2"+
		"\u015c\u015f\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015ev\3"+
		"\2\2\2\u015f\u015d\3\2\2\2\u0160\u0164\t\6\2\2\u0161\u0163\t\7\2\2\u0162"+
		"\u0161\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162\3\2\2\2\u0164\u0165\3\2"+
		"\2\2\u0165\u0169\3\2\2\2\u0166\u0164\3\2\2\2\u0167\u0169\7\62\2\2\u0168"+
		"\u0160\3\2\2\2\u0168\u0167\3\2\2\2\u0169x\3\2\2\2\u016a\u0170\7$\2\2\u016b"+
		"\u016f\n\b\2\2\u016c\u016d\7^\2\2\u016d\u016f\t\t\2\2\u016e\u016b\3\2"+
		"\2\2\u016e\u016c\3\2\2\2\u016f\u0172\3\2\2\2\u0170\u016e\3\2\2\2\u0170"+
		"\u0171\3\2\2\2\u0171\u0173\3\2\2\2\u0172\u0170\3\2\2\2\u0173\u0174\7$"+
		"\2\2\u0174z\3\2\2\2\u0175\u0178\5U+\2\u0176\u0178\5W,\2\u0177\u0175\3"+
		"\2\2\2\u0177\u0176\3\2\2\2\u0178|\3\2\2\2\16\2\u0133\u0139\u013c\u0146"+
		"\u0151\u015d\u0164\u0168\u016e\u0170\u0177\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}