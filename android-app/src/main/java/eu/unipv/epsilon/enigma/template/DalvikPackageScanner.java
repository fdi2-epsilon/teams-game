package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import dalvik.system.DexFile;
import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.JvmPackageScanner;
import eu.unipv.epsilon.enigma.template.util.IterableEnumeration;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An utility facade to find classes given a package in the Dalvik Virtual Machine.
 * Android alternative to {@link JvmPackageScanner JvmPackageScanner}.
 */
public class DalvikPackageScanner {

    private DalvikPackageScanner() { }

    /** Load from the local application dex using the context class loader */
    public static List<Class<?>> getClassesInPackage(
            Context context, String packageName) throws ClassNotFoundException {
        return loadClasses(packageName, context.getClassLoader(), context.getPackageCodePath());
    }

    /** Load from an eqc collection container using a PathClassLoader */
    public static List<Class<?>> getClassesinZipPkg(
            Context context, String packageName, GameAssetsSystem assetsSystem, String collectionId) throws ClassNotFoundException {

        DexAssetsClassLoader cl = new DexAssetsClassLoader(context, assetsSystem, collectionId);

        // Our collection may not have class files
        if (!cl.hasClasses())
            return Collections.emptyList();

        return loadClasses(packageName, cl, cl.getDexPath());
    }

    private static List<Class<?>> loadClasses(
            String packageName, ClassLoader classLoader, String dexPath) throws ClassNotFoundException {
        try {
            DexFile dex = new DexFile(dexPath);

            List<Class<?>> classes = new LinkedList<>();
            for (String name : IterableEnumeration.make(dex.entries())) {
                if (name.startsWith(packageName))
                    classes.add(classLoader.loadClass(name));
            }
            return classes;

        } catch (IOException e) {
            System.out.println("---------- EXCEPT " + e.getClass().getName() + " - " + e.getMessage());
            throw new ClassNotFoundException("Cannot open DEX for reading", e);
        }
    }

}
