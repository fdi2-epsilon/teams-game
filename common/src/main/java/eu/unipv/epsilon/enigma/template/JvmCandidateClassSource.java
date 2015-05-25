package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.template.reflect.AssetsClassLoader;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.JvmPackageScanner;

import java.util.Iterator;

/**
 * A {@link CandidateClassSource} using {@link JvmPackageScanner} as a package scanning utility.
 *
 * @see CandidateClassSource CandidateClassSource for more info
 */
public class JvmCandidateClassSource extends CandidateClassSource {

    public JvmCandidateClassSource() { /* implicit super call */ }

    public JvmCandidateClassSource(GameAssetsSystem assetsSystem) {
        super(assetsSystem);
    }

    @Override
    public Iterator<Class<?>> findLocalCandidateClasses() throws ClassNotFoundException {
        return JvmPackageScanner.getClassesInPackage(TEMPLATES_LOCATION_BUILTIN).iterator();
    }

    @Override
    public Iterator<Class<?>> findCollectionCandidateClasses(String collectionId) throws ClassNotFoundException {
        AssetsClassLoader classLoader = new AssetsClassLoader(assetsSystem, collectionId);
        return JvmPackageScanner.getClassesInPackage("", classLoader, true).iterator();
    }

}
