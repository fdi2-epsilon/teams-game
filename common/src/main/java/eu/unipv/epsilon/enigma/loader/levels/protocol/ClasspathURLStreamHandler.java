package eu.unipv.epsilon.enigma.loader.levels.protocol;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * A {@link URLStreamHandler} that handles resources on the classpath.
 */
public class ClasspathURLStreamHandler extends URLStreamHandler {

    public static final String PROTOCOL_NAME = "cp";

    private ClassLoader classLoader;

    public ClasspathURLStreamHandler() {
        this.classLoader = getClass().getClassLoader();
    }

    public ClasspathURLStreamHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        String path = u.getPath().startsWith("/") ? u.getPath().substring(1) : u.getPath();

        final URL resourceUrl = classLoader.getResource(path);
        if (resourceUrl == null)
            throw new IOException("Resource " + path + " not found.");
        return resourceUrl.openConnection();
    }

    public static URL createURL(String itemPath) {
        if (!itemPath.startsWith("/"))
            itemPath = '/' + itemPath;
        try {
            return new URL(PROTOCOL_NAME, null, -1, itemPath);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Failed to create a \"" + PROTOCOL_NAME +
                    "\" url, have you correctly registered an URLStreamHandlerFactory?");
        }
    }

}
