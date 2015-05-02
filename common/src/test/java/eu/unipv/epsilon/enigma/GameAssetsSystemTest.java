package eu.unipv.epsilon.enigma;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameAssetsSystemTest {

    public static final String TEST_COLLECTION_ID = "test_package01";
    public static final String PROTO_HEAD = LevelAssetsURLStreamHandler.PROTOCOL_NAME + "://test_package01/";

    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());
    private final GameAssetsSystem system = new GameAssetsSystem(new DirectoryPool(baseDir));

    @Before
    public void setUp() {
        try {
            new URL(LevelAssetsURLStreamHandler.PROTOCOL_NAME, "", -1, "");
        } catch (MalformedURLException e) {
            // URL Stream Handler not registered, register it now
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    if (protocol.equalsIgnoreCase(LevelAssetsURLStreamHandler.PROTOCOL_NAME))
                        return system.getURLStreamHandler();
                    return null;
                }
            });
        }
    }

    @Test
    public void testCollectionExistence() {
        assertTrue("Available collections set should contain " + TEST_COLLECTION_ID,
                system.getAvailableCollectionIDs().contains(TEST_COLLECTION_ID));

        assertTrue("Asset system should lookup " + TEST_COLLECTION_ID + "properly",
                system.containsCollection(TEST_COLLECTION_ID));
    }

    @Test
    public void testContainerContent() throws IOException {
        CollectionContainer container = system.getCollectionContainer(TEST_COLLECTION_ID);

        assertTrue("The system should be able to find entries",
                container.containsEntry("quests/03/story.html"));

        container.invalidate();
    }

    @Test
    public void testMetadata() throws IOException {
        QuestCollection questCollection = system.getCollectionContainer(TEST_COLLECTION_ID).loadCollectionMeta();

        // Size test
        assertEquals("Collection size must be 3",
                3, questCollection.size());

        // Collection metadata
        assertEquals("Name must be as defined",
                "Sample collection, for testing!", questCollection.getTitle());
        assertEquals("Icon must be not default",
                PROTO_HEAD + "pack_override.png", questCollection.getIconUrl().toString());

        // Quest metadata: defined values
        Quest q1 = questCollection.get(0);
        assertEquals("Something new!", q1.getName());
        assertTrue(q1.getDescription().startsWith("This is the description for"));
        assertTrue(q1.getDescription().endsWith("\"."));
        assertEquals(PROTO_HEAD + "quests/01/element.html", q1.getMainDocumentUrl().toString());
        assertEquals(PROTO_HEAD + "quests/01/historia.html", q1.getInfoDocumentUrl().toString());
        assertEquals(PROTO_HEAD + "quests/01/icon.png", q1.getIconUrl().toString());

        // Quest metadata: undefined (default) values
        /*   Q2 has defined main meta and undefined 'paths'.   */
        Quest q2 = questCollection.get(1);
        assertEquals("Another one!", q2.getName());                                             // Configured
        assertTrue(q2.getDescription().endsWith("\n- Stronzo\n"));                              // Configured
        assertEquals(PROTO_HEAD + "quests/02/index.html", q2.getMainDocumentUrl().toString());  // Default
        assertEquals(PROTO_HEAD + "quests/02/story.html", q2.getInfoDocumentUrl().toString());  // Default
        assertEquals(PROTO_HEAD + "quests/02/icon.png", q2.getIconUrl().toString());            // Default

        /*   Q3 has undefined main meta and partially defined 'paths'.   */
        Quest q3 = questCollection.get(2);
        assertEquals("Quest #3", q3.getName());                                                 // Default
        assertEquals("", q3.getDescription());                                                  // Default
        assertEquals(PROTO_HEAD + "quests/q3main.html", q3.getMainDocumentUrl().toString());    // Configured
        assertEquals(PROTO_HEAD + "quests/03/story.html", q3.getInfoDocumentUrl().toString());  // Default
        assertEquals(PROTO_HEAD + "quests/3icon.png", q3.getIconUrl().toString());              // Configured
        system.getCollectionContainer(TEST_COLLECTION_ID).invalidate();
    }

    @Test
    public void testURLStreams() throws IOException {
        CollectionContainer container = system.getCollectionContainer(TEST_COLLECTION_ID);
        QuestCollection qc = container.loadCollectionMeta();

        URL url = qc.get(1).getMainDocumentUrl();

        assertEquals("URL should be equal to the spec.",
                url, new URL(PROTO_HEAD + "quests/02/index.html"));
        assertEquals("URL should be equal to one created with createURL()",
                url, LevelAssetsURLStreamHandler.createURL(TEST_COLLECTION_ID, "quests/02/index.html"));

        // If container is invalidated before this, it will be reopened
        InputStream in = url.openStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        assertEquals("Stream should be an HTML file",
                reader.readLine(), "<!DOCTYPE html>");
        reader.close();

        container.invalidate();
    }

}
