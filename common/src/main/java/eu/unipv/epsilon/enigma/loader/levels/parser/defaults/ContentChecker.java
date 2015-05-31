package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

/**
 * An interface used by default field providers to generate valid links to existing files.
 * This is also to remove cyclic dependency with CollectionContainer...
 */
public interface ContentChecker {

    /**
     * Checks if a file exists in this container so that a default
     * field provider can use its path to set up an undefined metadata url field.
     *
     * @param entryPath the file path inside the container, follows ZipFile path format
     * @return {@code true} if there is an entry with the given path in this container
     */
    boolean containsEntry(String entryPath);

}
