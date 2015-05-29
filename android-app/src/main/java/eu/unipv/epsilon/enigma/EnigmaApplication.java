package eu.unipv.epsilon.enigma;

import android.app.Application;
import eu.unipv.epsilon.enigma.data.StorageLocator;
import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.status.GameStatus;
import eu.unipv.epsilon.enigma.template.DalvikAssetsClassLoaderFactory;
import eu.unipv.epsilon.enigma.template.DalvikPackageScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * Application instance for Enigma, holds assets and game status subsystems.
 */
public class EnigmaApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(EnigmaApplication.class);
    private GameAssetsSystem assetsSystem;
    private GameStatus gameStatus;

    @Override
    public void onCreate() {
        super.onCreate();

        // Keep initialization methods "side-effects free" and assign fields here
        this.assetsSystem = initializeAssetsSystem();
        this.gameStatus = initializeGameStatus();
    }

    public GameAssetsSystem getAssetsSystem() {
        return assetsSystem;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    private GameAssetsSystem initializeAssetsSystem() {
        StorageLocator storageLocator = new StorageLocator(this);
        File intStore = storageLocator.getInternalCollectionsLocation();
        File extStore = storageLocator.getExternalCollectionsLocation();

        LOG.info("Collections path [internal | external]: {} | {}",
                intStore == null ? "~none~" : intStore.getPath(),
                extStore == null ? "~none~" : extStore.getPath());

        ArrayList<CollectionsPool> pools = new ArrayList<>();
        if (intStore != null)
            pools.add(new DirectoryPool(intStore));
        if (extStore != null)
            pools.add(new DirectoryPool(extStore));

        // Create assets system with available collection sources
        CollectionsPool[] poolsVarargs = pools.toArray(new CollectionsPool[pools.size()]);
        GameAssetsSystem assetsSystem = new GameAssetsSystem(poolsVarargs);

        // Initialize templates subsystem using Android-specific reflection algorithms
        assetsSystem.createTemplateServer(
                new DalvikPackageScanner(this), new DalvikAssetsClassLoaderFactory(this, assetsSystem));

        return assetsSystem;
    }

    private GameStatus initializeGameStatus() {
        return new GameStatus(getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));
    }

}
