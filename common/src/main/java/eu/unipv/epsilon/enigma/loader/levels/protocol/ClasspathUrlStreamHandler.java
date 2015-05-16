package eu.unipv.epsilon.enigma.loader.levels.protocol;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * A {@link URLStreamHandler} that handles resources on the classpath.
 */
public class ClasspathUrlStreamHandler extends URLStreamHandler {

    public static final String PROTOCOL_NAME = "assets";

    private final ClassLoader classLoader;

    public ClasspathUrlStreamHandler() {
        this.classLoader = getClass().getClassLoader();
    }

    public ClasspathUrlStreamHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        // The following "assets" is the folder, not the protocol name!
        String path = "assets" + u.getPath();

        final URL resourceUrl = classLoader.getResource(path);
        if (resourceUrl == null)
            throw new IOException("Resource " + path + " not found.");
        return resourceUrl.openConnection();
    }

}
