package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.junit.Test;

import java.io.*;

public class TemplateServerTest {

    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());
    private final GameAssetsSystem assetsSystem = new GameAssetsSystem(new DirectoryPool(baseDir));

    public TemplateServerTest() {
        assetsSystem.createTemplateServer(new JvmCandidateClassSource(assetsSystem));
    }

    @Test
    public void testTemplateRegistryStream() throws IOException {
        TemplateServer ts = assetsSystem.getTemplateServer();

        String xmlDoc =
                "<quiz template=\"raw\">\n" +
                "    <document src=\"index.html\" />\n" +
                "</quiz>";

        InputStream out = ts.getDynamicContentStream(new ByteArrayInputStream(xmlDoc.getBytes()), null);

        //BufferedReader br = new BufferedReader(new InputStreamReader(out));

        //assertEquals("<html>5</html>", br.readLine());
        //assertNull(br.readLine());

    }

    @Test
    public void testTemplateRegistryCollection() throws IOException {
        QuestCollection qc = assetsSystem.getCollectionContainer("testpkg03_templates").loadCollectionMeta();

        InputStream is = qc.get(0).getMainDocumentUrl().openStream();
        printStreamContent(is);

        InputStream is2 = qc.get(1).getMainDocumentUrl().openStream();
        printStreamContent(is2);
    }

    @Test
    public void testAnotherFailingExternalThingy() throws IOException {
        QuestCollection qc = assetsSystem.getCollectionContainer("testpkg04_tplremote").loadCollectionMeta();

        InputStream is = qc.get(0).getMainDocumentUrl().openStream();
        printStreamContent(is);
    }

    private void printStreamContent(InputStream is) throws IOException {
        if (is == null) throw new IOException("Null InputStream!");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null)
            System.out.println(line);
    }

}
