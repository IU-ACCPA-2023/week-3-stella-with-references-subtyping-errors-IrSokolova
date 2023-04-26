package org.stella;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

class MainTest {


    @ParameterizedTest(name = "{index} Typechecking well-typed program {0}")
    @ValueSource(strings = {
//            "tests/records/well-typed/records-1.stella",
            "tests/references/well-typed/refs-1.stella",
            "tests/references/well-typed/refs-2.stella",
            "tests/references/well-typed/refs-3.stella"
    })
    void testWellTyped(String filepath) throws Exception {
        String[] args = new String[0];
        final InputStream original = System.in;
        final FileInputStream fips = new FileInputStream(filepath);
        System.setIn(fips);
        Assertions.assertDoesNotThrow(() -> Main.main(args));
        System.setIn(original);
    }

    @ParameterizedTest(name = "{index} Typechecking ill-typed program {0}")
    @ValueSource(strings = {
//            "tests/records/ill-typed/bad-records-1.stella",
            "tests/references/ill-typed/bad-refs-1.stella",
            "tests/references/ill-typed/bad-refs-2.stella",
            "tests/references/ill-typed/bad-refs-3.stella",
    })
    void testIllTyped(String filepath) throws Exception {
        String[] args = new String[0];
        final FileInputStream fips = new FileInputStream(filepath);
        System.setIn(fips);

        // Change Exception class to your specific
        Exception exception = assertThrows(Exception.class, () -> Main.main(args), "Expected the type checker to fail!");
        System.out.println("Type Error: " + exception.getMessage());
    }
}
