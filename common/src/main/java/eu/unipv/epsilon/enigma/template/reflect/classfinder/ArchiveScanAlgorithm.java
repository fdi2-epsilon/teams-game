package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;

import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ArchiveScanAlgorithm extends ScanAlgorithm {

    private final ZipFile zipFile;

    /*
     * Zip file crawler local utility.
     *
     * @param zip the Jar / Zip file to scan
     * @param packageName the package name used to find {@link JarEntry jar entries} and create {@link Class} objects
     * @param subDir the directory containing classes in the zip file ("" for JARs)
     * @param classes the list to add found classes to
     * @throws ClassNotFoundException if a class file in the Jar file could not be loaded
     */

    public ArchiveScanAlgorithm(ClassLoader classLoader, ZipFile zipFile) {
        super(classLoader);
        this.zipFile = zipFile;
    }

    @Override
    public List<Class<?>> scan(String packagename) throws ClassNotFoundException {
        return scan(packagename, "");
    }

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
