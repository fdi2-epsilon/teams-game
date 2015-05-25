package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;

/**
 * Due to the actual differences between Android's Dalvik and JVM,
 * this interface is used from {@link TemplateRegistry} to establish a class search algorithm.
 *
 * The algorithm should return candidate classes which will then be annotation-filtered by
 * the {@link TemplateRegistry} itself.
 *
 * <h2>Impotent rage!</h2>
 * <p>
 *   <b>TL;DR</b> - I do not want to spend more time with classpath scanning / class loading in Android
 *   due to frustrations while debugging the device emulator.
 * </p>
 * <p>
 *   The ONLY actual maintainer of this project does not have a real Android device and is waiting for
 *   a legitimate successor of the Nexus 5 to get some hardware...
 *   Until then I am struck with the emulator, and I REFUSE to wait 15 minutes to start a simple debug session
 *   every single time only because my Desktop CPU is STILL a Core 2 QX9650.
 * </p>
 * <p>
 *   <b><i>So, these classes can be structured better, package scanners can implement a single interface,
 *   but this requires some time-expensive research using that almighty-quantum-emulator.</i></b> Especially
 *   in a way to get 'classes.dex' back from a PathClassLoader.
 * </p>
 *
 * <h3>Implementation note</h3>
 * Any implementer establishes an "is-a" relationship with this interface, so the "abstract class" specifier
 * is intended. Since we are here, we also manage exception handling by returning empty iterators on errors.
 */
public abstract class CandidateClassSource {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateClassSource.class);

    protected static final String TEMPLATES_LOCATION_BUILTIN = "eu.unipv.epsilon.enigma.template.builtin";

    protected GameAssetsSystem assetsSystem;

    protected CandidateClassSource() {
        this.assetsSystem = null;
    }

    protected CandidateClassSource(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    public GameAssetsSystem getAssetsSystem() {
        return assetsSystem;
    }

    public void setAssetsSystem(GameAssetsSystem assetsSystem) {
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
        } catch (ClassNotFoundException e) {
            LOG.warn("No classes found or error, returning empty iterator", e);
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
        } catch (ClassNotFoundException e) {
            LOG.warn("No classes found or error, returning empty iterator", e);
            return Collections.emptyIterator();
        }
    }

    /** Called to find built-in candidate classes. */
    protected abstract Iterator<Class<?>>
            findLocalCandidateClasses() throws ClassNotFoundException;

    /** Called to find collection-related candidate classes. */
    protected abstract Iterator<Class<?>>
            findCollectionCandidateClasses(String collectionId) throws ClassNotFoundException;

}
