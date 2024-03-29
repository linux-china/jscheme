/*
 * @author Peter Norvig, peter@norvig.com http://www.norvig.com
 * Copyright 1998 Peter Norvig, see http://www.norvig.com/license.html
 */
package jscheme;

import static jscheme.SchemeUtils.first;

public class Continuation extends Procedure {
    RuntimeException cc;
    public Object value = null;

    public Continuation(RuntimeException cc) {
        this.cc = cc;
    }

    public Object apply(Scheme interpreter, Object args) {
        value = first(args);
        throw cc;
    }
}
