package eu.unipv.epsilon.enigma.loader.levels.pool;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.EqcFile;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A {@link CollectionsPool} represented by a directory containing EQC files as {@link CollectionContainer}s.
 *
 * Collection IDs are mapped directly to the file names, without extension.
 */
public class DirectoryPool extends CachedCollectionsPool {

    File baseDirectory;

    /**
     * Creates a directory pool from file, throws an {@link IllegalArgumentException}
     * if the file does not exist or is not a valid directory.
     *
     * @param baseDirectory The directory serving as a pool of {@link CollectionContainer}s
     */
    public DirectoryPool(File baseDirectory) {
        // Throw the exception only if the file EXISTS and is not a directory
        if (baseDirectory.exists() && !baseDirectory.isDirectory())
            throw new IllegalArgumentException(baseDirectory.getPath() + "is not a directory.");

        this.baseDirectory = baseDirectory;
    }

    @Override
    protected CollectionContainer createCollectionContainer(String id) throws IOException {
        return new EqcFile(id, getFileFromID(id));
    }

    @Override
    public boolean containsCollection(String id) {
        return getFileFromID(id).isFile();
    }

    @Override
    public SortedSet<String> getStoredCollectionIDs() {
        File[] files = baseDirectory.listFiles();
        if (files == null)
            return new TreeSet<>();

        SortedSet<String> names = new TreeSet<>();
        for (File file : files) {
            // Add a file to the set only if it is not a directory and has the proper extension
            if (file.isFile() && file.getName().endsWith("." + EqcFile.CONTAINER_FILE_EXTENSION))
                names.add(getIDFromFileName(file.getName()));
        }
        return names;
    }

    /* Utility method to get a file from collection ID */
    private File getFileFromID(String id) {
        return new File(baseDirectory, id + '.' + EqcFile.CONTAINER_FILE_EXTENSION);
    }

    /* Utility method to get an identifier from a filename */
    private String getIDFromFileName(String fileName) {
        // The ID is the name of the file, without extension
        int extIndex = fileName.lastIndexOf('.');
        return fileName.substring(0, extIndex);
    }

}
