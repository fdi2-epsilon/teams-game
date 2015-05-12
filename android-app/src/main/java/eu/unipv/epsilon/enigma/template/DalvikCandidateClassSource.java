package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import eu.unipv.epsilon.enigma.GameAssetsSystem;

import java.util.Iterator;

public class DalvikCandidateClassSource extends CandidateClassSource {

    private final Context context;
    private final GameAssetsSystem assetsSystem;

    public DalvikCandidateClassSource(Context context, GameAssetsSystem assetsSystem) {
        this.context = context;
        this.assetsSystem = assetsSystem;
    }

    @Override
    public Iterator<Class<?>> findLocalCandidateClasses() throws ClassNotFoundException {
        return DalvikPackageScanner.getClassesInPackage(context, TEMPLATES_LOCATION_BUILTIN).iterator();
    }

    @Override
    public Iterator<Class<?>> findCollectionCandidateClasses(String collectionId) throws ClassNotFoundException {
        return DalvikPackageScanner.getClassesinZipPkg("", assetsSystem, collectionId).iterator();
    }

}
