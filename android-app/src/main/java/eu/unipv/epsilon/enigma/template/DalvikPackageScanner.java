package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.EqcFile;
import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.ZipFile;

/**
 * An alternative to {@link eu.unipv.epsilon.enigma.template.reflect.classfinder.PackageScanner PackageScanner} for
 * the Dalvik Virtual Machine (Android devices). This may be pluggable with the original PackageScanner in the future.
 */
public class DalvikPackageScanner {

    /** Load from the local application dex using the context class loader */
    public static LinkedList<Class<?>> getClassesInPackage(
            Context context, String packageName) throws ClassNotFoundException {
        return loadClasses(packageName, context.getClassLoader(), context.getPackageCodePath());
    }

    /** Load from an eqc collection container using a PathClassLoader */
    public static LinkedList<Class<?>> getClassesinZipPkg(
            Context context, String packageName, GameAssetsSystem assetsSystem, String collectionId) throws ClassNotFoundException {

        // Currently we haven't found a way yet to dynamically load "classes.dex" without passing in the a zipFile instance
        CollectionContainer cc = assetsSystem.getCollectionContainer(collectionId);
        if (!(cc instanceof EqcFile))
            throw new ClassNotFoundException("Currently only EQC collections are supported");

        ZipFile eqcZip = ((EqcFile) cc).getZipFile();
        ClassLoader cl = new PathClassLoader(eqcZip.getName(), context.getClassLoader());

        return loadClasses(packageName, cl, eqcZip.getName());
    }

    /** Quick n' dirty implementation to load classes from a dex file with the passed in class loader **/
    private static LinkedList<Class<?>> loadClasses(
            String packageName, ClassLoader classLoader, String dexPath) throws ClassNotFoundException {
        try {
            DexFile dex = new DexFile(dexPath);

            LinkedList<Class<?>> classes = new LinkedList<>();
            for (String name : IterableEnumeration.make(dex.entries()))
                if (name.startsWith(packageName)) classes.add(classLoader.loadClass(name));
            return classes;

        } catch (IOException e) {
            throw new ClassNotFoundException("Cannot open DEX for reading", e);
        }
    }

}
