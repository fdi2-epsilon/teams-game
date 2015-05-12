package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.template.reflect.AssetsClassLoader;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.PackageScanner;

import java.util.Iterator;

public class JvmCandidateClassSource extends CandidateClassSource {

    private final GameAssetsSystem assetsSystem;

    public JvmCandidateClassSource(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    @Override
    public Iterator<Class<?>> findLocalCandidateClasses() throws ClassNotFoundException {
        return PackageScanner.getClassesInPackage(TEMPLATES_LOCATION_BUILTIN).iterator();
    }

    @Override
    public Iterator<Class<?>> findCollectionCandidateClasses(String collectionId) throws ClassNotFoundException {
        AssetsClassLoader classLoader = new AssetsClassLoader(assetsSystem, collectionId);
        return PackageScanner.getClassesInPackage("", classLoader, true).iterator();
    }

}
