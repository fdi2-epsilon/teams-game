package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class TemplateServerTest {

    @Test
    public void testTemplateRegistry() throws IOException {
        GameAssetsSystem system = new GameAssetsSystem();

        TemplateServer ts = new TemplateServer(new TemplateRegistry(new JvmCandidateClassSource(system)));

        String xmlDoc =
                "<quiz template=\"raw\">\n" +
                "    <document src=\"index.html\" />\n" +
                "</quiz>";

        InputStream out = ts.getDynamicContentStream(new ByteArrayInputStream(xmlDoc.getBytes()));

        BufferedReader br = new BufferedReader(new InputStreamReader(out));

        assertEquals("<html>5</html>", br.readLine());
        assertNull(br.readLine());

        //String line;
        //while ((line = br.readLine()) != null)
        //    System.out.println(line);
    }

}