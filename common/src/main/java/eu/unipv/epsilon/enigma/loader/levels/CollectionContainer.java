package eu.unipv.epsilon.enigma.loader.levels;

import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.IOException;

public abstract class CollectionContainer {

    private boolean closed = false;

    public void invalidate() {
        closed = true;
    }

    public boolean isInvalidated() {
        return closed;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        invalidate();
    }

    public abstract QuestCollection loadCollectionMeta() throws IOException;

    public abstract boolean containsEntry(String entryPath);

    public abstract ContainerEntry getEntry(String entryPath) throws IOException;

}
