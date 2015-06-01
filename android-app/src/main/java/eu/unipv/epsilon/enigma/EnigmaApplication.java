package eu.unipv.epsilon.enigma;

import android.app.Application;
import eu.unipv.epsilon.enigma.data.StorageLocator;
import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.loader.levels.pool.DirectoryPool;
import eu.unipv.epsilon.enigma.loader.levels.pool.MergedPool;
import eu.unipv.epsilon.enigma.loader.levels.protocol.ProtocolManager;
import eu.unipv.epsilon.enigma.status.GameStatus;
import eu.unipv.epsilon.enigma.template.DalvikAssetsClassLoaderFactory;
import eu.unipv.epsilon.enigma.template.DalvikPackageScanner;
import eu.unipv.epsilon.enigma.template.TemplateRegistry;
import eu.unipv.epsilon.enigma.template.TemplateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Application instance for Enigma, holds assets and game status subsystems.
 */
public class EnigmaApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(EnigmaApplication.class);

    private CollectionsPool collectionsPool;
    private GameStatus gameStatus;

    @Override
    public void onCreate() {
        super.onCreate();

        // Keep initialization methods "side-effects free" and assign fields here
        this.collectionsPool = initializeCollectionsPool();

        // Initialize templates subsystem using Android-specific reflection algorithms
        TemplateServer templateServer = new TemplateServer(new TemplateRegistry(
                new DalvikPackageScanner(this), new DalvikAssetsClassLoaderFactory(this, collectionsPool)));

        // Create url handlers for collection URLs and classpath URLs
        new ProtocolManager(collectionsPool, templateServer).registerURLStreamHandlers();

        // Initialize game status storage
        this.gameStatus = new GameStatus(getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));
    }

    public CollectionsPool getCollectionsPool() {
        return collectionsPool;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    private CollectionsPool initializeCollectionsPool() {
        StorageLocator storageLocator = new StorageLocator(this);
        File intStore = storageLocator.getInternalCollectionsLocation();
        File extStore = storageLocator.getExternalCollectionsLocation();

        LOG.info("Collections path [internal | external]: {} | {}",
                intStore == null ? "~none~" : intStore.getPath(),
                extStore == null ? "~none~" : extStore.getPath());

        List<CollectionsPool> pools = new ArrayList<>();
        if (intStore != null)
            pools.add(new DirectoryPool(intStore));
        if (extStore != null)
            pools.add(new DirectoryPool(extStore));

        // Create assets system with available collection sources
        CollectionsPool[] poolsVarargs = pools.toArray(new CollectionsPool[pools.size()]);

        return new MergedPool(poolsVarargs);
    }

}
