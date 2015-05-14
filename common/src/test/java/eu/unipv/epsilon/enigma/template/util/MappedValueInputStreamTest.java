package eu.unipv.epsilon.enigma.template.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MappedValueInputStreamTest {

    @Test
    public void testMacroExpansion() throws IOException {
        String input =
                "Hi ${your_name}, I am ${my_name} and this is a ${macro}, here is some money $$ to test if" +
                " $macros$ don't mess up $ $, and I already know that I can't code ${pointless_smiley}.";

        String expected =
                "Hi prof, I am Luca and this is a ${macro}, here is some money $$ to test if $macros$" +
                " don't mess up $ $, and I already know that I can't code :).";

        // After this I should buy a nice dollar chain, pure gangsta style, peace homie...

        MappedValueInputStream is = new MappedValueInputStream(new ByteArrayInputStream(input.getBytes()));

        is.addMacro("your_name", "prof");
        is.addMacro("my_name", "Luca");
        is.addMacro("pointless_smiley", ":)");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        assertEquals(expected, br.readLine());
        assertNull(br.readLine());

        br.close();
    }

}
