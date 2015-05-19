package eu.unipv.epsilon.enigma.loader.levels.protocol;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.ContainerEntry;
import eu.unipv.epsilon.enigma.template.TemplateServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LevelAssetsURLConnection extends URLConnection {

    private static final String TEMPLATE_DOCUMENT = "document.xml";
    // Default documents to search if we have a link to a directory
    private static final String[] DEFAULT_DOCUMENTS = { TEMPLATE_DOCUMENT, "index.html" };

    // Hey! Why is the above array private?
    //   Public arrays, even ones declared static final can have their contents edited by malicious programs.
    //   The final keyword on an array declaration means that the array object itself may only be assigned once,
    //   but its contents are still mutable. Therefore making arrays public is a security risk.

    GameAssetsSystem assetsSystem;
    ContainerEntry containerEntry = null;

    public LevelAssetsURLConnection(GameAssetsSystem assetsSystem, URL url) {
        super(url);
        this.assetsSystem = assetsSystem;
    }

    @Override
    public void connect() throws IOException {
        if (!connected) {

            CollectionContainer container = assetsSystem.getCollectionContainer(url.getHost());
            if (container == null)
                throw new IOException("Collection \"" + url.getHost() + "\" not found.");

            containerEntry = findURLEntry(container, url.getPath());

            connected = true;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        connect();
        TemplateServer templateServer = assetsSystem.getTemplateServer();

        // Do not use templates if template server is not defined or we don't have a "document.xml"
        if (templateServer == null || !containerEntry.getPath().endsWith(TEMPLATE_DOCUMENT))
            return containerEntry.getStream();

        return templateServer.getDynamicContentStream(containerEntry.getStream(),
                    LevelAssetsURLStreamHandler.createURL(url.getHost(), containerEntry.getPath()));
    }

    @Override
    public long getContentLengthLong() {
        if (!connected)
            return -1;
        return containerEntry.getSize();
    }

    public CollectionContainer getContainer() {
        // Get always-valid copy from the assets system
        return assetsSystem.getCollectionContainer(url.getHost());
    }

    private ContainerEntry findURLEntry(CollectionContainer container, String urlPath) throws IOException {
        // Probably out path starts with '/', remove it
        if (urlPath.startsWith("/"))
            urlPath = urlPath.substring(1);

        ContainerEntry entry = container.getEntry(urlPath);
        if (entry == null)
            throw new IOException(String.format(
                    "Entry \"%s\" not found in collection \"%s\".", urlPath, url.getHost()));

        // If it is a file, return it now
        if (!entry.isDirectory())
            return entry;

        // We have a directory, add a trailing slash if not present
        if (!urlPath.endsWith("/"))
            urlPath += '/';

        for (String fileName : DEFAULT_DOCUMENTS) {
            entry = container.getEntry(urlPath + fileName);
            if (entry != null && !entry.isDirectory())
                return entry;
        }

        // No default document found in directory
        throw new IOException(String.format(
                "No default document in directory \"%s\" inside collection \"%s\".", urlPath, url.getHost()));
    }

}
