package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.template.reflect.AssetsClassLoader;

/**
 * An implementation of {@link AssetsClassLoaderFactory} providing a class loader for the JVM.
 */
public class JvmAssetsClassLoaderFactory implements AssetsClassLoaderFactory {

    private GameAssetsSystem assetsSystem;

    public JvmAssetsClassLoaderFactory(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    @Override
    public ClassLoader createAssetsClassLoader(String collectionId) {
        return new AssetsClassLoader(assetsSystem, collectionId);
    }

}
