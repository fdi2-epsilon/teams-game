package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.EqcFile;
import eu.unipv.epsilon.enigma.template.reflect.AssetsClassLoader;

import java.util.List;
import java.util.zip.ZipFile;

/**
 * A scan algorithm to search inside quest collection containers.
 */
public class LevelAssetsScanAlgorithm extends ScanAlgorithm {

    // We use an ArchiveScanAlgorithm internally
    private final ArchiveScanAlgorithm archiveScanAlgorithm;

    /**
     * Creates a level assets scan algorithm.
     *
     * @param classLoader the class loader used to load found classes
     * @param collectionContainer the collection container containing class files
     * @throws ClassNotFoundException if an unsupported collection container is provided
     */
    public LevelAssetsScanAlgorithm(
            ClassLoader classLoader, CollectionContainer collectionContainer) throws ClassNotFoundException {

        super(classLoader);

        // Currently, only EQC files are supported, we should alter the API to add general support
        if (collectionContainer instanceof EqcFile) {
            // TODO: Generalize entries() from ZipFile to CollectionContainer and avoid this cast
            //       Start by adding an entries() in CollectionContainer returning IterableEnumeration...
            ZipFile eqcZip = ((EqcFile) collectionContainer).getZipFile();
            this.archiveScanAlgorithm = new ArchiveScanAlgorithm(classLoader, eqcZip);
        } else
            throw new ClassNotFoundException(getClass().getSimpleName() + "can only be used on EQC collections");
    }

    @Override
    public List<Class<?>> scan(String packageName) throws ClassNotFoundException {
        // Use the algorithm for archives passing the inner directory to search in
        return archiveScanAlgorithm.scan(packageName, AssetsClassLoader.EQC_CLASS_RESOURCES_PATH);
    }

}
