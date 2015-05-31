package eu.unipv.epsilon.enigma.loader.levels.protocol;

import eu.unipv.epsilon.enigma.loader.levels.pool.CollectionsPool;
import eu.unipv.epsilon.enigma.template.TemplateServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class LevelAssetsURLStreamHandler extends URLStreamHandler {

    public static final String PROTOCOL_NAME = "eqc";

    CollectionsPool questCollections;
    TemplateServer templateServer;

    /**
     * Creates a new {@code eqc:/} protocol URL stream handler.
     *
     * @param questCollections the {@link CollectionsPool} to search for assets in collection containers
     * @param templateServer an optional template server used to load dynamic content (can be {@code null})
     */
    public LevelAssetsURLStreamHandler(CollectionsPool questCollections, TemplateServer templateServer) {
        this.questCollections = questCollections;
        this.templateServer = templateServer;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new LevelAssetsURLConnection(u, questCollections, templateServer);
    }

    public static URL createURL(String collectionId, String entryPath) {
        if (!entryPath.startsWith("/"))
            entryPath = '/' + entryPath;
        try {
            return new URL(PROTOCOL_NAME, collectionId, -1, entryPath);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Failed to create a \"" + PROTOCOL_NAME +
                    "\" url, have you correctly registered an URLStreamHandlerFactory?");
        }
    }

}
