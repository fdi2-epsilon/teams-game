package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultQuestCollectionBuilderTest {

    public static final String FILE_PATH = "/test_package01.eqc";

    QuestCollection questCollection;

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
        assertEquals("Name must be as defined", "Sample collection, for testing!", questCollection.getName());
        assertEquals("Icon must be not default", "pack_override.png", questCollection.getIconPath());
    }

    /** Testing if values defined in metadata are correct. */
    @Test
    public void testQuestDefinedValues() {
        Quest q1 = questCollection.get(0);
        assertEquals("Something new!", q1.getName());
        assertTrue(q1.getDescription().startsWith("This is the description for"));
        assertTrue(q1.getDescription().endsWith("\"."));

        assertEquals("quests/01/element.html", q1.getMainDocumentPath());
        assertEquals("quests/01/historia.html", q1.getInfoDocumentPath());
        assertEquals("quests/01/icon.png", q1.getIconPath());
    }

    /** Testing if undefined values are given the correct defaults. */
    @Test
    public void testQuestPartiallyDefined() {
        // Q2 has defined main meta and undefined 'paths'
        Quest q2 = questCollection.get(1);
        assertEquals("Another one!", q2.getName());                         // Configured
        assertTrue(q2.getDescription().endsWith("\n- Stronzo\n"));          // Configured
        assertEquals("quests/02/index.html", q2.getMainDocumentPath());     // Default
        assertEquals("quests/02/story.html", q2.getInfoDocumentPath());     // Default
        assertEquals("quests/02/icon.png", q2.getIconPath());               // Default

        // Q3 has undefined main meta and partially defined 'paths'.
        Quest q3 = questCollection.get(2);
        assertEquals("Quest #3", q3.getName());                             // Default
        assertEquals("", q3.getDescription());                              // Default
        assertEquals("quests/q3main.html", q3.getMainDocumentPath());       // Configured
        assertEquals("quests/03/story.html", q3.getInfoDocumentPath());     // Default
        assertEquals("quests/3icon.png", q3.getIconPath());                 // Configured
    }

}
