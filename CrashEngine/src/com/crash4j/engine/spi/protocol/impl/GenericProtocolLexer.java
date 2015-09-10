/* The following code was generated by JFlex 1.4.3 on 9/9/15 9:14 PM */

/**
 * This is a test http parser
 */
package com.crash4j.engine.spi.protocol.impl;


import java.io.Reader;
import java.util.Stack;

import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.classtypes.HTTPTypes;
import com.crash4j.engine.types.classtypes.SMTPTypes;
import com.crash4j.engine.types.classtypes.FTPTypes;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 9/9/15 9:14 PM from the specification file
 * <tt>CrashEngine/src/com/crash4j/engine/spi/protocol/impl/protocols.lex</tt>
 */
public class GenericProtocolLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int HTTP_CLIENT_VERSION = 6;
  public static final int HTTP_CLIENT_METHOD = 2;
  public static final int YYINITIAL = 0;
  public static final int HTTP_CLIENT_URI = 4;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3, 3
  };

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = {
     0,  0,  0,  0,  0,  0,  0,  0,  0,  5,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     4, 35,  0, 42, 35, 37, 35, 35, 35, 35, 35, 41, 41, 29, 39, 36, 
    31,  2, 32,  2,  2, 30,  2,  2,  2,  2, 40, 35,  0,  6,  0, 38, 
    35, 15,  3, 21, 16,  8, 23,  7, 14, 17,  1, 24, 19, 22, 18, 11, 
    10, 27, 20, 12,  9, 13, 25, 33, 28, 26, 34,  0,  0,  0,  0, 35, 
     0, 15,  3, 21, 16,  8, 23,  7, 14, 17,  1, 24, 19, 22, 18, 11, 
    10, 27, 20, 12,  9, 13, 25, 33, 28, 26, 34,  0,  0,  0, 35,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
  };

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\24\0\2\2\1\3\1\2\1\0"+
    "\1\2\100\0\1\4\4\0\1\5\5\0\1\6\2\0"+
    "\1\7\20\0\1\10\15\0\1\11\3\0\1\12\4\0"+
    "\1\13\11\0\1\14\1\15\1\16\1\0\1\17\1\20"+
    "\1\0\1\21\1\0\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\36"+
    "\1\37\1\40\1\0\1\41\1\42\1\43\1\44\1\45"+
    "\1\46\1\47\3\0\1\50\1\51\1\0\1\52\1\53"+
    "\1\54\1\55\3\0\1\56\16\0\1\57\12\0\1\60"+
    "\1\0\1\61\1\62\10\0\1\63\2\0\1\64\1\0"+
    "\1\65\2\0\1\66\2\0\1\67\10\0\1\70\1\0"+
    "\1\71\4\0\1\72";

  private static int [] zzUnpackAction() {
    int [] result = new int[268];
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
    "\0\0\0\53\0\126\0\201\0\254\0\327\0\u0102\0\u012d"+
    "\0\u0158\0\u0183\0\u01ae\0\u01d9\0\u0204\0\u022f\0\u025a\0\u0285"+
    "\0\u02b0\0\u02db\0\u0306\0\u0331\0\u035c\0\u0387\0\u03b2\0\u03dd"+
    "\0\u0408\0\u0433\0\u045e\0\u0489\0\u04b4\0\u04df\0\u050a\0\u0535"+
    "\0\u0560\0\u058b\0\u05b6\0\u05e1\0\u060c\0\u0637\0\u0662\0\u068d"+
    "\0\u06b8\0\u06e3\0\u070e\0\u0739\0\u0764\0\u078f\0\u07ba\0\u07e5"+
    "\0\u0810\0\u083b\0\u0866\0\u0891\0\u08bc\0\u08e7\0\u0912\0\u093d"+
    "\0\u0968\0\u0993\0\u09be\0\u09e9\0\u0a14\0\u0a3f\0\u0a6a\0\u0a95"+
    "\0\u0ac0\0\u0aeb\0\u0b16\0\u0b41\0\u0b6c\0\u0b97\0\u0bc2\0\u0bed"+
    "\0\u0c18\0\u0c43\0\u0c6e\0\u0c99\0\u0cc4\0\u0cef\0\u0d1a\0\u0d45"+
    "\0\u0d70\0\u0d9b\0\u0dc6\0\u0df1\0\u0e1c\0\u0e47\0\u0e72\0\u0e9d"+
    "\0\u0ec8\0\u0ef3\0\u0f1e\0\u0f49\0\u0f74\0\u0f9f\0\u0fca\0\u0ff5"+
    "\0\u1020\0\u104b\0\u0f9f\0\u1076\0\u10a1\0\u10cc\0\u10f7\0\u1122"+
    "\0\u0f9f\0\u114d\0\u1178\0\u0f9f\0\u11a3\0\u11ce\0\u11f9\0\u1224"+
    "\0\u124f\0\u127a\0\u12a5\0\u12d0\0\u12fb\0\u1326\0\u1351\0\u137c"+
    "\0\u13a7\0\u13d2\0\u13fd\0\u1428\0\u0f9f\0\u1453\0\u147e\0\u14a9"+
    "\0\u14d4\0\u14ff\0\u152a\0\u1555\0\u1580\0\u15ab\0\u15d6\0\u1601"+
    "\0\u162c\0\u1657\0\u0f9f\0\u1682\0\u16ad\0\u16d8\0\u0f9f\0\u1703"+
    "\0\u172e\0\u1759\0\u1784\0\u0f9f\0\u17af\0\u17da\0\u1805\0\u1830"+
    "\0\u185b\0\u1886\0\u18b1\0\u18dc\0\u1907\0\u0f9f\0\u0f9f\0\u0f9f"+
    "\0\u1932\0\u0f9f\0\u0f9f\0\u195d\0\u0f9f\0\u1988\0\u0f9f\0\u0f9f"+
    "\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f"+
    "\0\u0f9f\0\u0f9f\0\u19b3\0\u0f9f\0\u0f9f\0\u19de\0\u0f9f\0\u0f9f"+
    "\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f\0\u1a09\0\u1a34\0\u1a5f"+
    "\0\u0f9f\0\u0f9f\0\u1a8a\0\u0f9f\0\u0f9f\0\u0f9f\0\u0f9f\0\u1ab5"+
    "\0\u1ae0\0\u1b0b\0\u0f9f\0\u1b36\0\u1b61\0\u1b8c\0\u1bb7\0\u1be2"+
    "\0\u1c0d\0\u1c38\0\u1c63\0\u1c8e\0\u1cb9\0\u1ce4\0\u1d0f\0\u1d3a"+
    "\0\u1d65\0\u0f9f\0\u1d90\0\u1dbb\0\u1de6\0\u1e11\0\u1e3c\0\u1e67"+
    "\0\u1e92\0\u1ebd\0\u1ee8\0\u1f13\0\u0f9f\0\u1f3e\0\u0f9f\0\u0f9f"+
    "\0\u1f69\0\u1f94\0\u1fbf\0\u1fea\0\u2015\0\u2040\0\u206b\0\u2096"+
    "\0\u0f9f\0\u20c1\0\u20ec\0\u0f9f\0\u2117\0\u0f9f\0\u2142\0\u216d"+
    "\0\u0f9f\0\u2198\0\u21c3\0\u0f9f\0\u21ee\0\u2219\0\u2244\0\u226f"+
    "\0\u229a\0\u22c5\0\u22f0\0\u231b\0\u0f9f\0\u2346\0\u0f9f\0\u2371"+
    "\0\u239c\0\u23c7\0\u23f2\0\u0f9f";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[268];
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
    "\7\0\1\5\1\6\1\7\1\10\1\11\1\12\1\13"+
    "\1\14\1\15\1\16\1\0\1\17\1\20\1\21\1\22"+
    "\1\23\1\24\1\0\1\25\1\0\1\26\1\27\22\0"+
    "\2\2\46\0\1\30\1\31\1\30\2\32\1\31\26\30"+
    "\4\31\2\30\1\31\1\33\1\34\1\0\1\31\1\0"+
    "\1\31\1\35\16\0\1\36\44\0\1\37\53\0\1\40"+
    "\1\41\3\0\1\42\3\0\1\43\45\0\1\44\6\0"+
    "\1\45\5\0\1\46\23\0\1\47\7\0\1\50\1\0"+
    "\1\51\1\0\1\52\1\0\1\53\2\0\1\54\14\0"+
    "\1\55\23\0\1\56\51\0\1\57\7\0\1\60\4\0"+
    "\1\61\3\0\1\62\34\0\1\63\46\0\1\64\45\0"+
    "\1\65\5\0\1\66\1\67\2\0\1\70\2\0\1\71"+
    "\2\0\1\72\1\0\1\73\35\0\1\74\3\0\1\75"+
    "\2\0\1\76\46\0\1\77\7\0\1\100\41\0\1\41"+
    "\1\101\3\0\1\102\1\0\1\103\41\0\1\104\3\0"+
    "\1\105\5\0\1\106\2\0\1\107\1\110\37\0\1\111"+
    "\2\0\1\112\1\0\1\113\4\0\1\43\13\0\1\114"+
    "\24\0\1\115\3\0\1\116\1\117\1\43\1\0\1\120"+
    "\4\0\1\121\32\0\1\71\66\0\1\122\43\0\1\123"+
    "\47\0\1\124\1\0\1\125\7\0\1\126\1\113\1\127"+
    "\6\0\1\130\16\0\3\30\2\0\1\31\34\30\1\31"+
    "\1\33\1\34\1\33\1\30\1\131\1\30\1\35\1\0"+
    "\3\31\2\0\36\31\1\33\1\34\1\33\1\31\1\0"+
    "\1\31\1\35\4\0\2\32\46\0\3\33\2\0\37\33"+
    "\1\132\4\33\1\35\2\0\2\133\4\0\1\133\6\0"+
    "\2\133\4\0\1\133\1\0\1\133\6\0\3\133\13\0"+
    "\3\35\2\0\37\35\1\134\4\35\12\0\1\135\52\0"+
    "\1\136\65\0\1\137\42\0\1\140\7\0\1\141\51\0"+
    "\1\142\54\0\1\143\51\0\1\144\45\0\1\145\45\0"+
    "\1\146\54\0\1\147\52\0\1\150\7\0\1\141\37\0"+
    "\1\151\55\0\1\152\50\0\1\153\53\0\1\141\57\0"+
    "\1\154\43\0\1\155\54\0\1\156\3\0\1\157\4\0"+
    "\1\160\37\0\1\161\63\0\1\162\44\0\1\163\46\0"+
    "\1\164\61\0\1\165\3\0\1\166\42\0\1\167\63\0"+
    "\1\170\40\0\1\171\51\0\1\172\60\0\1\141\56\0"+
    "\1\173\54\0\1\141\50\0\1\174\51\0\1\175\41\0"+
    "\1\176\54\0\1\177\53\0\1\200\45\0\1\201\65\0"+
    "\1\202\44\0\1\203\47\0\1\204\2\0\1\205\4\0"+
    "\1\206\41\0\1\207\53\0\1\210\15\0\1\211\35\0"+
    "\1\212\60\0\1\213\54\0\1\214\45\0\1\215\52\0"+
    "\1\216\55\0\1\217\52\0\1\220\53\0\1\221\42\0"+
    "\1\222\55\0\1\223\56\0\1\224\61\0\1\225\44\0"+
    "\1\226\72\0\1\55\21\0\1\227\56\0\1\230\10\0"+
    "\1\177\1\110\54\0\1\121\32\0\1\231\12\0\1\232"+
    "\30\0\3\33\2\0\37\33\1\132\4\33\3\0\2\233"+
    "\4\0\1\233\6\0\2\233\4\0\1\233\1\0\1\233"+
    "\6\0\3\233\14\0\2\31\4\0\1\31\6\0\2\31"+
    "\4\0\1\31\1\0\1\31\6\0\3\31\14\0\2\234"+
    "\4\0\1\234\6\0\2\234\4\0\1\234\1\0\1\234"+
    "\6\0\3\234\23\0\1\235\136\0\1\236\61\0\1\143"+
    "\32\0\1\143\54\0\1\237\61\0\1\240\55\0\1\241"+
    "\35\0\1\242\104\0\1\143\21\0\1\243\55\0\1\143"+
    "\14\0\1\143\31\0\1\244\56\0\1\245\4\0\1\246"+
    "\46\0\1\247\6\0\1\250\37\0\1\251\56\0\1\252"+
    "\45\0\1\253\53\0\1\254\52\0\1\255\65\0\1\143"+
    "\46\0\1\256\44\0\1\257\1\260\63\0\1\261\50\0"+
    "\1\262\40\0\1\143\60\0\1\143\47\0\1\143\47\0"+
    "\1\263\61\0\1\264\45\0\1\143\51\0\1\265\62\0"+
    "\1\266\40\0\1\143\54\0\1\267\65\0\1\270\37\0"+
    "\1\271\63\0\1\272\41\0\1\273\54\0\1\274\63\0"+
    "\1\275\37\0\1\276\63\0\1\277\4\0\1\143\45\0"+
    "\1\300\42\0\1\301\50\0\1\302\65\0\1\303\55\0"+
    "\1\143\35\0\1\304\6\0\1\305\64\0\1\306\31\0"+
    "\1\307\63\0\1\143\3\0\1\143\57\0\1\143\53\0"+
    "\1\310\37\0\1\311\33\0\2\33\4\0\1\33\6\0"+
    "\2\33\4\0\1\33\1\0\1\33\6\0\3\33\14\0"+
    "\2\35\4\0\1\35\6\0\2\35\4\0\1\35\1\0"+
    "\1\35\6\0\3\35\24\0\1\312\50\0\1\313\65\0"+
    "\1\314\42\0\1\315\50\0\1\316\63\0\1\317\34\0"+
    "\1\320\56\0\1\321\72\0\1\322\26\0\1\323\60\0"+
    "\1\324\12\0\1\325\47\0\1\326\74\0\1\327\27\0"+
    "\1\330\53\0\1\331\40\0\1\332\50\0\1\333\55\0"+
    "\1\334\66\0\1\335\46\0\1\336\60\0\1\337\37\0"+
    "\1\340\54\0\1\341\64\0\1\342\24\0\1\343\33\0"+
    "\3\343\34\0\1\344\44\0\1\345\72\0\1\346\31\0"+
    "\1\347\50\0\1\350\63\0\1\351\54\0\1\352\32\0"+
    "\1\353\104\0\1\354\54\0\1\355\61\0\1\356\24\0"+
    "\1\357\66\0\1\360\24\0\1\361\56\0\1\362\46\0"+
    "\1\363\102\0\1\364\27\0\1\365\40\0\1\366\33\0"+
    "\3\366\34\0\1\367\40\0\1\370\70\0\1\371\40\0"+
    "\1\372\47\0\1\373\50\0\1\374\77\0\1\375\32\0"+
    "\1\376\55\0\1\377\45\0\1\u0100\57\0\1\u0101\44\0"+
    "\1\u0102\55\0\1\u0103\50\0\1\u0104\50\0\1\u0105\50\0"+
    "\1\u0106\65\0\1\u0107\54\0\1\u0108\42\0\1\u0109\46\0"+
    "\1\u010a\64\0\1\u010b\53\0\1\u010c\30\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[9245];
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
    "\1\0\2\1\24\0\4\1\1\0\1\1\100\0\1\11"+
    "\4\0\1\11\5\0\1\11\2\0\1\11\20\0\1\11"+
    "\15\0\1\11\3\0\1\11\4\0\1\11\11\0\3\11"+
    "\1\0\2\11\1\0\1\11\1\0\14\11\1\1\2\11"+
    "\1\0\7\11\3\0\2\11\1\0\4\11\3\0\1\11"+
    "\16\0\1\11\12\0\1\11\1\0\2\11\10\0\1\11"+
    "\2\0\1\11\1\0\1\11\2\0\1\11\2\0\1\11"+
    "\10\0\1\11\1\0\1\11\4\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[268];
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

  /** the stack of open (nested) input streams to read from */
  private java.util.Stack zzStreams = new java.util.Stack();

  /**
   * inner class used to store info for nested
   * input streams
   */
  private static final class ZzFlexStreamInfo {
    java.io.Reader zzReader;
    int zzEndRead;
    int zzStartRead;
    int zzCurrentPos;
    int zzMarkedPos;
    int yyline;
    int yycolumn;
    char [] zzBuffer;
    boolean zzAtEOF;
    boolean zzEOFDone;

    /** sets all values stored in this class */
    ZzFlexStreamInfo(java.io.Reader zzReader, int zzEndRead, int zzStartRead,
                  int zzCurrentPos, int zzMarkedPos, 
                  char [] zzBuffer, boolean zzAtEOF, int yyline, int yycolumn) {
      this.zzReader      = zzReader;
      this.zzEndRead     = zzEndRead;
      this.zzStartRead   = zzStartRead;
      this.zzCurrentPos  = zzCurrentPos;
      this.zzMarkedPos   = zzMarkedPos;
      this.zzBuffer      = zzBuffer;
      this.zzAtEOF       = zzAtEOF;
      this.zzEOFDone     = zzEOFDone;
      this.yyline         = yyline;
      this.yycolumn       = yycolumn;
    }
  }

  /* user code: */
	/**
	 * Assigns action class. 
	 */
	private ActionClasses actionClass = null;
	/**
	 * ClassType assigned.
	 */
	private Enum<?> classType = null;
	
	private Stack<String> extractions = new Stack<String>();

	/**
	 * Return extracted data as related to ActionClasses and ActionClassTypes.
	 */
	public Stack<String> getExtractions()
	{
		return extractions;
	}
	
	/**
	 * Detected protocol Event
	 */
	public ActionClasses getActionClass()
	{
		return actionClass;
	}	

	/**
	 * Detected protocol Event
	 */
	public Enum<?> getActionClassType()
	{
		return classType;
	}	
		
	public boolean hasProtocol()
	{
		return (this.classType != null && this.actionClass != null);
	}
	
	/**
	 * Reset and point to a new stream to be consumed.
	 */
	public void resetLexer(Reader r)
	{
		actionClass = null;
		classType = null;
		extractions.clear();
		yyreset(r);
	}


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public GenericProtocolLexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public GenericProtocolLexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
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
    
    // numRead < 0) 
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
   * Stores the current input stream on a stack, and
   * reads from a new stream. Lexical state, line,
   * char, and column counting remain untouched.
   *
   * The current input stream can be restored with
   * yypopstream (usually in an <<EOF>> action).
   *
   * @param reader the new input stream to read from
   *
   * @see #yypopStream()
   */
  public final void yypushStream(java.io.Reader reader) {
    zzStreams.push(
      new ZzFlexStreamInfo(zzReader, zzEndRead, zzStartRead, zzCurrentPos,
                        zzMarkedPos, zzBuffer, zzAtEOF,
                        yyline, yycolumn)
    );
    zzAtEOF  = false;
    zzBuffer = new char[ZZ_BUFFERSIZE];
    zzReader = reader;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yycolumn = 0;
  }
    

  /**
   * Closes the current input stream and continues to
   * read from the one on top of the stream stack. 
   *
   * @throws java.util.EmptyStackException
   *         if there is no further stream to read from.
   *
   * @throws java.io.IOException
   *         if there was an error in closing the stream.
   *
   * @see #yypushStream(java.io.Reader)
   */
  public final void yypopStream() throws java.io.IOException {
    zzReader.close();
    ZzFlexStreamInfo s = (ZzFlexStreamInfo) zzStreams.pop();
    zzBuffer      = s.zzBuffer;
    zzReader      = s.zzReader;
    zzEndRead     = s.zzEndRead;
    zzStartRead   = s.zzStartRead;
    zzCurrentPos  = s.zzCurrentPos;
    zzMarkedPos   = s.zzMarkedPos ;
    zzAtEOF       = s.zzAtEOF;
    zzEOFDone     = s.zzEOFDone;
    yyline         = s.yyline;
    yycolumn       = s.yycolumn;
  }


  /**
   * Returns true iff there are still streams left 
   * to read from on the stream stack.
   */
  public final boolean yymoreStreams() {
    return !zzStreams.isEmpty();
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
   *
   * @see #yypushStream(java.io.Reader)
   * @see #yypopStream()
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
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Yytoken scan() throws java.io.IOException {
    int zzInput;
    int zzAction;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      // cached fields:
      int zzCurrentPosL;
      int zzMarkedPosL = zzMarkedPos;
      int zzEndReadL = zzEndRead;
      char [] zzBufferL = zzBuffer;
      char [] zzCMapL = ZZ_CMAP;

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
        case 4: 
          { classType = HTTPTypes.GET;
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 59: break;
        case 34: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RETR;
	      return null;
          }
        case 60: break;
        case 48: 
          { classType = HTTPTypes.OPTIONS;		
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 61: break;
        case 19: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STOR;
	      return null;
          }
        case 62: break;
        case 39: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RNFR;
	      return null;
          }
        case 63: break;
        case 41: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MODE;
	      return null;
          }
        case 64: break;
        case 20: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STAT;
	      return null;
          }
        case 65: break;
        case 44: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.VRFY;
		return null;
          }
        case 66: break;
        case 35: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.REST;
	      return null;
          }
        case 67: break;
        case 16: 
          { classType = HTTPTypes.POST;	
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 68: break;
        case 49: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.RCPTTO;
		return null;
          }
        case 69: break;
        case 25: 
          { classType = HTTPTypes.HEAD;		
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 70: break;
        case 5: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
          }
        case 71: break;
        case 55: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.PIPELINING;
		return null;
          }
        case 72: break;
        case 28: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.ABOR;
	      return null;
          }
        case 73: break;
        case 29: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.ATRN;
		return null;
          }
        case 74: break;
        case 52: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XEXCH50;
		return null;
          }
        case 75: break;
        case 10: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.CWD;
	      return null;
          }
        case 76: break;
        case 56: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XLINK2STATE;
		return null;
          }
        case 77: break;
        case 18: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STOU;
	      return null;
          }
        case 78: break;
        case 33: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.LIST;
	      return null;
          }
        case 79: break;
        case 21: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STRU;
	      return null;
          }
        case 80: break;
        case 6: 
          { classType = HTTPTypes.PUT;		
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 81: break;
        case 3: 
          { yybegin(HTTP_CLIENT_VERSION);
          }
        case 82: break;
        case 17: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.OPTS;
	      return null;
          }
        case 83: break;
        case 2: 
          { extractions.push(yytext());
          }
        case 84: break;
        case 54: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.MAILFROM;
		return null;
          }
        case 85: break;
        case 7: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.PWD;
	      return null;
          }
        case 86: break;
        case 43: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MLSD;
	      return null;
          }
        case 87: break;
        case 58: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XEXPS;
		return null;
          }
        case 88: break;
        case 24: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.SYST;
	      return null;
          }
        case 89: break;
        case 42: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MLST;
	      return null;
          }
        case 90: break;
        case 30: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.DELE;
	      return null;
          }
        case 91: break;
        case 23: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.SMNT;
	      return null;
          }
        case 92: break;
        case 31: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.DATA;
		return null;
          }
        case 93: break;
        case 11: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MKD;
	      return null;
          }
        case 94: break;
        case 40: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.CDUP;
	      return null;
          }
        case 95: break;
        case 22: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.SITE;
	      return null;
          }
        case 96: break;
        case 14: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.TURN;
		return null;
          }
        case 97: break;
        case 36: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.REIN;
	      return null;
          }
        case 98: break;
        case 26: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.HELP;
		return null;
          }
        case 99: break;
        case 47: 
          { classType = HTTPTypes.DELETE;		
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 100: break;
        case 12: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.ETRN;
		return null;
          }
        case 101: break;
        case 57: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XEXPSGSSAPI;
		return null;
          }
        case 102: break;
        case 9: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RMD;
	      return null;
          }
        case 103: break;
        case 45: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.QUIT;
		return null;
          }
        case 104: break;
        case 37: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.RSET;
		return null;
          }
        case 105: break;
        case 8: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.DSN;
		return null;
          }
        case 106: break;
        case 1: 
          { yybegin(HTTP_CLIENT_URI);
          }
        case 107: break;
        case 46: 
          { classType = HTTPTypes.TRACE;		
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 108: break;
        case 15: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.TYPE;
	      return null;
          }
        case 109: break;
        case 27: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.HELO;
		return null;
          }
        case 110: break;
        case 32: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.NLST;
	      return null;
          }
        case 111: break;
        case 53: 
          { actionClass = ActionClasses.HTTP;	
		extractions.push(yytext());
		yybegin(YYINITIAL);
		return null;
          }
        case 112: break;
        case 51: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.CHUNKING;
		return null;
          }
        case 113: break;
        case 13: 
          { actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.EHLO;
		return null;
          }
        case 114: break;
        case 50: 
          { classType = HTTPTypes.CONNECT;		
		yybegin(HTTP_CLIENT_METHOD);
          }
        case 115: break;
        case 38: 
          { actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RNTO;
	      return null;
          }
        case 116: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            return null;
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
