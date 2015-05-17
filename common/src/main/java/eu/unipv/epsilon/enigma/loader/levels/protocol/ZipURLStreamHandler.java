package eu.unipv.epsilon.enigma.loader.levels.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

///// UNUSED CLASS IN NEWER VERSIONS BUT KEPT FOR REFERENCE /////

/**
 * A {@link URLStreamHandler} which allows accessing specific zip entries within a zip file.
 *
 * The format of the file part of the URL is <code>[full path of the zip file]|[name of zip entry></code>,
 * but you are encouraged to use the {@link #createURL(String fileName, String entryName)} method instead of
 * creating a URL manually. The host and port part of the URL should be <code>null</code>.
 *
 * <p>
 *     <b>Note</b>: An application is responsible for setting a {@link java.net.URLStreamHandlerFactory} that will
 *     return a {@link ZipURLStreamHandler} when appropriate.
 * </p>
 *
 * @see <a href="http://www.java2s.com/Code/Java/Network-Protocol/ZipURLStreamHandler.htm">Original implementation</a>
 */
public class ZipURLStreamHandler extends URLStreamHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ZipURLStreamHandler.class);

    /** Returns a {@link ZipURLConnection} for the specified URL. */
    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        String urlFile = u.getFile();
        int barIndex = urlFile.indexOf("|");
        if (barIndex == -1)
            throw new MalformedURLException("Missing '|'");

        String fileName = urlFile.substring(0, barIndex);
        String entryName = urlFile.substring(barIndex + 1);

        return new ZipURLConnection(u, new File(fileName), entryName);
    }

    /** Parses the specified URL string. */
    @Override
    protected void parseURL(URL url, String spec, int start, int limit) {
        String urlFile = url.getFile();
        int barIndex = urlFile.indexOf("|");

        String fileName = barIndex == -1 ? urlFile : urlFile.substring(0, barIndex);
        String entryName = barIndex == -1 ? "" : urlFile.substring(barIndex + 1);

        int lastSlashIndex = entryName.lastIndexOf('/');
        String newEntryName = entryName.substring(0, lastSlashIndex + 1) + spec.substring(start, limit);

        // Deprecated (u, protocol, host, port, file, ref)
        // setURL(url, url.getProtocol(), "", -1, fileName + "|" + newEntryName, null);

        // (u, protocol, host, port, authority, userInfo, path, query, ref)
        setURL(url, url.getProtocol(), "", -1, "", "", fileName + "|" + newEntryName, "", null);
    }

    /**
     * Creates a {@link URL} that points to the specified entry within the specific zip file.
     * The {@link URL} will have {@code zip} as the protocol name.
     *
     * To use the resulting {@link URL}, an application must set a {@link java.net.URLStreamHandlerFactory}
     * (via the {@link URL} class) which will return a {@link ZipURLStreamHandler} for the {@code zip} protocol. Returns
     * {@code null} if the protocol is unrecognized.
     */
    public static URL createURL(String fileName, String entryName) {
        return createURL("zip", fileName, entryName);
    }

    /**
     * Creates a {@link URL} that points to the specific entry within the specified zip file and has
     * the specified protocol name.
     *
     * To use the resulting {@link URL}, an application must set a {@link java.net.URLStreamHandlerFactory}
     * (via the {@link URL} class) which will return a {@link ZipURLStreamHandler} for the protocol name given
     * to this method. Returns {@code null} if the protocol is unrecognized.
     */
    public static URL createURL(String protocol, String fileName, String entryName) {
        try {
            return new URL(protocol, "", -1, fileName + '|' + entryName);
        } catch (MalformedURLException e) {
            LOG.error("This protocol was not registered", e);
            return null;
        }
    }

}
