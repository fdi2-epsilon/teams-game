package eu.unipv.epsilon.enigma.loader.levels;

import java.io.IOException;
import java.io.InputStream;

/** An asset entry inside a {@link CollectionContainer}. */
public interface ContainerEntry {

    /**
     * Returns the size of the entry data.
     *
     * @return the size of the entry data, or -1 if not known
     */
    long getSize();

    /**
     * Obtains a stream for reading the content of this asset.
     *
     * @return an input stream to asset data
     * @throws IOException if the asset does not exist inside the container
     */
    InputStream getStream() throws IOException;

    /**
     * Checks if this entry exists and is a directory.
     *
     * @return {@code true} if this entry is a directory
     */
    boolean isDirectory();

    /**
     * Gets the path of this entry inside the container.
     *
     * @return the path of the entry in the container
     */
    String getPath();

}
