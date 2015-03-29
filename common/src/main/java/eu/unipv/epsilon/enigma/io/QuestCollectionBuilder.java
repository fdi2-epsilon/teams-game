package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.File;
import java.io.IOException;

public interface QuestCollectionBuilder {

    /** Populates a new Quest Collection from the metadata inside the given file. */
    QuestCollection createFromFile(File file) throws IOException;

}
