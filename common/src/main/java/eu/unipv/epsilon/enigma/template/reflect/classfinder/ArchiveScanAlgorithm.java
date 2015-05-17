package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;

import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A scan algorithm to search inside generic ZIP archives and JARs.
 */
public class ArchiveScanAlgorithm extends ScanAlgorithm {

    private final ZipFile zipFile;

    /**
     * Create an archive scan algorithm.
     *
     * @param classLoader the class loader used to load found classes
     * @param zipFile the Zip (or Jar) file to scan
     */
    public ArchiveScanAlgorithm(ClassLoader classLoader, ZipFile zipFile) {
        super(classLoader);
        this.zipFile = zipFile;
    }

    @Override
    public List<Class<?>> scan(String packageName) throws ClassNotFoundException {
        // Scan for classes in the root folder (as in JARs)
        return scan(packageName, "");
    }

    // The real scan implementation accepting a subfolder inside the archive
    public List<Class<?>> scan(String packageName, String subDir) throws ClassNotFoundException {
        LinkedList<Class<?>> classes = new LinkedList<>();

        for (ZipEntry entry : IterableEnumeration.make(zipFile.entries())) {
            String name = entry.getName();
            if (name.endsWith(CLASS_FILE_EXT) && name.startsWith(subDir)) {
                name = name.substring(subDir.length());
                name = stripClassExtension(name).replace('/', '.');
                if (name.startsWith(packageName)) classes.add(classLoader.loadClass(name));
            }
        }

        return classes;
    }

}
