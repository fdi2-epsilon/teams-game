package eu.unipv.epsilon.enigma.loader.levels.protocol;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.ContainerEntry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LevelAssetsURLConnection extends URLConnection {

    GameAssetsSystem assetsSystem;

    ContainerEntry containerEntry = null;

    public LevelAssetsURLConnection(GameAssetsSystem assetsSystem, URL url) {
        super(url);
        this.assetsSystem = assetsSystem;
    }

    @Override
    public void connect() throws IOException {
        if (!connected) {
            String urlPath = url.getPath().startsWith("/") ? url.getPath().substring(1) : url.getPath();

            CollectionContainer container = assetsSystem.getCollectionContainer(url.getHost());
            if (container == null)
                throw new IOException("Collection \"" + url.getHost() + "\" not found.");

            containerEntry = container.getEntry(urlPath);
            if (containerEntry == null)
                throw new IOException("Entry \"" + urlPath + "\" not found in \"" + url.getHost() + "\".");

            connected = true;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        connect();
        return containerEntry.getStream();
    }

    @Override
    public long getContentLengthLong() {
        if (!connected) return -1;
        return containerEntry.getSize();
    }

    public CollectionContainer getContainer() {
        // Get always-valid copy from the assets system
        return assetsSystem.getCollectionContainer(url.getHost());
    }

}
