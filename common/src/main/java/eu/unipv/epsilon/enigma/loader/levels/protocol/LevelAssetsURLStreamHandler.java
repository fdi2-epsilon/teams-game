package eu.unipv.epsilon.enigma.loader.levels.protocol;

import eu.unipv.epsilon.enigma.GameAssetsSystem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class LevelAssetsURLStreamHandler extends URLStreamHandler {

    public static final String PROTOCOL_NAME = "eqc";

    GameAssetsSystem assetsSystem;

    public LevelAssetsURLStreamHandler(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new LevelAssetsURLConnection(assetsSystem, u);
    }

    public static URL createURL(String collectionId, String entryPath) {
        if (!entryPath.startsWith("/")) entryPath = '/' + entryPath;
        try {
            return new URL(PROTOCOL_NAME, collectionId, -1, entryPath);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create an \"" + PROTOCOL_NAME +
                    "\" url, have you correctly registered an URLStreamHandlerFactory?");
        }
    }

}
