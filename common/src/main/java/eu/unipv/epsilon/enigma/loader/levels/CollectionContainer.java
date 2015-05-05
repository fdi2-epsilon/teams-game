package eu.unipv.epsilon.enigma.loader.levels;

import eu.unipv.epsilon.enigma.loader.levels.pool.CachedCollectionsPool;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.IOException;

/**
 * A container for {@link QuestCollection} metadata and its related assets.
 */
public abstract class CollectionContainer {

    private boolean closed = false;

    /**
     * Releases any system resources associated with this collection container and marks it as closed.
     * This is usually used to release resources before a cache held by a {@link CachedCollectionsPool} expires.
     */
    public void invalidate() {
        closed = true;
    }

    /**
     * Checks if this container is closed (i.e. {@link #invalidate()} was called).
     *
     * Obtain a new instance from an assets manager or a collections pool to reopen the container.
     * It is always suggested to obtain a container instance from a caching Assets Manager.
     */
    public boolean isInvalidated() {
        return closed;
    }

    /** <b><i>Should</i></b> be called by the Garbage Collector on cleanup. */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        invalidate();
    }

    /**
     * Loads the collection metadata bundled within this container.
     *
     * @return The loaded {@link QuestCollection} metadata
     * @throws IOException if the contained collection does not have any metadata (which it should)
     */
    public abstract QuestCollection loadCollectionMeta() throws IOException;

    /**
     * Checks if this container holds an asset identified by its absolute path.
     *
     * @param entryPath The path of the asset inside the container
     * @return {@code true} if the asset exists
     */
    public abstract boolean containsEntry(String entryPath);

    /**
     * Returns a {@link ContainerEntry} for an asset in this container.
     *
     * @param entryPath The path of the asset inside the container
     * @return a {@link ContainerEntry} for the given entry
     * @throws IOException if the entry was not found inside this container
     */
    public abstract ContainerEntry getEntry(String entryPath) throws IOException;

}
