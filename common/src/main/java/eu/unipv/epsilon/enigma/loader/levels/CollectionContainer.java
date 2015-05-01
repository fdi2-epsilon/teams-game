package eu.unipv.epsilon.enigma.loader.levels;

import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.IOException;

public interface CollectionContainer {

    QuestCollection loadCollectionMeta() throws IOException;

    boolean containsEntry(String entryPath);

    ContainerEntry getEntry(String entryPath) throws IOException;

}
