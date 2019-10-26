package org.mvnsearch.scheme;

import jscheme.Scheme;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

/**
 * Scheme test
 *
 * @author linux_china
 */
public class SchemeTest {
    private static Scheme scheme;

    @BeforeAll
    public static void setUp() {
        scheme = new Scheme();
    }

    @Test
    public void testEval() {
        Object result = scheme.eval(new StringReader("(+ 1 (+ 2 3))"));
        Assertions.assertEquals(result, 6.0);
    }
}
