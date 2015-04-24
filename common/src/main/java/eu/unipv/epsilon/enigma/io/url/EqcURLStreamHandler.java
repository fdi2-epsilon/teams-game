package eu.unipv.epsilon.enigma.io.url;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * A {@link java.net.URLStreamHandler} for accessing an eqc file entries.
 *
 * The URL is formatted like this: {@code eqc://[file id]/[entry path]} where the file ID is
 * the name of the file, without extension.
 * You can use the {@link #createURL(String fileId, String entryPath)} method to generate new
 * URLs on the fly.
 *
 * <p>
 *     <b>Note</b>: An application is responsible for setting a {@link java.net.URLStreamHandlerFactory} that will return a {@link ZipURLStreamHandler} on request.
 * </p>
 */
public class EqcURLStreamHandler extends URLStreamHandler {

    File homeDir;

    /**
     * @param baseCollectionsDir The directory containing eqc files
     */
    public EqcURLStreamHandler(File baseCollectionsDir) {
        this.homeDir = baseCollectionsDir;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        String urlString = u.getFile();

        int sepIndex = urlString.indexOf('/');
        String containerName = urlString.substring(0, sepIndex);
        String entryPath = urlString.substring(sepIndex + 1);

        File eqcFile = new File(homeDir, containerName + ".eqc");
        return new ZipURLConnection(u, eqcFile, entryPath);
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        // (u, protocol, host, port, authority, userInfo, path, query, ref)
        setURL(u, u.getProtocol(), "", -1, "", "", spec.substring(start, limit), "", null);
    }

    /**
     * Creates a {@link URL} that points to the given entry inside the file denoted by
     * the passed in fileId.
     * To use the resulting {@link URL}, an application must register {@link EqcURLStreamHandler},
     * otherwise this returns {@code null}.
     */
    public static URL createURL(String fileId, String entryPath) {
        try {
            return new URL("eqc", "", -1, fileId + '/' + entryPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
