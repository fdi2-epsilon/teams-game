package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;

import java.util.Collections;
import java.util.Iterator;

/**
 * Due to the actual differences between Android's Dalvik and JVM,
 * this interface is used from {@link TemplateRegistry} to establish a class search algorithm.
 *
 * The algorithm should return candidate classes which will then be annotation-filtered by
 * the {@link TemplateRegistry} itself.
 *
 * <h3>Implementation note</h3>
 * Any implementer establishes an "is-a" relationship with this interface, so the "abstract class" specifier
 * is intended. Since we are here, we also manage exception handling by returning empty iterators on errors.
 */
public abstract class CandidateClassSource {

    protected static final String TEMPLATES_LOCATION_BUILTIN = "eu.unipv.epsilon.enigma.template.builtin";

    protected GameAssetsSystem assetsSystem;

    protected CandidateClassSource() {
        this.assetsSystem = null;
    }

    protected CandidateClassSource(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    /**
     * Gets a list of built-in candidate template classes.
     * If there was an error or if no matching class was found, an empty iterator is returned.
     *
     * @return an iterator of found classes.
     */
    public final Iterator<Class<?>> getLocalCandidateClasses() {
        try {
            return findLocalCandidateClasses();
        } catch (ClassNotFoundException e) { // No local templates or error
            e.printStackTrace();
            return Collections.emptyIterator();
        }
    }

    /**
     * Gets a list of collection related candidate template classes.
     * If there was an error or if no matching class was found, an empty iterator is returned.
     *
     * @param collectionId the ID of the collection container to scan for classes
     * @return an iterator of found classes.
     */
    public final Iterator<Class<?>> getCollectionCandidateClasses(String collectionId) {
        try {
            if (assetsSystem == null)
                throw new ClassNotFoundException("Assets system unknown");

            return findCollectionCandidateClasses(collectionId);
        } catch (ClassNotFoundException e) { // No local templates or error
            e.printStackTrace();
            return Collections.emptyIterator();
        }
    }

    public GameAssetsSystem getAssetsSystem() {
        return assetsSystem;
    }

    public void setAssetsSystem(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    /** Called to find built-in candidate classes. */
    protected abstract Iterator<Class<?>>
            findLocalCandidateClasses() throws ClassNotFoundException;

    /** Called to find collection-related candidate classes. */
    protected abstract Iterator<Class<?>>
            findCollectionCandidateClasses(String collectionId) throws ClassNotFoundException;

}
