package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;

/**
 * A ready to use {@link AssetsClassLoaderFactory} for the Dalvik virtual machine on Android.
 */
public class DalvikAssetsClassLoaderFactory implements AssetsClassLoaderFactory {

    private Context context;
    private CollectionsPool questCollections;

    public DalvikAssetsClassLoaderFactory(Context context, CollectionsPool questCollections) {
        this.context = context;
        this.questCollections = questCollections;
    }

    @Override
    public ClassLoader createAssetsClassLoader(String collectionId) throws ClassNotFoundException {
        DexAssetsClassLoader cl = new DexAssetsClassLoader(context, questCollections, collectionId);

        // Our collection may not have class files
        if (!cl.hasClasses())
            throw new ClassNotFoundException("No classes in this collection");

        return cl;
    }

}
