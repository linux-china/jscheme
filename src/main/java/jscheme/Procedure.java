/*
 * @author Peter Norvig, peter@norvig.com http://www.norvig.com
 * Copyright 1998 Peter Norvig, see http://www.norvig.com/license.html
 */
package jscheme;

import static jscheme.SchemeUtils.error;
import static jscheme.SchemeUtils.stringify;

public abstract class Procedure {
    public static String ANONYMOUS_NAME = "anonymous procedure";
    String name = ANONYMOUS_NAME;

    public String toString() {
        return "{" + name + "}";
    }

    public abstract Object apply(Scheme interpreter, Object args);

    /**
     * Coerces a Scheme object to a procedure. *
     */
    static Procedure proc(Object x) {
        if (x instanceof Procedure) return (Procedure) x;
        else return proc(error("Not a procedure: " + stringify(x)));
    }

}
