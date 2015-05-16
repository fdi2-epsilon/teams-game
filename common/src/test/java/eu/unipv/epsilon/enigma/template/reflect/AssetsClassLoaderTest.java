package eu.unipv.epsilon.enigma.template.reflect;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.PackageScanner;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class AssetsClassLoaderTest {


    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());
    private final GameAssetsSystem system = new GameAssetsSystem(new DirectoryPool(baseDir));

    @Test
    public void testClassLoading() throws Exception {
        ClassLoader cl = new AssetsClassLoader(system, "packcomp");

        System.out.println("" +
                "----------\n" +
                "Trying to load \"MahClass\" from EQC file, which depends on \"Prank\"\n" +
                "returns Math.PI + 3 and prints \"-> *\" messages on screen\n" +
                "----------");

        Class<?> clazz = cl.loadClass("hayo.MahClass");
        Method m = clazz.getMethod("jokePlease");

        Object obj = clazz.newInstance();
        double d = (double) m.invoke(obj);
        System.out.println("IT RETOURNED " + d + "!!!");

        system.getCollectionContainer("packcomp").invalidate();
    }

    @Test
    public void testResourcesStd() throws ClassNotFoundException {
        List<Class<?>> elements = PackageScanner.getClassesInPackage("eu.unipv.epsilon.enigma.loader.levels.protocol");

        System.out.println("*testResourcesStd*");
        for (Class clazz : elements) System.out.println("> " + clazz);
    }

    @Test
    public void testResourcesCust() throws ClassNotFoundException {
        ClassLoader cl = new AssetsClassLoader(system, "packcomp");
        List<Class<?>> elements = PackageScanner.getClassesInPackage("", cl, true);

        // RESOURCES LOADING FALLS BACK TO PARENT CLASS LOADER
        System.out.println("*testResourcesCust*");
        for (Class clazz : elements) System.out.println("> " + clazz);
    }

}