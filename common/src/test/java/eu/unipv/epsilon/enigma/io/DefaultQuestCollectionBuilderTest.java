package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.io.url.EqcURLStreamHandler;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultQuestCollectionBuilderTest {

    public static final String FILE_PATH = "/test_package01.eqc";
    public static final String PROTO_HEAD = "eqc://test_package01/";

    QuestCollection questCollection;

    static {
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            @Override
            public URLStreamHandler createURLStreamHandler(String protocol) {
                // DO NOT USE THE EQC PROTOCOL TO LOAD FILES IN THIS TEST
                if (protocol.equalsIgnoreCase("eqc")) return new EqcURLStreamHandler(null);
                return null;
            }
        });
    }

    @Before
    public void setUp() throws IOException {
        URL fileURL = getClass().getResource(FILE_PATH);
        if (fileURL == null)
            throw new FileNotFoundException("File not found: \"" + FILE_PATH + "\".");
        questCollection = new DefaultQuestCollectionBuilder().createCollectionFromFile(new File(fileURL.getPath()));
    }

    @Test
    public void testCollectionSize() {
        assertEquals("Size must be 3", 3, questCollection.size());
    }

    @Test
    public void testCollectionMetadata() {
        assertEquals("Name must be as defined", "Sample collection, for testing!", questCollection.getTitle());
        assertEquals("Icon must be not default", PROTO_HEAD + "pack_override.png", questCollection.getIconUrl().toString());
    }

    /** Testing if values defined in metadata are correct. */
    @Test
    public void testQuestDefinedValues() {
        Quest q1 = questCollection.get(0);
        assertEquals("Something new!", q1.getName());
        assertTrue(q1.getDescription().startsWith("This is the description for"));
        assertTrue(q1.getDescription().endsWith("\"."));

        assertEquals(PROTO_HEAD + "quests/01/element.html", q1.getMainDocumentUrl().toString());
        assertEquals(PROTO_HEAD + "quests/01/historia.html", q1.getInfoDocumentUrl().toString());
        assertEquals(PROTO_HEAD + "quests/01/icon.png", q1.getIconUrl().toString());
    }

    /** Testing if undefined values are given the correct defaults. */
    @Test
    public void testQuestPartiallyDefined() {
        // Q2 has defined main meta and undefined 'paths'
        Quest q2 = questCollection.get(1);
        assertEquals("Another one!", q2.getName());                                             // Configured
        assertTrue(q2.getDescription().endsWith("\n- Stronzo\n"));                              // Configured
        assertEquals(PROTO_HEAD + "quests/02/index.html", q2.getMainDocumentUrl().toString());  // Default
        assertEquals(PROTO_HEAD + "quests/02/story.html", q2.getInfoDocumentUrl().toString());  // Default
        assertEquals(PROTO_HEAD + "quests/02/icon.png", q2.getIconUrl().toString());            // Default

        // Q3 has undefined main meta and partially defined 'paths'.
        Quest q3 = questCollection.get(2);
        assertEquals("Quest #3", q3.getName());                                                 // Default
        assertEquals("", q3.getDescription());                                                  // Default
        assertEquals(PROTO_HEAD + "quests/q3main.html", q3.getMainDocumentUrl().toString());    // Configured
        assertEquals(PROTO_HEAD + "quests/03/story.html", q3.getInfoDocumentUrl().toString());  // Default
        assertEquals(PROTO_HEAD + "quests/3icon.png", q3.getIconUrl().toString());              // Configured
    }

}
