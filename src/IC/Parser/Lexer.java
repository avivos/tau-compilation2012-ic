/* The following code was generated by JFlex 1.4.3 on 14:06 05/03/12 */

package IC.Parser;



/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 14:06 05/03/12 from the specification file
 * <tt>src/IC/Parser/IC.lex</tt>
 */
public @SuppressWarnings(value={"all"}) class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int STRING = 8;
  public static final int COMMENTSEND = 6;
  public static final int SL_COMMENTS = 2;
  public static final int YYINITIAL = 0;
  public static final int COMMENTS = 4;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4, 4
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\62\1\55\2\0\1\63\22\0\1\4\1\42\1\60\2\64"+
    "\1\46\1\35\1\64\1\5\1\6\1\47\1\50\1\22\1\45\1\27"+
    "\1\26\1\3\11\2\1\64\1\53\1\44\1\7\1\33\2\64\32\56"+
    "\1\36\1\61\1\51\1\64\1\1\1\64\1\14\1\10\1\20\1\31"+
    "\1\13\1\32\1\34\1\40\1\24\1\57\1\17\1\12\1\57\1\15"+
    "\1\11\2\57\1\16\1\21\1\23\1\25\1\54\1\41\1\30\2\57"+
    "\1\37\1\43\1\52\1\64\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\4\0\1\1\1\2\2\3\1\4\1\5\1\6\1\7"+
    "\10\10\1\11\2\10\1\12\1\13\1\10\1\14\1\2"+
    "\1\15\1\16\1\10\1\17\1\4\1\20\1\21\1\22"+
    "\1\23\1\24\1\25\1\26\1\27\1\10\1\30\1\31"+
    "\2\4\1\32\1\33\2\34\1\35\1\34\1\1\1\36"+
    "\1\37\1\40\2\36\1\41\1\42\1\43\16\10\1\44"+
    "\1\45\1\46\1\10\1\47\1\50\1\10\1\51\1\52"+
    "\1\53\1\0\1\10\1\0\5\10\1\54\10\10\1\55"+
    "\6\10\1\56\1\10\1\57\5\10\1\60\1\61\3\10"+
    "\1\62\1\10\1\63\4\10\1\64\3\10\1\65\1\66"+
    "\1\10\1\67\1\10\1\70\1\10\1\71\1\72\1\10"+
    "\1\73\1\74\1\10\1\75\1\76";

  private static int [] zzUnpackAction() {
    int [] result = new int[148];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\65\0\152\0\237\0\324\0\u0109\0\u013e\0\u0173"+
    "\0\u0109\0\u0109\0\u0109\0\u01a8\0\u01dd\0\u0212\0\u0247\0\u027c"+
    "\0\u02b1\0\u02e6\0\u031b\0\u0350\0\u0109\0\u0385\0\u03ba\0\u03ef"+
    "\0\u0109\0\u0424\0\u0459\0\u048e\0\u0109\0\u0109\0\u04c3\0\u04f8"+
    "\0\u052d\0\u0562\0\u0597\0\u0109\0\u0109\0\u0109\0\u0109\0\u0109"+
    "\0\u0109\0\u05cc\0\u0601\0\u0109\0\u0636\0\u0597\0\u0109\0\u0109"+
    "\0\u0109\0\u0636\0\u0109\0\u0597\0\u066b\0\u066b\0\u0109\0\u0109"+
    "\0\u06a0\0\u0109\0\u06d5\0\u070a\0\u0109\0\u073f\0\u0774\0\u07a9"+
    "\0\u07de\0\u0813\0\u0848\0\u087d\0\u08b2\0\u08e7\0\u091c\0\u0951"+
    "\0\u0986\0\u09bb\0\u09f0\0\u0212\0\u0109\0\u0109\0\u0a25\0\u0109"+
    "\0\u0109\0\u0a5a\0\u0109\0\u0109\0\u0109\0\u0636\0\u0a8f\0\u06a0"+
    "\0\u0ac4\0\u0af9\0\u0b2e\0\u0b63\0\u0b98\0\u0212\0\u0bcd\0\u0c02"+
    "\0\u0c37\0\u0c6c\0\u0ca1\0\u0cd6\0\u0d0b\0\u0d40\0\u0d75\0\u0daa"+
    "\0\u0ddf\0\u0e14\0\u0e49\0\u0e7e\0\u0eb3\0\u0212\0\u0ee8\0\u0212"+
    "\0\u0f1d\0\u0f52\0\u0f87\0\u0fbc\0\u0ff1\0\u0212\0\u0212\0\u1026"+
    "\0\u105b\0\u1090\0\u0212\0\u10c5\0\u0212\0\u10fa\0\u112f\0\u1164"+
    "\0\u1199\0\u0212\0\u11ce\0\u1203\0\u1238\0\u0212\0\u0212\0\u126d"+
    "\0\u0212\0\u12a2\0\u0212\0\u12d7\0\u0212\0\u0212\0\u130c\0\u0212"+
    "\0\u0212\0\u1341\0\u0212\0\u0212";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[148];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\2\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\16\1\21\1\22\1\16\1\23"+
    "\1\24\1\25\1\26\1\27\1\16\1\30\1\31\2\16"+
    "\1\32\1\33\1\16\1\34\1\35\1\36\1\16\1\37"+
    "\1\40\1\41\1\42\1\43\1\44\1\45\1\46\1\47"+
    "\1\50\1\51\1\52\1\11\1\53\1\16\1\54\1\6"+
    "\2\11\1\6\2\11\2\55\41\11\1\56\7\11\1\57"+
    "\11\11\2\55\41\11\1\56\1\11\1\60\15\11\2\61"+
    "\2\62\22\61\1\63\16\61\1\64\1\61\1\11\15\61"+
    "\1\0\42\65\1\66\11\65\1\67\2\65\1\70\1\71"+
    "\1\72\1\67\1\65\66\0\1\73\2\7\4\0\12\73"+
    "\1\0\3\73\2\0\3\73\1\0\1\73\3\0\2\73"+
    "\12\0\1\73\1\0\2\73\6\0\1\73\1\74\1\10"+
    "\4\0\12\73\1\0\3\73\2\0\3\73\1\0\1\73"+
    "\3\0\2\73\12\0\1\73\1\0\2\73\14\0\1\75"+
    "\56\0\3\16\4\0\1\16\1\76\4\16\1\77\3\16"+
    "\1\0\3\16\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\12\16"+
    "\1\0\3\16\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\3\16"+
    "\1\100\6\16\1\0\3\16\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\2\16\1\101\7\16\1\0\3\16\2\0\1\102"+
    "\2\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\3\16\1\103\6\16\1\0"+
    "\2\16\1\104\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\3\16"+
    "\1\105\6\16\1\0\3\16\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\1\16\1\106\1\107\7\16\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\12\16\1\0\1\110\2\16"+
    "\2\0\3\16\1\0\1\16\3\0\2\16\12\0\1\16"+
    "\1\0\2\16\6\0\3\16\4\0\6\16\1\111\3\16"+
    "\1\0\3\16\2\0\3\16\1\0\1\16\3\0\1\112"+
    "\1\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\5\16\1\113\4\16\1\0\3\16\2\0\2\16\1\114"+
    "\1\0\1\16\3\0\2\16\12\0\1\16\1\0\2\16"+
    "\33\0\1\115\20\0\1\116\16\0\3\16\4\0\4\16"+
    "\1\117\5\16\1\0\3\16\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\14\0\1\120"+
    "\112\0\1\121\30\0\3\16\4\0\12\16\1\0\3\16"+
    "\2\0\3\16\1\0\1\16\3\0\1\122\1\16\12\0"+
    "\1\16\1\0\2\16\14\0\1\123\120\0\1\124\30\0"+
    "\1\125\57\0\2\126\62\0\3\16\4\0\1\16\1\127"+
    "\10\16\1\0\3\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\53\4\0"+
    "\12\53\1\0\3\53\2\0\3\53\1\0\1\53\3\0"+
    "\2\53\12\0\1\53\1\0\2\53\6\0\1\73\2\126"+
    "\4\0\12\73\1\0\3\73\2\0\3\73\1\0\1\73"+
    "\3\0\2\73\12\0\1\73\1\0\2\73\6\0\54\65"+
    "\1\0\2\65\1\0\1\130\2\0\1\65\15\0\1\65"+
    "\5\0\1\65\34\0\2\65\4\0\1\73\6\0\12\73"+
    "\1\0\3\73\2\0\3\73\1\0\1\73\3\0\2\73"+
    "\12\0\1\73\1\0\2\73\6\0\1\73\2\74\4\0"+
    "\12\73\1\0\3\73\2\0\3\73\1\0\1\73\3\0"+
    "\2\73\12\0\1\73\1\0\2\73\6\0\3\16\4\0"+
    "\1\16\1\131\10\16\1\0\3\16\2\0\3\16\1\0"+
    "\1\16\3\0\2\16\12\0\1\16\1\0\2\16\6\0"+
    "\3\16\4\0\3\16\1\132\6\16\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\5\16\1\133\4\16\1\0"+
    "\3\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\11\16\1\134"+
    "\1\0\3\16\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\12\16"+
    "\1\0\1\135\2\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\12\16\1\0\3\16\2\0\3\16\1\0\1\16\3\0"+
    "\1\16\1\136\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\2\16\1\137\7\16\1\0\3\16\2\0\3\16"+
    "\1\0\1\16\3\0\2\16\12\0\1\16\1\0\2\16"+
    "\6\0\3\16\4\0\12\16\1\0\1\140\2\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\5\16\1\141\4\16\1\0"+
    "\3\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\4\16\1\142"+
    "\5\16\1\0\3\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\4\16\1\143\1\16\1\144\3\16\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\12\16\1\0\2\16\1\145"+
    "\2\0\3\16\1\0\1\16\3\0\2\16\12\0\1\16"+
    "\1\0\2\16\6\0\3\16\4\0\12\16\1\0\1\16"+
    "\1\146\1\16\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\12\16"+
    "\1\0\1\147\2\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\2\16\1\150\7\16\1\0\3\16\2\0\3\16\1\0"+
    "\1\16\3\0\2\16\12\0\1\16\1\0\2\16\6\0"+
    "\3\16\4\0\12\16\1\0\1\16\1\151\1\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\12\16\1\0\1\16\1\152"+
    "\1\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\2\16\1\153"+
    "\7\16\1\0\3\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\4\16\1\154\5\16\1\0\3\16\2\0\3\16\1\0"+
    "\1\16\3\0\2\16\12\0\1\16\1\0\2\16\6\0"+
    "\3\16\4\0\12\16\1\0\3\16\2\0\3\16\1\0"+
    "\1\155\3\0\2\16\12\0\1\16\1\0\2\16\6\0"+
    "\3\16\4\0\3\16\1\156\6\16\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\3\16\1\157\6\16\1\0"+
    "\3\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\2\16\1\160"+
    "\7\16\1\0\3\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\12\16\1\0\2\16\1\161\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\12\16\1\0\1\162\2\16\2\0\3\16\1\0"+
    "\1\16\3\0\2\16\12\0\1\16\1\0\2\16\6\0"+
    "\3\16\4\0\11\16\1\163\1\0\3\16\2\0\3\16"+
    "\1\0\1\16\3\0\2\16\12\0\1\16\1\0\2\16"+
    "\6\0\3\16\4\0\12\16\1\0\1\164\2\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\12\16\1\0\1\16\1\165"+
    "\1\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\3\16\1\166"+
    "\6\16\1\0\3\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\11\16\1\167\1\0\3\16\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\3\16\1\170\6\16\1\0\3\16\2\0\3\16"+
    "\1\0\1\16\3\0\2\16\12\0\1\16\1\0\2\16"+
    "\6\0\3\16\4\0\11\16\1\171\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\2\16\1\172\7\16\1\0"+
    "\3\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\12\16\1\0"+
    "\3\16\2\0\1\16\1\173\1\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\3\16\1\174\6\16\1\0\3\16\2\0\3\16\1\0"+
    "\1\16\3\0\2\16\12\0\1\16\1\0\2\16\6\0"+
    "\3\16\4\0\7\16\1\175\2\16\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\12\16\1\0\1\176\2\16"+
    "\2\0\3\16\1\0\1\16\3\0\2\16\12\0\1\16"+
    "\1\0\2\16\6\0\3\16\4\0\5\16\1\177\4\16"+
    "\1\0\3\16\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\6\16"+
    "\1\200\3\16\1\0\3\16\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\12\16\1\0\1\16\1\201\1\16\2\0\3\16"+
    "\1\0\1\16\3\0\2\16\12\0\1\16\1\0\2\16"+
    "\6\0\3\16\4\0\11\16\1\202\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\12\16\1\0\1\16\1\203"+
    "\1\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\5\16\1\204"+
    "\4\16\1\0\3\16\2\0\3\16\1\0\1\16\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\12\16\1\0\3\16\2\0\3\16\1\0\1\205\3\0"+
    "\2\16\12\0\1\16\1\0\2\16\6\0\3\16\4\0"+
    "\3\16\1\206\6\16\1\0\3\16\2\0\3\16\1\0"+
    "\1\16\3\0\2\16\12\0\1\16\1\0\2\16\6\0"+
    "\3\16\4\0\3\16\1\207\6\16\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\4\16\1\210\5\16\1\0"+
    "\3\16\2\0\3\16\1\0\1\16\3\0\2\16\12\0"+
    "\1\16\1\0\2\16\6\0\3\16\4\0\12\16\1\0"+
    "\3\16\2\0\3\16\1\0\1\16\3\0\1\211\1\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\12\16"+
    "\1\0\3\16\2\0\1\16\1\212\1\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\5\16\1\213\4\16\1\0\3\16\2\0\3\16"+
    "\1\0\1\16\3\0\2\16\12\0\1\16\1\0\2\16"+
    "\6\0\3\16\4\0\5\16\1\214\4\16\1\0\3\16"+
    "\2\0\3\16\1\0\1\16\3\0\2\16\12\0\1\16"+
    "\1\0\2\16\6\0\3\16\4\0\10\16\1\215\1\16"+
    "\1\0\3\16\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\12\16"+
    "\1\0\3\16\2\0\3\16\1\0\1\216\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\3\16"+
    "\1\217\6\16\1\0\3\16\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\6\0\3\16"+
    "\4\0\5\16\1\220\4\16\1\0\3\16\2\0\3\16"+
    "\1\0\1\16\3\0\2\16\12\0\1\16\1\0\2\16"+
    "\6\0\3\16\4\0\11\16\1\221\1\0\3\16\2\0"+
    "\3\16\1\0\1\16\3\0\2\16\12\0\1\16\1\0"+
    "\2\16\6\0\3\16\4\0\12\16\1\0\2\16\1\222"+
    "\2\0\3\16\1\0\1\16\3\0\2\16\12\0\1\16"+
    "\1\0\2\16\6\0\3\16\4\0\6\16\1\223\3\16"+
    "\1\0\3\16\2\0\3\16\1\0\1\16\3\0\2\16"+
    "\12\0\1\16\1\0\2\16\6\0\3\16\4\0\3\16"+
    "\1\224\6\16\1\0\3\16\2\0\3\16\1\0\1\16"+
    "\3\0\2\16\12\0\1\16\1\0\2\16\5\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4982];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\4\0\1\1\1\11\2\1\3\11\11\1\1\11\3\1"+
    "\1\11\3\1\2\11\5\1\6\11\2\1\1\11\2\1"+
    "\3\11\1\1\1\11\3\1\2\11\1\1\1\11\2\1"+
    "\1\11\17\1\2\11\1\1\2\11\1\1\3\11\1\0"+
    "\1\1\1\0\74\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[148];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
	//our code:
	
	StringBuffer STR_BUFFER = new StringBuffer();
	boolean COMMENTS_OPEN = false;
	boolean STRING_OPEN = false;
	
	public int getLineNumber() { return yyline+1; }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Lexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 130) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) throws LexicalError {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new LexicalError(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  throws LexicalError {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Token next_token() throws java.io.IOException, LexicalError {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          zzR = false;
          break;
        case '\r':
          yyline++;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
          }
          break;
        default:
          zzR = false;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 62: 
          { return new Token(sym.CONTINUE,yyline+1);
          }
        case 63: break;
        case 44: 
          { return new Token(sym.NEW ,yyline+1);
          }
        case 64: break;
        case 37: 
          { yybegin(SL_COMMENTS);
          }
        case 65: break;
        case 23: 
          { return new Token(sym.SEMI ,yyline+1);
          }
        case 66: break;
        case 41: 
          { return new Token(sym.NEQUAL ,yyline+1);
          }
        case 67: break;
        case 25: 
          { STRING_OPEN=true;STR_BUFFER.delete(0,STR_BUFFER.length());
				   yybegin(STRING);
          }
        case 68: break;
        case 55: 
          { return new Token(sym.LENGTH,yyline+1);
          }
        case 69: break;
        case 43: 
          { return new Token(sym.LTE ,yyline+1);
          }
        case 70: break;
        case 45: 
          { return new Token(sym.INT,yyline+1);
          }
        case 71: break;
        case 22: 
          { return new Token(sym.RCBR ,yyline+1);
          }
        case 72: break;
        case 10: 
          { return new Token(sym.DIVIDE,yyline+1);
          }
        case 73: break;
        case 11: 
          { return new Token(sym.DOT,yyline+1);
          }
        case 74: break;
        case 32: 
          { STRING_OPEN=false;	
		yybegin(YYINITIAL); 
		return new Token(sym.QUOTE,"\"" + STR_BUFFER.toString() + "\"",yyline+1);
          }
        case 75: break;
        case 54: 
          { return new Token(sym.WHILE ,yyline+1);
          }
        case 76: break;
        case 24: 
          { return new Token(sym.CLASS_ID, yytext(), yyline+1);
          }
        case 77: break;
        case 21: 
          { return new Token(sym.RB ,yyline+1);
          }
        case 78: break;
        case 59: 
          { return new Token(sym.BOOLEAN,yyline+1);
          }
        case 79: break;
        case 20: 
          { return new Token(sym.PLUS ,yyline+1);
          }
        case 80: break;
        case 29: 
          { COMMENTS_OPEN= false; yybegin(YYINITIAL);
          }
        case 81: break;
        case 53: 
          { return new Token(sym.FALSE,yyline+1);
          }
        case 82: break;
        case 5: 
          { return new Token(sym.LP,yyline+1);
          }
        case 83: break;
        case 18: 
          { return new Token(sym.MOD ,yyline+1);
          }
        case 84: break;
        case 26: 
          { yybegin(YYINITIAL);
          }
        case 85: break;
        case 51: 
          { return new Token(sym.BREAK,yyline+1);
          }
        case 86: break;
        case 19: 
          { return new Token(sym.MULTIPLY ,yyline+1);
          }
        case 87: break;
        case 9: 
          { return new Token(sym.COMMA,yyline+1);
          }
        case 88: break;
        case 47: 
          { return new Token(sym.NULL ,yyline+1);
          }
        case 89: break;
        case 27: 
          { yybegin(COMMENTSEND);
          }
        case 90: break;
        case 8: 
          { return new Token(sym.ID, yytext(), yyline+1);
          }
        case 91: break;
        case 61: 
          { return new Token(sym.INTEGER,yyline+1);
          }
        case 92: break;
        case 16: 
          { return new Token(sym.LT ,yyline+1);
          }
        case 93: break;
        case 34: 
          { throw new LexicalError("Illegal number: strats with zero '"+ yytext()+"'",yyline+1);
          }
        case 94: break;
        case 7: 
          { return new Token(sym.ASSIGN,yyline+1);
          }
        case 95: break;
        case 56: 
          { return new Token(sym.RETURN ,yyline+1);
          }
        case 96: break;
        case 33: 
          { throw new LexicalError("Illegal identifier: '" + yytext()+"'", yyline+1);
          }
        case 97: break;
        case 14: 
          { return new Token(sym.LCBR,yyline+1);
          }
        case 98: break;
        case 6: 
          { return new Token(sym.RP,yyline+1);
          }
        case 99: break;
        case 60: 
          { return new Token(sym.EXTENDS,yyline+1);
          }
        case 100: break;
        case 30: 
          { throw new LexicalError("Illegal string token: '" + yytext()+"'", yyline+1);
          }
        case 101: break;
        case 42: 
          { return new Token(sym.LOR ,yyline+1);
          }
        case 102: break;
        case 2: 
          { throw new LexicalError("Lexical error: illegal token '"+yytext()+"'",yyline+1);
          }
        case 103: break;
        case 15: 
          { return new Token(sym.LNEG ,yyline+1);
          }
        case 104: break;
        case 57: 
          { return new Token(sym.STATIC ,yyline+1);
          }
        case 105: break;
        case 50: 
          { return new Token(sym.VOID ,yyline+1);
          }
        case 106: break;
        case 38: 
          { COMMENTS_OPEN= true; yybegin(COMMENTS);
          }
        case 107: break;
        case 58: 
          { return new Token(sym.STRING ,yyline+1);
          }
        case 108: break;
        case 28: 
          { yybegin(COMMENTS);
          }
        case 109: break;
        case 52: 
          { return new Token(sym.CLASS,yyline+1);
          }
        case 110: break;
        case 17: 
          { return new Token(sym.MINUS ,yyline+1);
          }
        case 111: break;
        case 46: 
          { return new Token(sym.ELSE,yyline+1);
          }
        case 112: break;
        case 39: 
          { return new Token(sym.GTE,yyline+1);
          }
        case 113: break;
        case 13: 
          { return new Token(sym.LB,yyline+1);
          }
        case 114: break;
        case 3: 
          { try {
		Integer.parseInt(yytext());
		return new Token (sym.INTEGER,Integer.parseInt(yytext()),yyline+1);
    }
	catch (NumberFormatException e) {
		throw new LexicalError( "Number '"+yytext()+ "' is out of range",yyline+1);
	}
          }
        case 115: break;
        case 31: 
          { throw new LexicalError("Lexical error: Quote string is left unclosed",yyline+1);
          }
        case 116: break;
        case 1: 
          { STR_BUFFER.append(yytext());
          }
        case 117: break;
        case 35: 
          { return new Token(sym.EQUAL ,yyline+1);
          }
        case 118: break;
        case 36: 
          { return new Token(sym.IF,yyline+1);
          }
        case 119: break;
        case 49: 
          { return new Token(sym.THIS ,yyline+1);
          }
        case 120: break;
        case 40: 
          { return new Token(sym.LAND,yyline+1);
          }
        case 121: break;
        case 48: 
          { return new Token(sym.TRUE ,yyline+1);
          }
        case 122: break;
        case 4: 
          { 
          }
        case 123: break;
        case 12: 
          { return new Token(sym.GT,yyline+1);
          }
        case 124: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { 	if (COMMENTS_OPEN) 
		throw new LexicalError("Lexical error: Comment '/*' left unclosed",yyline+1);
	if (STRING_OPEN) 
		throw new LexicalError("Lexical error: Quote string is left unclosed",yyline+1);
  	return new Token(sym.EOF,yyline+1);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
