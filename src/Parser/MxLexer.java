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
		Plus=1, Minus=2, Multiply=3, Divide=4, Modulo=5, Greater=6, Less=7, GreaterEqual=8, 
		LessEqual=9, NotEqual=10, Equal=11, AndAnd=12, OrOr=13, Not=14, RightShift=15, 
		LeftShift=16, And=17, Or=18, Caret=19, Tilde=20, Assign=21, SelfPlus=22, 
		SelfMinus=23, Dot=24, Semi=25, Comma=26, Question=27, Colon=28, DbQuotation=29, 
		LeftParen=30, RightParen=31, LeftBracket=32, RightBracket=33, LeftBrace=34, 
		RightBrace=35, Int=36, Bool=37, String=38, Null=39, Void=40, True=41, 
		False=42, If=43, Else=44, For=45, While=46, Break=47, Continue=48, Return=49, 
		New=50, Class=51, This=52, WhiteSpace=53, NewLine=54, LineComment=55, 
		BlockComment=56, Identifier=57, ConstInteger=58, ConstString=59, ConstBool=60;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Plus", "Minus", "Multiply", "Divide", "Modulo", "Greater", "Less", "GreaterEqual", 
			"LessEqual", "NotEqual", "Equal", "AndAnd", "OrOr", "Not", "RightShift", 
			"LeftShift", "And", "Or", "Caret", "Tilde", "Assign", "SelfPlus", "SelfMinus", 
			"Dot", "Semi", "Comma", "Question", "Colon", "DbQuotation", "LeftParen", 
			"RightParen", "LeftBracket", "RightBracket", "LeftBrace", "RightBrace", 
			"Int", "Bool", "String", "Null", "Void", "True", "False", "If", "Else", 
			"For", "While", "Break", "Continue", "Return", "New", "Class", "This", 
			"WhiteSpace", "NewLine", "LineComment", "BlockComment", "Identifier", 
			"ConstInteger", "ConstString", "ConstBool"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", "'-'", "'*'", "'/'", "'%'", "'>'", "'<'", "'>='", "'<='", 
			"'!='", "'=='", "'&&'", "'||'", "'!'", "'>>'", "'<<'", "'&'", "'|'", 
			"'^'", "'~'", "'='", "'++'", "'--'", "'.'", "';'", "','", "'?'", "':'", 
			"'\"'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'int'", "'bool'", 
			"'string'", "'null'", "'void'", "'true'", "'false'", "'if'", "'else'", 
			"'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", "'class'", 
			"'this'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Plus", "Minus", "Multiply", "Divide", "Modulo", "Greater", "Less", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2>\u0170\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3"+
		"\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17"+
		"\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25"+
		"\3\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3"+
		"#\3$\3$\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3"+
		"(\3(\3(\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3,\3,\3,\3-\3"+
		"-\3-\3-\3-\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62"+
		"\3\62\3\62\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\65\3\65"+
		"\3\65\3\65\3\65\3\66\6\66\u0129\n\66\r\66\16\66\u012a\3\66\3\66\3\67\3"+
		"\67\5\67\u0131\n\67\3\67\5\67\u0134\n\67\3\67\3\67\38\38\38\38\78\u013c"+
		"\n8\f8\168\u013f\138\38\38\39\39\39\39\79\u0147\n9\f9\169\u014a\139\3"+
		"9\39\39\39\39\3:\3:\7:\u0153\n:\f:\16:\u0156\13:\3;\3;\7;\u015a\n;\f;"+
		"\16;\u015d\13;\3;\5;\u0160\n;\3<\3<\3<\3<\7<\u0166\n<\f<\16<\u0169\13"+
		"<\3<\3<\3=\3=\5=\u016f\n=\3\u0148\2>\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["+
		"/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>\3\2\n\4\2\13\13\"\"\4"+
		"\2\f\f\17\17\4\2C\\c|\6\2\62;C\\aac|\3\2\63;\3\2\62;\6\2\13\f\17\17$$"+
		"^^\7\2$$^^ppttvv\2\u017a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2"+
		"Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3"+
		"\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2"+
		"\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2"+
		"w\3\2\2\2\2y\3\2\2\2\3{\3\2\2\2\5}\3\2\2\2\7\177\3\2\2\2\t\u0081\3\2\2"+
		"\2\13\u0083\3\2\2\2\r\u0085\3\2\2\2\17\u0087\3\2\2\2\21\u0089\3\2\2\2"+
		"\23\u008c\3\2\2\2\25\u008f\3\2\2\2\27\u0092\3\2\2\2\31\u0095\3\2\2\2\33"+
		"\u0098\3\2\2\2\35\u009b\3\2\2\2\37\u009d\3\2\2\2!\u00a0\3\2\2\2#\u00a3"+
		"\3\2\2\2%\u00a5\3\2\2\2\'\u00a7\3\2\2\2)\u00a9\3\2\2\2+\u00ab\3\2\2\2"+
		"-\u00ad\3\2\2\2/\u00b0\3\2\2\2\61\u00b3\3\2\2\2\63\u00b5\3\2\2\2\65\u00b7"+
		"\3\2\2\2\67\u00b9\3\2\2\29\u00bb\3\2\2\2;\u00bd\3\2\2\2=\u00bf\3\2\2\2"+
		"?\u00c1\3\2\2\2A\u00c3\3\2\2\2C\u00c5\3\2\2\2E\u00c7\3\2\2\2G\u00c9\3"+
		"\2\2\2I\u00cb\3\2\2\2K\u00cf\3\2\2\2M\u00d4\3\2\2\2O\u00db\3\2\2\2Q\u00e0"+
		"\3\2\2\2S\u00e5\3\2\2\2U\u00ea\3\2\2\2W\u00f0\3\2\2\2Y\u00f3\3\2\2\2["+
		"\u00f8\3\2\2\2]\u00fc\3\2\2\2_\u0102\3\2\2\2a\u0108\3\2\2\2c\u0111\3\2"+
		"\2\2e\u0118\3\2\2\2g\u011c\3\2\2\2i\u0122\3\2\2\2k\u0128\3\2\2\2m\u0133"+
		"\3\2\2\2o\u0137\3\2\2\2q\u0142\3\2\2\2s\u0150\3\2\2\2u\u015f\3\2\2\2w"+
		"\u0161\3\2\2\2y\u016e\3\2\2\2{|\7-\2\2|\4\3\2\2\2}~\7/\2\2~\6\3\2\2\2"+
		"\177\u0080\7,\2\2\u0080\b\3\2\2\2\u0081\u0082\7\61\2\2\u0082\n\3\2\2\2"+
		"\u0083\u0084\7\'\2\2\u0084\f\3\2\2\2\u0085\u0086\7@\2\2\u0086\16\3\2\2"+
		"\2\u0087\u0088\7>\2\2\u0088\20\3\2\2\2\u0089\u008a\7@\2\2\u008a\u008b"+
		"\7?\2\2\u008b\22\3\2\2\2\u008c\u008d\7>\2\2\u008d\u008e\7?\2\2\u008e\24"+
		"\3\2\2\2\u008f\u0090\7#\2\2\u0090\u0091\7?\2\2\u0091\26\3\2\2\2\u0092"+
		"\u0093\7?\2\2\u0093\u0094\7?\2\2\u0094\30\3\2\2\2\u0095\u0096\7(\2\2\u0096"+
		"\u0097\7(\2\2\u0097\32\3\2\2\2\u0098\u0099\7~\2\2\u0099\u009a\7~\2\2\u009a"+
		"\34\3\2\2\2\u009b\u009c\7#\2\2\u009c\36\3\2\2\2\u009d\u009e\7@\2\2\u009e"+
		"\u009f\7@\2\2\u009f \3\2\2\2\u00a0\u00a1\7>\2\2\u00a1\u00a2\7>\2\2\u00a2"+
		"\"\3\2\2\2\u00a3\u00a4\7(\2\2\u00a4$\3\2\2\2\u00a5\u00a6\7~\2\2\u00a6"+
		"&\3\2\2\2\u00a7\u00a8\7`\2\2\u00a8(\3\2\2\2\u00a9\u00aa\7\u0080\2\2\u00aa"+
		"*\3\2\2\2\u00ab\u00ac\7?\2\2\u00ac,\3\2\2\2\u00ad\u00ae\7-\2\2\u00ae\u00af"+
		"\7-\2\2\u00af.\3\2\2\2\u00b0\u00b1\7/\2\2\u00b1\u00b2\7/\2\2\u00b2\60"+
		"\3\2\2\2\u00b3\u00b4\7\60\2\2\u00b4\62\3\2\2\2\u00b5\u00b6\7=\2\2\u00b6"+
		"\64\3\2\2\2\u00b7\u00b8\7.\2\2\u00b8\66\3\2\2\2\u00b9\u00ba\7A\2\2\u00ba"+
		"8\3\2\2\2\u00bb\u00bc\7<\2\2\u00bc:\3\2\2\2\u00bd\u00be\7$\2\2\u00be<"+
		"\3\2\2\2\u00bf\u00c0\7*\2\2\u00c0>\3\2\2\2\u00c1\u00c2\7+\2\2\u00c2@\3"+
		"\2\2\2\u00c3\u00c4\7]\2\2\u00c4B\3\2\2\2\u00c5\u00c6\7_\2\2\u00c6D\3\2"+
		"\2\2\u00c7\u00c8\7}\2\2\u00c8F\3\2\2\2\u00c9\u00ca\7\177\2\2\u00caH\3"+
		"\2\2\2\u00cb\u00cc\7k\2\2\u00cc\u00cd\7p\2\2\u00cd\u00ce\7v\2\2\u00ce"+
		"J\3\2\2\2\u00cf\u00d0\7d\2\2\u00d0\u00d1\7q\2\2\u00d1\u00d2\7q\2\2\u00d2"+
		"\u00d3\7n\2\2\u00d3L\3\2\2\2\u00d4\u00d5\7u\2\2\u00d5\u00d6\7v\2\2\u00d6"+
		"\u00d7\7t\2\2\u00d7\u00d8\7k\2\2\u00d8\u00d9\7p\2\2\u00d9\u00da\7i\2\2"+
		"\u00daN\3\2\2\2\u00db\u00dc\7p\2\2\u00dc\u00dd\7w\2\2\u00dd\u00de\7n\2"+
		"\2\u00de\u00df\7n\2\2\u00dfP\3\2\2\2\u00e0\u00e1\7x\2\2\u00e1\u00e2\7"+
		"q\2\2\u00e2\u00e3\7k\2\2\u00e3\u00e4\7f\2\2\u00e4R\3\2\2\2\u00e5\u00e6"+
		"\7v\2\2\u00e6\u00e7\7t\2\2\u00e7\u00e8\7w\2\2\u00e8\u00e9\7g\2\2\u00e9"+
		"T\3\2\2\2\u00ea\u00eb\7h\2\2\u00eb\u00ec\7c\2\2\u00ec\u00ed\7n\2\2\u00ed"+
		"\u00ee\7u\2\2\u00ee\u00ef\7g\2\2\u00efV\3\2\2\2\u00f0\u00f1\7k\2\2\u00f1"+
		"\u00f2\7h\2\2\u00f2X\3\2\2\2\u00f3\u00f4\7g\2\2\u00f4\u00f5\7n\2\2\u00f5"+
		"\u00f6\7u\2\2\u00f6\u00f7\7g\2\2\u00f7Z\3\2\2\2\u00f8\u00f9\7h\2\2\u00f9"+
		"\u00fa\7q\2\2\u00fa\u00fb\7t\2\2\u00fb\\\3\2\2\2\u00fc\u00fd\7y\2\2\u00fd"+
		"\u00fe\7j\2\2\u00fe\u00ff\7k\2\2\u00ff\u0100\7n\2\2\u0100\u0101\7g\2\2"+
		"\u0101^\3\2\2\2\u0102\u0103\7d\2\2\u0103\u0104\7t\2\2\u0104\u0105\7g\2"+
		"\2\u0105\u0106\7c\2\2\u0106\u0107\7m\2\2\u0107`\3\2\2\2\u0108\u0109\7"+
		"e\2\2\u0109\u010a\7q\2\2\u010a\u010b\7p\2\2\u010b\u010c\7v\2\2\u010c\u010d"+
		"\7k\2\2\u010d\u010e\7p\2\2\u010e\u010f\7w\2\2\u010f\u0110\7g\2\2\u0110"+
		"b\3\2\2\2\u0111\u0112\7t\2\2\u0112\u0113\7g\2\2\u0113\u0114\7v\2\2\u0114"+
		"\u0115\7w\2\2\u0115\u0116\7t\2\2\u0116\u0117\7p\2\2\u0117d\3\2\2\2\u0118"+
		"\u0119\7p\2\2\u0119\u011a\7g\2\2\u011a\u011b\7y\2\2\u011bf\3\2\2\2\u011c"+
		"\u011d\7e\2\2\u011d\u011e\7n\2\2\u011e\u011f\7c\2\2\u011f\u0120\7u\2\2"+
		"\u0120\u0121\7u\2\2\u0121h\3\2\2\2\u0122\u0123\7v\2\2\u0123\u0124\7j\2"+
		"\2\u0124\u0125\7k\2\2\u0125\u0126\7u\2\2\u0126j\3\2\2\2\u0127\u0129\t"+
		"\2\2\2\u0128\u0127\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u0128\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012d\b\66\2\2\u012dl\3\2\2\2"+
		"\u012e\u0130\7\17\2\2\u012f\u0131\7\f\2\2\u0130\u012f\3\2\2\2\u0130\u0131"+
		"\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0134\7\f\2\2\u0133\u012e\3\2\2\2\u0133"+
		"\u0132\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0136\b\67\2\2\u0136n\3\2\2\2"+
		"\u0137\u0138\7\61\2\2\u0138\u0139\7\61\2\2\u0139\u013d\3\2\2\2\u013a\u013c"+
		"\n\3\2\2\u013b\u013a\3\2\2\2\u013c\u013f\3\2\2\2\u013d\u013b\3\2\2\2\u013d"+
		"\u013e\3\2\2\2\u013e\u0140\3\2\2\2\u013f\u013d\3\2\2\2\u0140\u0141\b8"+
		"\2\2\u0141p\3\2\2\2\u0142\u0143\7\61\2\2\u0143\u0144\7,\2\2\u0144\u0148"+
		"\3\2\2\2\u0145\u0147\13\2\2\2\u0146\u0145\3\2\2\2\u0147\u014a\3\2\2\2"+
		"\u0148\u0149\3\2\2\2\u0148\u0146\3\2\2\2\u0149\u014b\3\2\2\2\u014a\u0148"+
		"\3\2\2\2\u014b\u014c\7,\2\2\u014c\u014d\7\61\2\2\u014d\u014e\3\2\2\2\u014e"+
		"\u014f\b9\2\2\u014fr\3\2\2\2\u0150\u0154\t\4\2\2\u0151\u0153\t\5\2\2\u0152"+
		"\u0151\3\2\2\2\u0153\u0156\3\2\2\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2"+
		"\2\2\u0155t\3\2\2\2\u0156\u0154\3\2\2\2\u0157\u015b\t\6\2\2\u0158\u015a"+
		"\t\7\2\2\u0159\u0158\3\2\2\2\u015a\u015d\3\2\2\2\u015b\u0159\3\2\2\2\u015b"+
		"\u015c\3\2\2\2\u015c\u0160\3\2\2\2\u015d\u015b\3\2\2\2\u015e\u0160\7\62"+
		"\2\2\u015f\u0157\3\2\2\2\u015f\u015e\3\2\2\2\u0160v\3\2\2\2\u0161\u0167"+
		"\7$\2\2\u0162\u0166\n\b\2\2\u0163\u0164\7^\2\2\u0164\u0166\t\t\2\2\u0165"+
		"\u0162\3\2\2\2\u0165\u0163\3\2\2\2\u0166\u0169\3\2\2\2\u0167\u0165\3\2"+
		"\2\2\u0167\u0168\3\2\2\2\u0168\u016a\3\2\2\2\u0169\u0167\3\2\2\2\u016a"+
		"\u016b\7$\2\2\u016bx\3\2\2\2\u016c\u016f\5S*\2\u016d\u016f\5U+\2\u016e"+
		"\u016c\3\2\2\2\u016e\u016d\3\2\2\2\u016fz\3\2\2\2\16\2\u012a\u0130\u0133"+
		"\u013d\u0148\u0154\u015b\u015f\u0165\u0167\u016e\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}