package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.ProtocolManager;
import eu.unipv.epsilon.enigma.template.JvmAssetsClassLoaderFactory;
import eu.unipv.epsilon.enigma.template.TemplateRegistry;
import eu.unipv.epsilon.enigma.template.TemplateServer;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.JvmPackageScanner;
import org.junit.Test;

import java.io.*;

public class TemplateClassesTest {

    //                TODO: WHAT THE HECK OF AUTOMATED TEST IS THIS? DO WE EVEN CHECK FOR OUTPUTS?
    //       Only temporary, the grid template isn't even meant to stay here, we (I) may fix this after 26-05
    // << we can perfectly see that part of this code is mine because I am the only one english-speaking guy here >>

    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());

    // Create a collections pool to serve game assets
    private final CollectionsPool questCollections = new DirectoryPool(baseDir);

    // We want to serve dynamic content using JVM reflection (on Android should be Dalvik)
    private final TemplateServer templateServer = new TemplateServer(new TemplateRegistry(
        new JvmPackageScanner(), new JvmAssetsClassLoaderFactory(questCollections)));

    public TemplateClassesTest() {
        // Register "eqc:/" and "cp:/" URLs for our templates and error handlers.
        new ProtocolManager(questCollections, templateServer).registerURLStreamHandlers();
    }

    @Test
    public void testRawTemplateFromStream() throws IOException {
        String xmlDoc =
                "<quiz template=\"list\">"+
                        "<title>Waz mah name?</title>"+
                        "<answers>"+
                        "<item>Homie</item>"+
                        "<item correct=\"true\">Never told ya</item>"+
                        "<item>Buddy</item>"+
                        "</answers>"+
                "</quiz>";

        InputStream out = templateServer.loadDynamicContent(
                new ByteArrayInputStream(xmlDoc.getBytes()), null).getResponseStream();
        String outString = buildStringFromStream(out);
        System.out.println(outString);
    }

    @Test
    public void testGridTemplateStream() throws IOException {
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

        InputStream out = templateServer.loadDynamicContent(
                new ByteArrayInputStream(xmlDoc.getBytes()), null).getResponseStream();
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
