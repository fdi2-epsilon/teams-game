package eu.unipv.epsilon.enigma.template;

import android.content.Context;
import eu.unipv.epsilon.enigma.GameAssetsSystem;

import java.util.Iterator;

/**
 * A {@link CandidateClassSource} using {@link DalvikPackageScanner} as a package scanning utility.
 *
 * @see CandidateClassSource CandidateClassSource for more info
 */
public class DalvikCandidateClassSource extends CandidateClassSource {

    private final Context context;

    public DalvikCandidateClassSource(Context context) {
        /* implicit super call */
        this.context = context;
    }

    public DalvikCandidateClassSource(Context context, GameAssetsSystem assetsSystem) {
        super(assetsSystem);
        this.context = context;
    }

    @Override
    public Iterator<Class<?>> findLocalCandidateClasses() throws ClassNotFoundException {
        return DalvikPackageScanner.getClassesInPackage(context, TEMPLATES_LOCATION_BUILTIN).iterator();
    }

    @Override
    public Iterator<Class<?>> findCollectionCandidateClasses(String collectionId) throws ClassNotFoundException {
        return DalvikPackageScanner.getClassesinZipPkg(context, "", assetsSystem, collectionId).iterator();
    }

}
