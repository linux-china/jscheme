/*
 * @author Peter Norvig, peter@norvig.com http://www.norvig.com
 * Copyright 1998 Peter Norvig, see http://www.norvig.com/license.html
 */
package jscheme;

import java.io.*;
import java.util.Arrays;

import static jscheme.SchemeUtils.*;

/**
 * A primitive is a procedure that is defined as part of the Scheme report,
 * and is implemented in Java code.
 *
 * @author Peter Norvig, peter@norvig.com http://www.norvig.com
 * Copyright 1998 Peter Norvig, see http://www.norvig.com/license.html  *
 */

@SuppressWarnings("SuspiciousNameCombination")
public class Primitive extends Procedure {
    int minArgs;
    int maxArgs;
    int idNumber;

    public Primitive(int id, int minArgs, int maxArgs) {
        this.idNumber = id;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    private static final int EQ = 0, LT = 1, GT = 2, GE = 3, LE = 4,
            ABS = 5, EOF_OBJECT = 6, EQQ = 7, EQUALQ = 8, FORCE = 9,
            CAR = 10, FLOOR = 11, CEILING = 12, CONS = 13,
            DIVIDE = 14, LENGTH = 15, LIST = 16, LISTQ = 17, APPLY = 18,
            MAX = 19, MIN = 20, MINUS = 21, NEWLINE = 22,
            NOT = 23, NULLQ = 24, NUMBERQ = 25, PAIRQ = 26, PLUS = 27,
            PROCEDUREQ = 28, READ = 29, CDR = 30, ROUND = 31, SECOND = 32,
            SYMBOLQ = 33, TIMES = 34, TRUNCATE = 35, WRITE = 36, APPEND = 37,
            BOOLEANQ = 38, SQRT = 39, EXPT = 40, REVERSE = 41, ASSOC = 42,
            ASSQ = 43, ASSV = 44, MEMBER = 45, MEMQ = 46, MEMV = 47, EQVQ = 48,
            LISTREF = 49, LISTTAIL = 50, STRINQ = 51, MAKESTRING = 52, STRING = 53,
            STRINGLENGTH = 54, STRINGREF = 55, STRINGSET = 56, SUBSTRING = 57,
            STRINGAPPEND = 58, STRINGTOLIST = 59, LISTTOSTRING = 60,
            SYMBOLTOSTRING = 61, STRINGTOSYMBOL = 62, EXP = 63, LOG = 64, SIN = 65,
            COS = 66, TAN = 67, ACOS = 68, ASIN = 69, ATAN = 70,
            NUMBERTOSTRING = 71, STRINGTONUMBER = 72, CHARQ = 73,
            CHARALPHABETICQ = 74, CHARNUMERICQ = 75, CHARWHITESPACEQ = 76,
            CHARUPPERCASEQ = 77, CHARLOWERCASEQ = 78, CHARTOINTEGER = 79,
            INTEGERTOCHAR = 80, CHARUPCASE = 81, CHARDOWNCASE = 82, STRINGQ = 83,
            VECTORQ = 84, MAKEVECTOR = 85, VECTOR = 86, VECTORLENGTH = 87,
            VECTORREF = 88, VECTORSET = 89, LISTTOVECTOR = 90, MAP = 91,
            FOREACH = 92, CALLCC = 93, VECTORTOLIST = 94, LOAD = 95, DISPLAY = 96,
            INPUTPORTQ = 98, CURRENTINPUTPORT = 99, OPENINPUTFILE = 100,
            CLOSEINPUTPORT = 101, OUTPUTPORTQ = 103, CURRENTOUTPUTPORT = 104,
            OPENOUTPUTFILE = 105, CLOSEOUTPUTPORT = 106, READCHAR = 107,
            PEEKCHAR = 108, EVAL = 109, QUOTIENT = 110, REMAINDER = 111,
            MODULO = 112, THIRD = 113, EOFOBJECTQ = 114, GCD = 115, LCM = 116,
            CXR = 117, ODDQ = 118, EVENQ = 119, ZEROQ = 120, POSITIVEQ = 121,
            NEGATIVEQ = 122,
            CHARCMP = 123 /* to 127 */, CHARCICMP = 128 /* to 132 */,
            STRINGCMP = 133 /* to 137 */, STRINGCICMP = 138 /* to 142 */,
            EXACTQ = 143, INEXACTQ = 144, INTEGERQ = 145,
            CALLWITHINPUTFILE = 146, CALLWITHOUTPUTFILE = 147;

    //////////////// Extensions ////////////////

    static final int NEW = -1, CLASS = -2, METHOD = -3, EXIT = -4,
            SETCAR = -5, SETCDR = -6, TIMECALL = -11, MACROEXPAND = -12,
            ERROR = -13, LISTSTAR = -14;


    public static void installPrimitives(Environment env) {
        int n = Integer.MAX_VALUE;
        env
                .defPrim("*", TIMES, 0, n)
                .defPrim("*", TIMES, 0, n)
                .defPrim("+", PLUS, 0, n)
                .defPrim("-", MINUS, 1, n)
                .defPrim("/", DIVIDE, 1, n)
                .defPrim("<", LT, 2, n)
                .defPrim("<=", LE, 2, n)
                .defPrim("=", EQ, 2, n)
                .defPrim(">", GT, 2, n)
                .defPrim(">=", GE, 2, n)
                .defPrim("abs", ABS, 1)
                .defPrim("acos", ACOS, 1)
                .defPrim("append", APPEND, 0, n)
                .defPrim("apply", APPLY, 2, n)
                .defPrim("asin", ASIN, 1)
                .defPrim("assoc", ASSOC, 2)
                .defPrim("assq", ASSQ, 2)
                .defPrim("assv", ASSV, 2)
                .defPrim("atan", ATAN, 1)
                .defPrim("boolean?", BOOLEANQ, 1)
                .defPrim("caaaar", CXR, 1)
                .defPrim("caaadr", CXR, 1)
                .defPrim("caaar", CXR, 1)
                .defPrim("caadar", CXR, 1)
                .defPrim("caaddr", CXR, 1)
                .defPrim("caadr", CXR, 1)
                .defPrim("caar", CXR, 1)
                .defPrim("cadaar", CXR, 1)
                .defPrim("cadadr", CXR, 1)
                .defPrim("cadar", CXR, 1)
                .defPrim("caddar", CXR, 1)
                .defPrim("cadddr", CXR, 1)
                .defPrim("caddr", THIRD, 1)
                .defPrim("cadr", SECOND, 1)
                .defPrim("call-with-current-continuation", CALLCC, 1)
                .defPrim("call-with-input-file", CALLWITHINPUTFILE, 2)
                .defPrim("call-with-output-file", CALLWITHOUTPUTFILE, 2)
                .defPrim("car", CAR, 1)
                .defPrim("cdaaar", CXR, 1)
                .defPrim("cdaadr", CXR, 1)
                .defPrim("cdaar", CXR, 1)
                .defPrim("cdadar", CXR, 1)
                .defPrim("cdaddr", CXR, 1)
                .defPrim("cdadr", CXR, 1)
                .defPrim("cdar", CXR, 1)
                .defPrim("cddaar", CXR, 1)
                .defPrim("cddadr", CXR, 1)
                .defPrim("cddar", CXR, 1)
                .defPrim("cdddar", CXR, 1)
                .defPrim("cddddr", CXR, 1)
                .defPrim("cdddr", CXR, 1)
                .defPrim("cddr", CXR, 1)
                .defPrim("cdr", CDR, 1)
                .defPrim("char->integer", CHARTOINTEGER, 1)
                .defPrim("char-alphabetic?", CHARALPHABETICQ, 1)
                .defPrim("char-ci<=?", CHARCICMP + LE, 2)
                .defPrim("char-ci<?", CHARCICMP + LT, 2)
                .defPrim("char-ci=?", CHARCICMP + EQ, 2)
                .defPrim("char-ci>=?", CHARCICMP + GE, 2)
                .defPrim("char-ci>?", CHARCICMP + GT, 2)
                .defPrim("char-downcase", CHARDOWNCASE, 1)
                .defPrim("char-lower-case?", CHARLOWERCASEQ, 1)
                .defPrim("char-numeric?", CHARNUMERICQ, 1)
                .defPrim("char-upcase", CHARUPCASE, 1)
                .defPrim("char-upper-case?", CHARUPPERCASEQ, 1)
                .defPrim("char-whitespace?", CHARWHITESPACEQ, 1)
                .defPrim("char<=?", CHARCMP + LE, 2)
                .defPrim("char<?", CHARCMP + LT, 2)
                .defPrim("char=?", CHARCMP + EQ, 2)
                .defPrim("char>=?", CHARCMP + GE, 2)
                .defPrim("char>?", CHARCMP + GT, 2)
                .defPrim("char?", CHARQ, 1)
                .defPrim("close-input-port", CLOSEINPUTPORT, 1)
                .defPrim("close-output-port", CLOSEOUTPUTPORT, 1)
                .defPrim("complex?", NUMBERQ, 1)
                .defPrim("cons", CONS, 2)
                .defPrim("cos", COS, 1)
                .defPrim("current-input-port", CURRENTINPUTPORT, 0)
                .defPrim("current-output-port", CURRENTOUTPUTPORT, 0)
                .defPrim("display", DISPLAY, 1, 2)
                .defPrim("eof-object?", EOFOBJECTQ, 1)
                .defPrim("eq?", EQQ, 2)
                .defPrim("equal?", EQUALQ, 2)
                .defPrim("eqv?", EQVQ, 2)
                .defPrim("eval", EVAL, 1, 2)
                .defPrim("even?", EVENQ, 1)
                .defPrim("exact?", INTEGERQ, 1)
                .defPrim("exp", EXP, 1)
                .defPrim("expt", EXPT, 2)
                .defPrim("force", FORCE, 1)
                .defPrim("for-each", FOREACH, 1, n)
                .defPrim("gcd", GCD, 0, n)
                .defPrim("inexact?", INEXACTQ, 1)
                .defPrim("input-port?", INPUTPORTQ, 1)
                .defPrim("integer->char", INTEGERTOCHAR, 1)
                .defPrim("integer?", INTEGERQ, 1)
                .defPrim("lcm", LCM, 0, n)
                .defPrim("length", LENGTH, 1)
                .defPrim("list", LIST, 0, n)
                .defPrim("list->string", LISTTOSTRING, 1)
                .defPrim("list->vector", LISTTOVECTOR, 1)
                .defPrim("list-ref", LISTREF, 2)
                .defPrim("list-tail", LISTTAIL, 2)
                .defPrim("list?", LISTQ, 1)
                .defPrim("load", LOAD, 1)
                .defPrim("log", LOG, 1)
                .defPrim("macro-expand", MACROEXPAND, 1)
                .defPrim("make-string", MAKESTRING, 1, 2)
                .defPrim("make-vector", MAKEVECTOR, 1, 2)
                .defPrim("map", MAP, 1, n)
                .defPrim("max", MAX, 1, n)
                .defPrim("member", MEMBER, 2)
                .defPrim("memq", MEMQ, 2)
                .defPrim("memv", MEMV, 2)
                .defPrim("min", MIN, 1, n)
                .defPrim("modulo", MODULO, 2)
                .defPrim("negative?", NEGATIVEQ, 1)
                .defPrim("newline", NEWLINE, 0, 1)
                .defPrim("not", NOT, 1)
                .defPrim("null?", NULLQ, 1)
                .defPrim("number->string", NUMBERTOSTRING, 1, 2)
                .defPrim("number?", NUMBERQ, 1)
                .defPrim("odd?", ODDQ, 1)
                .defPrim("open-input-file", OPENINPUTFILE, 1)
                .defPrim("open-output-file", OPENOUTPUTFILE, 1)
                .defPrim("output-port?", OUTPUTPORTQ, 1)
                .defPrim("pair?", PAIRQ, 1)
                .defPrim("peek-char", PEEKCHAR, 0, 1)
                .defPrim("positive?", POSITIVEQ, 1)
                .defPrim("procedure?", PROCEDUREQ, 1)
                .defPrim("quotient", QUOTIENT, 2)
                .defPrim("rational?", INTEGERQ, 1)
                .defPrim("read", READ, 0, 1)
                .defPrim("read-char", READCHAR, 0, 1)
                .defPrim("real?", NUMBERQ, 1)
                .defPrim("remainder", REMAINDER, 2)
                .defPrim("reverse", REVERSE, 1)
                .defPrim("round", ROUND, 1)
                .defPrim("set-car!", SETCAR, 2)
                .defPrim("set-cdr!", SETCDR, 2)
                .defPrim("sin", SIN, 1)
                .defPrim("sqrt", SQRT, 1)
                .defPrim("string", STRING, 0, n)
                .defPrim("string->list", STRINGTOLIST, 1)
                .defPrim("string->number", STRINGTONUMBER, 1, 2)
                .defPrim("string->symbol", STRINGTOSYMBOL, 1)
                .defPrim("string-append", STRINGAPPEND, 0, n)
                .defPrim("string-ci<=?", STRINGCICMP + LE, 2)
                .defPrim("string-ci<?", STRINGCICMP + LT, 2)
                .defPrim("string-ci=?", STRINGCICMP + EQ, 2)
                .defPrim("string-ci>=?", STRINGCICMP + GE, 2)
                .defPrim("string-ci>?", STRINGCICMP + GT, 2)
                .defPrim("string-length", STRINGLENGTH, 1)
                .defPrim("string-ref", STRINGREF, 2)
                .defPrim("string-set!", STRINGSET, 3)
                .defPrim("string<=?", STRINGCMP + LE, 2)
                .defPrim("string<?", STRINGCMP + LT, 2)
                .defPrim("string=?", STRINGCMP + EQ, 2)
                .defPrim("string>=?", STRINGCMP + GE, 2)
                .defPrim("string>?", STRINGCMP + GT, 2)
                .defPrim("string?", STRINGQ, 1)
                .defPrim("substring", SUBSTRING, 3)
                .defPrim("symbol->string", SYMBOLTOSTRING, 1)
                .defPrim("symbol?", SYMBOLQ, 1)
                .defPrim("tan", TAN, 1)
                .defPrim("vector", VECTOR, 0, n)
                .defPrim("vector->list", VECTORTOLIST, 1)
                .defPrim("vector-length", VECTORLENGTH, 1)
                .defPrim("vector-ref", VECTORREF, 2)
                .defPrim("vector-set!", VECTORSET, 3)
                .defPrim("vector?", VECTORQ, 1)
                .defPrim("write", WRITE, 1, 2)
                .defPrim("write-char", DISPLAY, 1, 2)
                .defPrim("zero?", ZEROQ, 1)

                ///////////// Extensions ////////////////

                .defPrim("new", NEW, 1)
                .defPrim("class", CLASS, 1)
                .defPrim("method", METHOD, 2, n)
                .defPrim("exit", EXIT, 0, 1)
                .defPrim("error", ERROR, 0, n)
                .defPrim("time-call", TIMECALL, 1, 2)
                .defPrim("_list*", LISTSTAR, 0, n);

    }

    /**
     * Apply a primitive to a list of arguments. *
     */
    public Object apply(Scheme interp, Object args) {
        //First make sure there are the right number of arguments.
        int nArgs = length(args);
        if (nArgs < minArgs)
            return error("too few args, " + nArgs +
                    ", for " + this.name + ": " + args);
        else if (nArgs > maxArgs)
            return error("too many args, " + nArgs +
                    ", for " + this.name + ": " + args);

        Object arg1 = first(args);
        Object arg2 = second(args);

        switch (idNumber) {

            ////////////////  SECTION 6.1 BOOLEANS
            case NOT:
                return truth(arg1 == FALSE);
            case BOOLEANQ:
                return truth(arg1 == TRUE || arg1 == FALSE);

            ////////////////  SECTION 6.2 EQUIVALENCE PREDICATES
            case EQVQ:
                return truth(eqv(arg1, arg2));
            case EQQ:
                return truth(arg1 == arg2);
            case EQUALQ:
                return truth(equal(arg1, arg2));

            ////////////////  SECTION 6.3 LISTS AND PAIRS
            case PAIRQ:
                return truth(arg1 instanceof Pair);
            case LISTQ:
                return truth(isList(arg1));
            case CXR:
                for (int i = name.length() - 2; i >= 1; i--)
                    arg1 = (name.charAt(i) == 'a') ? first(arg1) : rest(arg1);
                return arg1;
            case CONS:
                return cons(arg1, arg2);
            case CAR:
                return first(arg1);
            case CDR:
                return rest(arg1);
            case SETCAR:
                return setFirst(arg1, arg2);
            case SETCDR:
                return setRest(arg1, arg2);
            case SECOND:
                return second(arg1);
            case THIRD:
                return third(arg1);
            case NULLQ:
                return truth(arg1 == null);
            case LIST:
                return args;
            case LENGTH:
                return num(length(arg1));
            case APPEND:
                return (args == null) ? null : append(args);
            case REVERSE:
                return reverse(arg1);
            case LISTTAIL:
                for (int k = (int) num(arg2); k > 0; k--) arg1 = rest(arg1);
                return arg1;
            case LISTREF:
                for (int k = (int) num(arg2); k > 0; k--) arg1 = rest(arg1);
                return first(arg1);
            case MEMQ:
                return memberAssoc(arg1, arg2, 'm', 'q');
            case MEMV:
                return memberAssoc(arg1, arg2, 'm', 'v');
            case MEMBER:
                return memberAssoc(arg1, arg2, 'm', ' ');
            case ASSQ:
                return memberAssoc(arg1, arg2, 'a', 'q');
            case ASSV:
                return memberAssoc(arg1, arg2, 'a', 'v');
            case ASSOC:
                return memberAssoc(arg1, arg2, 'a', ' ');

            ////////////////  SECTION 6.4 SYMBOLS
            case SYMBOLQ:
                return truth(arg1 instanceof String);
            case SYMBOLTOSTRING:
                return sym(arg1).toCharArray();
            case STRINGTOSYMBOL:
                return new String(str(arg1)).intern();

            ////////////////  SECTION 6.5 NUMBERS
            case NUMBERQ:
                return truth(arg1 instanceof Number);
            case ODDQ:
                return truth(Math.abs(num(arg1)) % 2 != 0);
            case EVENQ:
                return truth(Math.abs(num(arg1)) % 2 == 0);
            case ZEROQ:
                return truth(num(arg1) == 0);
            case POSITIVEQ:
                return truth(num(arg1) > 0);
            case NEGATIVEQ:
                return truth(num(arg1) < 0);
            case INTEGERQ:
                return truth(isExact(arg1));
            case INEXACTQ:
                return truth(!isExact(arg1));
            case LT:
                return numCompare(args, '<');
            case GT:
                return numCompare(args, '>');
            case EQ:
                return numCompare(args, '=');
            case LE:
                return numCompare(args, 'L');
            case GE:
                return numCompare(args, 'G');
            case MAX:
                return numCompute(args, 'X', num(arg1));
            case MIN:
                return numCompute(args, 'N', num(arg1));
            case PLUS:
                return numCompute(args, '+', 0.0);
            case MINUS:
                return numCompute(rest(args), '-', num(arg1));
            case TIMES:
                return numCompute(args, '*', 1.0);
            case DIVIDE:
                return numCompute(rest(args), '/', num(arg1));
            case QUOTIENT:
                double d = num(arg1) / num(arg2);
                return num(d > 0 ? Math.floor(d) : Math.ceil(d));
            case REMAINDER:
                return num((long) num(arg1) % (long) num(arg2));
            case MODULO:
                long xi = (long) num(arg1), yi = (long) num(arg2), m = xi % yi;
                return num((xi * yi > 0 || m == 0) ? m : m + yi);
            case ABS:
                return num(Math.abs(num(arg1)));
            case FLOOR:
                return num(Math.floor(num(arg1)));
            case CEILING:
                return num(Math.ceil(num(arg1)));
            case TRUNCATE:
                d = num(arg1);
                return num((d < 0.0) ? Math.ceil(d) : Math.floor(d));
            case ROUND:
                return num(Math.round(num(arg1)));
            case EXP:
                return num(Math.exp(num(arg1)));
            case LOG:
                return num(Math.log(num(arg1)));
            case SIN:
                return num(Math.sin(num(arg1)));
            case COS:
                return num(Math.cos(num(arg1)));
            case TAN:
                return num(Math.tan(num(arg1)));
            case ASIN:
                return num(Math.asin(num(arg1)));
            case ACOS:
                return num(Math.acos(num(arg1)));
            case ATAN:
                return num(Math.atan(num(arg1)));
            case SQRT:
                return num(Math.sqrt(num(arg1)));
            case EXPT:
                return num(Math.pow(num(arg1), num(arg2)));
            case NUMBERTOSTRING:
                return numberToString(arg1, arg2);
            case STRINGTONUMBER:
                return stringToNumber(arg1, arg2);
            case GCD:
                return (args == null) ? ZERO : gcd(args);
            case LCM:
                return (args == null) ? ONE : lcm(args);

            ////////////////  SECTION 6.6 CHARACTERS
            case CHARQ:
                return truth(arg1 instanceof Character);
            case CHARALPHABETICQ:
                return truth(Character.isLetter(chr(arg1)));
            case CHARNUMERICQ:
                return truth(Character.isDigit(chr(arg1)));
            case CHARWHITESPACEQ:
                return truth(Character.isWhitespace(chr(arg1)));
            case CHARUPPERCASEQ:
                return truth(Character.isUpperCase(chr(arg1)));
            case CHARLOWERCASEQ:
                return truth(Character.isLowerCase(chr(arg1)));
            case CHARTOINTEGER:
                return (double) chr(arg1);
            case INTEGERTOCHAR:
                return chr((char) (int) num(arg1));
            case CHARUPCASE:
                return chr(Character.toUpperCase(chr(arg1)));
            case CHARDOWNCASE:
                return chr(Character.toLowerCase(chr(arg1)));
            case CHARCMP + EQ:
                return truth(charCompare(arg1, arg2, false) == 0);
            case CHARCMP + LT:
                return truth(charCompare(arg1, arg2, false) < 0);
            case CHARCMP + GT:
                return truth(charCompare(arg1, arg2, false) > 0);
            case CHARCMP + GE:
                return truth(charCompare(arg1, arg2, false) >= 0);
            case CHARCMP + LE:
                return truth(charCompare(arg1, arg2, false) <= 0);
            case CHARCICMP + EQ:
                return truth(charCompare(arg1, arg2, true) == 0);
            case CHARCICMP + LT:
                return truth(charCompare(arg1, arg2, true) < 0);
            case CHARCICMP + GT:
                return truth(charCompare(arg1, arg2, true) > 0);
            case CHARCICMP + GE:
                return truth(charCompare(arg1, arg2, true) >= 0);
            case CHARCICMP + LE:
                return truth(charCompare(arg1, arg2, true) <= 0);

            case ERROR:
                return error(stringify(args));

            ////////////////  SECTION 6.7 STRINGS
            case STRINGQ:
                return truth(arg1 instanceof char[]);
            case MAKESTRING:
                char[] str = new char[(int) num(arg1)];
                if (arg2 != null) {
                    char c = chr(arg2);
                    for (int i = str.length - 1; i >= 0; i--) str[i] = c;
                }
                return str;
            case STRING:
                return listToString(args);
            case STRINGLENGTH:
                return num(str(arg1).length);
            case STRINGREF:
                return chr(str(arg1)[(int) num(arg2)]);
            case STRINGSET:
                Object z = third(args);
                str(arg1)[(int) num(arg2)] = chr(z);
                return z;
            case SUBSTRING:
                int start = (int) num(arg2), end = (int) num(third(args));
                return new String(str(arg1), start, end - start).toCharArray();
            case STRINGAPPEND:
                return stringAppend(args);
            case STRINGTOLIST:
                Pair result = null;
                char[] str2 = str(arg1);
                for (int i = str2.length - 1; i >= 0; i--)
                    result = cons(chr(str2[i]), result);
                return result;
            case LISTTOSTRING:
                return listToString(arg1);
            case STRINGCMP + EQ:
                return truth(stringCompare(arg1, arg2, false) == 0);
            case STRINGCMP + LT:
                return truth(stringCompare(arg1, arg2, false) < 0);
            case STRINGCMP + GT:
                return truth(stringCompare(arg1, arg2, false) > 0);
            case STRINGCMP + GE:
                return truth(stringCompare(arg1, arg2, false) >= 0);
            case STRINGCMP + LE:
                return truth(stringCompare(arg1, arg2, false) <= 0);
            case STRINGCICMP + EQ:
                return truth(stringCompare(arg1, arg2, true) == 0);
            case STRINGCICMP + LT:
                return truth(stringCompare(arg1, arg2, true) < 0);
            case STRINGCICMP + GT:
                return truth(stringCompare(arg1, arg2, true) > 0);
            case STRINGCICMP + GE:
                return truth(stringCompare(arg1, arg2, true) >= 0);
            case STRINGCICMP + LE:
                return truth(stringCompare(arg1, arg2, true) <= 0);

            ////////////////  SECTION 6.8 VECTORS
            case VECTORQ:
                return truth(arg1 instanceof Object[]);
            case MAKEVECTOR:
                Object[] vec = new Object[(int) num(arg1)];
                if (arg2 != null) {
                    Arrays.fill(vec, arg2);
                }
                return vec;
            case VECTOR:
                return listToVector(args);
            case VECTORLENGTH:
                return num(vec(arg1).length);
            case VECTORREF:
                return vec(arg1)[(int) num(arg2)];
            case VECTORSET:
                return vec(arg1)[(int) num(arg2)] = third(args);
            case VECTORTOLIST:
                return vectorToList(arg1);
            case LISTTOVECTOR:
                return listToVector(arg1);

            ////////////////  SECTION 6.9 CONTROL FEATURES
            case EVAL:
                return interp.eval(arg1);
            case FORCE:
                return (!(arg1 instanceof Procedure)) ? arg1
                        : proc(arg1).apply(interp, null);
            case MACROEXPAND:
                return Macro.macroExpand(interp, arg1);
            case PROCEDUREQ:
                return truth(arg1 instanceof Procedure);
            case APPLY:
                return proc(arg1).apply(interp, listStar(rest(args)));
            case MAP:
                return map(proc(arg1), rest(args), interp, list(null));
            case FOREACH:
                return map(proc(arg1), rest(args), interp, null);
            case CALLCC:
                RuntimeException cc = new RuntimeException();
                Continuation proc = new Continuation(cc);
                try {
                    return proc(arg1).apply(interp, list(proc));
                } catch (RuntimeException e) {
                    if (e == cc) return proc.value;
                    else throw e;
                }

                ////////////////  SECTION 6.10 INPUT AND OUPUT
            case EOFOBJECTQ:
                return truth(arg1 == SchemeReader.EOF);
            case INPUTPORTQ:
                return truth(arg1 instanceof SchemeReader);
            case CURRENTINPUTPORT:
                return interp.input;
            case OPENINPUTFILE:
                return openInputFile(arg1);
            case CLOSEINPUTPORT:
                return inPort(arg1, interp).close();
            case OUTPUTPORTQ:
                return truth(arg1 instanceof PrintWriter);
            case CURRENTOUTPUTPORT:
                return interp.output;
            case OPENOUTPUTFILE:
                return openOutputFile(arg1);
            case CALLWITHOUTPUTFILE:
                try (PrintWriter p = openOutputFile(arg1)) {
                    z = proc(arg2).apply(interp, list(p));
                }
                return z;
            case CALLWITHINPUTFILE:
                SchemeReader p2 = null;
                try {
                    p2 = openInputFile(arg1);
                    z = proc(arg2).apply(interp, list(p2));
                } finally {
                    if (p2 != null) p2.close();
                }
                return z;
            case CLOSEOUTPUTPORT:
                outPort(arg1, interp).close();
                return TRUE;
            case READCHAR:
                return inPort(arg1, interp).readChar();
            case PEEKCHAR:
                return inPort(arg1, interp).peekChar();
            case LOAD:
                return interp.load(arg1);
            case READ:
                return inPort(arg1, interp).read();
            case EOF_OBJECT:
                return truth(SchemeReader.isEOF(arg1));
            case WRITE:
                return write(arg1, outPort(arg2, interp), true);
            case DISPLAY:
                return write(arg1, outPort(arg2, interp), false);
            case NEWLINE:
                outPort(arg1, interp).println();
                outPort(arg1, interp).flush();
                return TRUE;

            ////////////////  EXTENSIONS
            case CLASS:
                try {
                    return Class.forName(stringify(arg1, false));
                } catch (ClassNotFoundException e) {
                    return FALSE;
                }
            case NEW:
                try {
                    return JavaMethodProcedure.toClass(arg1).newInstance();
                } catch (InstantiationException | ClassNotFoundException | IllegalAccessException ignore) {

                }
                return FALSE;
            case METHOD:
                return new JavaMethodProcedure(stringify(arg1, false), arg2,
                        rest(rest(args)));
            case EXIT:
                System.exit((arg1 == null) ? 0 : (int) num(arg1));
            case LISTSTAR:
                return listStar(args);
            case TIMECALL:
                Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                long startTime = System.currentTimeMillis();
                long startMem = runtime.freeMemory();
                Object ans = FALSE;
                int nTimes = (arg2 == null ? 1 : (int) num(arg2));
                for (int i = 0; i < nTimes; i++) {
                    ans = proc(arg1).apply(interp, null);
                }
                long time = System.currentTimeMillis() - startTime;
                long mem = startMem - runtime.freeMemory();
                return cons(ans, list(list(num(time), "msec"),
                        list(num(mem), "bytes")));
            default:
                return error("internal error: unknown primitive: "
                        + this + " applied to " + args);
        }
    }

    public static char[] stringAppend(Object args) {
        StringBuilder result = new StringBuilder();
        for (; args instanceof Pair; args = rest(args)) {
            result.append(stringify(first(args), false));
        }
        return result.toString().toCharArray();
    }

    public static Object memberAssoc(Object obj, Object list, char m, char eq) {
        while (list instanceof Pair) {
            Object target = (m == 'm') ? first(list) : first(first(list));
            boolean found;
            switch (eq) {
                case 'q':
                    found = (target == obj);
                    break;
                case 'v':
                    found = eqv(target, obj);
                    break;
                case ' ':
                    found = equal(target, obj);
                    break;
                default:
                    warn("Bad option to memberAssoc:" + eq);
                    return FALSE;
            }
            if (found) return (m == 'm') ? list : first(list);
            list = rest(list);
        }
        return FALSE;
    }

    public static Object numCompare(Object args, char op) {
        while (rest(args) instanceof Pair) {
            double x = num(first(args));
            args = rest(args);
            double y = num(first(args));
            switch (op) {
                case '>':
                    if (!(x > y)) return FALSE;
                    break;
                case '<':
                    if (!(x < y)) return FALSE;
                    break;
                case '=':
                    if (!(x == y)) return FALSE;
                    break;
                case 'L':
                    if (!(x <= y)) return FALSE;
                    break;
                case 'G':
                    if (!(x >= y)) return FALSE;
                    break;
                default:
                    error("internal error: unrecognized op: " + op);
                    break;
            }
        }
        return TRUE;
    }

    public static Object numCompute(Object args, char op, double result) {
        if (args == null) {
            switch (op) {
                case '-':
                    return num(0 - result);
                case '/':
                    return num(1 / result);
                default:
                    return num(result);
            }
        } else {
            while (args instanceof Pair) {
                double x = num(first(args));
                args = rest(args);
                switch (op) {
                    case 'X':
                        if (x > result) result = x;
                        break;
                    case 'N':
                        if (x < result) result = x;
                        break;
                    case '+':
                        result += x;
                        break;
                    case '-':
                        result -= x;
                        break;
                    case '*':
                        result *= x;
                        break;
                    case '/':
                        result /= x;
                        break;
                    default:
                        error("internal error: unrecognized op: " + op);
                        break;
                }
            }
            return num(result);
        }
    }

    /**
     * Return the sign of the argument: +1, -1, or 0. *
     */
    static int sign(int x) {
        return Integer.compare(x, 0);
    }

    /**
     * Return <0 if x is alphabetically first, >0 if y is first,
     * 0 if same.  Case insensitive iff ci is true.  Error if not both chars. *
     */
    public static int charCompare(Object x, Object y, boolean ci) {
        char xc = chr(x), yc = chr(y);
        if (ci) {
            xc = Character.toLowerCase(xc);
            yc = Character.toLowerCase(yc);
        }
        return xc - yc;
    }

    /**
     * Return <0 if x is alphabetically first, >0 if y is first,
     * 0 if same.  Case insensitive iff ci is true.  Error if not strings. *
     */
    public static int stringCompare(Object x, Object y, boolean ci) {
        if (x instanceof char[] && y instanceof char[]) {
            char[] xc = (char[]) x, yc = (char[]) y;
            for (int i = 0; i < xc.length; i++) {
                int diff = (!ci) ? xc[i] - yc[i]
                        : Character.toUpperCase(xc[i]) - Character.toUpperCase(yc[i]);
                if (diff != 0) return diff;
            }
            return xc.length - yc.length;
        } else {
            error("expected two strings, got: " + stringify(list(x, y)));
            return 0;
        }
    }

    static Object numberToString(Object x, Object y) {
        int base = (y instanceof Number) ? (int) num(y) : 10;
        if (base != 10 || num(x) == Math.round(num(x))) {
            // An integer
            return Long.toString((long) num(x), base).toCharArray();
        } else {
            // A floating point number
            return x.toString().toCharArray();
        }
    }

    static Object stringToNumber(Object x, Object y) {
        int base = (y instanceof Number) ? (int) num(y) : 10;
        try {
            return (base == 10)
                    ? Double.valueOf(stringify(x, false))
                    : num(Long.parseLong(stringify(x, false), base));
        } catch (NumberFormatException e) {
            return FALSE;
        }
    }

    static Object gcd(Object args) {
        long gcd = 0;
        while (args instanceof Pair) {
            gcd = gcd2(Math.abs((long) num(first(args))), gcd);
            args = rest(args);
        }
        return num(gcd);
    }

    static long gcd2(long a, long b) {
        if (b == 0) return a;
        else return gcd2(b, a % b);
    }

    static Object lcm(Object args) {
        long L = 1, g;
        while (args instanceof Pair) {
            long n = Math.abs((long) num(first(args)));
            g = gcd2(n, L);
            L = (g == 0) ? g : (n / g) * L;
            args = rest(args);
        }
        return num(L);
    }

    static boolean isExact(Object x) {
        if (!(x instanceof Double)) return false;
        double d = num(x);
        return (d == Math.round(d) && Math.abs(d) < 102962884861573423.0);
    }

    static PrintWriter openOutputFile(Object filename) {
        try {
            return new PrintWriter(new FileWriter(stringify(filename, false)));
        } catch (FileNotFoundException e) {
            return (PrintWriter) error("No such file: " + stringify(filename));
        } catch (IOException e) {
            return (PrintWriter) error("IOException: " + e);
        }
    }

    static SchemeReader openInputFile(Object filename) {
        try {
            return new SchemeReader(new FileInputStream(stringify(filename, false)));
        } catch (FileNotFoundException e) {
            return (SchemeReader) error("No such file: " + stringify(filename));
        }
    }

    static boolean isList(Object x) {
        Object slow = x, fast = x;
        for (; ; ) {
            if (fast == null) return true;
            if (slow == rest(fast) || !(fast instanceof Pair)
                    || !(slow instanceof Pair)) return false;
            slow = rest(slow);
            fast = rest(fast);
            if (fast == null) return true;
            if (!(fast instanceof Pair)) return false;
            fast = rest(fast);
        }
    }

    static Object append(Object args) {
        if (rest(args) == null) return first(args);
        else return append2(first(args), append(rest(args)));
    }

    static Object append2(Object x, Object y) {
        if (x instanceof Pair) return cons(first(x), append2(rest(x), y));
        else return y;
    }

    /**
     * Map proc over a list of lists of args, in the given interpreter.
     * If result is non-null, accumulate the results of each call there
     * and return that at the end.  Otherwise, just return null. *
     */
    static Pair map(Procedure proc, Object args, Scheme interp, Pair result) {
        Pair accum = result;
        if (rest(args) == null) {
            args = first(args);
            while (args instanceof Pair) {
                Object x = proc.apply(interp, list(first(args)));
                if (accum != null) accum = (Pair) (accum.rest = list(x));
                args = rest(args);
            }
        } else {
            Procedure car = proc(interp.eval("car")), cdr = proc(interp.eval("cdr"));
            while (first(args) instanceof Pair) {
                Object x = proc.apply(interp, map(car, list(args), interp, list(null)));
                if (accum != null) accum = (Pair) (accum.rest = list(x));
                args = map(cdr, list(args), interp, list(null));
            }
        }
        return (Pair) rest(result);
    }

}

