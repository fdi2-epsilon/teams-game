package eu.unipv.epsilon.enigma;

import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class URLHandlerFactory implements URLStreamHandlerFactory {

    public static boolean isUrlHandlerRegistered = false;

    public final GameAssetsSystem assetsSystem;

    public URLHandlerFactory(GameAssetsSystem assetsSystem) {
        this.assetsSystem = assetsSystem;
    }

    public static void register(GameAssetsSystem assetsSystem) {
        if (!isUrlHandlerRegistered) {
            URL.setURLStreamHandlerFactory(new URLHandlerFactory(assetsSystem));
            isUrlHandlerRegistered = true;
        }
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol.equalsIgnoreCase(LevelAssetsURLStreamHandler.PROTOCOL_NAME))
            return new LevelAssetsURLStreamHandler(assetsSystem);
        return null;
    }

}
