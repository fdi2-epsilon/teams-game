package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import eu.unipv.epsilon.enigma.GameAssetsSystem;

/**
 * A ready to use {@link AssetsClassLoaderFactory} for the Dalvik virtual machine on Android.
 */
public class DalvikAssetsClassLoaderFactory implements AssetsClassLoaderFactory {

    private Context context;
    private GameAssetsSystem assetsSystem;

    public DalvikAssetsClassLoaderFactory(Context context, GameAssetsSystem assetsSystem) {
        this.context = context;
        this.assetsSystem = assetsSystem;
    }

    @Override
    public ClassLoader createAssetsClassLoader(String collectionId) throws ClassNotFoundException {
        DexAssetsClassLoader cl = new DexAssetsClassLoader(context, assetsSystem, collectionId);

        // Our collection may not have class files
        if (!cl.hasClasses())
            throw new ClassNotFoundException("No classes in this collection");

        return cl;
    }

}
