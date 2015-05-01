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

    LevelAssetsURLStreamHandler(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new LevelAssetsURLConnection(assetsSystem, u);
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        String urlString = spec.substring(start, limit);
        int slashIndex = urlString.indexOf('/');

        String host = urlString.substring(0, slashIndex);
        String path = urlString.substring(slashIndex + 1);

        // (u, protocol, host, port, authority, userInfo, path, query, ref)
        setURL(u, u.getProtocol(), host, -1, "", "", path, "", null);
    }

    public static URL createURL(String collectionId, String entryPath) {
        try {
            return new URL(PROTOCOL_NAME, collectionId, -1, entryPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
