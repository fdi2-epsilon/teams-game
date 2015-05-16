package eu.unipv.epsilon.enigma;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class GameAssetsSystemTest {

    /*
     * This performs only generic metadata loading tests,
     * for templates, take a look at TemplateServerTest
     */

    public static final String PROTO_HEAD = LevelAssetsURLStreamHandler.PROTOCOL_NAME + "://";

    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());
    private final GameAssetsSystem system = new GameAssetsSystem(new DirectoryPool(baseDir));
    private final String cid;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        // https://github.com/junit-team/junit/wiki/Parameterized-tests
        return Arrays.asList(new Object[][] {{"testpkg01_yamlmeta"}, {"testpkg02_xmlmeta"}});
    }

    public GameAssetsSystemTest(String collectionId) {
        this.cid = collectionId;
    }

    @Test
    public void testCollectionExistence() {
        assertTrue("Available collections set should contain " + cid,
                system.getAvailableCollectionIDs().contains(cid));

        assertTrue("Asset system should lookup " + cid + "properly",
                system.containsCollection(cid));
    }

    @Test
    public void testContainerContent() {
        CollectionContainer container = system.getCollectionContainer(cid);

        assertNotNull("Container should exist",
                container);

        assertTrue("The system should be able to find entries",
                container.containsEntry("quests/03/story.html"));

        container.invalidate();
    }

    @Test
    public void testMetadata() throws IOException {
        QuestCollection questCollection = system.getCollectionContainer(cid).loadCollectionMeta();

        // Size test
        assertEquals("Collection size must be 3",
                3, questCollection.size());

        // Collection metadata
        assertEquals("Name must be as defined",
                "Sample collection, for testing!", questCollection.getTitle());
        assertEquals("Icon must be not default",
                PROTO_HEAD + cid + "/pack_override.png", questCollection.getIconUrl().toString());

        // Quest metadata: defined values
        Quest q1 = questCollection.get(0);
        assertEquals("Something new!", q1.getName());
        assertTrue(q1.getDescription().startsWith("This is the description for"));
        assertTrue(q1.getDescription().endsWith("\"."));
        assertEquals(PROTO_HEAD + cid + "/quests/01/element.html", q1.getMainDocumentUrl().toString());
        assertEquals(PROTO_HEAD + cid + "/quests/01/historia.html", q1.getInfoDocumentUrl().toString());
        assertEquals(PROTO_HEAD + cid + "/quests/01/icon.png", q1.getIconUrl().toString());

        // Quest metadata: undefined (default) values
        /*   Q2 has defined main meta and undefined 'paths'.   */
        Quest q2 = questCollection.get(1);
        assertEquals("Another one!", q2.getName());                                                     // Configured
        assertTrue(q2.getDescription().endsWith("\n- Stronzo\n"));                                      // Configured
        assertEquals(PROTO_HEAD + cid + "/quests/02/", q2.getMainDocumentUrl().toString());   // Default
        assertEquals(PROTO_HEAD + cid + "/quests/02/story.html", q2.getInfoDocumentUrl().toString());   // Default
        assertEquals(PROTO_HEAD + cid + "/quests/02/icon.png", q2.getIconUrl().toString());             // Default

        /*   Q3 has undefined main meta and partially defined 'paths'.   */
        Quest q3 = questCollection.get(2);
        assertEquals("Quest #3", q3.getName());                                                         // Default
        assertEquals("", q3.getDescription());                                                          // Default
        assertEquals(PROTO_HEAD + cid + "/quests/q3main.html", q3.getMainDocumentUrl().toString());     // Configured
        assertEquals(PROTO_HEAD + cid + "/quests/03/story.html", q3.getInfoDocumentUrl().toString());   // Default
        assertEquals(PROTO_HEAD + cid + "/quests/3icon.png", q3.getIconUrl().toString());               // Configured

        system.getCollectionContainer(cid).invalidate();
    }

    @Test
    public void testURLStreams() throws IOException {
        CollectionContainer container = system.getCollectionContainer(cid);
        QuestCollection qc = container.loadCollectionMeta();

        URL url = qc.get(1).getMainDocumentUrl();

        assertEquals("URL should be equal to the spec.",
                url, new URL(PROTO_HEAD + cid + "/quests/02/"));
        assertEquals("URL should be equal to one created with createURL()",
                url, LevelAssetsURLStreamHandler.createURL(cid, "quests/02/"));

        // If container is invalidated before this, it will be reopened
        InputStream in = url.openStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        assertEquals("Stream should be an HTML file",
                reader.readLine(), "<!DOCTYPE html>");
        reader.close();

        container.invalidate();
    }

}
