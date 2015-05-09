package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.EqcFile;

import java.util.List;
import java.util.zip.ZipFile;

public class LevelAssetsScanAlgorithm extends ScanAlgorithm {

    private final ArchiveScanAlgorithm archiveScanAlgorithm;

    public LevelAssetsScanAlgorithm(ClassLoader classLoader, CollectionContainer collectionContainer) {
        super(classLoader);

        // TODO: generalize elements from ZipFile to CollectionContainer (may be a long task)
        ZipFile eqcZip = ((EqcFile) collectionContainer).getZipFile();
        this.archiveScanAlgorithm = new ArchiveScanAlgorithm(classLoader, eqcZip);
    }

    @Override
    public List<Class<?>> scan(String packageName) throws ClassNotFoundException {
        return archiveScanAlgorithm.scan(packageName, "classes/");
    }

}
