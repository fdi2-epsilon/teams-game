package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.template.JvmCandidateClassSource;
import eu.unipv.epsilon.enigma.template.TemplateServer;
import org.junit.Test;

import java.io.*;

public class TemplateClassesTest {
    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());
    private final GameAssetsSystem assetsSystem = new GameAssetsSystem(new DirectoryPool(baseDir));

    public TemplateClassesTest() {
        // We want to serve dynamic content using JVM reflection (on Android should be Dalvik)
        assetsSystem.createTemplateServer(new JvmCandidateClassSource(assetsSystem));
    }

    @Test
    public void testRawTemplateFromStream() throws IOException {
        TemplateServer ts = assetsSystem.getTemplateServer();

        String xmlDoc =
                "<quiz template=\"list\">"+
                        "<title>Waz mah name?</title>"+
                        "<answers>"+
                        "<item>Homie</item>"+
                        "<item correct=\"true\">Never told ya</item>"+
                        "<item>Buddy</item>"+
                        "</answers>"+
                "</quiz>";

        InputStream out = ts.getDynamicContentStream(new ByteArrayInputStream(xmlDoc.getBytes()), null);
        String outString = buildStringFromStream(out);
        System.out.println(outString);
    }

    @Test
    public void testGridTemplateStream() throws IOException {
        TemplateServer ts = assetsSystem.getTemplateServer();

        String xmlDoc =
                "<quiz template=\"grid\">"+
                        "<title>Eioeioeio</title>"+
                        "<grid>"+
                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+
                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+
                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+

                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+
                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+
                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+

                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+
                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+
                        "<element img=\"bla.png\" pos=\"0\" init_pos=\"2\" />"+
                        "</grid>"+
                "</quiz>";

        InputStream out = ts.getDynamicContentStream(new ByteArrayInputStream(xmlDoc.getBytes()), null);
        String outString = buildStringFromStream(out);
        System.out.println(outString);

    }

    // Utility method to create a string from an InputStream
    private static String buildStringFromStream(InputStream is) throws IOException {
        if (is == null) throw new IOException("Null InputStream!");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null)
            sb.append(line).append('\n');
        br.close();

        return sb.toString();
    }

}
