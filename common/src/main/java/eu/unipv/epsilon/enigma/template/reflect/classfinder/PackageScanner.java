package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import java.util.List;

/**
 * An utility to search for classes inside a package, can use a custom class loader to load classes and perform
 * only local (not extending to parent class loader) scans.
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
 *   every single time only because my Desktop CPU is STILL a Core 2 QX9650 which does not seem to have support
 *   for POPCNT, an SSE 4.2 instruction to count active bits in a word... no fallback HAXM, really?
 * </p>
 * <p>
 *     ...or maybe it's my fault... in any case why should I confide myself with a JavaDoc comment?
 *     People want documentation in here, not blablablebla...
 * </p>
 * <p>
 *   <b><i>So, any known implementation of this can be structured better,
 *   but this requires some time-expensive research using that almighty-quantum-emulator.</i></b>
 * </p>
 */
public interface PackageScanner {

    /**
     * Attempts to list all the classes in the specified package as determined by the context class loader.
     *
     * @param packageName The name of the package to search classes in
     * @return a list of classes found within the given package
     * @throws ClassNotFoundException
     */
    List<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException;

    /**
     * Attempts to list all the classes in the specified package as determined by the given class loader.
     *
     * Disabling recursive scans (i.e. walk through the class loader hierarchy) may require security permissions
     * to access-transform {@link ClassLoader#findResources(String)} to {@code public}, depending on the implementation.
     *
     * @param packageName The name of the package to search classes in
     * @param classLoader The class loader used to find resources and load classes
     * @param local If the scan should not be extended also to the parent class loaders, false by default
     * @return a list of classes found within the given package
     * @throws ClassNotFoundException if there was a problem while searching for classes
     */
    List<Class<?>> getClassesInPackage(
            String packageName, ClassLoader classLoader, boolean local) throws ClassNotFoundException;

}
