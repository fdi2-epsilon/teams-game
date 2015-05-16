package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class TemplateServerTest {

    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());
    private final GameAssetsSystem assetsSystem = new GameAssetsSystem(new DirectoryPool(baseDir));

    public TemplateServerTest() {
        // We want to serve dynamic content using JVM reflection (on Android should be Dalvik)
        assetsSystem.createTemplateServer(new JvmCandidateClassSource(assetsSystem));
    }

    @Test
    public void testRawTemplateFromStream() throws IOException {
        TemplateServer ts = assetsSystem.getTemplateServer();

        String xmlDoc =
                "<quiz template=\"raw\">\n" +
                "    <document src=\"index.html\" />\n" +
                "</quiz>";

        InputStream out = ts.getDynamicContentStream(new ByteArrayInputStream(xmlDoc.getBytes()), null);
        String outString = buildStringFromStream(out);

        assertTrue("\"raw\" template loaded from InputStream should throw an UnsupportedOperationException",
                outString.contains("UnsupportedOperationException"));
    }

    @Test
    public void testCollectionContainerBuiltin() throws IOException {
        CollectionContainer container = assetsSystem.getCollectionContainer("testpkg03_templates");
        QuestCollection qc = container.loadCollectionMeta();

        InputStream is1a = qc.get(0).getMainDocumentUrl().openStream();
        InputStream is1b = container.getEntry("quests/01/blah.html").getStream();

        // Here we check if links to a folder (as defined in metadata)
        // are redirected to a "document.xml" file inside it.
        assertTrue("The \"raw\" template should load \"blah.html\", so the content should be equal",
                streamEquals(is1a, is1b));

        // The second collection points to a folder too,
        // when "document.xml" is not found, "index.html" should be loaded.
        InputStream is2 = qc.get(1).getMainDocumentUrl().openStream();
        assertTrue("We should have content inside \"index.html\"",
                buildStringFromStream(is2).contains("Thiz should be loaded!"));
    }

    @Test
    public void testCollectionContainerCustom() throws IOException {
        QuestCollection qc = assetsSystem.getCollectionContainer("testpkg04_tplremote").loadCollectionMeta();

        InputStream is = qc.get(0).getMainDocumentUrl().openStream();
        assertTrue("Custom template should be loaded from the collection and throw a JesusChristException",
                buildStringFromStream(is).contains("JesusChristException"));
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

    // Utility method to check if the content of two streams is the same
    private static boolean streamEquals(InputStream in1, InputStream in2) throws IOException {
        if (!(in1 instanceof BufferedInputStream))
            in1 = new BufferedInputStream(in1);
        if (!(in2 instanceof BufferedInputStream))
            in2 = new BufferedInputStream(in2);

        int ch;

        while ((ch = in1.read()) == (in2.read()))
            if (ch == -1) return true;
        return false;
    }

}
