package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLConnection;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class ScanAlgorithmFactory {

    private final ClassLoader classLoader;

    public ScanAlgorithmFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ScanAlgorithm getUrlScanner(URL url) throws ClassNotFoundException, IOException {
        URLConnection connection = url.openConnection();

        if (connection instanceof JarURLConnection)
            return new ArchiveScanAlgorithm(classLoader, ((JarURLConnection) connection).getJarFile());
        else if (connection instanceof FileURLConnection)
            return new DirectoryScanAlgorithm(classLoader, new File(URLDecoder.decode(url.getPath(), "UTF-8")));
        else if (connection instanceof LevelAssetsURLConnection)
            return new LevelAssetsScanAlgorithm(classLoader, ((LevelAssetsURLConnection) connection).getContainer());
        else
            throw new ClassNotFoundException("Cannot get archive scanner for URL \"" + url.getPath() + "\"");

    }

}