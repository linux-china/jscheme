/*
 * @author Peter Norvig, peter@norvig.com http://www.norvig.com
 * Copyright 1998 Peter Norvig, see http://www.norvig.com/license.html
 */
package jscheme;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static jscheme.SchemeUtils.*;

/**
 * This class represents a Scheme interpreter.
 * See http://www.norvig.com/jscheme.html for more documentation.
 * This is version 1.4.
 *
 * @author Peter Norvig, peter@norvig.com http://www.norvig.com
 * Copyright 1998 Peter Norvig, see http://www.norvig.com/license.html *
 */
public class Scheme {
    SchemeReader input = new SchemeReader(System.in);
    PrintWriter output = new PrintWriter(System.out, true);
    Environment globalEnvironment = new Environment();
    public static List<Object> OUTPUT_PROCEDURES = Arrays.asList("display", "write", "write-char");

    public Scheme() {
        Primitive.installPrimitives(globalEnvironment);
        load(new SchemeReader(new InputStreamReader(this.getClass().getResourceAsStream("/jscheme/default.scm"))));
    }

    /**
     * Create a Scheme interpreter and load an array of files into it.
     * Also load SchemePrimitives.CODE. *
     */
    public Scheme(String[] files) {
        this();
        if (files != null) {
            try {
                for (String file : files) {
                    load(new File(file));
                }
            } catch (RuntimeException ignore) {
            }
        }
    }

    /**
     * Create a new Scheme interpreter, passing in the command line args
     * as files to load, and then enter a read eval write loop. *
     */
    public static void main(String[] files) {
        new Scheme(files).readEvalWriteLoop();
    }

    /**
     * Prompt, read, eval, and write the result.
     * Also sets up a catch for any RuntimeExceptions encountered. *
     */
    public void readEvalWriteLoop() {
        Object x;
        for (; ; ) {
            try {
                output.print("\u03BB> ");
                output.flush();
                if (SchemeReader.isEOF(x = input.read())) return;
                if (x instanceof Pair && OUTPUT_PROCEDURES.contains(((Pair) x).first)) {
                    eval(x);
                } else {
                    write(eval(x), output, true);
                }
                output.println();
                output.flush();
            } catch (RuntimeException ignore) {
            }
        }
    }

    /**
     * Eval all the expressions in a file. Calls load(InputPort).
     */
    public Object load(Object fileName) {
        String name = stringify(fileName, false);
        try {
            return load(new SchemeReader(new FileInputStream(name)));
        } catch (IOException e) {
            return error("can't load " + name);
        }
    }

    /**
     * Eval all the expressions coming from an InputPort.
     */
    public Object load(SchemeReader in) {
        Object x;
        for (; ; ) {
            if (SchemeReader.isEOF(x = in.read())) {
                return TRUE;
            }
            eval(x);
        }
    }

    /**
     * Evaluate an object, x, in an environment. *
     */
    public Object eval(Object x, Environment env) {
        // The purpose of the while loop is to allow tail recursion.
        // The idea is that in a tail recursive position, we do "x = ..."
        // and loop, rather than doing "return eval(...)".
        while (true) {
            if (x instanceof String) {         // VARIABLE
                return env.lookup((String) x);
            } else if (!(x instanceof Pair)) { // CONSTANT
                return x;
            } else {
                Object fn = first(x);
                Object args = rest(x);
                if (fn == "quote") {             // QUOTE
                    return first(args);
                } else if (fn == "begin") {      // BEGIN
                    for (; rest(args) != null; args = rest(args)) {
                        eval(first(args), env);
                    }
                    x = first(args);
                } else if (fn == "define") {     // DEFINE
                    if (first(args) instanceof Pair)
                        return env.define(first(first(args)),
                                eval(cons("lambda", cons(rest(first(args)), rest(args))), env));
                    else return env.define(first(args), eval(second(args), env));
                } else if (fn == "set!") {       // SET!
                    return env.set(first(args), eval(second(args), env));
                } else if (fn == "if") {         // IF
                    x = (truth(eval(first(args), env))) ? second(args) : third(args);
                } else if (fn == "cond") {       // COND
                    x = reduceCond(args, env);
                } else if (fn == "lambda") {     // LAMBDA
                    return new Closure(first(args), rest(args), env);
                } else if (fn == "macro") {      // MACRO
                    return new Macro(first(args), rest(args), env);
                } else {                         // PROCEDURE CALL:
                    fn = eval(fn, env);
                    if (fn instanceof Macro) {          // (MACRO CALL)
                        x = ((Macro) fn).expand(this, (Pair) x, args);
                    } else if (fn instanceof Closure) { // (CLOSURE CALL)
                        Closure f = (Closure) fn;
                        x = f.body;
                        env = new Environment(f.parms, evalList(args, env), f.env);
                    } else {                            // (OTHER PROCEDURE CALL)
                        return Procedure.proc(fn).apply(this, evalList(args, env));
                    }
                }
            }
        }
    }

    /**
     * Eval in the global environment. *
     */
    public Object eval(Object x) {
        return eval(x, this.globalEnvironment);
    }

    /**
     * eval scheme code
     *
     * @param code code
     * @return last return result
     */
    public Object eval(StringReader code) {
        SchemeReader input = new SchemeReader(code);
        Object result = null;
        Object x = input.read();
        while (!SchemeReader.isEOF(x)) {
            result = eval(x);
            x = input.read();
        }
        input.close();
        return result;
    }

    /**
     * Evaluate each of a list of expressions. *
     */
    Pair evalList(Object list, Environment env) {
        if (list == null)
            return null;
        else if (!(list instanceof Pair)) {
            error("Illegal arg list: " + list);
            return null;
        } else
            return cons(eval(first(list), env), evalList(rest(list), env));
    }

    /**
     * Reduce a cond expression to some code which, when evaluated,
     * gives the value of the cond expression.  We do it that way to
     * maintain tail recursion. *
     */
    Object reduceCond(Object clauses, Environment env) {
        Object result = null;
        for (; ; ) {
            if (clauses == null) return FALSE;
            Object clause = first(clauses);
            clauses = rest(clauses);
            if (first(clause) == "else"
                    || truth(result = eval(first(clause), env)))
                if (rest(clause) == null) return list("quote", result);
                else if (second(clause) == "=>")
                    return list(third(clause), list("quote", result));
                else return cons("begin", rest(clause));
        }
    }

}


