package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.template.reflect.AssetsClassLoader;

/**
 * An implementation of {@link AssetsClassLoaderFactory} providing a class loader for the JVM.
 */
public class JvmAssetsClassLoaderFactory implements AssetsClassLoaderFactory {

    private CollectionsPool questCollections;

    public JvmAssetsClassLoaderFactory(CollectionsPool questCollections) {
        this.questCollections = questCollections;
    }

    @Override
    public ClassLoader createAssetsClassLoader(String collectionId) {
        return new AssetsClassLoader(questCollections, collectionId);
    }

}
