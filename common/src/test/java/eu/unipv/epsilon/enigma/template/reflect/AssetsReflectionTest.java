package eu.unipv.epsilon.enigma.template.reflect;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLConnection;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.PackageScanner;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AssetsReflectionTest {


    private final File baseDir = new File(getClass().getResource("/collections_pool").getPath());
    private final GameAssetsSystem system = new GameAssetsSystem(new DirectoryPool(baseDir));

    @Test
    public void testLocalPackageScan() throws ClassNotFoundException {
        List<Class<?>> elements = PackageScanner.getClassesInPackage("eu.unipv.epsilon.enigma.loader.levels.protocol");

        assertTrue("The loaded package contain the right classes",
                elements.contains(LevelAssetsURLStreamHandler.class));
        assertTrue("The loaded package contain the right classes",
                elements.contains(LevelAssetsURLConnection.class));
        assertTrue("The loaded package contain the right classes",
                elements.contains(ClasspathURLStreamHandler.class));
    }

    @Test
    public void testCollectionPackageScan() throws ClassNotFoundException {
        ClassLoader cl = new AssetsClassLoader(system, "testpkg04_tplremote");
        List<Class<?>> elementsGlobal = PackageScanner.getClassesInPackage("", cl, false);
        List<Class<?>> elementsLocal = PackageScanner.getClassesInPackage("", cl, true);

        // elementsGlobal should contain both application and collection defined classes
        assertTrue(elementsGlobal.contains(GameAssetsSystem.class));
        assertTrue(elementsGlobal.contains(cl.loadClass("mypkg.JesusChristException")));

        // elementsLocal should only contain collection defined classes
        assertFalse(elementsLocal.contains(GameAssetsSystem.class));
        assertTrue(elementsLocal.contains(cl.loadClass("mypkg.JesusChristException")));
    }

    @Test
    public void testCollectionClassLoading() throws Exception {
        ClassLoader cl = new AssetsClassLoader(system, "testpkg04_tplremote");

        Class<?> clazz = cl.loadClass("mypkg.ForcedExceptionTemplate");
        Method m = clazz.getMethod("blabla", DocumentGenerationEvent.class);
        Object obj = clazz.newInstance();

        boolean passed = false;

        try {
            m.invoke(obj, (Object) null);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof NullPointerException)
                passed = true;
        }

        system.getCollectionContainer("testpkg04_tplremote").invalidate();

        assertTrue("Method should have thrown an NPE inside an InvocationTargetException", passed);
    }

}
